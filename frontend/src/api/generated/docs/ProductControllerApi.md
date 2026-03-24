# ProductControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createProduct**](#createproduct) | **POST** /api/products | Create a new product|
|[**getProduct**](#getproduct) | **GET** /api/products/{id} | |
|[**getProductByCode**](#getproductbycode) | **GET** /api/products/code/{code} | |
|[**getProductDetail**](#getproductdetail) | **GET** /api/products/{id}/detail | |
|[**searchProducts**](#searchproducts) | **GET** /api/products/search | |
|[**updateProduct**](#updateproduct) | **PUT** /api/products/{id} | Update an existing product|

# **createProduct**
> ProductResponse createProduct(createProductRequest)

Create product with basic info. Requires X-Username header for audit.

### Example

```typescript
import {
    ProductControllerApi,
    Configuration,
    CreateProductRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let xUsername: string; //Username of the user performing the action (default to undefined)
let createProductRequest: CreateProductRequest; //

const { status, data } = await apiInstance.createProduct(
    xUsername,
    createProductRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createProductRequest** | **CreateProductRequest**|  | |
| **xUsername** | [**string**] | Username of the user performing the action | defaults to undefined|


### Return type

**ProductResponse**

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

# **getProduct**
> ProductResponse getProduct()


### Example

```typescript
import {
    ProductControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getProduct(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ProductResponse**

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

# **getProductByCode**
> ProductResponse getProductByCode()


### Example

```typescript
import {
    ProductControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getProductByCode(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**ProductResponse**

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

# **getProductDetail**
> ProductDetailResponse getProductDetail()


### Example

```typescript
import {
    ProductControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getProductDetail(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ProductDetailResponse**

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

# **searchProducts**
> PageProductResponse searchProducts()


### Example

```typescript
import {
    ProductControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let family: string; // (optional) (default to undefined)
let status: string; // (optional) (default to undefined)
let customerType: string; // (optional) (default to undefined)
let page: number; // (optional) (default to 0)
let size: number; // (optional) (default to 10)

const { status, data } = await apiInstance.searchProducts(
    family,
    status,
    customerType,
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **family** | [**string**] |  | (optional) defaults to undefined|
| **status** | [**string**] |  | (optional) defaults to undefined|
| **customerType** | [**string**] |  | (optional) defaults to undefined|
| **page** | [**number**] |  | (optional) defaults to 0|
| **size** | [**number**] |  | (optional) defaults to 10|


### Return type

**PageProductResponse**

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

# **updateProduct**
> ProductResponse updateProduct(updateProductRequest)


### Example

```typescript
import {
    ProductControllerApi,
    Configuration,
    UpdateProductRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new ProductControllerApi(configuration);

let id: number; //Product ID (default to undefined)
let xUsername: string; //Username of the user performing the action (default to undefined)
let updateProductRequest: UpdateProductRequest; //

const { status, data } = await apiInstance.updateProduct(
    id,
    xUsername,
    updateProductRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateProductRequest** | **UpdateProductRequest**|  | |
| **id** | [**number**] | Product ID | defaults to undefined|
| **xUsername** | [**string**] | Username of the user performing the action | defaults to undefined|


### Return type

**ProductResponse**

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

