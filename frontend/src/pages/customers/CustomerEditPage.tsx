import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Spin } from 'antd';
import { CorporateCustomerForm } from './CorporateCustomerForm';
import { SMECustomerForm } from './SMECustomerForm';
import { IndividualCustomerForm } from './IndividualCustomerForm';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchCustomerById } from '@/store/slices/customerSlice';
import type { CustomerResponse } from '@/api/api';

export function CustomerEditPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedCustomer: customer, loading } = useAppSelector((state) => state.customer);

  useEffect(() => {
    if (id) {
      dispatch(fetchCustomerById(parseInt(id, 10)));
    }
  }, [dispatch, id]);

  if (loading || !customer) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  // Render the appropriate form based on customer type - only one form is rendered
  switch (customer.type) {
    case 'CORPORATE':
      return <CorporateCustomerForm />;
    case 'SME':
      return <SMECustomerForm />;
    case 'INDIVIDUAL':
    default:
      return <IndividualCustomerForm />;
  }
}
