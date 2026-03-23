# ProductQueryControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**getActiveProductByCode**](#getactiveproductbycode) | **GET** /api/product-query/active | |
|[**getProductVersionById**](#getproductversionbyid) | **GET** /api/product-query/version/{versionId} | |
|[**getProductsByCustomerType**](#getproductsbycustomertype) | **GET** /api/product-query/customer-type/{type} | |
|[**getProductsByFamily**](#getproductsbyfamily) | **GET** /api/product-query/family/{family} | |

# **getActiveProductByCode**
> ProductVersionResponse getActiveProductByCode()


### Example

```typescript
import {
    ProductQueryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductQueryControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getActiveProductByCode(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


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

# **getProductVersionById**
> ProductVersionResponse getProductVersionById()


### Example

```typescript
import {
    ProductQueryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductQueryControllerApi(configuration);

let versionId: number; // (default to undefined)

const { status, data } = await apiInstance.getProductVersionById(
    versionId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
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

# **getProductsByCustomerType**
> Array<ProductVersionResponse> getProductsByCustomerType()


### Example

```typescript
import {
    ProductQueryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductQueryControllerApi(configuration);

let type: 'CORPORATE' | 'SME' | 'INDIVIDUAL'; // (default to undefined)
let status: 'DRAFT' | 'PENDING_APPROVAL' | 'APPROVED' | 'ACTIVE' | 'SUPERSEDED' | 'RETIRED'; // (optional) (default to undefined)

const { status, data } = await apiInstance.getProductsByCustomerType(
    type,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **type** | [**&#39;CORPORATE&#39; | &#39;SME&#39; | &#39;INDIVIDUAL&#39;**]**Array<&#39;CORPORATE&#39; &#124; &#39;SME&#39; &#124; &#39;INDIVIDUAL&#39;>** |  | defaults to undefined|
| **status** | [**&#39;DRAFT&#39; | &#39;PENDING_APPROVAL&#39; | &#39;APPROVED&#39; | &#39;ACTIVE&#39; | &#39;SUPERSEDED&#39; | &#39;RETIRED&#39;**]**Array<&#39;DRAFT&#39; &#124; &#39;PENDING_APPROVAL&#39; &#124; &#39;APPROVED&#39; &#124; &#39;ACTIVE&#39; &#124; &#39;SUPERSEDED&#39; &#124; &#39;RETIRED&#39;>** |  | (optional) defaults to undefined|


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

# **getProductsByFamily**
> Array<ProductVersionResponse> getProductsByFamily()


### Example

```typescript
import {
    ProductQueryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductQueryControllerApi(configuration);

let family: 'ACCOUNT' | 'PAYMENT' | 'TRADE_FINANCE'; // (default to undefined)
let status: 'DRAFT' | 'PENDING_APPROVAL' | 'APPROVED' | 'ACTIVE' | 'SUPERSEDED' | 'RETIRED'; // (optional) (default to undefined)

const { status, data } = await apiInstance.getProductsByFamily(
    family,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **family** | [**&#39;ACCOUNT&#39; | &#39;PAYMENT&#39; | &#39;TRADE_FINANCE&#39;**]**Array<&#39;ACCOUNT&#39; &#124; &#39;PAYMENT&#39; &#124; &#39;TRADE_FINANCE&#39;>** |  | defaults to undefined|
| **status** | [**&#39;DRAFT&#39; | &#39;PENDING_APPROVAL&#39; | &#39;APPROVED&#39; | &#39;ACTIVE&#39; | &#39;SUPERSEDED&#39; | &#39;RETIRED&#39;**]**Array<&#39;DRAFT&#39; &#124; &#39;PENDING_APPROVAL&#39; &#124; &#39;APPROVED&#39; &#124; &#39;ACTIVE&#39; &#124; &#39;SUPERSEDED&#39; &#124; &#39;RETIRED&#39;>** |  | (optional) defaults to undefined|


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

