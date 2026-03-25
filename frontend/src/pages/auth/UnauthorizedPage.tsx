import React from 'react';
import { Result, Button } from 'antd';
import { useNavigate } from 'react-router-dom';

const UnauthorizedPage: React.FC = () => {
  const navigate = useNavigate();

  return (
    <Result
      status="403"
      title="Access Denied"
      subTitle="You do not have permission to access this page."
      extra={
        <Button type="primary" onClick={() => navigate('/dashboard')}>
          Back to Dashboard
        </Button>
      }
    />
  );
};

export default UnauthorizedPage;
