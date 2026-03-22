# ChargeRuleControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**addRule**](#addrule) | **POST** /api/charges/definitions/{chargeId}/rules | |
|[**getRules**](#getrules) | **GET** /api/charges/definitions/{chargeId}/rules | |
|[**removeRule**](#removerule) | **DELETE** /api/charges/definitions/{chargeId}/rules/{ruleId} | |
|[**updateRule**](#updaterule) | **PUT** /api/charges/definitions/{chargeId}/rules/{ruleId} | |

# **addRule**
> ChargeRuleResponse addRule(createChargeRuleRequest)


### Example

```typescript
import {
    ChargeRuleControllerApi,
    Configuration,
    CreateChargeRuleRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeRuleControllerApi(configuration);

let chargeId: number; // (default to undefined)
let createChargeRuleRequest: CreateChargeRuleRequest; //

const { status, data } = await apiInstance.addRule(
    chargeId,
    createChargeRuleRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createChargeRuleRequest** | **CreateChargeRuleRequest**|  | |
| **chargeId** | [**number**] |  | defaults to undefined|


### Return type

**ChargeRuleResponse**

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

# **getRules**
> Array<ChargeRuleResponse> getRules()


### Example

```typescript
import {
    ChargeRuleControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeRuleControllerApi(configuration);

let chargeId: number; // (default to undefined)

const { status, data } = await apiInstance.getRules(
    chargeId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeId** | [**number**] |  | defaults to undefined|


### Return type

**Array<ChargeRuleResponse>**

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

# **removeRule**
> removeRule()


### Example

```typescript
import {
    ChargeRuleControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeRuleControllerApi(configuration);

let chargeId: number; // (default to undefined)
let ruleId: number; // (default to undefined)

const { status, data } = await apiInstance.removeRule(
    chargeId,
    ruleId
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeId** | [**number**] |  | defaults to undefined|
| **ruleId** | [**number**] |  | defaults to undefined|


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

# **updateRule**
> ChargeRuleResponse updateRule(createChargeRuleRequest)


### Example

```typescript
import {
    ChargeRuleControllerApi,
    Configuration,
    CreateChargeRuleRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeRuleControllerApi(configuration);

let chargeId: number; // (default to undefined)
let ruleId: number; // (default to undefined)
let createChargeRuleRequest: CreateChargeRuleRequest; //

const { status, data } = await apiInstance.updateRule(
    chargeId,
    ruleId,
    createChargeRuleRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createChargeRuleRequest** | **CreateChargeRuleRequest**|  | |
| **chargeId** | [**number**] |  | defaults to undefined|
| **ruleId** | [**number**] |  | defaults to undefined|


### Return type

**ChargeRuleResponse**

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

