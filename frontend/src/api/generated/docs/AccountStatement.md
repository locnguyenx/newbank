# AccountStatement


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**accountNumber** | **string** |  | [optional] [default to undefined]
**fromDate** | **string** |  | [optional] [default to undefined]
**toDate** | **string** |  | [optional] [default to undefined]
**openingBalance** | **number** |  | [optional] [default to undefined]
**closingBalance** | **number** |  | [optional] [default to undefined]
**totalCredits** | **number** |  | [optional] [default to undefined]
**totalDebits** | **number** |  | [optional] [default to undefined]
**transactions** | [**Array&lt;TransactionEntry&gt;**](TransactionEntry.md) |  | [optional] [default to undefined]

## Example

```typescript
import { AccountStatement } from './api';

const instance: AccountStatement = {
    accountNumber,
    fromDate,
    toDate,
    openingBalance,
    closingBalance,
    totalCredits,
    totalDebits,
    transactions,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
