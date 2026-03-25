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
  DollarOutlined,
  FileTextOutlined,
  CreditCardOutlined,
} from '@ant-design/icons';

const { Sider } = Layout;

const menuConfig: Record<string, MenuProps['items']> = {
  SYSTEM_ADMIN: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/customers', icon: <UserOutlined />, label: 'Customers' },
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
        {
          key: '/cash-management',
          icon: <DollarOutlined />,
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
    },
    {
      type: 'group',
      label: 'Foundation',
      key: 'foundation',
      children: [
        { key: '/products', icon: <SettingOutlined />, label: 'Products' },
        { key: '/master-data', icon: <FileTextOutlined />, label: 'Master Data' },
        { key: '/charges', icon: <CreditCardOutlined />, label: 'Charges' },
        { key: '/limits', icon: <BankOutlined />, label: 'Limits' },
      ],
    },
    {
      type: 'group',
      label: 'Operations',
      key: 'operations',
      children: [
        { key: '/iam/users', icon: <TeamOutlined />, label: 'User Management' },
        { key: '/iam/roles', icon: <TeamOutlined />, label: 'Role Management' },
        { key: '/iam/activity', icon: <HistoryOutlined />, label: 'Activity' },
        { key: '/iam/thresholds', icon: <BankOutlined />, label: 'Thresholds' },
      ],
    },
  ],
  COMPANY_ADMIN: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
        {
          key: '/cash-management',
          icon: <DollarOutlined />,
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
    },
    {
      type: 'group',
      label: 'Operations',
      key: 'operations',
      children: [
        { key: '/company/users', icon: <TeamOutlined />, label: 'Users' },
        { key: '/company/thresholds', icon: <BankOutlined />, label: 'Thresholds' },
      ],
    },
  ],
  COMPANY_MAKER: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
        {
          key: '/cash-management',
          icon: <DollarOutlined />,
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
    },
  ],
  COMPANY_CHECKER: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
        {
          key: '/cash-management',
          icon: <DollarOutlined />,
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
    },
  ],
  COMPANY_VIEWER: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
      ],
    },
  ],
  DEFAULT: [
    {
      type: 'group',
      label: 'Dashboard',
      key: 'dashboard',
      children: [
        { key: '/dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
      ],
    },
    {
      type: 'group',
      label: 'Business',
      key: 'business',
      children: [
        { key: '/customers', icon: <UserOutlined />, label: 'Customers' },
        { key: '/accounts', icon: <BankOutlined />, label: 'Accounts' },
        {
          key: '/cash-management',
          icon: <DollarOutlined />,
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
    },
    {
      type: 'group',
      label: 'Foundation',
      key: 'foundation',
      children: [
        { key: '/products', icon: <SettingOutlined />, label: 'Products' },
        { key: '/master-data', icon: <FileTextOutlined />, label: 'Master Data' },
        { key: '/limits', icon: <BankOutlined />, label: 'Limits' },
        { key: '/charges', icon: <CreditCardOutlined />, label: 'Charges' },
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
    <Sider width={220} collapsible breakpoint="lg" className="sidebar">
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
