# ChargeAssignmentControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**assignToCustomer1**](#assigntocustomer1) | **POST** /api/charges/assignments/customer | |
|[**assignToProduct1**](#assigntoproduct1) | **POST** /api/charges/assignments/product | |
|[**getCustomerOverrides**](#getcustomeroverrides) | **GET** /api/charges/assignments/customer/{customerId} | |
|[**getProductCharges**](#getproductcharges) | **GET** /api/charges/assignments/product/{productCode} | |
|[**unassignFromCustomer**](#unassignfromcustomer) | **DELETE** /api/charges/assignments/customer | |
|[**unassignFromProduct**](#unassignfromproduct) | **DELETE** /api/charges/assignments/product | |

# **assignToCustomer1**
> CustomerChargeOverrideResponse assignToCustomer1(assignChargeRequest)


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration,
    AssignChargeRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let assignChargeRequest: AssignChargeRequest; //

const { status, data } = await apiInstance.assignToCustomer1(
    assignChargeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assignChargeRequest** | **AssignChargeRequest**|  | |


### Return type

**CustomerChargeOverrideResponse**

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

# **assignToProduct1**
> ProductChargeResponse assignToProduct1(assignChargeRequest)


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration,
    AssignChargeRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let assignChargeRequest: AssignChargeRequest; //

const { status, data } = await apiInstance.assignToProduct1(
    assignChargeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **assignChargeRequest** | **AssignChargeRequest**|  | |


### Return type

**ProductChargeResponse**

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

# **getCustomerOverrides**
> Array<CustomerChargeOverrideResponse> getCustomerOverrides()


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let customerId: number; // (default to undefined)

const { status, data } = await apiInstance.getCustomerOverrides(
    customerId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **customerId** | [**number**] |  | defaults to undefined|


### Return type

**Array<CustomerChargeOverrideResponse>**

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

# **getProductCharges**
> Array<ProductChargeResponse> getProductCharges()


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let productCode: string; // (default to undefined)

const { status, data } = await apiInstance.getProductCharges(
    productCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productCode** | [**string**] |  | defaults to undefined|


### Return type

**Array<ProductChargeResponse>**

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

# **unassignFromCustomer**
> unassignFromCustomer()


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let chargeId: number; // (default to undefined)
let customerId: number; // (default to undefined)

const { status, data } = await apiInstance.unassignFromCustomer(
    chargeId,
    customerId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeId** | [**number**] |  | defaults to undefined|
| **customerId** | [**number**] |  | defaults to undefined|


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

# **unassignFromProduct**
> unassignFromProduct()


### Example

```typescript
import {
    ChargeAssignmentControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeAssignmentControllerApi(configuration);

let chargeId: number; // (default to undefined)
let productCode: string; // (default to undefined)

const { status, data } = await apiInstance.unassignFromProduct(
    chargeId,
    productCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeId** | [**number**] |  | defaults to undefined|
| **productCode** | [**string**] |  | defaults to undefined|


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

