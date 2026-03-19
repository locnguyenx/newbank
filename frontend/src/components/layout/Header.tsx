import React from 'react';
import { Layout, theme, Avatar, Dropdown, Space } from 'antd';
import type { MenuProps } from 'antd';
import { UserOutlined, LogoutOutlined } from '@ant-design/icons';

const { Header } = Layout;

const AppHeader: React.FC = () => {
  const {
    token: { colorBgContainer },
  } = theme.useToken();

  const userMenuItems: MenuProps['items'] = [
    { key: 'profile', icon: <UserOutlined />, label: 'Profile' },
    { type: 'divider' },
    { key: 'logout', icon: <LogoutOutlined />, label: 'Logout' },
  ];

  return (
    <Header style={{ padding: '0 24px', background: colorBgContainer }}>
      <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', height: '100%' }}>
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <Space style={{ cursor: 'pointer' }}>
            <Avatar icon={<UserOutlined />} />
            <span>Admin User</span>
          </Space>
        </Dropdown>
      </div>
    </Header>
  );
};

export default AppHeader;
