export type EmploymentStatus = 'ACTIVE' | 'INACTIVE' | 'TERMINATED';

export interface Employment {
  id: number;
  customerId: number;
  employeeName: string;
  employeeId: string;
  department: string;
  position: string;
  startDate: string;
  endDate?: string;
  status: EmploymentStatus;
  salary: number;
  currency: string;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  updatedBy: string;
}

export interface BulkUploadResult {
  totalRows: number;
  successCount: number;
  failureCount: number;
  errors: BulkUploadError[];
}

export interface BulkUploadError {
  row: number;
  field: string;
  message: string;
  value: string;
}

export interface CreateEmploymentPayload {
  customerId: number;
  employeeName: string;
  employeeId: string;
  department: string;
  position: string;
  startDate: string;
  salary: number;
  currency: string;
}
