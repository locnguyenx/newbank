# ProductFeeEntryRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**feeType** | **string** |  | [default to undefined]
**calculationMethod** | **string** |  | [default to undefined]
**amount** | **number** |  | [optional] [default to undefined]
**rate** | **number** |  | [optional] [default to undefined]
**currency** | **string** |  | [default to undefined]
**tiers** | [**Array&lt;ProductFeeTierRequest&gt;**](ProductFeeTierRequest.md) |  | [optional] [default to undefined]

## Example

```typescript
import { ProductFeeEntryRequest } from '@openapitools/openapi-typescript-axios';

const instance: ProductFeeEntryRequest = {
    feeType,
    calculationMethod,
    amount,
    rate,
    currency,
    tiers,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
