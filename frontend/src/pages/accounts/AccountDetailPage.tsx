import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Tabs, Table, Button, Space, Tag, Form, Modal, message, Input, Select, Row, Col, Statistic, DatePicker, Spin } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchAccountByNumber, closeAccount, freezeAccount, unfreezeAccount, addAccountHolder, removeAccountHolder } from '@/store/slices/accountSlice';
import type { AccountHolderSummary, AccountHolderRole, AccountStatement } from '@/types/account.types';
import { accountService } from '@/services/accountService';
import { Plus, Trash2, Lock, Unlock, CreditCard, Users, Calendar, Shield, TrendingUp, TrendingDown } from 'lucide-react';

const { RangePicker } = DatePicker;

const { TabPane } = Tabs;

export function AccountDetailPage() {
  const { accountNumber } = useParams<{ accountNumber: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { selectedAccount, loading } = useAppSelector((state) => state.account);
  
  const [holders, setHolders] = useState<AccountHolderSummary[]>([]);
  const [isAddHolderModalOpen, setIsAddHolderModalOpen] = useState(false);
  const [addHolderForm] = Form.useForm();
  const [statement, setStatement] = useState<AccountStatement | null>(null);
  const [statementLoading, setStatementLoading] = useState(false);
  const [dateRange, setDateRange] = useState<[string, string] | null>(null);

  useEffect(() => {
    if (accountNumber) {
      dispatch(fetchAccountByNumber(accountNumber));
    }
  }, [dispatch, accountNumber]);

  useEffect(() => {
    if (selectedAccount) {
      setHolders(selectedAccount.holders || []);
    }
  }, [selectedAccount]);

  const fetchStatement = async (from?: string, to?: string) => {
    if (!accountNumber) return;
    setStatementLoading(true);
    try {
      const data = await accountService.getStatement(accountNumber, from || '', to || '');
      setStatement(data);
    } catch {
      message.error('Failed to load statement');
    } finally {
      setStatementLoading(false);
    }
  };

  useEffect(() => {
    if (accountNumber) {
      const now = new Date();
      const threeMonthsAgo = new Date(now.setMonth(now.getMonth() - 3));
      const from = threeMonthsAgo.toISOString().split('T')[0];
      const to = new Date().toISOString().split('T')[0];
      setDateRange([from, to]);
      fetchStatement(from, to);
    }
  }, [accountNumber]);

  const handleClose = async () => {
    if (accountNumber) {
      await dispatch(closeAccount(accountNumber));
      message.success('Account closed');
    }
  };

  const handleFreeze = async () => {
    if (accountNumber) {
      await dispatch(freezeAccount(accountNumber));
      message.success('Account frozen');
    }
  };

  const handleUnfreeze = async () => {
    if (accountNumber) {
      await dispatch(unfreezeAccount(accountNumber));
      message.success('Account unfrozen');
    }
  };

  const handleAddHolder = async (values: { customerId: number; role: AccountHolderRole }) => {
    if (accountNumber) {
      await dispatch(addAccountHolder({ accountNumber, holderData: values }));
      message.success('Holder added');
      setIsAddHolderModalOpen(false);
      addHolderForm.resetFields();
      // Reload account to get updated holders
      dispatch(fetchAccountByNumber(accountNumber));
    }
  };

  const getStatusColor = (status: string) => {
    const colors: Record<string, string> = {
      ACTIVE: 'green',
      CLOSED: 'red',
      FROZEN: 'orange',
      DORMANT: 'default',
      PENDING: 'blue',
    };
    return colors[status] || 'default';
  };

  const holderColumns: ColumnsType<AccountHolderSummary> = [
    {
      title: 'Customer',
      dataIndex: 'customerName',
      key: 'customerName',
    },
    {
      title: 'Role',
      dataIndex: 'role',
      key: 'role',
      render: (role: string) => role,
    },
    {
      title: 'Status',
      dataIndex: 'status',
      key: 'status',
      render: (status: string) => <Tag>{status}</Tag>,
    },
    {
      title: 'Effective From',
      dataIndex: 'effectiveFrom',
      key: 'effectiveFrom',
    },
    {
      title: 'Actions',
      key: 'actions',
      render: (_, record) => (
        <Button
          danger
          size="small"
          icon={<Trash2 size={16} />}
          onClick={() => {
            if (accountNumber) {
           dispatch(removeAccountHolder({ accountNumber, holderId: record.id }));
              message.success('Holder removed');
            }
          }}
        >
          Remove
        </Button>
      ),
    },
  ];

  if (!selectedAccount && !loading) {
    return <Card>Account not found</Card>;
  }

  return (
    <Card
      title={`Account: ${accountNumber}`}
      loading={loading}
      extra={
        <Space>
          {selectedAccount?.status === 'ACTIVE' && (
            <Button danger onClick={handleClose}>
              Close Account
            </Button>
          )}
          {selectedAccount?.status === 'FROZEN' ? (
            <Button onClick={handleUnfreeze} icon={<Unlock size={16} />}>
              Unfreeze
            </Button>
          ) : selectedAccount?.status === 'ACTIVE' && (
            <Button onClick={handleFreeze} icon={<Lock size={16} />}>
              Freeze
            </Button>
          )}
          <Button onClick={() => navigate('/accounts')}>Back to List</Button>
        </Space>
      }
    >
      <Tabs defaultActiveKey="info">
        <TabPane tab="Account Info" key="info">
          <div
            style={{
              background: 'linear-gradient(135deg, #1890ff 0%, #096dd9 100%)',
              borderRadius: 12,
              padding: '24px 32px',
              color: '#fff',
              marginBottom: 24,
            }}
          >
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 16 }}>
              <Space>
                <CreditCard size={20} />
                <span style={{ fontSize: 16, fontWeight: 500 }}>{selectedAccount?.accountNumber}</span>
              </Space>
              <Tag
                color={getStatusColor(selectedAccount?.status || '')}
                style={{ margin: 0 }}
              >
                {selectedAccount?.status}
              </Tag>
            </div>
            <div style={{ fontSize: 13, opacity: 0.85, marginBottom: 4 }}>Available Balance</div>
            <div style={{ fontSize: 32, fontWeight: 700, marginBottom: 2 }}>
              {selectedAccount?.balance?.toLocaleString(undefined, { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
            </div>
            <div style={{ fontSize: 14, opacity: 0.85 }}>{selectedAccount?.currency}</div>
          </div>

          <Row gutter={24}>
            <Col xs={24} md={12}>
              <Card
                title={
                  <Space>
                    <CreditCard size={16} />
                    <span>Account Details</span>
                  </Space>
                }
                style={{ marginBottom: 16 }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ color: '#8c8c8c' }}>Type</span>
                  <span style={{ fontWeight: 500 }}>{selectedAccount?.type}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ color: '#8c8c8c' }}>Product ID</span>
                  <span style={{ fontWeight: 500 }}>{selectedAccount?.productId}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ color: '#8c8c8c' }}>Currency</span>
                  <span style={{ fontWeight: 500 }}>{selectedAccount?.currency}</span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
                  <span style={{ color: '#8c8c8c' }}>
                    <Space>
                      <Users size={14} />
                      <span>Holders</span>
                    </Space>
                  </span>
                  <span style={{ fontWeight: 500 }}>{holders.length}</span>
                </div>
              </Card>
            </Col>
            <Col xs={24} md={12}>
              <Card
                title={
                  <Space>
                    <Shield size={16} />
                    <span>Status & Timeline</span>
                  </Space>
                }
                style={{ marginBottom: 16 }}
              >
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ color: '#8c8c8c' }}>Status</span>
                  <Tag color={getStatusColor(selectedAccount?.status || '')}>{selectedAccount?.status}</Tag>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                  <span style={{ color: '#8c8c8c' }}>
                    <Space>
                      <Calendar size={14} />
                      <span>Opened</span>
                    </Space>
                  </span>
                  <span style={{ fontWeight: 500 }}>
                    {selectedAccount?.openedAt
                      ? new Date(selectedAccount.openedAt).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
                      : '—'}
                  </span>
                </div>
                <div style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0' }}>
                  <span style={{ color: '#8c8c8c' }}>
                    <Space>
                      <Calendar size={14} />
                      <span>Closed</span>
                    </Space>
                  </span>
                  <span style={{ fontWeight: 500 }}>
                    {selectedAccount?.closedAt
                      ? new Date(selectedAccount.closedAt).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' })
                      : '—'}
                  </span>
                </div>
              </Card>
            </Col>
          </Row>
        </TabPane>

        <TabPane
          tab={
            <span>
              Account Holders
              <Button
                type="primary"
                size="small"
                icon={<Plus size={16} />}
                style={{ marginLeft: 8 }}
                onClick={() => setIsAddHolderModalOpen(true)}
              >
                Add Holder
              </Button>
            </span>
          }
          key="holders"
        >
          <Table
            columns={holderColumns}
            dataSource={holders}
            rowKey="id"
            pagination={false}
          />
        </TabPane>

        <TabPane tab="Transactions" key="transactions">
          <Space style={{ marginBottom: 16 }} wrap>
            <RangePicker
              onChange={(dates, dateStrings) => {
                if (dates && dates[0] && dates[1]) {
                  setDateRange([dateStrings[0], dateStrings[1]]);
                  fetchStatement(dateStrings[0], dateStrings[1]);
                }
              }}
            />
            <Button onClick={() => fetchStatement(dateRange?.[0], dateRange?.[1])}>
              Refresh
            </Button>
          </Space>

          {statementLoading ? (
            <div style={{ textAlign: 'center', padding: 40 }}>
              <Spin />
            </div>
          ) : statement ? (
            <>
              <Row gutter={16} style={{ marginBottom: 24 }}>
                <Col span={6}>
                  <Card size="small">
                    <Statistic
                      title="Opening Balance"
                      value={statement.openingBalance}
                      precision={2}
                      prefix="$"
                    />
                  </Card>
                </Col>
                <Col span={6}>
                  <Card size="small">
                    <Statistic
                      title="Closing Balance"
                      value={statement.closingBalance}
                      precision={2}
                      prefix="$"
                    />
                  </Card>
                </Col>
                <Col span={6}>
                  <Card size="small">
                    <Statistic
                      title="Total Credits"
                      value={statement.totalCredits}
                      precision={2}
                      prefix="$"
                      valueStyle={{ color: '#3f8600' }}
                      prefix={<TrendingUp size={16} />}
                    />
                  </Card>
                </Col>
                <Col span={6}>
                  <Card size="small">
                    <Statistic
                      title="Total Debits"
                      value={statement.totalDebits}
                      precision={2}
                      prefix="$"
                      valueStyle={{ color: '#cf1322' }}
                      prefix={<TrendingDown size={16} />}
                    />
                  </Card>
                </Col>
              </Row>

              <Table
                dataSource={statement.transactions.map((t, i) => ({ ...t, key: t.id || i }))}
                rowKey="id"
                pagination={{ pageSize: 10, showSizeChanger: true }}
                columns={[
                  {
                    title: 'Date',
                    dataIndex: 'date',
                    key: 'date',
                    render: (date: string) => new Date(date).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' }),
                  },
                  {
                    title: 'Reference',
                    dataIndex: 'id',
                    key: 'id',
                  },
                  {
                    title: 'Description',
                    dataIndex: 'description',
                    key: 'description',
                  },
                  {
                    title: 'Type',
                    dataIndex: 'type',
                    key: 'type',
                    render: (type: string) => (
                      <Tag color={type === 'CREDIT' ? 'green' : 'red'}>{type}</Tag>
                    ),
                  },
                  {
                    title: 'Amount',
                    dataIndex: 'amount',
                    key: 'amount',
                    align: 'right' as const,
                    render: (amount: number, record: { type: string }) => (
                      <span style={{ color: record.type === 'CREDIT' ? '#3f8600' : '#cf1322' }}>
                        {record.type === 'CREDIT' ? '+' : '-'}${amount.toFixed(2)}
                      </span>
                    ),
                  },
                ]}
              />
            </>
          ) : (
            <div style={{ textAlign: 'center', padding: 40, color: '#999' }}>
              No transactions found for the selected period.
            </div>
          )}
        </TabPane>
      </Tabs>

      <Modal
        title="Add Account Holder"
        open={isAddHolderModalOpen}
        onCancel={() => {
          setIsAddHolderModalOpen(false);
          addHolderForm.resetFields();
        }}
        onOk={() => addHolderForm.submit()}
      >
        <Form form={addHolderForm} onFinish={handleAddHolder} layout="vertical">
          <Form.Item
            name="customerId"
            label="Customer ID"
            rules={[{ required: true, message: 'Please enter customer ID' }]}
          >
            <Input type="number" placeholder="Enter customer ID" />
          </Form.Item>
          <Form.Item
            name="role"
            label="Role"
            rules={[{ required: true, message: 'Please select role' }]}
          >
            <Select
              placeholder="Select role"
              options={[
                { label: 'PRIMARY', value: 'PRIMARY' },
                { label: 'JOINT', value: 'JOINT' },
                { label: 'AUTHORIZED_SIGNATORY', value: 'AUTHORIZED_SIGNATORY' },
                { label: 'NOMINEE', value: 'NOMINEE' },
              ]}
            />
          </Form.Item>
        </Form>
      </Modal>
    </Card>
  );
}
