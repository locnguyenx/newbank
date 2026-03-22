import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Table, Select, Button, Space, Tag, Card, Typography } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchEmployments } from '@/store/slices/employmentSlice';
// @ts-expect-error - Employment and EmploymentStatus may not be exported
import type { Employment, EmploymentStatus } from '@/types';

const { Title } = Typography;

export function EmploymentListPage() {
  const { customerId } = useParams<{ customerId: string }>();
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { employments, loading } = useAppSelector((state) => state.employment);
  const [statusFilter, setStatusFilter] = useState<EmploymentStatus | undefined>();

  useEffect(() => {
    if (customerId) {
      dispatch(fetchEmployments(Number(customerId)));
    }
  }, [dispatch, customerId]);

  const filteredEmployments = statusFilter
    ? employments.filter((e) => e.status === statusFilter)
    : employments;

  const columns: ColumnsType<Employment> = [
    {
      title: 'Employee ID',
      dataIndex: 'employeeId',
      key: 'employeeId',
      width: 130,
    },
    {
      title: 'Name',
      dataIndex: 'employeeName',
      key: 'employeeName',
    },
    {
      title: 'Department',
      dataIndex: 'department',
      key: 'department',
    },
    {
      title: 'Position',
      dataIndex: 'position',
      key: 'position',
    },
    {
      title: 'Start Date',
      dataIndex: 'startDate',
      key: 'startDate',
      width: 120,
      render: (date: string) => new Date(date).toLocaleDateString(),
    },
    {
      title: 'Salary',
      key: 'salary',
      width: 150,
      render: (_, record) => `${record.currency} ${record.salary.toLocaleString()}`,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      width: 120,
      render: (status: EmploymentStatus) => {
        const colorMap: Record<EmploymentStatus, string> = {
          ACTIVE: 'green',
          INACTIVE: 'orange',
          TERMINATED: 'red',
        };
        return <Tag color={colorMap[status]}>{status}</Tag>;
      },
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <Title level={2} style={{ margin: 0 }}>
          Employees
        </Title>
        <Space>
          <Button type="primary" onClick={() => navigate(`/customers/${customerId}/employees/bulk-upload`)}>
            Bulk Upload
          </Button>
        </Space>
      </div>

      <Card>
        <Space direction="vertical" style={{ width: '100%' }} size="middle">
          <Space wrap>
            <Select
              placeholder="Filter by status"
              style={{ width: 180 }}
              allowClear
              onChange={(value) => setStatusFilter(value)}
            >
              <Select.Option value="ACTIVE">Active</Select.Option>
              <Select.Option value="INACTIVE">Inactive</Select.Option>
              <Select.Option value="TERMINATED">Terminated</Select.Option>
            </Select>
          </Space>

          <Table
            columns={columns}
            dataSource={filteredEmployments}
            rowKey="id"
            loading={loading}
            pagination={{
              showSizeChanger: true,
              showTotal: (total) => `Total ${total} employees`,
            }}
          />
        </Space>
      </Card>
    </div>
  );
}
