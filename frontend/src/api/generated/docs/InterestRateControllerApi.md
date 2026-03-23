# InterestRateControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createInterestRate**](#createinterestrate) | **POST** /api/charges/interest | |
|[**getInterestRate**](#getinterestrate) | **GET** /api/charges/interest/{id} | |
|[**getInterestRatesByProduct**](#getinterestratesbyproduct) | **GET** /api/charges/interest/product/{productCode} | |
|[**updateInterestRate**](#updateinterestrate) | **PUT** /api/charges/interest/{id} | |

# **createInterestRate**
> InterestRateResponse createInterestRate(createInterestRateRequest)


### Example

```typescript
import {
    InterestRateControllerApi,
    Configuration,
    CreateInterestRateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new InterestRateControllerApi(configuration);

let createInterestRateRequest: CreateInterestRateRequest; //

const { status, data } = await apiInstance.createInterestRate(
    createInterestRateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createInterestRateRequest** | **CreateInterestRateRequest**|  | |


### Return type

**InterestRateResponse**

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

# **getInterestRate**
> InterestRateResponse getInterestRate()


### Example

```typescript
import {
    InterestRateControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new InterestRateControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getInterestRate(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**InterestRateResponse**

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

# **getInterestRatesByProduct**
> Array<InterestRateResponse> getInterestRatesByProduct()


### Example

```typescript
import {
    InterestRateControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new InterestRateControllerApi(configuration);

let productCode: string; // (default to undefined)

const { status, data } = await apiInstance.getInterestRatesByProduct(
    productCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **productCode** | [**string**] |  | defaults to undefined|


### Return type

**Array<InterestRateResponse>**

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

# **updateInterestRate**
> InterestRateResponse updateInterestRate(createInterestRateRequest)


### Example

```typescript
import {
    InterestRateControllerApi,
    Configuration,
    CreateInterestRateRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new InterestRateControllerApi(configuration);

let id: number; // (default to undefined)
let createInterestRateRequest: CreateInterestRateRequest; //

const { status, data } = await apiInstance.updateInterestRate(
    id,
    createInterestRateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createInterestRateRequest** | **CreateInterestRateRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**InterestRateResponse**

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

