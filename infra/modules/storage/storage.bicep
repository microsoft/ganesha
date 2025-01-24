param storageAccountName string
param location string = resourceGroup().location
param tags object
param utcValue string = utcNow()

resource storage 'Microsoft.Storage/storageAccounts@2022-09-01' = {
  name: storageAccountName
  location: location
  kind: 'StorageV2'
  tags: tags
  sku: {
    name: 'Standard_LRS'
  }
  properties: {
    largeFileSharesState: 'Enabled'
  }
}


output storageAccountKey string = storage.listKeys().keys[0].value
