# ChargeCalculationControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**calculate**](#calculate) | **POST** /api/charges/calculate | |

# **calculate**
> ChargeCalculationResponse calculate(chargeCalculationRequest)


### Example

```typescript
import {
    ChargeCalculationControllerApi,
    Configuration,
    ChargeCalculationRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChargeCalculationControllerApi(configuration);

let chargeCalculationRequest: ChargeCalculationRequest; //

const { status, data } = await apiInstance.calculate(
    chargeCalculationRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **chargeCalculationRequest** | **ChargeCalculationRequest**|  | |


### Return type

**ChargeCalculationResponse**

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

