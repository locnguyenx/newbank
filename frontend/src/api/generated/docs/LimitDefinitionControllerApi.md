# LimitDefinitionControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**activateLimit**](#activatelimit) | **PUT** /api/limits/definitions/{id}/activate | |
|[**createLimit**](#createlimit) | **POST** /api/limits/definitions | |
|[**deactivateLimit**](#deactivatelimit) | **PUT** /api/limits/definitions/{id}/deactivate | |
|[**getAllLimits**](#getalllimits) | **GET** /api/limits/definitions | |
|[**getLimit**](#getlimit) | **GET** /api/limits/definitions/{id} | |
|[**updateLimit**](#updatelimit) | **PUT** /api/limits/definitions/{id} | |

# **activateLimit**
> LimitDefinitionResponse activateLimit()


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.activateLimit(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**LimitDefinitionResponse**

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

# **createLimit**
> LimitDefinitionResponse createLimit(createLimitDefinitionRequest)


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration,
    CreateLimitDefinitionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let createLimitDefinitionRequest: CreateLimitDefinitionRequest; //

const { status, data } = await apiInstance.createLimit(
    createLimitDefinitionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createLimitDefinitionRequest** | **CreateLimitDefinitionRequest**|  | |


### Return type

**LimitDefinitionResponse**

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

# **deactivateLimit**
> LimitDefinitionResponse deactivateLimit()


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deactivateLimit(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**LimitDefinitionResponse**

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

# **getAllLimits**
> Array<LimitDefinitionResponse> getAllLimits()


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let status: 'ACTIVE' | 'INACTIVE'; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAllLimits(
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **status** | [**&#39;ACTIVE&#39; | &#39;INACTIVE&#39;**]**Array<&#39;ACTIVE&#39; &#124; &#39;INACTIVE&#39;>** |  | (optional) defaults to undefined|


### Return type

**Array<LimitDefinitionResponse>**

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

# **getLimit**
> LimitDefinitionResponse getLimit()


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getLimit(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**LimitDefinitionResponse**

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

# **updateLimit**
> LimitDefinitionResponse updateLimit(createLimitDefinitionRequest)


### Example

```typescript
import {
    LimitDefinitionControllerApi,
    Configuration,
    CreateLimitDefinitionRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new LimitDefinitionControllerApi(configuration);

let id: number; // (default to undefined)
let createLimitDefinitionRequest: CreateLimitDefinitionRequest; //

const { status, data } = await apiInstance.updateLimit(
    id,
    createLimitDefinitionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createLimitDefinitionRequest** | **CreateLimitDefinitionRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**LimitDefinitionResponse**

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

