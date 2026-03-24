# AccountOpeningRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**customerId** | **number** |  | [default to undefined]
**productCode** | **string** |  | [default to undefined]
**type** | **string** |  | [default to undefined]
**currency** | **string** |  | [default to undefined]
**initialDeposit** | **number** |  | [default to undefined]
**holders** | [**Array&lt;AccountHolderRequest&gt;**](AccountHolderRequest.md) |  | [default to undefined]

## Example

```typescript
import { AccountOpeningRequest } from './api';

const instance: AccountOpeningRequest = {
    customerId,
    productCode,
    type,
    currency,
    initialDeposit,
    holders,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
