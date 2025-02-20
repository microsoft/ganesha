targetScope = 'subscription'

@minLength(1)
@maxLength(64)
@description('Name of the the environment which is used to generate a short unique hash used in all resources.')
param environmentName string

@minLength(1)
@description('Primary location for all resources')
param location string

@description('Id of the user or app to assign application roles')
param principalId string = ''

param serviceName string = 'api'

@description('The MongoDB username to use for the cluster')
param mongodbUsername string = 'Admin001'

@description('The MongoDB password to use for the cluster')
@secure()
param mongodbPassword string = ''

@description('The name of the MongoDB database')
param mongodbDatabaseName string = 'ganeshadb'

@description('The name of the MongoDB collection')
param mongodbCollectionName string = 'chathistory'

var abbrs = loadJsonContent('./abbreviations.json')
var resourceToken = toLower(uniqueString(subscription().id, environmentName, location))
var storageAccountName = '${abbrs.storageStorageAccounts}${resourceToken}'
var cognitiveAccountName = '${abbrs.cognitiveServicesAccounts}${resourceToken}'
var mongoClusterName = '${abbrs.documentDBDatabaseAccounts}${resourceToken}'
var containerRegistryName = '${abbrs.containerRegistryRegistries}${resourceToken}'

var tags = {
  'azd-env-name': environmentName
}

resource rg 'Microsoft.Resources/resourceGroups@2021-04-01' = {
  name: '${abbrs.resourcesResourceGroups}${environmentName}'
  location: location
  tags: tags
}

module storage 'modules/storage/storage.bicep' = {
  name: '${deployment().name}--storage'
  scope: resourceGroup(rg.name)
  params: {
    location: location
    tags: tags
    storageAccountName: storageAccountName
  }
}

module managedIdentity 'br/public:avm/res/managed-identity/user-assigned-identity:0.4.0' = {
  name: 'user-assigned-identity'
  scope: resourceGroup(rg.name)
  params: {
    name: 'managed-identity-${resourceToken}'
    location: location
    tags: tags
  }
}

module cognitive 'modules/cognitive/cognitive.bicep' = {
  name: '${deployment().name}--cog'
  scope: resourceGroup(rg.name)
  params: {
    location: location
    tags: tags
    accountName: cognitiveAccountName
    deployments: [
      {
        name: 'chat'
        model: {
          format: 'OpenAI'
          name: 'gpt-4o'
          version: '2024-08-06'
        }
        capacity: 30
      }
      {
        name: 'embedding'
        model: {
          format: 'OpenAI'
          name: 'text-embedding-ada-002'
          version: '2'
        }
        capacity: 30
      }
    ]
  }
}

module mongoCluster 'br/public:avm/res/document-db/mongo-cluster:0.1.1' = {
  name: 'mongoClusterDeployment'
  scope: resourceGroup(rg.name)
  params: {
    // Required parameters
    administratorLogin: mongodbUsername
    administratorLoginPassword: mongodbPassword
    name: mongoClusterName
    nodeCount: 1
    sku: 'M25'
    storage: 32
    // Non-required parameters
    location: location
  }
}

module containerRegistry 'br/public:avm/res/container-registry/registry:0.5.1' = {
  name: 'container-registry'
  scope: resourceGroup(rg.name)
  params: {
    name: containerRegistryName
    location: location
    tags: tags
    acrAdminUserEnabled: false
    anonymousPullEnabled: true
    publicNetworkAccess: 'Enabled'
    acrSku: 'Standard'
  }
}

var containerRegistryRole = subscriptionResourceId(
  'Microsoft.Authorization/roleDefinitions',
  '8311e382-0749-4cb8-b61a-304f252e45ec'
) // AcrPush built-in role

module registryUserAssignment 'br/public:avm/ptn/authorization/resource-role-assignment:0.1.1' = if (!empty(principalId)) {
  name: 'container-registry-role-assignment-push-user'
  scope: resourceGroup(rg.name)
  params: {
    principalId: principalId
    resourceId: containerRegistry.outputs.resourceId
    roleDefinitionId: containerRegistryRole
  }
}

module logAnalyticsWorkspace 'br/public:avm/res/operational-insights/workspace:0.7.0' = {
  name: 'log-analytics-workspace'
  scope: resourceGroup(rg.name)
  params: {
    name: 'log-analytics-${resourceToken}'
    location: location
    tags: tags
  }
}

module containerAppsEnvironment 'br/public:avm/res/app/managed-environment:0.8.0' = {
  name: 'container-apps-env'
  scope: resourceGroup(rg.name)
  params: {
    name: 'container-env-${resourceToken}'
    location: location
    tags: tags
    logAnalyticsWorkspaceResourceId: logAnalyticsWorkspace.outputs.resourceId
    zoneRedundant: false
  }
}

module containerAppsApp 'br/public:avm/res/app/container-app:0.9.0' = {
  name: 'container-apps-app'
  scope: resourceGroup(rg.name)
  params: {
    name: 'container-app-${resourceToken}'
    environmentResourceId: containerAppsEnvironment.outputs.resourceId
    location: location
    tags: union(tags, { 'azd-service-name': serviceName })
    ingressTargetPort: 8080
    ingressExternal: true
    ingressTransport: 'auto'
    stickySessionsAffinity: 'sticky'
    scaleMaxReplicas: 1
    scaleMinReplicas: 1
    corsPolicy: {
      allowCredentials: true
      allowedOrigins: [
        '*'
      ]
    }
    managedIdentities: {
      systemAssigned: false
      userAssignedResourceIds: [
        managedIdentity.outputs.resourceId
      ]
    }
    secrets: {
      secureList: [
        {
          name: 'user-assigned-managed-identity-client-id'
          value: managedIdentity.outputs.clientId
        }
        {
          name: 'azure-cosmos-connection-string'
          value: mongoCluster.outputs.connectionStringKey
        }
      ]
    }
    containers: [
      {
        image: 'mcr.microsoft.com/azuredocs/containerapps-helloworld:latest'
        name: 'web-front-end'
        resources: {
          cpu: '0.25'
          memory: '.5Gi'
        }
        env: [
          {
            name: 'AZURE_CLIENT_ID'
            secretRef: 'user-assigned-managed-identity-client-id'
          }
          {
            name: 'AAZURE_COSMOS_CONN_STR'
            secretRef: 'azure-cosmos-connection-string'
          }
        ]
      }
    ]
  }
}

output STORAGE_ACCOUNT_NAME string = storageAccountName
output STORAGE_ACCOUNT_KEY string = storage.outputs.storageAccountKey
output AZURE_RESOURCE_GROUP string = rg.name
output AZURE_CONTAINER_REGISTRY_ENDPOINT string = containerRegistry.outputs.loginServer
