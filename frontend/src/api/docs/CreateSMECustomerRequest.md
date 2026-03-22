# CreateSMECustomerRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **string** |  | [default to undefined]
**taxId** | **string** |  | [default to undefined]
**registrationNumber** | **string** |  | [optional] [default to undefined]
**industry** | **string** |  | [optional] [default to undefined]
**businessType** | **string** |  | [optional] [default to undefined]
**annualTurnoverAmount** | **string** |  | [optional] [default to undefined]
**annualTurnoverCurrency** | **string** |  | [optional] [default to undefined]
**yearsInOperation** | **number** |  | [optional] [default to undefined]
**addresses** | [**Array&lt;AddressDto&gt;**](AddressDto.md) |  | [optional] [default to undefined]
**phones** | [**Array&lt;PhoneDto&gt;**](PhoneDto.md) |  | [optional] [default to undefined]
**emails** | **Array&lt;string&gt;** |  | [optional] [default to undefined]

## Example

```typescript
import { CreateSMECustomerRequest } from '@openapitools/openapi-typescript-axios';

const instance: CreateSMECustomerRequest = {
    name,
    taxId,
    registrationNumber,
    industry,
    businessType,
    annualTurnoverAmount,
    annualTurnoverCurrency,
    yearsInOperation,
    addresses,
    phones,
    emails,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
