# IndustryControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createIndustry**](#createindustry) | **POST** /api/master-data/industries | |
|[**getAllIndustries**](#getallindustries) | **GET** /api/master-data/industries | |
|[**getIndustry**](#getindustry) | **GET** /api/master-data/industries/{code} | |

# **createIndustry**
> IndustryResponse createIndustry(createIndustryRequest)


### Example

```typescript
import {
    IndustryControllerApi,
    Configuration,
    CreateIndustryRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new IndustryControllerApi(configuration);

let createIndustryRequest: CreateIndustryRequest; //

const { status, data } = await apiInstance.createIndustry(
    createIndustryRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createIndustryRequest** | **CreateIndustryRequest**|  | |


### Return type

**IndustryResponse**

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

# **getAllIndustries**
> Array<IndustryResponse> getAllIndustries()


### Example

```typescript
import {
    IndustryControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new IndustryControllerApi(configuration);

let activeOnly: boolean; // (optional) (default to false)

const { status, data } = await apiInstance.getAllIndustries(
    activeOnly
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **activeOnly** | [**boolean**] |  | (optional) defaults to false|


### Return type

**Array<IndustryResponse>**

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

# **getIndustry**
> IndustryResponse getIndustry()


### Example

```typescript
import {
    IndustryControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new IndustryControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getIndustry(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**IndustryResponse**

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

