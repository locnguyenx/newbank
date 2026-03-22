## @openapitools/openapi-typescript-axios@v0

This generator creates TypeScript/JavaScript client that utilizes [axios](https://github.com/axios/axios). The generated Node module can be used in the following environments:

Environment
* Node.js
* Webpack
* Browserify

Language level
* ES5 - you must have a Promises/A+ library installed
* ES6

Module system
* CommonJS
* ES6 module system

It can be used in both TypeScript and JavaScript. In TypeScript, the definition will be automatically resolved via `package.json`. ([Reference](https://www.typescriptlang.org/docs/handbook/declaration-files/consumption.html))

### Building

To build and compile the typescript sources to javascript use:
```
npm install
npm run build
```

### Publishing

First build the package then run `npm publish`

### Consuming

navigate to the folder of your consuming project and run one of the following commands.

_published:_

```
npm install @openapitools/openapi-typescript-axios@v0 --save
```

_unPublished (not recommended):_

```
npm install PATH_TO_GENERATED_PACKAGE --save
```

### Documentation for API Endpoints

All URIs are relative to *http://localhost:8080*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*AccountControllerApi* | [**closeAccount**](docs/AccountControllerApi.md#closeaccount) | **PUT** /api/accounts/{accountNumber}/close | 
*AccountControllerApi* | [**freezeAccount**](docs/AccountControllerApi.md#freezeaccount) | **PUT** /api/accounts/{accountNumber}/freeze | 
*AccountControllerApi* | [**getAccountBalance**](docs/AccountControllerApi.md#getaccountbalance) | **GET** /api/accounts/{accountNumber}/balance | 
*AccountControllerApi* | [**getAccountDetails**](docs/AccountControllerApi.md#getaccountdetails) | **GET** /api/accounts/{accountNumber} | 
*AccountControllerApi* | [**getAccountStatement**](docs/AccountControllerApi.md#getaccountstatement) | **GET** /api/accounts/{accountNumber}/statement | 
*AccountControllerApi* | [**getAccounts**](docs/AccountControllerApi.md#getaccounts) | **GET** /api/accounts | 
*AccountControllerApi* | [**openAccount**](docs/AccountControllerApi.md#openaccount) | **POST** /api/accounts | 
*AccountControllerApi* | [**unfreezeAccount**](docs/AccountControllerApi.md#unfreezeaccount) | **PUT** /api/accounts/{accountNumber}/unfreeze | 
*AccountHolderControllerApi* | [**addHolder**](docs/AccountHolderControllerApi.md#addholder) | **POST** /api/accounts/{accountNumber}/holders | 
*AccountHolderControllerApi* | [**removeHolder**](docs/AccountHolderControllerApi.md#removeholder) | **DELETE** /api/accounts/{accountNumber}/holders/{holderId} | 
*ApprovalControllerApi* | [**approve1**](docs/ApprovalControllerApi.md#approve1) | **POST** /api/limits/approvals/{id}/approve | 
*ApprovalControllerApi* | [**getPendingApprovals**](docs/ApprovalControllerApi.md#getpendingapprovals) | **GET** /api/limits/approvals/pending | 
*ApprovalControllerApi* | [**reject1**](docs/ApprovalControllerApi.md#reject1) | **POST** /api/limits/approvals/{id}/reject | 
*AuthorizationControllerApi* | [**createAuthorization**](docs/AuthorizationControllerApi.md#createauthorization) | **POST** /api/authorizations | 
*AuthorizationControllerApi* | [**getAuthorizationById**](docs/AuthorizationControllerApi.md#getauthorizationbyid) | **GET** /api/authorizations/{id} | 
*AuthorizationControllerApi* | [**getAuthorizationsByCustomer**](docs/AuthorizationControllerApi.md#getauthorizationsbycustomer) | **GET** /api/authorizations/customer/{customerId} | 
*AuthorizationControllerApi* | [**revokeAuthorization**](docs/AuthorizationControllerApi.md#revokeauthorization) | **POST** /api/authorizations/{id}/revoke | 
*AuthorizationControllerApi* | [**updateAuthorization**](docs/AuthorizationControllerApi.md#updateauthorization) | **PUT** /api/authorizations/{id} | 
*BranchControllerApi* | [**createBranch**](docs/BranchControllerApi.md#createbranch) | **POST** /api/master-data/branches | 
*BranchControllerApi* | [**deactivateBranch**](docs/BranchControllerApi.md#deactivatebranch) | **PUT** /api/master-data/branches/{code}/deactivate | 
*BranchControllerApi* | [**getActiveBranches**](docs/BranchControllerApi.md#getactivebranches) | **GET** /api/master-data/branches | 
*BranchControllerApi* | [**getBranch**](docs/BranchControllerApi.md#getbranch) | **GET** /api/master-data/branches/{code} | 
*BranchControllerApi* | [**updateBranch**](docs/BranchControllerApi.md#updatebranch) | **PUT** /api/master-data/branches/{code} | 
*ChannelControllerApi* | [**createChannel**](docs/ChannelControllerApi.md#createchannel) | **POST** /api/master-data/channels | 
*ChannelControllerApi* | [**deactivateChannel**](docs/ChannelControllerApi.md#deactivatechannel) | **PUT** /api/master-data/channels/{code}/deactivate | 
*ChannelControllerApi* | [**getActiveChannels**](docs/ChannelControllerApi.md#getactivechannels) | **GET** /api/master-data/channels | 
*ChannelControllerApi* | [**updateChannel**](docs/ChannelControllerApi.md#updatechannel) | **PUT** /api/master-data/channels/{code} | 
*ChargeAssignmentControllerApi* | [**assignToCustomer1**](docs/ChargeAssignmentControllerApi.md#assigntocustomer1) | **POST** /api/charges/assignments/customer | 
*ChargeAssignmentControllerApi* | [**assignToProduct1**](docs/ChargeAssignmentControllerApi.md#assigntoproduct1) | **POST** /api/charges/assignments/product | 
*ChargeAssignmentControllerApi* | [**getCustomerOverrides**](docs/ChargeAssignmentControllerApi.md#getcustomeroverrides) | **GET** /api/charges/assignments/customer/{customerId} | 
*ChargeAssignmentControllerApi* | [**getProductCharges**](docs/ChargeAssignmentControllerApi.md#getproductcharges) | **GET** /api/charges/assignments/product/{productCode} | 
*ChargeAssignmentControllerApi* | [**unassignFromCustomer**](docs/ChargeAssignmentControllerApi.md#unassignfromcustomer) | **DELETE** /api/charges/assignments/customer | 
*ChargeAssignmentControllerApi* | [**unassignFromProduct**](docs/ChargeAssignmentControllerApi.md#unassignfromproduct) | **DELETE** /api/charges/assignments/product | 
*ChargeCalculationControllerApi* | [**calculate**](docs/ChargeCalculationControllerApi.md#calculate) | **POST** /api/charges/calculate | 
*ChargeDefinitionControllerApi* | [**activateCharge**](docs/ChargeDefinitionControllerApi.md#activatecharge) | **PUT** /api/charges/definitions/{id}/activate | 
*ChargeDefinitionControllerApi* | [**createCharge**](docs/ChargeDefinitionControllerApi.md#createcharge) | **POST** /api/charges/definitions | 
*ChargeDefinitionControllerApi* | [**deactivateCharge**](docs/ChargeDefinitionControllerApi.md#deactivatecharge) | **PUT** /api/charges/definitions/{id}/deactivate | 
*ChargeDefinitionControllerApi* | [**getAllCharges**](docs/ChargeDefinitionControllerApi.md#getallcharges) | **GET** /api/charges/definitions | 
*ChargeDefinitionControllerApi* | [**getCharge**](docs/ChargeDefinitionControllerApi.md#getcharge) | **GET** /api/charges/definitions/{id} | 
*ChargeDefinitionControllerApi* | [**updateCharge**](docs/ChargeDefinitionControllerApi.md#updatecharge) | **PUT** /api/charges/definitions/{id} | 
*ChargeRuleControllerApi* | [**addRule**](docs/ChargeRuleControllerApi.md#addrule) | **POST** /api/charges/definitions/{chargeId}/rules | 
*ChargeRuleControllerApi* | [**getRules**](docs/ChargeRuleControllerApi.md#getrules) | **GET** /api/charges/definitions/{chargeId}/rules | 
*ChargeRuleControllerApi* | [**removeRule**](docs/ChargeRuleControllerApi.md#removerule) | **DELETE** /api/charges/definitions/{chargeId}/rules/{ruleId} | 
*ChargeRuleControllerApi* | [**updateRule**](docs/ChargeRuleControllerApi.md#updaterule) | **PUT** /api/charges/definitions/{chargeId}/rules/{ruleId} | 
*CountryControllerApi* | [**createCountry**](docs/CountryControllerApi.md#createcountry) | **POST** /api/master-data/countries | 
*CountryControllerApi* | [**deactivateCountry**](docs/CountryControllerApi.md#deactivatecountry) | **PUT** /api/master-data/countries/{isoCode}/deactivate | 
*CountryControllerApi* | [**getAllCountries**](docs/CountryControllerApi.md#getallcountries) | **GET** /api/master-data/countries | 
*CountryControllerApi* | [**getCountry**](docs/CountryControllerApi.md#getcountry) | **GET** /api/master-data/countries/{isoCode} | 
*CountryControllerApi* | [**updateCountry**](docs/CountryControllerApi.md#updatecountry) | **PUT** /api/master-data/countries/{isoCode} | 
*CurrencyControllerApi* | [**createCurrency**](docs/CurrencyControllerApi.md#createcurrency) | **POST** /api/master-data/currencies | 
*CurrencyControllerApi* | [**deactivateCurrency**](docs/CurrencyControllerApi.md#deactivatecurrency) | **PUT** /api/master-data/currencies/{code}/deactivate | 
*CurrencyControllerApi* | [**getAllCurrencies**](docs/CurrencyControllerApi.md#getallcurrencies) | **GET** /api/master-data/currencies | 
*CurrencyControllerApi* | [**getCurrency**](docs/CurrencyControllerApi.md#getcurrency) | **GET** /api/master-data/currencies/{code} | 
*CurrencyControllerApi* | [**updateCurrency**](docs/CurrencyControllerApi.md#updatecurrency) | **PUT** /api/master-data/currencies/{code} | 
*CustomerControllerApi* | [**createCorporate**](docs/CustomerControllerApi.md#createcorporate) | **POST** /api/customers/corporate | Create a new corporate customer
*CustomerControllerApi* | [**createIndividual**](docs/CustomerControllerApi.md#createindividual) | **POST** /api/customers/individual | Create a new individual customer
*CustomerControllerApi* | [**createSME**](docs/CustomerControllerApi.md#createsme) | **POST** /api/customers/sme | Create a new SME customer
*CustomerControllerApi* | [**getCustomerById**](docs/CustomerControllerApi.md#getcustomerbyid) | **GET** /api/customers/{id} | Get customer by ID
*CustomerControllerApi* | [**listCustomers**](docs/CustomerControllerApi.md#listcustomers) | **GET** /api/customers | List all customers
*CustomerControllerApi* | [**searchCustomers**](docs/CustomerControllerApi.md#searchcustomers) | **GET** /api/customers/search | Search customers
*CustomerControllerApi* | [**updateCustomer**](docs/CustomerControllerApi.md#updatecustomer) | **PUT** /api/customers/{id} | Update customer
*DocumentTypeControllerApi* | [**createDocumentType**](docs/DocumentTypeControllerApi.md#createdocumenttype) | **POST** /api/master-data/document-types | 
*DocumentTypeControllerApi* | [**deactivateDocumentType**](docs/DocumentTypeControllerApi.md#deactivatedocumenttype) | **PUT** /api/master-data/document-types/{code}/deactivate | 
*DocumentTypeControllerApi* | [**getActiveDocumentTypes**](docs/DocumentTypeControllerApi.md#getactivedocumenttypes) | **GET** /api/master-data/document-types | 
*DocumentTypeControllerApi* | [**updateDocumentType**](docs/DocumentTypeControllerApi.md#updatedocumenttype) | **PUT** /api/master-data/document-types/{code} | 
*EmploymentControllerApi* | [**bulkUploadEmployees**](docs/EmploymentControllerApi.md#bulkuploademployees) | **POST** /api/employments/bulk | 
*EmploymentControllerApi* | [**createEmployment**](docs/EmploymentControllerApi.md#createemployment) | **POST** /api/employments | 
*EmploymentControllerApi* | [**getEmploymentById**](docs/EmploymentControllerApi.md#getemploymentbyid) | **GET** /api/employments/{id} | 
*ExchangeRateControllerApi* | [**convertAmount**](docs/ExchangeRateControllerApi.md#convertamount) | **GET** /api/master-data/exchange-rates/convert | 
*ExchangeRateControllerApi* | [**createExchangeRate**](docs/ExchangeRateControllerApi.md#createexchangerate) | **POST** /api/master-data/exchange-rates | 
*ExchangeRateControllerApi* | [**getLatestRate**](docs/ExchangeRateControllerApi.md#getlatestrate) | **GET** /api/master-data/exchange-rates/latest | 
*FeeWaiverControllerApi* | [**createWaiver**](docs/FeeWaiverControllerApi.md#createwaiver) | **POST** /api/charges/waivers | 
*FeeWaiverControllerApi* | [**getApplicableWaivers**](docs/FeeWaiverControllerApi.md#getapplicablewaivers) | **GET** /api/charges/waivers/applicable | 
*FeeWaiverControllerApi* | [**getWaivers**](docs/FeeWaiverControllerApi.md#getwaivers) | **GET** /api/charges/waivers | 
*FeeWaiverControllerApi* | [**removeWaiver**](docs/FeeWaiverControllerApi.md#removewaiver) | **DELETE** /api/charges/waivers/{id} | 
*HolidayControllerApi* | [**createHoliday**](docs/HolidayControllerApi.md#createholiday) | **POST** /api/master-data/holidays | 
*HolidayControllerApi* | [**getHolidays**](docs/HolidayControllerApi.md#getholidays) | **GET** /api/master-data/holidays | 
*HolidayControllerApi* | [**isHoliday**](docs/HolidayControllerApi.md#isholiday) | **GET** /api/master-data/holidays/check | 
*IndustryControllerApi* | [**createIndustry**](docs/IndustryControllerApi.md#createindustry) | **POST** /api/master-data/industries | 
*IndustryControllerApi* | [**getAllIndustries**](docs/IndustryControllerApi.md#getallindustries) | **GET** /api/master-data/industries | 
*IndustryControllerApi* | [**getIndustry**](docs/IndustryControllerApi.md#getindustry) | **GET** /api/master-data/industries/{code} | 
*InterestRateControllerApi* | [**createInterestRate**](docs/InterestRateControllerApi.md#createinterestrate) | **POST** /api/charges/interest | 
*InterestRateControllerApi* | [**getInterestRate**](docs/InterestRateControllerApi.md#getinterestrate) | **GET** /api/charges/interest/{id} | 
*InterestRateControllerApi* | [**getInterestRatesByProduct**](docs/InterestRateControllerApi.md#getinterestratesbyproduct) | **GET** /api/charges/interest/product/{productCode} | 
*InterestRateControllerApi* | [**updateInterestRate**](docs/InterestRateControllerApi.md#updateinterestrate) | **PUT** /api/charges/interest/{id} | 
*KycControllerApi* | [**approveKYC**](docs/KycControllerApi.md#approvekyc) | **POST** /api/kyc/{id}/approve | 
*KycControllerApi* | [**getKYCCheckById**](docs/KycControllerApi.md#getkyccheckbyid) | **GET** /api/kyc/{id} | 
*KycControllerApi* | [**initiateKYC**](docs/KycControllerApi.md#initiatekyc) | **POST** /api/kyc | 
*KycControllerApi* | [**rejectKYC**](docs/KycControllerApi.md#rejectkyc) | **POST** /api/kyc/{id}/reject | 
*KycControllerApi* | [**submitDocuments**](docs/KycControllerApi.md#submitdocuments) | **POST** /api/kyc/{id}/documents | 
*KycControllerApi* | [**submitForReview**](docs/KycControllerApi.md#submitforreview) | **POST** /api/kyc/{id}/submit | 
*KycControllerApi* | [**updateKYC**](docs/KycControllerApi.md#updatekyc) | **PUT** /api/kyc/{id} | 
*LimitAssignmentControllerApi* | [**assignToAccount**](docs/LimitAssignmentControllerApi.md#assigntoaccount) | **POST** /api/limits/assignments/account | 
*LimitAssignmentControllerApi* | [**assignToCustomer**](docs/LimitAssignmentControllerApi.md#assigntocustomer) | **POST** /api/limits/assignments/customer | 
*LimitAssignmentControllerApi* | [**assignToProduct**](docs/LimitAssignmentControllerApi.md#assigntoproduct) | **POST** /api/limits/assignments/product | 
*LimitAssignmentControllerApi* | [**getAccountLimits**](docs/LimitAssignmentControllerApi.md#getaccountlimits) | **GET** /api/limits/assignments/account/{num} | 
*LimitAssignmentControllerApi* | [**getCustomerLimits**](docs/LimitAssignmentControllerApi.md#getcustomerlimits) | **GET** /api/limits/assignments/customer/{id} | 
*LimitAssignmentControllerApi* | [**getProductLimits**](docs/LimitAssignmentControllerApi.md#getproductlimits) | **GET** /api/limits/assignments/product/{code} | 
*LimitCheckControllerApi* | [**checkLimit**](docs/LimitCheckControllerApi.md#checklimit) | **POST** /api/limits/check | 
*LimitCheckControllerApi* | [**getEffectiveLimits**](docs/LimitCheckControllerApi.md#geteffectivelimits) | **GET** /api/limits/check/effective | 
*LimitDefinitionControllerApi* | [**activateLimit**](docs/LimitDefinitionControllerApi.md#activatelimit) | **PUT** /api/limits/definitions/{id}/activate | 
*LimitDefinitionControllerApi* | [**createLimit**](docs/LimitDefinitionControllerApi.md#createlimit) | **POST** /api/limits/definitions | 
*LimitDefinitionControllerApi* | [**deactivateLimit**](docs/LimitDefinitionControllerApi.md#deactivatelimit) | **PUT** /api/limits/definitions/{id}/deactivate | 
*LimitDefinitionControllerApi* | [**getAllLimits**](docs/LimitDefinitionControllerApi.md#getalllimits) | **GET** /api/limits/definitions | 
*LimitDefinitionControllerApi* | [**getLimit**](docs/LimitDefinitionControllerApi.md#getlimit) | **GET** /api/limits/definitions/{id} | 
*LimitDefinitionControllerApi* | [**updateLimit**](docs/LimitDefinitionControllerApi.md#updatelimit) | **PUT** /api/limits/definitions/{id} | 
*ProductControllerApi* | [**createProduct**](docs/ProductControllerApi.md#createproduct) | **POST** /api/products | Create a new product
*ProductControllerApi* | [**getProduct**](docs/ProductControllerApi.md#getproduct) | **GET** /api/products/{id} | 
*ProductControllerApi* | [**getProductByCode**](docs/ProductControllerApi.md#getproductbycode) | **GET** /api/products/code/{code} | 
*ProductControllerApi* | [**getProductDetail**](docs/ProductControllerApi.md#getproductdetail) | **GET** /api/products/{id}/detail | 
*ProductControllerApi* | [**searchProducts**](docs/ProductControllerApi.md#searchproducts) | **GET** /api/products/search | 
*ProductControllerApi* | [**updateProduct**](docs/ProductControllerApi.md#updateproduct) | **PUT** /api/products/{id} | Update an existing product
*ProductFeatureControllerApi* | [**addFeature**](docs/ProductFeatureControllerApi.md#addfeature) | **POST** /api/products/{productId}/versions/{versionId}/features | 
*ProductFeatureControllerApi* | [**deleteFeature**](docs/ProductFeatureControllerApi.md#deletefeature) | **DELETE** /api/products/{productId}/versions/{versionId}/features/{featureId} | 
*ProductFeatureControllerApi* | [**listFeatures**](docs/ProductFeatureControllerApi.md#listfeatures) | **GET** /api/products/{productId}/versions/{versionId}/features | 
*ProductFeatureControllerApi* | [**updateFeature**](docs/ProductFeatureControllerApi.md#updatefeature) | **PUT** /api/products/{productId}/versions/{versionId}/features/{featureId} | 
*ProductPricingControllerApi* | [**addFeeEntry**](docs/ProductPricingControllerApi.md#addfeeentry) | **POST** /api/products/{productId}/versions/{versionId}/fees | 
*ProductPricingControllerApi* | [**deleteFeeEntry**](docs/ProductPricingControllerApi.md#deletefeeentry) | **DELETE** /api/products/{productId}/versions/{versionId}/fees/{feeEntryId} | 
*ProductPricingControllerApi* | [**listFeeEntries**](docs/ProductPricingControllerApi.md#listfeeentries) | **GET** /api/products/{productId}/versions/{versionId}/fees | 
*ProductPricingControllerApi* | [**updateFeeEntry**](docs/ProductPricingControllerApi.md#updatefeeentry) | **PUT** /api/products/{productId}/versions/{versionId}/fees/{feeEntryId} | 
*ProductQueryControllerApi* | [**getActiveProductByCode**](docs/ProductQueryControllerApi.md#getactiveproductbycode) | **GET** /api/product-query/active | 
*ProductQueryControllerApi* | [**getProductVersionById**](docs/ProductQueryControllerApi.md#getproductversionbyid) | **GET** /api/product-query/version/{versionId} | 
*ProductQueryControllerApi* | [**getProductsByCustomerType**](docs/ProductQueryControllerApi.md#getproductsbycustomertype) | **GET** /api/product-query/customer-type/{type} | 
*ProductQueryControllerApi* | [**getProductsByFamily**](docs/ProductQueryControllerApi.md#getproductsbyfamily) | **GET** /api/product-query/family/{family} | 
*ProductSegmentControllerApi* | [**assignSegments**](docs/ProductSegmentControllerApi.md#assignsegments) | **PUT** /api/products/{productId}/versions/{versionId}/segments | 
*ProductSegmentControllerApi* | [**getSegments**](docs/ProductSegmentControllerApi.md#getsegments) | **GET** /api/products/{productId}/versions/{versionId}/segments | 
*ProductVersionControllerApi* | [**activate**](docs/ProductVersionControllerApi.md#activate) | **POST** /api/products/{productId}/versions/{versionId}/activate | 
*ProductVersionControllerApi* | [**approve**](docs/ProductVersionControllerApi.md#approve) | **POST** /api/products/{productId}/versions/{versionId}/approve | 
*ProductVersionControllerApi* | [**compareVersions**](docs/ProductVersionControllerApi.md#compareversions) | **GET** /api/products/{productId}/versions/compare | 
*ProductVersionControllerApi* | [**getVersionDetail**](docs/ProductVersionControllerApi.md#getversiondetail) | **GET** /api/products/{productId}/versions/{versionId} | 
*ProductVersionControllerApi* | [**listVersions**](docs/ProductVersionControllerApi.md#listversions) | **GET** /api/products/{productId}/versions | 
*ProductVersionControllerApi* | [**reject**](docs/ProductVersionControllerApi.md#reject) | **POST** /api/products/{productId}/versions/{versionId}/reject | 
*ProductVersionControllerApi* | [**retire**](docs/ProductVersionControllerApi.md#retire) | **POST** /api/products/{productId}/versions/{versionId}/retire | 
*ProductVersionControllerApi* | [**submitForApproval**](docs/ProductVersionControllerApi.md#submitforapproval) | **POST** /api/products/{productId}/versions/{versionId}/submit | 


### Documentation For Models

 - [AccountBalance](docs/AccountBalance.md)
 - [AccountHolderRequest](docs/AccountHolderRequest.md)
 - [AccountLimitResponse](docs/AccountLimitResponse.md)
 - [AccountOpeningRequest](docs/AccountOpeningRequest.md)
 - [AccountResponse](docs/AccountResponse.md)
 - [AccountStatement](docs/AccountStatement.md)
 - [AddressDto](docs/AddressDto.md)
 - [ApprovalActionRequest](docs/ApprovalActionRequest.md)
 - [ApprovalRequestResponse](docs/ApprovalRequestResponse.md)
 - [ApproveKYCRequest](docs/ApproveKYCRequest.md)
 - [AssignChargeRequest](docs/AssignChargeRequest.md)
 - [AssignLimitRequest](docs/AssignLimitRequest.md)
 - [AuthorizationResponse](docs/AuthorizationResponse.md)
 - [BranchResponse](docs/BranchResponse.md)
 - [BulkUploadResult](docs/BulkUploadResult.md)
 - [ChannelResponse](docs/ChannelResponse.md)
 - [ChargeCalculationRequest](docs/ChargeCalculationRequest.md)
 - [ChargeCalculationResponse](docs/ChargeCalculationResponse.md)
 - [ChargeDefinitionResponse](docs/ChargeDefinitionResponse.md)
 - [ChargeRuleResponse](docs/ChargeRuleResponse.md)
 - [ChargeTierResponse](docs/ChargeTierResponse.md)
 - [CountryResponse](docs/CountryResponse.md)
 - [CreateAuthorizationRequest](docs/CreateAuthorizationRequest.md)
 - [CreateBranchRequest](docs/CreateBranchRequest.md)
 - [CreateChannelRequest](docs/CreateChannelRequest.md)
 - [CreateChargeDefinitionRequest](docs/CreateChargeDefinitionRequest.md)
 - [CreateChargeRuleRequest](docs/CreateChargeRuleRequest.md)
 - [CreateCorporateCustomerRequest](docs/CreateCorporateCustomerRequest.md)
 - [CreateCountryRequest](docs/CreateCountryRequest.md)
 - [CreateCurrencyRequest](docs/CreateCurrencyRequest.md)
 - [CreateDocumentTypeRequest](docs/CreateDocumentTypeRequest.md)
 - [CreateEmploymentRequest](docs/CreateEmploymentRequest.md)
 - [CreateExchangeRateRequest](docs/CreateExchangeRateRequest.md)
 - [CreateFeeWaiverRequest](docs/CreateFeeWaiverRequest.md)
 - [CreateHolidayRequest](docs/CreateHolidayRequest.md)
 - [CreateIndividualCustomerRequest](docs/CreateIndividualCustomerRequest.md)
 - [CreateIndustryRequest](docs/CreateIndustryRequest.md)
 - [CreateInterestRateRequest](docs/CreateInterestRateRequest.md)
 - [CreateLimitDefinitionRequest](docs/CreateLimitDefinitionRequest.md)
 - [CreateProductRequest](docs/CreateProductRequest.md)
 - [CreateSMECustomerRequest](docs/CreateSMECustomerRequest.md)
 - [CurrencyResponse](docs/CurrencyResponse.md)
 - [CustomerChargeOverrideResponse](docs/CustomerChargeOverrideResponse.md)
 - [CustomerLimitResponse](docs/CustomerLimitResponse.md)
 - [CustomerResponse](docs/CustomerResponse.md)
 - [CustomerSearchCriteria](docs/CustomerSearchCriteria.md)
 - [DocumentTypeResponse](docs/DocumentTypeResponse.md)
 - [EffectiveLimitResponse](docs/EffectiveLimitResponse.md)
 - [EmploymentResponse](docs/EmploymentResponse.md)
 - [ExchangeRateResponse](docs/ExchangeRateResponse.md)
 - [FeatureDiff](docs/FeatureDiff.md)
 - [FeeDiff](docs/FeeDiff.md)
 - [FeeWaiverResponse](docs/FeeWaiverResponse.md)
 - [FieldDiff](docs/FieldDiff.md)
 - [HolidayResponse](docs/HolidayResponse.md)
 - [IndustryResponse](docs/IndustryResponse.md)
 - [InitiateKYCRequest](docs/InitiateKYCRequest.md)
 - [InterestRateResponse](docs/InterestRateResponse.md)
 - [InterestTierRequest](docs/InterestTierRequest.md)
 - [InterestTierResponse](docs/InterestTierResponse.md)
 - [KYCResponse](docs/KYCResponse.md)
 - [LimitCheckRequest](docs/LimitCheckRequest.md)
 - [LimitCheckResponse](docs/LimitCheckResponse.md)
 - [LimitDefinitionResponse](docs/LimitDefinitionResponse.md)
 - [PageAccountResponse](docs/PageAccountResponse.md)
 - [PageCustomerResponse](docs/PageCustomerResponse.md)
 - [PageProductResponse](docs/PageProductResponse.md)
 - [Pageable](docs/Pageable.md)
 - [PageableObject](docs/PageableObject.md)
 - [PhoneDto](docs/PhoneDto.md)
 - [ProductChargeResponse](docs/ProductChargeResponse.md)
 - [ProductCustomerSegmentResponse](docs/ProductCustomerSegmentResponse.md)
 - [ProductDetailResponse](docs/ProductDetailResponse.md)
 - [ProductFeatureRequest](docs/ProductFeatureRequest.md)
 - [ProductFeatureResponse](docs/ProductFeatureResponse.md)
 - [ProductFeeEntryRequest](docs/ProductFeeEntryRequest.md)
 - [ProductFeeEntryResponse](docs/ProductFeeEntryResponse.md)
 - [ProductFeeTierRequest](docs/ProductFeeTierRequest.md)
 - [ProductFeeTierResponse](docs/ProductFeeTierResponse.md)
 - [ProductInfo](docs/ProductInfo.md)
 - [ProductLimitResponse](docs/ProductLimitResponse.md)
 - [ProductResponse](docs/ProductResponse.md)
 - [ProductSegmentRequest](docs/ProductSegmentRequest.md)
 - [ProductVersionDiffResponse](docs/ProductVersionDiffResponse.md)
 - [ProductVersionResponse](docs/ProductVersionResponse.md)
 - [RejectKYCRequest](docs/RejectKYCRequest.md)
 - [RejectProductRequest](docs/RejectProductRequest.md)
 - [SegmentDiff](docs/SegmentDiff.md)
 - [SegmentInfo](docs/SegmentInfo.md)
 - [SortObject](docs/SortObject.md)
 - [SubmitDocumentsRequest](docs/SubmitDocumentsRequest.md)
 - [TierRequest](docs/TierRequest.md)
 - [TransactionEntry](docs/TransactionEntry.md)
 - [UpdateAuthorizationRequest](docs/UpdateAuthorizationRequest.md)
 - [UpdateBranchRequest](docs/UpdateBranchRequest.md)
 - [UpdateChannelRequest](docs/UpdateChannelRequest.md)
 - [UpdateChargeDefinitionRequest](docs/UpdateChargeDefinitionRequest.md)
 - [UpdateCountryRequest](docs/UpdateCountryRequest.md)
 - [UpdateCurrencyRequest](docs/UpdateCurrencyRequest.md)
 - [UpdateCustomerRequest](docs/UpdateCustomerRequest.md)
 - [UpdateDocumentTypeRequest](docs/UpdateDocumentTypeRequest.md)
 - [UpdateKYCRequest](docs/UpdateKYCRequest.md)
 - [UpdateProductRequest](docs/UpdateProductRequest.md)


<a id="documentation-for-authorization"></a>
## Documentation For Authorization

Endpoints do not require authorization.

