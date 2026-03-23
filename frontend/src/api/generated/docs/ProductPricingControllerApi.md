# ProductPricingControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addFeeEntry**](#addfeeentry) | **POST** /api/products/{productId}/versions/{versionId}/fees | |
|[**deleteFeeEntry**](#deletefeeentry) | **DELETE** /api/products/{productId}/versions/{versionId}/fees/{feeEntryId} | |
|[**listFeeEntries**](#listfeeentries) | **GET** /api/products/{productId}/versions/{versionId}/fees | |
|[**updateFeeEntry**](#updatefeeentry) | **PUT** /api/products/{productId}/versions/{versionId}/fees/{feeEntryId} | |

# **addFeeEntry**
> ProductFeeEntryResponse addFeeEntry(productFeeEntryRequest)


### Example

```typescript
import {
    ProductPricingControllerApi,
    Configuration,
    ProductFeeEntryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductPricingControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let productFeeEntryRequest: ProductFeeEntryRequest; //

const { status, data } = await apiInstance.addFeeEntry(
    productId,
    versionId,
    xUsername,
    productFeeEntryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productFeeEntryRequest** | **ProductFeeEntryRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductFeeEntryResponse**

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

# **deleteFeeEntry**
> deleteFeeEntry()


### Example

```typescript
import {
    ProductPricingControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductPricingControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let feeEntryId: number; // (default to undefined)

const { status, data } = await apiInstance.deleteFeeEntry(
    productId,
    versionId,
    feeEntryId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **feeEntryId** | [**number**] |  | defaults to undefined|


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

# **listFeeEntries**
> Array<ProductFeeEntryResponse> listFeeEntries()


### Example

```typescript
import {
    ProductPricingControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductPricingControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)

const { status, data } = await apiInstance.listFeeEntries(
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

**Array<ProductFeeEntryResponse>**

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

# **updateFeeEntry**
> ProductFeeEntryResponse updateFeeEntry(productFeeEntryRequest)


### Example

```typescript
import {
    ProductPricingControllerApi,
    Configuration,
    ProductFeeEntryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductPricingControllerApi(configuration);

let productId: number; // (default to undefined)
let versionId: number; // (default to undefined)
let feeEntryId: number; // (default to undefined)
let xUsername: string; // (default to undefined)
let productFeeEntryRequest: ProductFeeEntryRequest; //

const { status, data } = await apiInstance.updateFeeEntry(
    productId,
    versionId,
    feeEntryId,
    xUsername,
    productFeeEntryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productFeeEntryRequest** | **ProductFeeEntryRequest**|  | |
| **productId** | [**number**] |  | defaults to undefined|
| **versionId** | [**number**] |  | defaults to undefined|
| **feeEntryId** | [**number**] |  | defaults to undefined|
| **xUsername** | [**string**] |  | defaults to undefined|


### Return type

**ProductFeeEntryResponse**

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

