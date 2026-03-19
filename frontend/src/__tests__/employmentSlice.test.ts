import { describe, it, expect } from 'vitest';
import employmentReducer, {
  setUploadProgress,
  clearUploadResult,
} from '../store/slices/employmentSlice';

describe('employmentSlice', () => {
  const initialState = {
    employments: [],
    loading: false,
    error: null,
    uploadProgress: 0,
    uploadResult: null,
    uploadLoading: false,
  };

  it('should return the initial state', () => {
    expect(employmentReducer(undefined, { type: 'unknown' })).toEqual(initialState);
  });

  it('should handle setUploadProgress', () => {
    const actual = employmentReducer(initialState, setUploadProgress(50));
    expect(actual.uploadProgress).toBe(50);
  });

  it('should handle clearUploadResult', () => {
    const stateWithResult = {
      ...initialState,
      uploadProgress: 100,
      uploadResult: { totalRows: 10, successCount: 8, failureCount: 2, errors: [] },
      error: 'some error',
    };
    const actual = employmentReducer(stateWithResult, clearUploadResult());
    expect(actual.uploadResult).toBeNull();
    expect(actual.uploadProgress).toBe(0);
    expect(actual.error).toBeNull();
  });
});
