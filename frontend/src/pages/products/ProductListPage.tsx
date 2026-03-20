import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Input, Select, Button, Space, Card } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchProducts } from '@/store/slices/productSlice';
import { ProductFamily, type Product, type ProductSearchParams } from '@/types/product.types';

const { Search } = Input;

export function ProductListPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { products, loading, pagination } = useAppSelector((state) => state.products);
  const [filters, setFilters] = useState<ProductSearchParams>({});
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  useEffect(() => {
    dispatch(fetchProducts({ ...filters, page, size: pageSize }));
  }, [dispatch, filters, page, pageSize]);

  const filteredProducts = products.filter((product) => {
    if (filters.search) {
      const searchLower = filters.search.toLowerCase();
      const matchesSearch =
        product.code.toLowerCase().includes(searchLower) ||
        product.name.toLowerCase().includes(searchLower) ||
        (product.description && product.description.toLowerCase().includes(searchLower));
      if (!matchesSearch) return false;
    }
    if (filters.family && product.family !== filters.family) return false;
    return true;
  });

  const getFamilyLabel = (family: ProductFamily): string => {
    const labels: Record<ProductFamily, string> = {
      [ProductFamily.ACCOUNT]: 'Account',
      [ProductFamily.PAYMENT]: 'Payment',
      [ProductFamily.TRADE_FINANCE]: 'Trade Finance',
    };
    return labels[family] || family;
  };

  const columns: ColumnsType<Product> = [
    {
      title: 'Code',
      dataIndex: 'code',
      key: 'code',
      width: 120,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Family',
      dataIndex: 'family',
      key: 'family',
      width: 150,
      render: (family: ProductFamily) => getFamilyLabel(family),
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true,
    },
    {
      title: 'Action',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space>
          <Button type="link" onClick={() => navigate(`/products/${record.id}`)}>
            View
          </Button>
          <Button type="link" onClick={() => navigate(`/products/${record.id}/edit`)}>
            Edit
          </Button>
        </Space>
      ),
    },
  ];

  const handleSearch = (value: string) => {
    setFilters((prev) => ({ ...prev, search: value }));
    setPage(1);
  };

  const handleFilterChange = (key: keyof ProductSearchParams, value: ProductSearchParams[keyof ProductSearchParams]) => {
    setFilters((prev) => ({ ...prev, [key]: value }));
    setPage(1);
  };

  return (
    <div style={{ padding: 24 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <h1>Products</h1>
        <Button type="primary" onClick={() => navigate('/products/new')}>
          Add Product
        </Button>
      </div>

      <Card>
        <Space direction="vertical" style={{ width: '100%' }} size="middle">
          <Space wrap>
            <Search
              placeholder="Search by code, name, or description"
              onSearch={handleSearch}
              enterButton
              style={{ width: 300 }}
              allowClear
            />
            <Select
              placeholder="Family"
              style={{ width: 150 }}
              allowClear
              onChange={(value) => handleFilterChange('family', value)}
            >
              <Select.Option value={ProductFamily.ACCOUNT}>Account</Select.Option>
              <Select.Option value={ProductFamily.PAYMENT}>Payment</Select.Option>
              <Select.Option value={ProductFamily.TRADE_FINANCE}>Trade Finance</Select.Option>
            </Select>
          </Space>

          <Table
            columns={columns}
            dataSource={filteredProducts}
            rowKey="id"
            loading={loading}
            scroll={{ x: 'max-content' }}
            pagination={{
              current: page,
              pageSize,
              total: pagination.totalElements,
              showSizeChanger: true,
              showTotal: (total) => `Total ${total} products`,
              onChange: (p, ps) => {
                setPage(p);
                setPageSize(ps);
              },
            }}
          />
        </Space>
      </Card>
    </div>
  );
}
