import { useEffect, useState } from 'react';
import { Table, Button, Tabs, Card, message, Tag, Modal, Form, Input, Select, Space, Popconfirm } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { PlusOutlined, EditOutlined, StopOutlined } from '@ant-design/icons';
import { masterDataService } from '@/services/masterDataService';

interface Currency {
  code?: string;
  name?: string;
  symbol?: string;
  decimalPlaces?: number;
  active?: boolean;
}

interface Country {
  isoCode?: string;
  name?: string;
  region?: string;
  active?: boolean;
}

interface Branch {
  code?: string;
  name?: string;
  countryCode?: string;
  address?: string;
  active?: boolean;
}

interface Channel {
  code?: string;
  name?: string;
  active?: boolean;
}

interface DocumentType {
  code?: string;
  name?: string;
  category?: string;
  active?: boolean;
}

type MasterDataType = 'currency' | 'country' | 'branch' | 'channel' | 'documentType';
type ModalMode = 'create' | 'edit';

const currencyColumns: ColumnsType<Currency> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Symbol', dataIndex: 'symbol', key: 'symbol', width: 80 },
  { title: 'Decimal Places', dataIndex: 'decimalPlaces', key: 'decimalPlaces', width: 120 },
  {
    title: 'Active',
    dataIndex: 'active',
    key: 'active',
    width: 80,
    render: (active: boolean) => <Tag color={active ? 'green' : 'red'}>{active ? 'Yes' : 'No'}</Tag>,
  },
];

const countryColumns: ColumnsType<Country> = [
  { title: 'ISO Code', dataIndex: 'isoCode', key: 'isoCode', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Region', dataIndex: 'region', key: 'region', width: 150 },
  {
    title: 'Active',
    dataIndex: 'active',
    key: 'active',
    width: 80,
    render: (active: boolean) => <Tag color={active ? 'green' : 'red'}>{active ? 'Yes' : 'No'}</Tag>,
  },
];

const branchColumns: ColumnsType<Branch> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Country', dataIndex: 'countryCode', key: 'countryCode', width: 100 },
  {
    title: 'Active',
    dataIndex: 'active',
    key: 'active',
    width: 80,
    render: (active: boolean) => <Tag color={active ? 'green' : 'red'}>{active ? 'Yes' : 'No'}</Tag>,
  },
];

const channelColumns: ColumnsType<Channel> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  {
    title: 'Active',
    dataIndex: 'active',
    key: 'active',
    width: 80,
    render: (active: boolean) => <Tag color={active ? 'green' : 'red'}>{active ? 'Yes' : 'No'}</Tag>,
  },
];

const documentTypeColumns: ColumnsType<DocumentType> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Category', dataIndex: 'category', key: 'category', width: 150 },
  {
    title: 'Active',
    dataIndex: 'active',
    key: 'active',
    width: 80,
    render: (active: boolean) => <Tag color={active ? 'green' : 'red'}>{active ? 'Yes' : 'No'}</Tag>,
  },
];

export function MasterDataListPage() {
  const [activeTab, setActiveTab] = useState('currencies');
  const [currencies, setCurrencies] = useState<Currency[]>([]);
  const [countries, setCountries] = useState<Country[]>([]);
  const [branches, setBranches] = useState<Branch[]>([]);
  const [channels, setChannels] = useState<Channel[]>([]);
  const [documentTypes, setDocumentTypes] = useState<DocumentType[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [modalMode, setModalMode] = useState<ModalMode>('create');
  const [modalType, setModalType] = useState<MasterDataType | null>(null);
  const [editingRecord, setEditingRecord] = useState<any>(null);
  const [form] = Form.useForm();
  const [saving, setSaving] = useState(false);

  const fetchAllData = async () => {
    setLoading(true);
    try {
      const [curr, count, br, ch, doc] = await Promise.all([
        masterDataService.getCurrencies(),
        masterDataService.getCountries(),
        masterDataService.getBranches(),
        masterDataService.getChannels(),
        masterDataService.getDocumentTypes(),
      ]);
      setCurrencies(curr || []);
      setCountries(count || []);
      setBranches(br || []);
      setChannels(ch || []);
      setDocumentTypes(doc || []);
    } catch (error) {
      message.error('Failed to load master data');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchAllData();
  }, []);

  const handleCreate = (type: MasterDataType) => {
    form.resetFields();
    setModalType(type);
    setModalMode('create');
    setEditingRecord(null);
    setModalOpen(true);
  };

  const handleEdit = (record: any, type: MasterDataType) => {
    form.resetFields();
    setModalType(type);
    setModalMode('edit');
    setEditingRecord(record);
    form.setFieldsValue(getFormValues(record, type));
    setModalOpen(true);
  };

  const getFormValues = (record: any, type: MasterDataType) => {
    switch (type) {
      case 'currency':
        return { code: record.code, name: record.name, symbol: record.symbol, decimalPlaces: record.decimalPlaces };
      case 'country':
        return { isoCode: record.isoCode, name: record.name, region: record.region };
      case 'branch':
        return { code: record.code, name: record.name, countryCode: record.countryCode };
      case 'channel':
        return { code: record.code, name: record.name };
      case 'documentType':
        return { code: record.code, name: record.name, category: record.category };
      default:
        return {};
    }
  };

  const handleSave = async () => {
    try {
      const values = await form.validateFields();
      setSaving(true);
      
      if (modalMode === 'edit' && editingRecord) {
        switch (modalType) {
          case 'currency':
            await masterDataService.updateCurrency(editingRecord.code, values);
            break;
          case 'country':
            await masterDataService.updateCountry(editingRecord.isoCode, values);
            break;
          case 'branch':
            await masterDataService.updateBranch(editingRecord.code, values);
            break;
          case 'channel':
            await masterDataService.updateChannel(editingRecord.code, values);
            break;
          case 'documentType':
            await masterDataService.updateDocumentType(editingRecord.code, values);
            break;
        }
        message.success('Updated successfully');
      } else {
        switch (modalType) {
          case 'currency':
            await masterDataService.createCurrency(values);
            break;
          case 'country':
            await masterDataService.createCountry(values);
            break;
          case 'branch':
            await masterDataService.createBranch(values);
            break;
          case 'channel':
            await masterDataService.createChannel(values);
            break;
          case 'documentType':
            await masterDataService.createDocumentType(values);
            break;
        }
        message.success('Created successfully');
      }
      
      setModalOpen(false);
      fetchAllData();
    } catch (error: any) {
      message.error(error?.response?.data?.message || `Failed to ${modalMode === 'edit' ? 'update' : 'create'}`);
    } finally {
      setSaving(false);
    }
  };

  const handleDeactivate = async (record: any, type: MasterDataType) => {
    try {
      let code: string;
      switch (type) {
        case 'currency':
          code = record.code;
          await masterDataService.deactivateCurrency(code);
          break;
        case 'country':
          code = record.isoCode;
          await masterDataService.deactivateCountry(code);
          break;
        case 'branch':
          code = record.code;
          await masterDataService.deactivateBranch(code);
          break;
        case 'channel':
          code = record.code;
          await masterDataService.deactivateChannel(code);
          break;
        case 'documentType':
          code = record.code;
          await masterDataService.deactivateDocumentType(code);
          break;
      }
      message.success('Deactivated successfully');
      fetchAllData();
    } catch (error: any) {
      message.error(error?.response?.data?.message || 'Failed to deactivate');
    }
  };

  const getModalTitle = () => {
    const modePrefix = modalMode === 'edit' ? 'Edit' : 'Create';
    const titles: Record<string, string> = {
      currency: `${modePrefix} Currency`,
      country: `${modePrefix} Country`,
      branch: `${modePrefix} Branch`,
      channel: `${modePrefix} Channel`,
      documentType: `${modePrefix} Document Type`,
    };
    return titles[modalType || ''] || 'Master Data';
  };

  const renderForm = () => {
    switch (modalType) {
      case 'currency':
        return (
          <>
            <Form.Item name="code" label="Currency Code" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., USD" disabled={modalMode === 'edit'} />
            </Form.Item>
            <Form.Item name="name" label="Name" rules={[{ required: true }]}>
              <Input placeholder="e.g., US Dollar" />
            </Form.Item>
            <Form.Item name="symbol" label="Symbol" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., $" />
            </Form.Item>
            <Form.Item name="decimalPlaces" label="Decimal Places" initialValue={2}>
              <Input type="number" min={0} max={6} />
            </Form.Item>
          </>
        );
      case 'country':
        return (
          <>
            <Form.Item name="isoCode" label="ISO Code" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., US" maxLength={3} disabled={modalMode === 'edit'} />
            </Form.Item>
            <Form.Item name="name" label="Name" rules={[{ required: true }]}>
              <Input placeholder="e.g., United States" />
            </Form.Item>
            <Form.Item name="region" label="Region">
              <Input placeholder="e.g., North America" />
            </Form.Item>
          </>
        );
      case 'branch':
        return (
          <>
            <Form.Item name="code" label="Branch Code" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., HQ-001" disabled={modalMode === 'edit'} />
            </Form.Item>
            <Form.Item name="name" label="Name" rules={[{ required: true }]}>
              <Input placeholder="e.g., Headquarters" />
            </Form.Item>
            <Form.Item name="countryCode" label="Country Code">
              <Input placeholder="e.g., US" />
            </Form.Item>
          </>
        );
      case 'channel':
        return (
          <>
            <Form.Item name="code" label="Channel Code" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., MOBILE" disabled={modalMode === 'edit'} />
            </Form.Item>
            <Form.Item name="name" label="Name" rules={[{ required: true }]}>
              <Input placeholder="e.g., Mobile Banking" />
            </Form.Item>
          </>
        );
      case 'documentType':
        return (
          <>
            <Form.Item name="code" label="Document Code" rules={[{ required: modalMode === 'create' }]}>
              <Input placeholder="e.g., PASSPORT" disabled={modalMode === 'edit'} />
            </Form.Item>
            <Form.Item name="name" label="Name" rules={[{ required: true }]}>
              <Input placeholder="e.g., Passport" />
            </Form.Item>
            <Form.Item name="category" label="Category">
              <Select>
                <Select.Option value="IDENTITY">Identity</Select.Option>
                <Select.Option value="ADDRESS">Address</Select.Option>
                <Select.Option value="FINANCIAL">Financial</Select.Option>
                <Select.Option value="CORPORATE">Corporate</Select.Option>
                <Select.Option value="OTHER">Other</Select.Option>
              </Select>
            </Form.Item>
          </>
        );
      default:
        return null;
    }
  };

  const getActionColumn = (type: MasterDataType): ColumnsType<any>[0] => ({
    title: 'Actions',
    key: 'actions',
    width: 150,
    render: (_: any, record: any) => (
      <Space>
        <Button 
          type="link" 
          icon={<EditOutlined />} 
          onClick={() => handleEdit(record, type)}
        >
          Edit
        </Button>
        {record.active && (
          <Popconfirm
            title="Deactivate this record?"
            description="This will mark the record as inactive."
            onConfirm={() => handleDeactivate(record, type)}
            okText="Deactivate"
            cancelText="Cancel"
          >
            <Button type="link" danger icon={<StopOutlined />}>
              Deactivate
            </Button>
          </Popconfirm>
        )}
      </Space>
    ),
  });

  const renderTable = (type: MasterDataType, data: any[], columns: ColumnsType<any>) => {
    const actionCol = getActionColumn(type);
    return (
      <Table 
        columns={[...columns, actionCol]} 
        dataSource={data} 
        rowKey="code" 
        loading={loading} 
        scroll={{ x: 'max-content' }}
      />
    );
  };

  const tabItems = [
    {
      key: 'currencies',
      label: 'Currencies',
      children: (
        <Card extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleCreate('currency')}>
            Create
          </Button>
        }>
          {renderTable('currency', currencies, currencyColumns)}
        </Card>
      ),
    },
    {
      key: 'countries',
      label: 'Countries',
      children: (
        <Card extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleCreate('country')}>
            Create
          </Button>
        }>
          {renderTable('country', countries, countryColumns)}
        </Card>
      ),
    },
    {
      key: 'branches',
      label: 'Branches',
      children: (
        <Card extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleCreate('branch')}>
            Create
          </Button>
        }>
          {renderTable('branch', branches, branchColumns)}
        </Card>
      ),
    },
    {
      key: 'channels',
      label: 'Channels',
      children: (
        <Card extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleCreate('channel')}>
            Create
          </Button>
        }>
          {renderTable('channel', channels, channelColumns)}
        </Card>
      ),
    },
    {
      key: 'document-types',
      label: 'Document Types',
      children: (
        <Card extra={
          <Button type="primary" icon={<PlusOutlined />} onClick={() => handleCreate('documentType')}>
            Create
          </Button>
        }>
          {renderTable('documentType', documentTypes, documentTypeColumns)}
        </Card>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <h1>Master Data</h1>
      <Tabs activeKey={activeTab} onChange={setActiveTab} items={tabItems} />
      
      <Modal
        title={getModalTitle()}
        open={modalOpen}
        onOk={handleSave}
        onCancel={() => setModalOpen(false)}
        confirmLoading={saving}
        width={500}
      >
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          {renderForm()}
        </Form>
      </Modal>
    </div>
  );
}
