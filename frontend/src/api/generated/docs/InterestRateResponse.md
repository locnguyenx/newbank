# InterestRateResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**id** | **number** |  | [optional] [default to undefined]
**chargeDefinitionId** | **number** |  | [optional] [default to undefined]
**productCode** | **string** |  | [optional] [default to undefined]
**fixedRate** | **number** |  | [optional] [default to undefined]
**accrualSchedule** | **string** |  | [optional] [default to undefined]
**applicationSchedule** | **string** |  | [optional] [default to undefined]
**tiers** | [**Array&lt;InterestTierResponse&gt;**](InterestTierResponse.md) |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]

## Example

```typescript
import { InterestRateResponse } from './api';

const instance: InterestRateResponse = {
    id,
    chargeDefinitionId,
    productCode,
    fixedRate,
    accrualSchedule,
    applicationSchedule,
    tiers,
    createdAt,
    updatedAt,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
