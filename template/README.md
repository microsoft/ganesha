# Experience-API : {ServiceName}
Experience API for Advocate Central

### Installing Lombok plugin to IDE
Please follow this link to install lombok to the IDE https://howtodoinjava.com/automation/lombok-eclipse-installation-examples/

### Changes to be made after creating a repo from Common Template :

#### 1.) Project Name
- Update the Project Name from "STANDARD_EXP_API_TEMPLATE" to "Your Project Name". For instance if it is Order Details service, the project name should be "OrderDetails", "GetDrugHistory", etc.

#### 2.) README.md
- Mention the name of service in place of {ServiceName} in Line 1
- Remove everything from Line 3 to the end of the file

#### 3.) pom.xml
- Replace all instances of "servicename" with the service name

#### 4.) ExperienceApiApplication.java
- Path: src/main/java/com
- Replace the "servicename" with the service name

#### 5.) Creating package
- Path: src/main/java/com
- Create the package with the service name. For instance if it is Order Details service, than the package name should be "src/main/java/com/getorderdetails"

#### 6.) application.yml
- Path: src/main/resources
- Update "servicename-endpoint" in the yaml file with the service name. For instance if it is Order Details service, "servicename-endpoint" should be "order-details-endpoint"

#### 7.) Mock Request & Response
- Path: src/test/resouce/mocks/responses/servicename
- Update "servicename" to service name. For instance if it is Order Details service, "src/test/resouce/mocks/responses/servicename" should be "src/test/resouce/mocks/responses/getorderdetails"

-----------------------------------------------------------------------------------------------------------

### Points to Remember :

#### Informing DevOps
- Please inform the DevOps team once a team starts working on any microservice because it will take them some time to setup and configure the deployment process

#### Branch Name
- The release branch should be named as "{Releasename}_{ServiceName}”. For instance if the OrderDetails is to be deployed as a part of June,2020 in the second release than the branch should be named as "release/20.06.01".

#### Development process
- Create a "develop" branch for all the development work. Once the "develop" branch has the updated working code, raise a PR to get the code merged to the release branch. Once the code is reviewed it needs to be merged to the release branch.

## Guidelines for Adding to EAPIs

### Should You Add to an Existing EAPI

The primary factor for deciding whether or not to add to a particular EAPI is the topic. If you’re adding a claims-related function then 
you will want to consider adding to the Claims EAPI as opposed to making a new EAPI. Naturally this means you shouldn’t add to an 
existing EAPI if the topics don’t go well together. If an existing EAPI has similar upstream endpoints in the application.yml file 
then that could be a sign to add a subservice to it instead of creating a new EAPI.

However, something to keep in mind is performance of the EAPI. If adding to an EAPI causes performance drops, then that’s a sign 
to create a new EAPI. Furthermore, try not to merge/add to EAPIs just because they are a part of the same workflow. We wish to avoid 
this partially because some EAPIs would grow to be very large and thus potentially hit the previously mentioned performance issues. 
The other reason is because some EAPIs will serve more than one workflow.

Another potential concern when combining/adding to EAPIs are the dependencies. Make sure to remedy any conflicting dependencies in the 
pom.xml file.

### Organizing the New Additions
If you have decided to add to an existing EAPI, then follow the following file structure (not all files/folders are shown):

- src
  - main 
    - java/com/\<EAPI>
      - common 
        - getaccesstoken files (if appropriate)
        - YAMLConfig.java (if appropriate)
      - Subservice 1 
        - Controller file 
        - Folder with request models 
        - Folder with response models 
        - Folder/file with utility functions 
      - Subservice 2 
        - Controller file 
        - Folder with request models 
        - Folder with response models 
        - Folder/file with utility functions 
      - Etc. 
  - test
    - java/com/\<EAPI>
      - Folder for subservice 1’s tests 
      - Folder for subservice 2’s tests 
      - Files for any common tests 
    - Resources/mocks 
      - Folder for subservice 1’s mocks 
        - Request models 
        - Response models 
      - Folder for subservice 2’s mocks 
        - Request models 
        - Response models 
      - Etc. 
- Dockerfile 
- Jenkinsfile 
- README.md 
- Pom.xml

### Other Things to Consider When Updating an EAPI
You may find that some EAPIs have subservices that have the same or similar request and/or response models. When combining/adding 
to an EAPI, take a look at these and see if there is any opportunities to pull any of these out into the src/main/java/<EAPI>/common 
folder.  

## Post EAPI Update
The following sections cover steps for after you’ve combined two EAPIs or created a new one.

### Adding New/Moving Existing Endpoints
If you are adding a new endpoint to an EAPI, or moving endpoints from one EAPI to another, there are some steps to get these endpoints running 
in the UI. First, make sure that the endpoints of the EAPI don’t conflict with one another. It also helps to make them relatively simple.

Second, if you’re adding a new EAPI you need to add a new API to the APIM (API management) service. If you’re moving endpoints to an existing EAPI
then you need to add the endpoints to the APIM of the receiving EAPI. In either case, you will want to use the Pipeline 
[AdvocateCentral_Update_APIM_Operations](https://acqsec-jenkins.optum.com/job/Advocate_Central/job/AdvocateCentral_Update_APIM_Operations/build?delay=0sec) Jenkins job.

You can find the APIMs on Azure; for instance, go to [acq-test-us-c-apim APIs](https://portal.azure.com/#@uhgazure.onmicrosoft.com/resource/subscriptions/1e5f7b51-a672-442b-b85c-76807f66730b/resourceGroups/acq-test-us-c-rg/providers/Microsoft.ApiManagement/service/acq-test-us-c-apim/apim-apis) to check the APIs for the test environment. If you search for an EAPI 
here, you’ll see the different endpoints/operations associated with the EAPI. These mappings go after the base URL of the service. Under the settings 
tab, you’ll see the name and base endpoint of the service. For example, claims has the base URL of “https://advocatecentralapi-test.optumrx.com/claims” 
which means that the full endpoint for the getClaimDetails operation (as you see in the design tab) is https://advocatecentralapi-test.optumrx.com/claims/getClaimDetails

Once the APIM has been updated with the appropriate endpoints, you just need to add them to the UI code. For existing endpoints, there will already be 
services that utilize the endpoint (like src/routes/Member/redux/sagas/getClaimHistory/getClaimHistoryService.ts). In this case, you just need to update
the endpoint to match the full endpoint as seen in the APIM (of course you’ll still be using the experienceApiURL env variable for the 
“https://advocatecentralapi-test.optumrx.com” part).

Brand new endpoints will of course require more UI changes to integrate. Follow the example of other sagas/services and pay close
attention to where all the parts are used.  This will involve files for the saga, the service, the actions, the reducers, and the 
use<EAPI-operation>State.

### Automated Tests
For EAPI combinations, be sure to make sure automated tests still work properly. Service tests should have the endpoints updated. 
Additionally you may have to reorganize the tests. Of course for adding a new EAPI, you’ll have to make brand new tests.

### Performance Tests
When combining EAPIs, you will also want to combine the Jmeter scripts. You can find the repo with all of these scripts here: 
https://github.com/optum-rx-consumer-digital/advocate-central-performance-jmeter

Generally speaking, this should mean that you move the thread group of one test into the other test. Be sure to rename the 
thread groups to clarify what test is for what endpoint, and ensure the jmx file and the test plan item have an appropriate
name for the EAPI it covers. There should only be one Aggregate Report and Backend Listener items each in the file (at the 
same level as the thread groups).



Don’t forget to go into [Jarvis](http://performance.optum.com/301dcf0f-05ad-4f7a-ac06-d9414a41c4d5/dashboard?continue) 
and add new simulations/remove now unused simulations as necessary.  

----------------------------------------------------------------------------------------------

## Steps to use the EAPI collection

### 1.) Download eapi_collection.json and use __Insomnia__ / Postman to import the downloaded json file

![import eapi_collection.json](collection/images/001_import.jpg)

### 2.) Update env variables

![Env setting](collection/images/002_envSetting.jpg)

Add in base_url, client_id, client_secret, resource_id for each environment

![env value updates](collection/images/003_updateEnv.jpg)

In Base Environment add in your **msid** 

![base env msid update](collection/images/004_updateBaseEnv.jpg)

### 3.) You may need to reassign the token function 

Request once for the token, then go to the Auth tab and set the bearer token function

![token update](collection/images/005_updateToken.jpg)

Gif 

![token update simulation](collection/images/006_tokenUpdation.gif)