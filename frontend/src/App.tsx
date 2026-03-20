import { Routes, Route, Navigate } from 'react-router-dom';
import { AppLayout } from '@/components/layout';
import { ConfigProvider } from 'antd';
import DashboardPage from '@/pages/DashboardPage';
import {
  CustomerListPage,
  CustomerDetailPage,
  CorporateCustomerForm,
  SMECustomerForm,
  IndividualCustomerForm,
} from '@/pages/customers';
import { EmploymentListPage, BulkUploadPage } from '@/pages/employment';
import { AuthorizationListPage, AuthorizationFormPage } from '@/pages/authorizations';
import {
  AccountListPage,
  AccountDetailPage,
  AccountOpeningForm,
} from '@/pages/accounts';

function App() {
  return (
    <ConfigProvider>
      <AppLayout>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<DashboardPage />} />
          <Route path="/customers" element={<CustomerListPage />} />
          <Route path="/customers/new" element={<IndividualCustomerForm />} />
          <Route path="/customers/new/corporate" element={<CorporateCustomerForm />} />
          <Route path="/customers/new/sme" element={<SMECustomerForm />} />
          <Route path="/customers/:id" element={<CustomerDetailPage />} />
          <Route path="/customers/:id/edit" element={<IndividualCustomerForm />} />
          <Route path="/accounts" element={<AccountListPage />} />
          <Route path="/accounts/new" element={<AccountOpeningForm />} />
          <Route path="/accounts/:accountNumber" element={<AccountDetailPage />} />
          <Route path="/transactions" element={<div>Transactions</div>} />
          <Route path="/customers/:customerId/employees" element={<EmploymentListPage />} />
          <Route path="/customers/:customerId/employees/bulk-upload" element={<BulkUploadPage />} />
          <Route path="/customers/:customerId/authorizations" element={<AuthorizationListPage />} />
          <Route path="/customers/:customerId/authorizations/new" element={<AuthorizationFormPage />} />
          <Route path="/customers/:customerId/authorizations/:authorizationId/edit" element={<AuthorizationFormPage />} />
        </Routes>
      </AppLayout>
    </ConfigProvider>
  );
}

export default App;
