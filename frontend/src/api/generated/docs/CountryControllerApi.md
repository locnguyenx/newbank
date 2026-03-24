# CountryControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createCountry**](#createcountry) | **POST** /api/master-data/countries | |
|[**deactivateCountry**](#deactivatecountry) | **PUT** /api/master-data/countries/{isoCode}/deactivate | |
|[**getAllCountries**](#getallcountries) | **GET** /api/master-data/countries | |
|[**getCountry**](#getcountry) | **GET** /api/master-data/countries/{isoCode} | |
|[**updateCountry**](#updatecountry) | **PUT** /api/master-data/countries/{isoCode} | |

# **createCountry**
> CountryResponse createCountry(createCountryRequest)


### Example

```typescript
import {
    CountryControllerApi,
    Configuration,
    CreateCountryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CountryControllerApi(configuration);

let createCountryRequest: CreateCountryRequest; //

const { status, data } = await apiInstance.createCountry(
    createCountryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createCountryRequest** | **CreateCountryRequest**|  | |


### Return type

**CountryResponse**

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

# **deactivateCountry**
> CountryResponse deactivateCountry()


### Example

```typescript
import {
    CountryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CountryControllerApi(configuration);

let isoCode: string; // (default to undefined)

const { status, data } = await apiInstance.deactivateCountry(
    isoCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **isoCode** | [**string**] |  | defaults to undefined|


### Return type

**CountryResponse**

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

# **getAllCountries**
> Array<CountryResponse> getAllCountries()


### Example

```typescript
import {
    CountryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CountryControllerApi(configuration);

let activeOnly: boolean; // (optional) (default to false)

const { status, data } = await apiInstance.getAllCountries(
    activeOnly
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **activeOnly** | [**boolean**] |  | (optional) defaults to false|


### Return type

**Array<CountryResponse>**

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

# **getCountry**
> CountryResponse getCountry()


### Example

```typescript
import {
    CountryControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new CountryControllerApi(configuration);

let isoCode: string; // (default to undefined)

const { status, data } = await apiInstance.getCountry(
    isoCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **isoCode** | [**string**] |  | defaults to undefined|


### Return type

**CountryResponse**

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

# **updateCountry**
> CountryResponse updateCountry(updateCountryRequest)


### Example

```typescript
import {
    CountryControllerApi,
    Configuration,
    UpdateCountryRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new CountryControllerApi(configuration);

let isoCode: string; // (default to undefined)
let updateCountryRequest: UpdateCountryRequest; //

const { status, data } = await apiInstance.updateCountry(
    isoCode,
    updateCountryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateCountryRequest** | **UpdateCountryRequest**|  | |
| **isoCode** | [**string**] |  | defaults to undefined|


### Return type

**CountryResponse**

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

