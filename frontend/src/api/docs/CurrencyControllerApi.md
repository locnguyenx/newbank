# CurrencyControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createCurrency**](#createcurrency) | **POST** /api/master-data/currencies | |
|[**deactivateCurrency**](#deactivatecurrency) | **PUT** /api/master-data/currencies/{code}/deactivate | |
|[**getAllCurrencies**](#getallcurrencies) | **GET** /api/master-data/currencies | |
|[**getCurrency**](#getcurrency) | **GET** /api/master-data/currencies/{code} | |
|[**updateCurrency**](#updatecurrency) | **PUT** /api/master-data/currencies/{code} | |

# **createCurrency**
> CurrencyResponse createCurrency(createCurrencyRequest)


### Example

```typescript
import {
    CurrencyControllerApi,
    Configuration,
    CreateCurrencyRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new CurrencyControllerApi(configuration);

let createCurrencyRequest: CreateCurrencyRequest; //

const { status, data } = await apiInstance.createCurrency(
    createCurrencyRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createCurrencyRequest** | **CreateCurrencyRequest**|  | |


### Return type

**CurrencyResponse**

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

# **deactivateCurrency**
> CurrencyResponse deactivateCurrency()


### Example

```typescript
import {
    CurrencyControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new CurrencyControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.deactivateCurrency(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**CurrencyResponse**

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

# **getAllCurrencies**
> Array<CurrencyResponse> getAllCurrencies()


### Example

```typescript
import {
    CurrencyControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new CurrencyControllerApi(configuration);

let activeOnly: boolean; // (optional) (default to false)

const { status, data } = await apiInstance.getAllCurrencies(
    activeOnly
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **activeOnly** | [**boolean**] |  | (optional) defaults to false|


### Return type

**Array<CurrencyResponse>**

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

# **getCurrency**
> CurrencyResponse getCurrency()


### Example

```typescript
import {
    CurrencyControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new CurrencyControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getCurrency(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**CurrencyResponse**

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

# **updateCurrency**
> CurrencyResponse updateCurrency(updateCurrencyRequest)


### Example

```typescript
import {
    CurrencyControllerApi,
    Configuration,
    UpdateCurrencyRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new CurrencyControllerApi(configuration);

let code: string; // (default to undefined)
let updateCurrencyRequest: UpdateCurrencyRequest; //

const { status, data } = await apiInstance.updateCurrency(
    code,
    updateCurrencyRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateCurrencyRequest** | **UpdateCurrencyRequest**|  | |
| **code** | [**string**] |  | defaults to undefined|


### Return type

**CurrencyResponse**

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

