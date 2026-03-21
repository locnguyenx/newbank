package com.banking.product.dto.response;

import java.util.List;

public class ProductVersionDiffResponse {

    private Long versionId1;
    private Long versionId2;
    private List<FieldDiff> generalFields;
    private List<FeatureDiff> features;
    private List<FeeDiff> fees;
    private List<SegmentDiff> segments;

    public ProductVersionDiffResponse() {
    }

    public static class FieldDiff {
        private String fieldName;
        private String oldValue;
        private String newValue;
        private ChangeType changeType;

        public FieldDiff() {
        }

        public FieldDiff(String fieldName, String oldValue, String newValue, ChangeType changeType) {
            this.fieldName = fieldName;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changeType = changeType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        public ChangeType getChangeType() {
            return changeType;
        }

        public void setChangeType(ChangeType changeType) {
            this.changeType = changeType;
        }
    }

    public static class FeatureDiff {
        private String featureKey;
        private String oldValue;
        private String newValue;
        private ChangeType changeType;

        public FeatureDiff() {
        }

        public FeatureDiff(String featureKey, String oldValue, String newValue, ChangeType changeType) {
            this.featureKey = featureKey;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changeType = changeType;
        }

        public String getFeatureKey() {
            return featureKey;
        }

        public void setFeatureKey(String featureKey) {
            this.featureKey = featureKey;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        public ChangeType getChangeType() {
            return changeType;
        }

        public void setChangeType(ChangeType changeType) {
            this.changeType = changeType;
        }
    }

    /**
     * @deprecated Use Charges module instead. Kept for historical version diffs.
     */
    @Deprecated
    public static class FeeDiff {
        private String feeType;
        private String oldValue;
        private String newValue;
        private ChangeType changeType;

        public FeeDiff() {
        }

        public FeeDiff(String feeType, String oldValue, String newValue, ChangeType changeType) {
            this.feeType = feeType;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changeType = changeType;
        }

        public String getFeeType() {
            return feeType;
        }

        public void setFeeType(String feeType) {
            this.feeType = feeType;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        public ChangeType getChangeType() {
            return changeType;
        }

        public void setChangeType(ChangeType changeType) {
            this.changeType = changeType;
        }
    }

    public static class SegmentDiff {
        private String customerType;
        private String oldValue;
        private String newValue;
        private ChangeType changeType;

        public SegmentDiff() {
        }

        public SegmentDiff(String customerType, String oldValue, String newValue, ChangeType changeType) {
            this.customerType = customerType;
            this.oldValue = oldValue;
            this.newValue = newValue;
            this.changeType = changeType;
        }

        public String getCustomerType() {
            return customerType;
        }

        public void setCustomerType(String customerType) {
            this.customerType = customerType;
        }

        public String getOldValue() {
            return oldValue;
        }

        public void setOldValue(String oldValue) {
            this.oldValue = oldValue;
        }

        public String getNewValue() {
            return newValue;
        }

        public void setNewValue(String newValue) {
            this.newValue = newValue;
        }

        public ChangeType getChangeType() {
            return changeType;
        }

        public void setChangeType(ChangeType changeType) {
            this.changeType = changeType;
        }
    }

    public enum ChangeType {
        ADDED,
        REMOVED,
        CHANGED
    }

    public Long getVersionId1() {
        return versionId1;
    }

    public void setVersionId1(Long versionId1) {
        this.versionId1 = versionId1;
    }

    public Long getVersionId2() {
        return versionId2;
    }

    public void setVersionId2(Long versionId2) {
        this.versionId2 = versionId2;
    }

    public List<FieldDiff> getGeneralFields() {
        return generalFields;
    }

    public void setGeneralFields(List<FieldDiff> generalFields) {
        this.generalFields = generalFields;
    }

    public List<FeatureDiff> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDiff> features) {
        this.features = features;
    }

    public List<FeeDiff> getFees() {
        return fees;
    }

    public void setFees(List<FeeDiff> fees) {
        this.fees = fees;
    }

    public List<SegmentDiff> getSegments() {
        return segments;
    }

    public void setSegments(List<SegmentDiff> segments) {
        this.segments = segments;
    }
}