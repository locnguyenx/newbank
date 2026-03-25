import React from 'react';
import { Layout, Menu } from 'antd';
import { useNavigate, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import type { RootState } from '@/store';
import type { MenuProps } from 'antd';
import {
  DashboardOutlined,
  UserOutlined,
  TeamOutlined,
  HistoryOutlined,
  BankOutlined,
  SettingOutlined,
} from '@ant-design/icons';

const { Sider } = Layout;

const menuConfig: Record<string, MenuProps['items']> = {
  SYSTEM_ADMIN: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/customers', icon: <UserOutlined />, label: 'Customers' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
    { key: '/products', icon: <SettingOutlined />, label: 'Products' },
    { key: '/iam/users', icon: <TeamOutlined />, label: 'User Management' },
    { key: '/iam/roles', icon: <TeamOutlined />, label: 'Role Management' },
    { key: '/iam/activity', icon: <HistoryOutlined />, label: 'Activity' },
    { key: '/iam/thresholds', icon: <BankOutlined />, label: 'Thresholds' },
    { key: '/master-data', icon: <SettingOutlined />, label: 'Master Data' },
    { key: '/limits', icon: <BankOutlined />, label: 'Limits' },
    { key: '/charges', icon: <BankOutlined />, label: 'Charges' },
    {
      key: '/cash-management',
      icon: <BankOutlined />,
      label: 'Cash Management',
      children: [
        { key: '/cash-management/payroll', label: 'Payroll' },
        { key: '/cash-management/liquidity', label: 'Liquidity' },
        { key: '/cash-management/receivables', label: 'Receivables' },
        { key: '/cash-management/batch-payments', label: 'Batch Payments' },
        { key: '/cash-management/auto-collection', label: 'Auto-Collection' },
      ],
    },
  ],
  COMPANY_ADMIN: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/company/users', icon: <TeamOutlined />, label: 'Users' },
    { key: '/company/thresholds', icon: <BankOutlined />, label: 'Thresholds' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
    { key: '/cash-management', icon: <BankOutlined />, label: 'Cash Management' },
  ],
  COMPANY_MAKER: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
    { key: '/cash-management', icon: <BankOutlined />, label: 'Cash Management' },
  ],
  COMPANY_CHECKER: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
    { key: '/cash-management', icon: <BankOutlined />, label: 'Cash Management' },
  ],
  COMPANY_VIEWER: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
  ],
  DEFAULT: [
    { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: '/customers', icon: <UserOutlined />, label: 'Customers' },
    { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
    { key: '/products', icon: <SettingOutlined />, label: 'Products' },
    { key: '/master-data', icon: <SettingOutlined />, label: 'Master Data' },
    { key: '/limits', icon: <BankOutlined />, label: 'Limits' },
    { key: '/charges', icon: <BankOutlined />, label: 'Charges' },
    {
      key: '/cash-management',
      icon: <BankOutlined />,
      label: 'Cash Management',
      children: [
        { key: '/cash-management/payroll', label: 'Payroll' },
        { key: '/cash-management/liquidity', label: 'Liquidity' },
        { key: '/cash-management/receivables', label: 'Receivables' },
        { key: '/cash-management/batch-payments', label: 'Batch Payments' },
        { key: '/cash-management/auto-collection', label: 'Auto-Collection' },
      ],
    },
  ],
};

const Sidebar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useSelector((state: RootState) => state.auth);

  const userRoles = user?.roles || [];
  const primaryRole = userRoles.length > 0 ? userRoles[0] : 'DEFAULT';
  const menuItems = menuConfig[primaryRole] || menuConfig.DEFAULT;

  const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
    navigate(key);
  };

  return (
    <Sider width={200} collapsible breakpoint="lg" className="sidebar">
      <Menu
        mode="inline"
        selectedKeys={[location.pathname]}
        items={menuItems}
        onClick={handleMenuClick}
        style={{ height: '100%', borderRight: 0 }}
      />
    </Sider>
  );
};

export default Sidebar;
