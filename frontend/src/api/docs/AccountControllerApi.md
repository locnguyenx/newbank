# AccountControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**closeAccount**](#closeaccount) | **PUT** /api/accounts/{accountNumber}/close | |
|[**freezeAccount**](#freezeaccount) | **PUT** /api/accounts/{accountNumber}/freeze | |
|[**getAccountBalance**](#getaccountbalance) | **GET** /api/accounts/{accountNumber}/balance | |
|[**getAccountDetails**](#getaccountdetails) | **GET** /api/accounts/{accountNumber} | |
|[**getAccountStatement**](#getaccountstatement) | **GET** /api/accounts/{accountNumber}/statement | |
|[**getAccounts**](#getaccounts) | **GET** /api/accounts | |
|[**openAccount**](#openaccount) | **POST** /api/accounts | |
|[**unfreezeAccount**](#unfreezeaccount) | **PUT** /api/accounts/{accountNumber}/unfreeze | |

# **closeAccount**
> closeAccount()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)

const { status, data } = await apiInstance.closeAccount(
    accountNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **freezeAccount**
> freezeAccount()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)

const { status, data } = await apiInstance.freezeAccount(
    accountNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAccountBalance**
> AccountBalance getAccountBalance()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)

const { status, data } = await apiInstance.getAccountBalance(
    accountNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

**AccountBalance**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAccountDetails**
> object getAccountDetails()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)

const { status, data } = await apiInstance.getAccountDetails(
    accountNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

**object**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAccountStatement**
> AccountStatement getAccountStatement()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)
let fromDate: string; // (optional) (default to undefined)
let toDate: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAccountStatement(
    accountNumber,
    fromDate,
    toDate
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|
| **fromDate** | [**string**] |  | (optional) defaults to undefined|
| **toDate** | [**string**] |  | (optional) defaults to undefined|


### Return type

**AccountStatement**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getAccounts**
> PageAccountResponse getAccounts()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let search: string; // (optional) (default to undefined)
let type: 'CURRENT' | 'SAVINGS' | 'FIXED_DEPOSIT' | 'LOAN' | 'ESCROW'; // (optional) (default to undefined)
let status: 'PENDING' | 'ACTIVE' | 'DORMANT' | 'FROZEN' | 'CLOSED'; // (optional) (default to undefined)
let customerId: number; // (optional) (default to undefined)
let page: number; // (optional) (default to 0)
let size: number; // (optional) (default to 10)

const { status, data } = await apiInstance.getAccounts(
    search,
    type,
    status,
    customerId,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **search** | [**string**] |  | (optional) defaults to undefined|
| **type** | [**&#39;CURRENT&#39; | &#39;SAVINGS&#39; | &#39;FIXED_DEPOSIT&#39; | &#39;LOAN&#39; | &#39;ESCROW&#39;**]**Array<&#39;CURRENT&#39; &#124; &#39;SAVINGS&#39; &#124; &#39;FIXED_DEPOSIT&#39; &#124; &#39;LOAN&#39; &#124; &#39;ESCROW&#39;>** |  | (optional) defaults to undefined|
| **status** | [**&#39;PENDING&#39; | &#39;ACTIVE&#39; | &#39;DORMANT&#39; | &#39;FROZEN&#39; | &#39;CLOSED&#39;**]**Array<&#39;PENDING&#39; &#124; &#39;ACTIVE&#39; &#124; &#39;DORMANT&#39; &#124; &#39;FROZEN&#39; &#124; &#39;CLOSED&#39;>** |  | (optional) defaults to undefined|
| **customerId** | [**number**] |  | (optional) defaults to undefined|
| **page** | [**number**] |  | (optional) defaults to 0|
| **size** | [**number**] |  | (optional) defaults to 10|


### Return type

**PageAccountResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **openAccount**
> AccountResponse openAccount(accountOpeningRequest)


### Example

```typescript
import {
    AccountControllerApi,
    Configuration,
    AccountOpeningRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountOpeningRequest: AccountOpeningRequest; //

const { status, data } = await apiInstance.openAccount(
    accountOpeningRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountOpeningRequest** | **AccountOpeningRequest**|  | |


### Return type

**AccountResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **unfreezeAccount**
> unfreezeAccount()


### Example

```typescript
import {
    AccountControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new AccountControllerApi(configuration);

let accountNumber: string; // (default to undefined)

const { status, data } = await apiInstance.unfreezeAccount(
    accountNumber
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

