# ProductFeatureControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addFeature**](#addfeature) | **POST** /api/products/{productId}/versions/{versionId}/features | |
|[**deleteFeature**](#deletefeature) | **DELETE** /api/products/{productId}/versions/{versionId}/features/{featureId} | |
|[**listFeatures**](#listfeatures) | **GET** /api/products/{productId}/versions/{versionId}/features | |
|[**updateFeature**](#updatefeature) | **PUT** /api/products/{productId}/versions/{versionId}/features/{featureId} | |

# **addFeature**
> ProductFeatureResponse addFeature(productFeatureRequest)


### Example

```typescript
import {
    ProductFeatureControllerApi,
    Configuration,
    ProductFeatureRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductFeatureControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let productFeatureRequest: ProductFeatureRequest; //

const { status, data } = await apiInstance.addFeature(
    productId,
    versionId,
    xUsername,
    productFeatureRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productFeatureRequest** | **ProductFeatureRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductFeatureResponse**

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

# **deleteFeature**
> deleteFeature()


### Example

```typescript
import {
    ProductFeatureControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductFeatureControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let featureId: number; // (default to undefined)

const { status, data } = await apiInstance.deleteFeature(
    productId,
    versionId,
    featureId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **featureId** | [**number**] |  | defaults to undefined|


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

# **listFeatures**
> Array<ProductFeatureResponse> listFeatures()


### Example

```typescript
import {
    ProductFeatureControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductFeatureControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)

const { status, data } = await apiInstance.listFeatures(
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

**Array<ProductFeatureResponse>**

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

# **updateFeature**
> ProductFeatureResponse updateFeature(productFeatureRequest)


### Example

```typescript
import {
    ProductFeatureControllerApi,
    Configuration,
    ProductFeatureRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductFeatureControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let featureId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let productFeatureRequest: ProductFeatureRequest; //

const { status, data } = await apiInstance.updateFeature(
    productId,
    versionId,
    featureId,
    xUsername,
    productFeatureRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productFeatureRequest** | **ProductFeatureRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **featureId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductFeatureResponse**

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

