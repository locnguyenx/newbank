import { useEffect, useState } from 'react';
import { Table, Button, Tabs, Card, message, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { PlusOutlined } from '@ant-design/icons';
import { useAppDispatch } from '@/hooks/useRedux';

interface Currency {
  id: number;
  code: string;
  name: string;
  symbol: string;
  decimalPlaces: number;
  isActive: boolean;
}

interface Country {
  id: number;
  isoCode: string;
  name: string;
  region: string;
  isActive: boolean;
}

interface Branch {
  id: number;
  code: string;
  name: string;
  countryCode: string;
  isActive: boolean;
}

interface Channel {
  id: number;
  code: string;
  name: string;
  isActive: boolean;
}

interface DocumentType {
  id: number;
  code: string;
  name: string;
  category: string;
  isActive: boolean;
}

const currencyColumns: ColumnsType<Currency> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Symbol', dataIndex: 'symbol', key: 'symbol', width: 80 },
  { title: 'Decimal Places', dataIndex: 'decimalPlaces', key: 'decimalPlaces', width: 120 },
  {
    title: 'Active',
    dataIndex: 'isActive',
    key: 'isActive',
    width: 80,
    render: (isActive: boolean) => (
      <Tag color={isActive ? 'green' : 'red'}>{isActive ? 'Yes' : 'No'}</Tag>
    ),
  },
];

const countryColumns: ColumnsType<Country> = [
  { title: 'ISO Code', dataIndex: 'isoCode', key: 'isoCode', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Region', dataIndex: 'region', key: 'region', width: 150 },
  {
    title: 'Active',
    dataIndex: 'isActive',
    key: 'isActive',
    width: 80,
    render: (isActive: boolean) => (
      <Tag color={isActive ? 'green' : 'red'}>{isActive ? 'Yes' : 'No'}</Tag>
    ),
  },
];

const branchColumns: ColumnsType<Branch> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Country', dataIndex: 'countryCode', key: 'countryCode', width: 100 },
  {
    title: 'Active',
    dataIndex: 'isActive',
    key: 'isActive',
    width: 80,
    render: (isActive: boolean) => (
      <Tag color={isActive ? 'green' : 'red'}>{isActive ? 'Yes' : 'No'}</Tag>
    ),
  },
];

const channelColumns: ColumnsType<Channel> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  {
    title: 'Active',
    dataIndex: 'isActive',
    key: 'isActive',
    width: 80,
    render: (isActive: boolean) => (
      <Tag color={isActive ? 'green' : 'red'}>{isActive ? 'Yes' : 'No'}</Tag>
    ),
  },
];

const documentTypeColumns: ColumnsType<DocumentType> = [
  { title: 'Code', dataIndex: 'code', key: 'code', width: 100 },
  { title: 'Name', dataIndex: 'name', key: 'name' },
  { title: 'Category', dataIndex: 'category', key: 'category', width: 150 },
  {
    title: 'Active',
    dataIndex: 'isActive',
    key: 'isActive',
    width: 80,
    render: (isActive: boolean) => (
      <Tag color={isActive ? 'green' : 'red'}>{isActive ? 'Yes' : 'No'}</Tag>
    ),
  },
];

export function MasterDataListPage() {
  const dispatch = useAppDispatch();
  const [activeTab, setActiveTab] = useState('currencies');

  // TODO: Replace with actual Redux state when slice is created
  const currencies: Currency[] = [];
  const countries: Country[] = [];
  const branches: Branch[] = [];
  const channels: Channel[] = [];
  const documentTypes: DocumentType[] = [];
  const loading = false;

  useEffect(() => {
    // TODO: Dispatch fetch thunks when slice is created
    // dispatch(fetchCurrencies());
    // dispatch(fetchCountries());
    // dispatch(fetchBranches());
    // dispatch(fetchChannels());
    // dispatch(fetchDocumentTypes());
  }, [dispatch]);

  const handleCreate = (entityType: string) => {
    message.success(`Create ${entityType} - Coming soon!`);
  };

  const tabItems = [
    {
      key: 'currencies',
      label: 'Currencies',
      children: (
        <Card
          extra={
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => handleCreate('Currency')}
            >
              Create
            </Button>
          }
        >
          <Table
            columns={currencyColumns}
            dataSource={currencies}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
    {
      key: 'countries',
      label: 'Countries',
      children: (
        <Card
          extra={
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => handleCreate('Country')}
            >
              Create
            </Button>
          }
        >
          <Table
            columns={countryColumns}
            dataSource={countries}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
    {
      key: 'branches',
      label: 'Branches',
      children: (
        <Card
          extra={
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => handleCreate('Branch')}
            >
              Create
            </Button>
          }
        >
          <Table
            columns={branchColumns}
            dataSource={branches}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
    {
      key: 'channels',
      label: 'Channels',
      children: (
        <Card
          extra={
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => handleCreate('Channel')}
            >
              Create
            </Button>
          }
        >
          <Table
            columns={channelColumns}
            dataSource={channels}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
    {
      key: 'document-types',
      label: 'Document Types',
      children: (
        <Card
          extra={
            <Button
              type="primary"
              icon={<PlusOutlined />}
              onClick={() => handleCreate('Document Type')}
            >
              Create
            </Button>
          }
        >
          <Table
            columns={documentTypeColumns}
            dataSource={documentTypes}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
          />
        </Card>
      ),
    },
  ];

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h1>Master Data</h1>
      </div>

      <Tabs
        activeKey={activeTab}
        onChange={setActiveTab}
        items={tabItems}
      />
    </div>
  );
}
