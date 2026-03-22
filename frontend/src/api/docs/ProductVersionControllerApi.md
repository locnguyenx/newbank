# ProductVersionControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**activate**](#activate) | **POST** /api/products/{productId}/versions/{versionId}/activate | |
|[**approve**](#approve) | **POST** /api/products/{productId}/versions/{versionId}/approve | |
|[**compareVersions**](#compareversions) | **GET** /api/products/{productId}/versions/compare | |
|[**getVersionDetail**](#getversiondetail) | **GET** /api/products/{productId}/versions/{versionId} | |
|[**listVersions**](#listversions) | **GET** /api/products/{productId}/versions | |
|[**reject**](#reject) | **POST** /api/products/{productId}/versions/{versionId}/reject | |
|[**retire**](#retire) | **POST** /api/products/{productId}/versions/{versionId}/retire | |
|[**submitForApproval**](#submitforapproval) | **POST** /api/products/{productId}/versions/{versionId}/submit | |

# **activate**
> ProductVersionResponse activate()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)

const { status, data } = await apiInstance.activate(
    productId,
    versionId,
    xUsername
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

# **approve**
> ProductVersionResponse approve()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)

const { status, data } = await apiInstance.approve(
    productId,
    versionId,
    xUsername
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

# **compareVersions**
> ProductVersionDiffResponse compareVersions()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let v1: number; // (default to undefined)
let v2: number; // (default to undefined)

const { status, data } = await apiInstance.compareVersions(
    productId,
    v1,
    v2
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **v1** | [**number**] |  | defaults to undefined|
| **v2** | [**number**] |  | defaults to undefined|


### Return type

**ProductVersionDiffResponse**

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

# **getVersionDetail**
> ProductVersionResponse getVersionDetail()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)

const { status, data } = await apiInstance.getVersionDetail(
    productId,
    versionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

# **listVersions**
> Array<ProductVersionResponse> listVersions()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)

const { status, data } = await apiInstance.listVersions(
    productId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|


### Return type

**Array<ProductVersionResponse>**

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

# **reject**
> ProductVersionResponse reject(rejectProductRequest)


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration,
    RejectProductRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let rejectProductRequest: RejectProductRequest; //

const { status, data } = await apiInstance.reject(
    productId,
    versionId,
    xUsername,
    rejectProductRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rejectProductRequest** | **RejectProductRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

# **retire**
> ProductVersionResponse retire()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)

const { status, data } = await apiInstance.retire(
    productId,
    versionId,
    xUsername
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

# **submitForApproval**
> ProductVersionResponse submitForApproval()


### Example

```typescript
import {
    ProductVersionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductVersionControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)

const { status, data } = await apiInstance.submitForApproval(
    productId,
    versionId,
    xUsername
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductVersionResponse**

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

