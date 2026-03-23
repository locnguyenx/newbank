# CustomerControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createCorporate**](#createcorporate) | **POST** /api/customers/corporate | Create a new corporate customer|
|[**createIndividual**](#createindividual) | **POST** /api/customers/individual | Create a new individual customer|
|[**createSME**](#createsme) | **POST** /api/customers/sme | Create a new SME customer|
|[**getCustomerById**](#getcustomerbyid) | **GET** /api/customers/{id} | Get customer by ID|
|[**listCustomers**](#listcustomers) | **GET** /api/customers | List all customers|
|[**searchCustomers**](#searchcustomers) | **GET** /api/customers/search | Search customers|
|[**updateCustomer**](#updatecustomer) | **PUT** /api/customers/{id} | Update customer|

# **createCorporate**
> CustomerResponse createCorporate(createCorporateCustomerRequest)

Creates a new corporate customer with registration details, industry, and annual revenue

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    CreateCorporateCustomerRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let createCorporateCustomerRequest: CreateCorporateCustomerRequest; //

const { status, data } = await apiInstance.createCorporate(
    createCorporateCustomerRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createCorporateCustomerRequest** | **CreateCorporateCustomerRequest**|  | |


### Return type

**CustomerResponse**

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

# **createIndividual**
> CustomerResponse createIndividual(createIndividualCustomerRequest)

Creates a new individual customer with personal details

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    CreateIndividualCustomerRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let createIndividualCustomerRequest: CreateIndividualCustomerRequest; //

const { status, data } = await apiInstance.createIndividual(
    createIndividualCustomerRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createIndividualCustomerRequest** | **CreateIndividualCustomerRequest**|  | |


### Return type

**CustomerResponse**

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

# **createSME**
> CustomerResponse createSME(createSMECustomerRequest)

Creates a new small/medium enterprise customer with business type and turnover

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    CreateSMECustomerRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let createSMECustomerRequest: CreateSMECustomerRequest; //

const { status, data } = await apiInstance.createSME(
    createSMECustomerRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createSMECustomerRequest** | **CreateSMECustomerRequest**|  | |


### Return type

**CustomerResponse**

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

# **getCustomerById**
> CustomerResponse getCustomerById()

Retrieve customer details by internal database ID

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let id: number; //Customer database ID (default to undefined)

const { status, data } = await apiInstance.getCustomerById(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] | Customer database ID | defaults to undefined|


### Return type

**CustomerResponse**

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

# **listCustomers**
> PageCustomerResponse listCustomers()

Returns paginated list of all customers (no filtering)

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    Pageable
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let pageable: Pageable; // (default to undefined)

const { status, data } = await apiInstance.listCustomers(
    pageable
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **pageable** | **Pageable** |  | defaults to undefined|


### Return type

**PageCustomerResponse**

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

# **searchCustomers**
> PageCustomerResponse searchCustomers()

Search customers by criteria (name, type, status, etc.)

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    CustomerSearchCriteria,
    Pageable
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let criteria: CustomerSearchCriteria; // (default to undefined)
let pageable: Pageable; // (default to undefined)

const { status, data } = await apiInstance.searchCustomers(
    criteria,
    pageable
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **criteria** | **CustomerSearchCriteria** |  | defaults to undefined|
| **pageable** | **Pageable** |  | defaults to undefined|


### Return type

**PageCustomerResponse**

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

# **updateCustomer**
> CustomerResponse updateCustomer(updateCustomerRequest)

Update customer details. Supports partial updates.

### Example

```typescript
import {
    CustomerControllerApi,
    Configuration,
    UpdateCustomerRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CustomerControllerApi(configuration);

let id: number; //Customer database ID (default to undefined)
let updateCustomerRequest: UpdateCustomerRequest; //

const { status, data } = await apiInstance.updateCustomer(
    id,
    updateCustomerRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateCustomerRequest** | **UpdateCustomerRequest**|  | |
| **id** | [**number**] | Customer database ID | defaults to undefined|


### Return type

**CustomerResponse**

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

