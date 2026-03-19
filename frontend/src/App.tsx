import { Routes, Route, Navigate } from 'react-router-dom';
import { AppLayout } from '@/components/layout';
import { ConfigProvider } from 'antd';

function App() {
  return (
    <ConfigProvider>
      <AppLayout>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/dashboard" element={<div>Dashboard</div>} />
          <Route path="/customers" element={<div>Customers</div>} />
          <Route path="/accounts" element={<div>Accounts</div>} />
          <Route path="/transactions" element={<div>Transactions</div>} />
        </Routes>
      </AppLayout>
    </ConfigProvider>
  );
}

export default App;
