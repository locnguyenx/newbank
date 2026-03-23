# CreateChargeRuleRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**calculationMethod** | **string** |  | [default to undefined]
**flatAmount** | **number** |  | [optional] [default to undefined]
**percentageRate** | **number** |  | [optional] [default to undefined]
**minAmount** | **number** |  | [optional] [default to undefined]
**maxAmount** | **number** |  | [optional] [default to undefined]
**tiers** | [**Array&lt;TierRequest&gt;**](TierRequest.md) |  | [optional] [default to undefined]

## Example

```typescript
import { CreateChargeRuleRequest } from './api';

const instance: CreateChargeRuleRequest = {
    calculationMethod,
    flatAmount,
    percentageRate,
    minAmount,
    maxAmount,
    tiers,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
