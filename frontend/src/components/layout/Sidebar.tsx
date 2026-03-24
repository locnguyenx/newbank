import React from 'react';
import { Layout, Menu } from 'antd';
import { useNavigate, useLocation } from 'react-router-dom';
import type { MenuProps } from 'antd';

const { Sider } = Layout;

const Sidebar: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems: MenuProps['items'] = [
    { key: '/dashboard', label: 'Dashboard' },
    { key: '/customers', label: 'Customers' },
    { key: '/accounts', label: 'Accounts' },
    { key: '/products', label: 'Products' },
    { key: '/master-data', label: 'Master Data' },
    { key: '/limits', label: 'Limits' },
    { key: '/charges', label: 'Charges' },
    {
      key: '/cash-management',
      label: 'Cash Management',
      children: [
        { key: '/cash-management/payroll', label: 'Payroll' },
        { key: '/cash-management/liquidity', label: 'Liquidity' },
        { key: '/cash-management/receivables', label: 'Receivables' },
        { key: '/cash-management/batch-payments', label: 'Batch Payments' },
        { key: '/cash-management/auto-collection', label: 'Auto-Collection' },
      ],
    },
  ];

  const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
    navigate(key);
  };

  return (
    <Sider
      width={200}
      collapsible
      breakpoint="lg"
      className="sidebar"
    >
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
