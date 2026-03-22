# FeeWaiverControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createWaiver**](#createwaiver) | **POST** /api/charges/waivers | |
|[**getApplicableWaivers**](#getapplicablewaivers) | **GET** /api/charges/waivers/applicable | |
|[**getWaivers**](#getwaivers) | **GET** /api/charges/waivers | |
|[**removeWaiver**](#removewaiver) | **DELETE** /api/charges/waivers/{id} | |

# **createWaiver**
> FeeWaiverResponse createWaiver(createFeeWaiverRequest)


### Example

```typescript
import {
    FeeWaiverControllerApi,
    Configuration,
    CreateFeeWaiverRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new FeeWaiverControllerApi(configuration);

let createFeeWaiverRequest: CreateFeeWaiverRequest; //

const { status, data } = await apiInstance.createWaiver(
    createFeeWaiverRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createFeeWaiverRequest** | **CreateFeeWaiverRequest**|  | |


### Return type

**FeeWaiverResponse**

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

# **getApplicableWaivers**
> Array<FeeWaiverResponse> getApplicableWaivers()


### Example

```typescript
import {
    FeeWaiverControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new FeeWaiverControllerApi(configuration);

let chargeId: number; // (default to undefined)
let date: string; // (default to undefined)
let customerId: number; // (optional) (default to undefined)
let accountNumber: string; // (optional) (default to undefined)
let productCode: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getApplicableWaivers(
    chargeId,
    date,
    customerId,
    accountNumber,
    productCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeId** | [**number**] |  | defaults to undefined|
| **date** | [**string**] |  | defaults to undefined|
| **customerId** | [**number**] |  | (optional) defaults to undefined|
| **accountNumber** | [**string**] |  | (optional) defaults to undefined|
| **productCode** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<FeeWaiverResponse>**

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

# **getWaivers**
> Array<FeeWaiverResponse> getWaivers()


### Example

```typescript
import {
    FeeWaiverControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new FeeWaiverControllerApi(configuration);

let scope: string; // (optional) (default to undefined)
let referenceId: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getWaivers(
    scope,
    referenceId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **scope** | [**string**] |  | (optional) defaults to undefined|
| **referenceId** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<FeeWaiverResponse>**

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

# **removeWaiver**
> removeWaiver()


### Example

```typescript
import {
    FeeWaiverControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new FeeWaiverControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.removeWaiver(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


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

