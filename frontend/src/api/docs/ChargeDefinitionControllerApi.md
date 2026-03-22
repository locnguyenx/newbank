# ChargeDefinitionControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**activateCharge**](#activatecharge) | **PUT** /api/charges/definitions/{id}/activate | |
|[**createCharge**](#createcharge) | **POST** /api/charges/definitions | |
|[**deactivateCharge**](#deactivatecharge) | **PUT** /api/charges/definitions/{id}/deactivate | |
|[**getAllCharges**](#getallcharges) | **GET** /api/charges/definitions | |
|[**getCharge**](#getcharge) | **GET** /api/charges/definitions/{id} | |
|[**updateCharge**](#updatecharge) | **PUT** /api/charges/definitions/{id} | |

# **activateCharge**
> ChargeDefinitionResponse activateCharge()


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.activateCharge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ChargeDefinitionResponse**

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

# **createCharge**
> ChargeDefinitionResponse createCharge(createChargeDefinitionRequest)


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration,
    CreateChargeDefinitionRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let createChargeDefinitionRequest: CreateChargeDefinitionRequest; //

const { status, data } = await apiInstance.createCharge(
    createChargeDefinitionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createChargeDefinitionRequest** | **CreateChargeDefinitionRequest**|  | |


### Return type

**ChargeDefinitionResponse**

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

# **deactivateCharge**
> ChargeDefinitionResponse deactivateCharge()


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.deactivateCharge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ChargeDefinitionResponse**

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

# **getAllCharges**
> Array<ChargeDefinitionResponse> getAllCharges()


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let chargeType: string; // (optional) (default to undefined)
let status: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getAllCharges(
    chargeType,
    status
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeType** | [**string**] |  | (optional) defaults to undefined|
| **status** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<ChargeDefinitionResponse>**

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

# **getCharge**
> ChargeDefinitionResponse getCharge()


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getCharge(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ChargeDefinitionResponse**

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

# **updateCharge**
> ChargeDefinitionResponse updateCharge(updateChargeDefinitionRequest)


### Example

```typescript
import {
    ChargeDefinitionControllerApi,
    Configuration,
    UpdateChargeDefinitionRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeDefinitionControllerApi(configuration);

let id: number; // (default to undefined)
let updateChargeDefinitionRequest: UpdateChargeDefinitionRequest; //

const { status, data } = await apiInstance.updateCharge(
    id,
    updateChargeDefinitionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateChargeDefinitionRequest** | **UpdateChargeDefinitionRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**ChargeDefinitionResponse**

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

