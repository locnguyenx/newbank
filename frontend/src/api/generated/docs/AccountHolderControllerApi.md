# AccountHolderControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addHolder**](#addholder) | **POST** /api/accounts/{accountNumber}/holders | |
|[**removeHolder**](#removeholder) | **DELETE** /api/accounts/{accountNumber}/holders/{holderId} | |

# **addHolder**
> addHolder(accountHolderRequest)


### Example

```typescript
import {
    AccountHolderControllerApi,
    Configuration,
    AccountHolderRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountHolderControllerApi(configuration);

let accountNumber: string; // (default to undefined)
let accountHolderRequest: AccountHolderRequest; //

const { status, data } = await apiInstance.addHolder(
    accountNumber,
    accountHolderRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountHolderRequest** | **AccountHolderRequest**|  | |
| **accountNumber** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **removeHolder**
> removeHolder()


### Example

```typescript
import {
    AccountHolderControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AccountHolderControllerApi(configuration);

let accountNumber: string; // (default to undefined)
let holderId: number; // (default to undefined)

const { status, data } = await apiInstance.removeHolder(
    accountNumber,
    holderId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|
| **holderId** | [**number**] |  | defaults to undefined|


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
|**204** | No Content |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

