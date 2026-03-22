# ProductDetailResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**product** | [**ProductInfo**](ProductInfo.md) |  | [optional] [default to undefined]
**id** | **number** |  | [optional] [default to undefined]
**versionNumber** | **number** |  | [optional] [default to undefined]
**status** | **string** |  | [optional] [default to undefined]
**contractCount** | **number** |  | [optional] [default to undefined]
**submittedBy** | **string** |  | [optional] [default to undefined]
**approvedBy** | **string** |  | [optional] [default to undefined]
**rejectionComment** | **string** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**createdBy** | **string** |  | [optional] [default to undefined]
**features** | [**Array&lt;ProductFeatureResponse&gt;**](ProductFeatureResponse.md) |  | [optional] [default to undefined]
**feeEntries** | [**Array&lt;ProductFeeEntryResponse&gt;**](ProductFeeEntryResponse.md) |  | [optional] [default to undefined]
**segments** | [**Array&lt;SegmentInfo&gt;**](SegmentInfo.md) |  | [optional] [default to undefined]

## Example

```typescript
import { ProductDetailResponse } from '@openapitools/openapi-typescript-axios';

const instance: ProductDetailResponse = {
    product,
    id,
    versionNumber,
    status,
    contractCount,
    submittedBy,
    approvedBy,
    rejectionComment,
    createdAt,
    createdBy,
    features,
    feeEntries,
    segments,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
