# CreateInterestRateRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**chargeId** | **number** |  | [optional] [default to undefined]
**productCode** | **string** |  | [optional] [default to undefined]
**fixedRate** | **number** |  | [optional] [default to undefined]
**accrualSchedule** | **string** |  | [optional] [default to undefined]
**applicationSchedule** | **string** |  | [optional] [default to undefined]
**tiers** | [**Array&lt;InterestTierRequest&gt;**](InterestTierRequest.md) |  | [optional] [default to undefined]

## Example

```typescript
import { CreateInterestRateRequest } from '@openapitools/openapi-typescript-axios';

const instance: CreateInterestRateRequest = {
    chargeId,
    productCode,
    fixedRate,
    accrualSchedule,
    applicationSchedule,
    tiers,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
