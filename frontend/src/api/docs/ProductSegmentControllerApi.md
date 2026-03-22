# ProductSegmentControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**assignSegments**](#assignsegments) | **PUT** /api/products/{productId}/versions/{versionId}/segments | |
|[**getSegments**](#getsegments) | **GET** /api/products/{productId}/versions/{versionId}/segments | |

# **assignSegments**
> Array<ProductCustomerSegmentResponse> assignSegments(productSegmentRequest)


### Example

```typescript
import {
    ProductSegmentControllerApi,
    Configuration,
    ProductSegmentRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductSegmentControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let productSegmentRequest: ProductSegmentRequest; //

const { status, data } = await apiInstance.assignSegments(
    productId,
    versionId,
    xUsername,
    productSegmentRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productSegmentRequest** | **ProductSegmentRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**Array<ProductCustomerSegmentResponse>**

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

# **getSegments**
> Array<ProductCustomerSegmentResponse> getSegments()


### Example

```typescript
import {
    ProductSegmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ProductSegmentControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)

const { status, data } = await apiInstance.getSegments(
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

**Array<ProductCustomerSegmentResponse>**

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

