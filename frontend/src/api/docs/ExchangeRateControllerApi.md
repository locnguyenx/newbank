# ExchangeRateControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**convertAmount**](#convertamount) | **GET** /api/master-data/exchange-rates/convert | |
|[**createExchangeRate**](#createexchangerate) | **POST** /api/master-data/exchange-rates | |
|[**getLatestRate**](#getlatestrate) | **GET** /api/master-data/exchange-rates/latest | |

# **convertAmount**
> number convertAmount()


### Example

```typescript
import {
    ExchangeRateControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ExchangeRateControllerApi(configuration);

let base: string; // (default to undefined)
let target: string; // (default to undefined)
let amount: number; // (default to undefined)

const { status, data } = await apiInstance.convertAmount(
    base,
    target,
    amount
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **base** | [**string**] |  | defaults to undefined|
| **target** | [**string**] |  | defaults to undefined|
| **amount** | [**number**] |  | defaults to undefined|


### Return type

**number**

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

# **createExchangeRate**
> ExchangeRateResponse createExchangeRate(createExchangeRateRequest)


### Example

```typescript
import {
    ExchangeRateControllerApi,
    Configuration,
    CreateExchangeRateRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ExchangeRateControllerApi(configuration);

let createExchangeRateRequest: CreateExchangeRateRequest; //

const { status, data } = await apiInstance.createExchangeRate(
    createExchangeRateRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createExchangeRateRequest** | **CreateExchangeRateRequest**|  | |


### Return type

**ExchangeRateResponse**

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

# **getLatestRate**
> ExchangeRateResponse getLatestRate()


### Example

```typescript
import {
    ExchangeRateControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ExchangeRateControllerApi(configuration);

let base: string; // (default to undefined)
let target: string; // (default to undefined)

const { status, data } = await apiInstance.getLatestRate(
    base,
    target
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **base** | [**string**] |  | defaults to undefined|
| **target** | [**string**] |  | defaults to undefined|


### Return type

**ExchangeRateResponse**

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

