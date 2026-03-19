export type AuthorizationStatus = 'ACTIVE' | 'REVOKED' | 'EXPIRED' | 'PENDING';

export type AuthorizationType = 'SIGNATORY' | 'POWER_OF_ATTORNEY' | 'JOINT_AUTHORITY' | 'SOLE_AUTHORITY' | 'DELEGATED';

export type AuthorizationDocumentType = 'ID_DOCUMENT' | 'POWER_OF_ATTORNEY_LETTER' | 'BOARD_RESOLUTION' | 'SPECIMEN_SIGNATURE' | 'OTHER';

export interface AuthorizationDocument {
  id: number;
  documentType: AuthorizationDocumentType;
  fileName: string;
  uploadedAt: string;
  status: 'UPLOADED' | 'VERIFIED' | 'REJECTED';
  rejectionReason?: string;
}

export interface Authorization {
  id: number;
  customerId: number;
  customerNumber: string;
  authorizationType: AuthorizationType;
  status: AuthorizationStatus;
  authorizedPersonName: string;
  authorizedPersonEmail: string;
  authorizedPersonPhone: string;
  transactionLimit: number | null;
  currency: string;
  effectiveDate: string;
  expiryDate: string | null;
  documents: AuthorizationDocument[];
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateAuthorizationRequest {
  customerId: number;
  authorizationType: AuthorizationType;
  authorizedPersonName: string;
  authorizedPersonEmail: string;
  authorizedPersonPhone: string;
  transactionLimit: number | null;
  currency: string;
  effectiveDate: string;
  expiryDate: string | null;
  notes: string | null;
}

export interface UpdateAuthorizationRequest {
  id: number;
  authorizationType?: AuthorizationType;
  authorizedPersonName?: string;
  authorizedPersonEmail?: string;
  authorizedPersonPhone?: string;
  transactionLimit?: number | null;
  currency?: string;
  effectiveDate?: string;
  expiryDate?: string | null;
  notes?: string | null;
}

export interface AuthorizationDocumentUpload {
  documentType: AuthorizationDocumentType;
  file: File;
}
