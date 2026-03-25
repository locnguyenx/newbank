import React from 'react';
import { Layout, theme, Avatar, Dropdown, Space, message } from 'antd';
import type { MenuProps } from 'antd';
import { UserOutlined, LogoutOutlined, SettingOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logout } from '@/store/slices/authSlice';
import type { RootState } from '@/store';

const { Header } = Layout;

const AppHeader: React.FC = () => {
  const { token: { colorBgContainer } } = theme.useToken();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.auth);

  const handleLogout = async () => {
    try {
      await dispatch(logout() as any);
      message.success('Logged out successfully');
      navigate('/login');
    } catch (error) {
      message.success('Logged out');
      navigate('/login');
    }
  };

  const userMenuItems: MenuProps['items'] = [
    { 
      key: 'profile', 
      icon: <UserOutlined />, 
      label: 'Profile',
      onClick: () => navigate('/profile'),
    },
    { 
      key: 'settings', 
      icon: <SettingOutlined />, 
      label: 'Settings',
      onClick: () => navigate('/settings'),
    },
    { type: 'divider' },
    { 
      key: 'logout', 
      icon: <LogoutOutlined />, 
      label: 'Logout',
      onClick: handleLogout,
    },
  ];

  return (
    <Header style={{ padding: '0 24px', background: colorBgContainer }}>
      <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', height: '100%' }}>
        <Dropdown menu={{ items: userMenuItems }} placement="bottomRight">
          <Space style={{ cursor: 'pointer' }}>
            <Avatar icon={<UserOutlined />} />
            <span>{user?.email || 'User'}</span>
          </Space>
        </Dropdown>
      </div>
    </Header>
  );
};

export default AppHeader;
