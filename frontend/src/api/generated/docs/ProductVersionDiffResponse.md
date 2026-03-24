# ProductVersionDiffResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**versionId1** | **number** |  | [optional] [default to undefined]
**versionId2** | **number** |  | [optional] [default to undefined]
**generalFields** | [**Array&lt;FieldDiff&gt;**](FieldDiff.md) |  | [optional] [default to undefined]
**features** | [**Array&lt;FeatureDiff&gt;**](FeatureDiff.md) |  | [optional] [default to undefined]
**fees** | [**Array&lt;FeeDiff&gt;**](FeeDiff.md) |  | [optional] [default to undefined]
**segments** | [**Array&lt;SegmentDiff&gt;**](SegmentDiff.md) |  | [optional] [default to undefined]

## Example

```typescript
import { ProductVersionDiffResponse } from './api';

const instance: ProductVersionDiffResponse = {
    versionId1,
    versionId2,
    generalFields,
    features,
    fees,
    segments,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
