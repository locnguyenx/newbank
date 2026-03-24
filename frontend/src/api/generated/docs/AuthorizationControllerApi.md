# AuthorizationControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createAuthorization**](#createauthorization) | **POST** /api/authorizations | |
|[**getAuthorizationById**](#getauthorizationbyid) | **GET** /api/authorizations/{id} | |
|[**getAuthorizationsByCustomer**](#getauthorizationsbycustomer) | **GET** /api/authorizations/customer/{customerId} | |
|[**revokeAuthorization**](#revokeauthorization) | **POST** /api/authorizations/{id}/revoke | |
|[**updateAuthorization**](#updateauthorization) | **PUT** /api/authorizations/{id} | |

# **createAuthorization**
> AuthorizationResponse createAuthorization(createAuthorizationRequest)


### Example

```typescript
import {
    AuthorizationControllerApi,
    Configuration,
    CreateAuthorizationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationControllerApi(configuration);

let customerId: number; // (default to undefined)
let createAuthorizationRequest: CreateAuthorizationRequest; //

const { status, data } = await apiInstance.createAuthorization(
    customerId,
    createAuthorizationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createAuthorizationRequest** | **CreateAuthorizationRequest**|  | |
| **customerId** | [**number**] |  | defaults to undefined|


### Return type

**AuthorizationResponse**

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

# **getAuthorizationById**
> AuthorizationResponse getAuthorizationById()


### Example

```typescript
import {
    AuthorizationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getAuthorizationById(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**AuthorizationResponse**

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

# **getAuthorizationsByCustomer**
> Array<AuthorizationResponse> getAuthorizationsByCustomer()


### Example

```typescript
import {
    AuthorizationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationControllerApi(configuration);

let customerId: number; // (default to undefined)

const { status, data } = await apiInstance.getAuthorizationsByCustomer(
    customerId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **customerId** | [**number**] |  | defaults to undefined|


### Return type

**Array<AuthorizationResponse>**

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

# **revokeAuthorization**
> revokeAuthorization()


### Example

```typescript
import {
    AuthorizationControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.revokeAuthorization(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


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

# **updateAuthorization**
> AuthorizationResponse updateAuthorization(updateAuthorizationRequest)


### Example

```typescript
import {
    AuthorizationControllerApi,
    Configuration,
    UpdateAuthorizationRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthorizationControllerApi(configuration);

let id: number; // (default to undefined)
let updateAuthorizationRequest: UpdateAuthorizationRequest; //

const { status, data } = await apiInstance.updateAuthorization(
    id,
    updateAuthorizationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateAuthorizationRequest** | **UpdateAuthorizationRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**AuthorizationResponse**

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

