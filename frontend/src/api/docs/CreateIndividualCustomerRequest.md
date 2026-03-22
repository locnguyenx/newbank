# CreateIndividualCustomerRequest


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **string** |  | [default to undefined]
**taxId** | **string** |  | [default to undefined]
**dateOfBirth** | **string** |  | [optional] [default to undefined]
**placeOfBirth** | **string** |  | [optional] [default to undefined]
**nationality** | **string** |  | [optional] [default to undefined]
**employerId** | **number** |  | [optional] [default to undefined]
**employmentStatus** | **string** |  | [optional] [default to undefined]
**addresses** | [**Array&lt;AddressDto&gt;**](AddressDto.md) |  | [optional] [default to undefined]
**phones** | [**Array&lt;PhoneDto&gt;**](PhoneDto.md) |  | [optional] [default to undefined]
**emails** | **Array&lt;string&gt;** |  | [optional] [default to undefined]

## Example

```typescript
import { CreateIndividualCustomerRequest } from '@openapitools/openapi-typescript-axios';

const instance: CreateIndividualCustomerRequest = {
    name,
    taxId,
    dateOfBirth,
    placeOfBirth,
    nationality,
    employerId,
    employmentStatus,
    addresses,
    phones,
    emails,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
