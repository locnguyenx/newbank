import React, { useState } from 'react';
import { Form, Input, Button, Card, message } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { verifyMfa } from '@/store/slices/authSlice';
import type { RootState } from '@/store';

const MfaVerifyPage: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { error } = useSelector((state: RootState) => state.auth);

  const onFinish = async (values: { code: string }) => {
    setLoading(true);
    try {
      const result = await dispatch(verifyMfa({ code: values.code } as any) as any);
      if (verifyMfa.fulfilled.match(result)) {
        message.success('Verification successful');
        navigate('/dashboard');
      } else {
        message.error(error || 'Invalid verification code');
      }
    } catch (err) {
      message.error('Invalid verification code');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ 
      display: 'flex', 
      justifyContent: 'center', 
      alignItems: 'center', 
      minHeight: '100vh', 
      background: '#f0f2f5' 
    }}>
      <Card style={{ width: 400, boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
        <h2 style={{ textAlign: 'center', marginBottom: 8, marginTop: 0 }}>
          Two-Factor Authentication
        </h2>
        <p style={{ textAlign: 'center', marginBottom: 24, color: '#666' }}>
          Enter the 6-digit code from your authenticator app
        </p>
        <Form
          name="mfa-verify"
          onFinish={onFinish}
          layout="vertical"
          size="large"
        >
          <Form.Item
            name="code"
            rules={[{ required: true, message: 'Please enter the code' }]}
          >
            <Input.OTP length={6} />
          </Form.Item>

          <Form.Item>
            <Button 
              type="primary" 
              htmlType="submit" 
              loading={loading} 
              block
            >
              Verify
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
};

export default MfaVerifyPage;
