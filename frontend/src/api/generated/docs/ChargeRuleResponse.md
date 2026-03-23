# ChargeRuleResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **number** |  | [optional] [default to undefined]
**chargeDefinitionId** | **number** |  | [optional] [default to undefined]
**calculationMethod** | **string** |  | [optional] [default to undefined]
**flatAmount** | **number** |  | [optional] [default to undefined]
**percentageRate** | **number** |  | [optional] [default to undefined]
**minAmount** | **number** |  | [optional] [default to undefined]
**maxAmount** | **number** |  | [optional] [default to undefined]
**tiers** | [**Array&lt;ChargeTierResponse&gt;**](ChargeTierResponse.md) |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { ChargeRuleResponse } from './api';

const instance: ChargeRuleResponse = {
    id,
    chargeDefinitionId,
    calculationMethod,
    flatAmount,
    percentageRate,
    minAmount,
    maxAmount,
    tiers,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
