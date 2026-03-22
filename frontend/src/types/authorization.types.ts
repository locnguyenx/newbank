// Re-export types from API
import type { AuthorizationResponse } from '@/api/api';
export type { AuthorizationResponse } from '@/api/api';
import {
  AuthorizationResponseStatusEnum,
  CreateAuthorizationRequestRelationshipTypeEnum,
  UpdateAuthorizationRequestRelationshipTypeEnum,
} from '@/api/api';
export {
  AuthorizationResponseStatusEnum,
  CreateAuthorizationRequestRelationshipTypeEnum,
  UpdateAuthorizationRequestRelationshipTypeEnum,
};
import type { CreateAuthorizationRequest, UpdateAuthorizationRequest } from '@/api/api';

export type Authorization = AuthorizationResponse;
export type { CreateAuthorizationRequest, UpdateAuthorizationRequest };

// Define enums that are used but not directly exported by API
export type AuthorizationType = typeof CreateAuthorizationRequestRelationshipTypeEnum[keyof typeof CreateAuthorizationRequestRelationshipTypeEnum];
export type AuthorizationStatus = typeof AuthorizationResponseStatusEnum[keyof typeof AuthorizationResponseStatusEnum];

// Define document type (not in API, used in frontend)
export type AuthorizationDocumentType = 
  | 'ID_DOCUMENT'
  | 'POWER_OF_ATTORNEY_LETTER'
  | 'BOARD_RESOLUTION'
  | 'SPECIMEN_SIGNATURE'
  | 'OTHER';
