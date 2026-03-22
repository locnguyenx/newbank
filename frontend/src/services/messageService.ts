export const ErrorMessages: Record<string, string> = {
  // Account errors
  ACCOUNT_001: 'Account not found',
  ACCOUNT_002: 'This account is already closed',
  ACCOUNT_003: 'Cannot close a frozen account. Please unfreeze it first.',
  ACCOUNT_004: 'Cannot close account with non-zero balance. Please withdraw all funds first.',
  ACCOUNT_005: 'Cannot freeze a closed account',
  ACCOUNT_006: 'Account is not frozen',
  ACCOUNT_007: 'Account with this number already exists',

  // Customer errors
  CUSTOMER_001: 'Customer not found',
  CUSTOMER_002: 'Customer with this ID already exists',

  // Product errors
  PRODUCT_001: 'Product not found',
  PRODUCT_002: 'Product with this code already exists',

  // Limit errors
  LIMIT_001: 'Transaction exceeds available limit',
  LIMIT_002: 'Limit not found',

  // Validation errors
  VALIDATION_001: 'Please check your input and try again',

  // System errors
  SYSTEM_001: 'An unexpected error occurred. Please try again later.',
};

export interface ApiError {
  messageCode?: string;
  errorCode?: string;
  message: string;
  status: number;
}

export function getErrorMessage(error: unknown): string {
  // Handle various error formats
  let messageCode: string | undefined;
  let userMessage: string;

  // Extract message code from different error shapes
  if (typeof error === 'object' && error !== null) {
    const err = error as Record<string, unknown>;
    const response = err.response as Record<string, unknown> | undefined;
    const responseData = response?.data as Record<string, unknown> | string | undefined;
    
    messageCode = (err.messageCode || err.errorCode) as string | undefined;
    
    // Try to get the message
    if (typeof err.message === 'string') {
      userMessage = err.message;
    } else if (typeof responseData === 'object' && responseData !== null && typeof (responseData as Record<string, unknown>).message === 'string') {
      userMessage = (responseData as Record<string, unknown>).message as string;
    } else if (typeof responseData === 'string') {
      userMessage = responseData;
    } else if (typeof err.message === 'string') {
      userMessage = err.message;
    } else {
      userMessage = ErrorMessages.SYSTEM_001;
    }
  } else if (typeof error === 'string') {
    userMessage = error;
  } else {
    userMessage = ErrorMessages.SYSTEM_001;
  }

  // If we have a message code, try to get a user-friendly message
  if (messageCode && ErrorMessages[messageCode]) {
    return ErrorMessages[messageCode];
  }

  // Check if the userMessage matches any known message code
  const knownMessage = Object.entries(ErrorMessages).find(
    ([_, msg]) => msg.toLowerCase() === userMessage.toLowerCase()
  );
  if (knownMessage) {
    return knownMessage[1];
  }

  // Return the API message if it's user-friendly enough, otherwise use default
  if (userMessage && userMessage.length < 100 && !userMessage.includes('[object')) {
    return userMessage;
  }

  return ErrorMessages.SYSTEM_001;
}

export function getErrorCode(error: unknown): string | undefined {
  if (typeof error === 'object' && error !== null) {
    const err = error as Record<string, unknown>;
    return (err.messageCode || err.errorCode) as string | undefined;
  }
  return undefined;
}
