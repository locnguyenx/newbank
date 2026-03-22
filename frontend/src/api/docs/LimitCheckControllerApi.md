# LimitCheckControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**checkLimit**](#checklimit) | **POST** /api/limits/check | |
|[**getEffectiveLimits**](#geteffectivelimits) | **GET** /api/limits/check/effective | |

# **checkLimit**
> LimitCheckResponse checkLimit(limitCheckRequest)


### Example

```typescript
import {
    LimitCheckControllerApi,
    Configuration,
    LimitCheckRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitCheckControllerApi(configuration);

let limitCheckRequest: LimitCheckRequest; //

const { status, data } = await apiInstance.checkLimit(
    limitCheckRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **limitCheckRequest** | **LimitCheckRequest**|  | |


### Return type

**LimitCheckResponse**

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

# **getEffectiveLimits**
> Array<EffectiveLimitResponse> getEffectiveLimits()


### Example

```typescript
import {
    LimitCheckControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new LimitCheckControllerApi(configuration);

let accountNumber: string; // (default to undefined)
let currency: string; // (default to undefined)
let customerId: number; // (optional) (default to undefined)
let productCode: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getEffectiveLimits(
    accountNumber,
    currency,
    customerId,
    productCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **accountNumber** | [**string**] |  | defaults to undefined|
| **currency** | [**string**] |  | defaults to undefined|
| **customerId** | [**number**] |  | (optional) defaults to undefined|
| **productCode** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<EffectiveLimitResponse>**

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

