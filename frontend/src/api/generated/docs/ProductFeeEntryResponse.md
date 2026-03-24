# ProductFeeEntryResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **number** |  | [optional] [default to undefined]
**feeType** | **string** |  | [optional] [default to undefined]
**calculationMethod** | **string** |  | [optional] [default to undefined]
**amount** | **number** |  | [optional] [default to undefined]
**rate** | **number** |  | [optional] [default to undefined]
**currency** | **string** |  | [optional] [default to undefined]
**tiers** | [**Array&lt;ProductFeeTierResponse&gt;**](ProductFeeTierResponse.md) |  | [optional] [default to undefined]

## Example

```typescript
import { ProductFeeEntryResponse } from './api';

const instance: ProductFeeEntryResponse = {
    id,
    feeType,
    calculationMethod,
    amount,
    rate,
    currency,
    tiers,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
