# HolidayControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createHoliday**](#createholiday) | **POST** /api/master-data/holidays | |
|[**getHolidays**](#getholidays) | **GET** /api/master-data/holidays | |
|[**isHoliday**](#isholiday) | **GET** /api/master-data/holidays/check | |

# **createHoliday**
> HolidayResponse createHoliday(createHolidayRequest)


### Example

```typescript
import {
    HolidayControllerApi,
    Configuration,
    CreateHolidayRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new HolidayControllerApi(configuration);

let createHolidayRequest: CreateHolidayRequest; //

const { status, data } = await apiInstance.createHoliday(
    createHolidayRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createHolidayRequest** | **CreateHolidayRequest**|  | |


### Return type

**HolidayResponse**

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

# **getHolidays**
> Array<HolidayResponse> getHolidays()


### Example

```typescript
import {
    HolidayControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new HolidayControllerApi(configuration);

let countryCode: string; // (default to undefined)
let year: number; // (default to undefined)

const { status, data } = await apiInstance.getHolidays(
    countryCode,
    year
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **countryCode** | [**string**] |  | defaults to undefined|
| **year** | [**number**] |  | defaults to undefined|


### Return type

**Array<HolidayResponse>**

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

# **isHoliday**
> boolean isHoliday()


### Example

```typescript
import {
    HolidayControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new HolidayControllerApi(configuration);

let countryCode: string; // (default to undefined)
let date: string; // (default to undefined)

const { status, data } = await apiInstance.isHoliday(
    countryCode,
    date
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **countryCode** | [**string**] |  | defaults to undefined|
| **date** | [**string**] |  | defaults to undefined|


### Return type

**boolean**

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

