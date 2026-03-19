export interface Customer {
  id: number;
  customerNumber: string;
  customerType: CustomerType;
  status: CustomerStatus;
  createdAt: string;
  createdBy: string;
  updatedAt: string;
  updatedBy: string;
}

export type CustomerType = 'INDIVIDUAL' | 'SME' | 'CORPORATE';
export type CustomerStatus = 'PENDING' | 'ACTIVE' | 'SUSPENDED' | 'CLOSED';

export interface IndividualCustomer extends Customer {
  customerType: 'INDIVIDUAL';
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  email: string;
  phoneNumber: string;
  idNumber: string;
}

export interface SMECustomer extends Customer {
  customerType: 'SME';
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  numberOfEmployees: number;
}

export interface CorporateCustomer extends Customer {
  customerType: 'CORPORATE';
  companyName: string;
  registrationNumber: string;
  incorporationDate: string;
  email: string;
  phoneNumber: string;
  industry: string;
  employeeCountRange: string;
  annualRevenue: number;
  revenueCurrency: string;
}

export type CustomerVariant = IndividualCustomer | SMECustomer | CorporateCustomer;
