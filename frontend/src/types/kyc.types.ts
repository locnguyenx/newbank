export type KYCStatus = 'PENDING' | 'SUBMITTED' | 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED';

export type KYCDocumentType = 'PASSPORT' | 'DRIVERS_LICENSE' | 'NATIONAL_ID' | 'PROOF_OF_ADDRESS' | 'BANK_STATEMENT' | 'BUSINESS_LICENSE' | 'INCORPORATION_CERTIFICATE';

export type SanctionsStatus = 'CLEAR' | 'MATCH' | 'PENDING' | 'ERROR';

export interface KYCDocument {
  id: number;
  documentType: KYCDocumentType;
  fileName: string;
  uploadedAt: string;
  status: 'UPLOADED' | 'VERIFIED' | 'REJECTED';
  rejectionReason?: string;
}

export interface SanctionsScreening {
  id: number;
  screeningDate: string;
  status: SanctionsStatus;
  matchDetails?: string;
  lists: string[];
}

export interface KYCReviewTimeline {
  id: number;
  action: string;
  performedBy: string;
  performedAt: string;
  notes?: string;
}

export interface KYC {
  id: number;
  customerId: number;
  customerNumber: string;
  status: KYCStatus;
  documents: KYCDocument[];
  sanctionsScreening: SanctionsScreening | null;
  reviewTimeline: KYCReviewTimeline[];
  submittedAt?: string;
  reviewedAt?: string;
  reviewedBy?: string;
  rejectionReason?: string;
  createdAt: string;
  updatedAt: string;
}

export interface KYCDocumentUpload {
  documentType: KYCDocumentType;
  file: File;
}

export interface KYCReviewAction {
  kycId: number;
  approved: boolean;
  notes: string;
}
