// Re-export types from API
import type {
  EmploymentResponse,
  BulkUploadResult,
  CreateEmploymentRequest,
} from '@/api/api';
export type {
  EmploymentResponse,
  BulkUploadResult,
  CreateEmploymentRequest,
};

// Export type aliases
export type Employment = EmploymentResponse;
export type CreateEmploymentPayload = CreateEmploymentRequest;
