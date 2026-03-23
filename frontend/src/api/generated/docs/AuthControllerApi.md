# AuthControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**disableMfa**](#disablemfa) | **POST** /api/auth/mfa/disable | |
|[**enableMfa**](#enablemfa) | **POST** /api/auth/mfa/enable | |
|[**enrollMfa**](#enrollmfa) | **POST** /api/auth/mfa/enroll | |
|[**getMfaStatus**](#getmfastatus) | **POST** /api/auth/mfa/status | |
|[**verifyMfa**](#verifymfa) | **POST** /api/auth/mfa/verify | |

# **disableMfa**
> EnableMfa200Response disableMfa()


### Example

```typescript
import {
    AuthControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthControllerApi(configuration);

const { status, data } = await apiInstance.disableMfa();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**EnableMfa200Response**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **enableMfa**
> EnableMfa200Response enableMfa(mfaRequest)


### Example

```typescript
import {
    AuthControllerApi,
    Configuration,
    MfaRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthControllerApi(configuration);

let mfaRequest: MfaRequest; //

const { status, data } = await apiInstance.enableMfa(
    mfaRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mfaRequest** | **MfaRequest**|  | |


### Return type

**EnableMfa200Response**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **enrollMfa**
> MfaEnrollResponse enrollMfa()


### Example

```typescript
import {
    AuthControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthControllerApi(configuration);

const { status, data } = await apiInstance.enrollMfa();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**MfaEnrollResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getMfaStatus**
> EnableMfa200Response getMfaStatus()


### Example

```typescript
import {
    AuthControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthControllerApi(configuration);

const { status, data } = await apiInstance.getMfaStatus();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**EnableMfa200Response**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **verifyMfa**
> VerifyMfa200Response verifyMfa(mfaRequest)


### Example

```typescript
import {
    AuthControllerApi,
    Configuration,
    MfaRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new AuthControllerApi(configuration);

let mfaRequest: MfaRequest; //

const { status, data } = await apiInstance.verifyMfa(
    mfaRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **mfaRequest** | **MfaRequest**|  | |


### Return type

**VerifyMfa200Response**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

