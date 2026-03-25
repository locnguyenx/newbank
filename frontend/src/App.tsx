import { Routes, Route, Navigate } from 'react-router-dom';
import { AppLayout } from '@/components/layout';
import ProtectedRoute from '@/components/common/ProtectedRoute';
import { ConfigProvider, Spin } from 'antd';
import { useSelector } from 'react-redux';
import type { RootState } from '@/store';

const SystemAdminOnly: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user } = useSelector((state: RootState) => state.auth);
  const hasRole = user?.roles?.some(role => role === 'SYSTEM_ADMIN');
  if (!hasRole) {
    return <Navigate to="/unauthorized" replace />;
  }
  return <>{children}</>;
};

const CompanyAdminOnly: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { user } = useSelector((state: RootState) => state.auth);
  const hasRole = user?.roles?.some(role => role === 'COMPANY_ADMIN');
  if (!hasRole) {
    return <Navigate to="/unauthorized" replace />;
  }
  return <>{children}</>;
};
import DashboardPage from '@/pages/DashboardPage';
import LoginPage from '@/pages/auth/LoginPage';
import MfaVerifyPage from '@/pages/auth/MfaVerifyPage';
import UnauthorizedPage from '@/pages/auth/UnauthorizedPage';
import UserListPage from '@/pages/iam/UserListPage';
import UserFormPage from '@/pages/iam/UserFormPage';
import RoleListPage from '@/pages/iam/RoleListPage';
import RoleFormPage from '@/pages/iam/RoleFormPage';
import ActivityPage from '@/pages/iam/ActivityPage';
import ThresholdListPage from '@/pages/iam/ThresholdListPage';
import ProfilePage from '@/pages/profile/ProfilePage';
import ChangePasswordPage from '@/pages/profile/ChangePasswordPage';
import SettingsPage from '@/pages/settings/SettingsPage';
import CompanyUserListPage from '@/pages/company/CompanyUserListPage';
import {
  CustomerListPage,
  CustomerDetailPage,
  CorporateCustomerForm,
  SMECustomerForm,
  IndividualCustomerForm,
  CustomerEditPage,
} from '@/pages/customers';
import { EmploymentListPage, BulkUploadPage } from '@/pages/employment';
import { AuthorizationListPage, AuthorizationFormPage } from '@/pages/authorizations';
import {
  AccountListPage,
  AccountDetailPage,
  AccountOpeningForm,
} from '@/pages/accounts';
import {
  ProductListPage,
  ProductDetailPage,
  ProductFormPage,
  ProductVersionComparePage,
} from '@/pages/products';
import { MasterDataListPage } from '@/pages/master-data';
import { LimitListPage } from '@/pages/limits';
import { ChargeListPage } from '@/pages/charges';
import {
  PayrollPage,
  LiquidityPage,
  ReceivablesPage,
  BatchPaymentPage,
  AutoCollectionPage,
} from '@/pages/cash-management';

function App() {
  return (
    <ConfigProvider>
      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<LoginPage />} />
        <Route path="/mfa-verify" element={<MfaVerifyPage />} />
        <Route path="/unauthorized" element={<UnauthorizedPage />} />
        
        {/* Protected routes */}
        <Route
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          
          {/* Profile - accessible to all authenticated users */}
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/profile/change-password" element={<ChangePasswordPage />} />
          <Route path="/settings" element={<SettingsPage />} />
          
          {/* IAM Routes - System Admin only - role check via element prop */}
          <Route path="/iam" element={<Navigate to="/iam/users" replace />} />
          <Route path="/iam/users" element={<SystemAdminOnly><UserListPage /></SystemAdminOnly>} />
          <Route path="/iam/users/new" element={<SystemAdminOnly><UserFormPage /></SystemAdminOnly>} />
          <Route path="/iam/users/:id" element={<SystemAdminOnly><UserFormPage /></SystemAdminOnly>} />
          <Route path="/iam/roles" element={<SystemAdminOnly><RoleListPage /></SystemAdminOnly>} />
          <Route path="/iam/roles/new" element={<SystemAdminOnly><RoleFormPage /></SystemAdminOnly>} />
          <Route path="/iam/roles/:id" element={<SystemAdminOnly><RoleFormPage /></SystemAdminOnly>} />
          <Route path="/iam/roles/:id/edit" element={<SystemAdminOnly><RoleFormPage /></SystemAdminOnly>} />
          <Route path="/iam/activity" element={<SystemAdminOnly><ActivityPage /></SystemAdminOnly>} />
          <Route path="/iam/thresholds" element={<SystemAdminOnly><ThresholdListPage /></SystemAdminOnly>} />
          
          {/* Company Admin Routes */}
          <Route path="/company" element={<Navigate to="/company/users" replace />} />
          <Route path="/company/users" element={<CompanyAdminOnly><CompanyUserListPage /></CompanyAdminOnly>} />
          
          {/* Other routes */}
          <Route path="/customers" element={<CustomerListPage />} />
          <Route path="/customers/new" element={<IndividualCustomerForm />} />
          <Route path="/customers/new/corporate" element={<CorporateCustomerForm />} />
          <Route path="/customers/new/sme" element={<SMECustomerForm />} />
          <Route path="/customers/:id" element={<CustomerDetailPage />} />
          <Route path="/customers/:id/edit" element={<CustomerEditPage />} />
          <Route path="/accounts" element={<AccountListPage />} />
          <Route path="/accounts/new" element={<AccountOpeningForm />} />
          <Route path="/accounts/:accountNumber" element={<AccountDetailPage />} />
          <Route path="/products" element={<ProductListPage />} />
          <Route path="/products/new" element={<ProductFormPage />} />
          <Route path="/products/:id" element={<ProductDetailPage />} />
          <Route path="/products/:id/edit" element={<ProductFormPage />} />
          <Route path="/products/:id/compare" element={<ProductVersionComparePage />} />
          <Route path="/master-data" element={<MasterDataListPage />} />
          <Route path="/limits" element={<LimitListPage />} />
          <Route path="/charges" element={<ChargeListPage />} />
          <Route path="/cash-management/payroll" element={<PayrollPage />} />
          <Route path="/cash-management/liquidity" element={<LiquidityPage />} />
          <Route path="/cash-management/receivables" element={<ReceivablesPage />} />
          <Route path="/cash-management/batch-payments" element={<BatchPaymentPage />} />
          <Route path="/cash-management/auto-collection" element={<AutoCollectionPage />} />
          <Route path="/transactions" element={<div>Transactions</div>} />
          <Route path="/customers/:customerId/employees" element={<EmploymentListPage />} />
          <Route path="/customers/:customerId/employees/bulk-upload" element={<BulkUploadPage />} />
          <Route path="/customers/:customerId/authorizations" element={<AuthorizationListPage />} />
          <Route path="/customers/:customerId/authorizations/new" element={<AuthorizationFormPage />} />
          <Route path="/customers/:customerId/authorizations/:authorizationId/edit" element={<AuthorizationFormPage />} />
        </Route>
      </Routes>
    </ConfigProvider>
  );
}

export default App;
