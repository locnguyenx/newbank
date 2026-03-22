# LimitAssignmentControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**assignToAccount**](#assigntoaccount) | **POST** /api/limits/assignments/account | |
|[**assignToCustomer**](#assigntocustomer) | **POST** /api/limits/assignments/customer | |
|[**assignToProduct**](#assigntoproduct) | **POST** /api/limits/assignments/product | |
|[**getAccountLimits**](#getaccountlimits) | **GET** /api/limits/assignments/account/{num} | |
|[**getCustomerLimits**](#getcustomerlimits) | **GET** /api/limits/assignments/customer/{id} | |
|[**getProductLimits**](#getproductlimits) | **GET** /api/limits/assignments/product/{code} | |

# **assignToAccount**
> AccountLimitResponse assignToAccount(assignLimitRequest)


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration,
    AssignLimitRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let assignLimitRequest: AssignLimitRequest; //

const { status, data } = await apiInstance.assignToAccount(
    assignLimitRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assignLimitRequest** | **AssignLimitRequest**|  | |


### Return type

**AccountLimitResponse**

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

# **assignToCustomer**
> CustomerLimitResponse assignToCustomer(assignLimitRequest)


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration,
    AssignLimitRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let assignLimitRequest: AssignLimitRequest; //

const { status, data } = await apiInstance.assignToCustomer(
    assignLimitRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assignLimitRequest** | **AssignLimitRequest**|  | |


### Return type

**CustomerLimitResponse**

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

# **assignToProduct**
> ProductLimitResponse assignToProduct(assignLimitRequest)


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration,
    AssignLimitRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let assignLimitRequest: AssignLimitRequest; //

const { status, data } = await apiInstance.assignToProduct(
    assignLimitRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assignLimitRequest** | **AssignLimitRequest**|  | |


### Return type

**ProductLimitResponse**

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

# **getAccountLimits**
> Array<AccountLimitResponse> getAccountLimits()


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let num: string; // (default to undefined)

const { status, data } = await apiInstance.getAccountLimits(
    num
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **num** | [**string**] |  | defaults to undefined|


### Return type

**Array<AccountLimitResponse>**

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

# **getCustomerLimits**
> Array<CustomerLimitResponse> getCustomerLimits()


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getCustomerLimits(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**Array<CustomerLimitResponse>**

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

# **getProductLimits**
> Array<ProductLimitResponse> getProductLimits()


### Example

```typescript
import {
    LimitAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitAssignmentControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getProductLimits(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**Array<ProductLimitResponse>**

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

