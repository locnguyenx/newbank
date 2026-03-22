// @ts-nocheck - Type mismatches with OpenAPI-generated types
import { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Form, Input, Button, Card, Row, Col, Select, message } from 'antd';
import { ArrowLeftOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { fetchProductById, createProduct, updateProduct } from '@/store/slices/productSlice';
import { ProductFamily, ProductCustomerType, type CreateProductRequest, type UpdateProductRequest } from '@/types/product.types';

interface ProductFormData {
  code: string;
  name: string;
  family: ProductFamily;
  customerTypes: ProductCustomerType[];
  description?: string;
}

// Helper function to extract user-friendly error messages
const extractErrorMessage = (err: any): string => {
  if (typeof err === 'string') return err;
  
  if (err?.response?.data) {
    const { message, errors, code } = err.response.data;
    if (message) return message;
    if (errors) {
      const errorList = errors.map((e: any) => `${e.field}: ${e.message}`).join('; ');
      return errorList;
    }
    if (code) return `Error ${code}: ${err.response.statusText || 'Please check your data'}`;
  }
  
  if (err?.message) {
    if (err.message.includes('Network Error') || err.message.includes('timeout')) {
      return 'Network error: Please check your connection and try again.';
    }
    return err.message;
  }
  
  return 'An unexpected error occurred. Please try again or contact support.';
};

export function ProductFormPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [form] = Form.useForm<ProductFormData>();
  const { selectedProduct, loading } = useAppSelector((state) => state.products);
  const isEditing = Boolean(id);

  useEffect(() => {
    if (isEditing && id) {
      dispatch(fetchProductById(parseInt(id, 10)));
    }
  }, [dispatch, id, isEditing]);

   useEffect(() => {
     if (isEditing && selectedProduct) {
       form.setFieldsValue({
         code: selectedProduct.product?.code,
         name: selectedProduct.product?.name,
         family: selectedProduct.product?.family,
         customerTypes: selectedProduct.segments?.map(s => s.customerType) || [],
         description: selectedProduct.product?.description,
       });
     }
   }, [selectedProduct, form, isEditing]);

  const handleSubmit = async (values: ProductFormData) => {
    try {
      if (isEditing && id) {
        const data: UpdateProductRequest = {
          name: values.name,
          description: values.description,
          customerTypes: values.customerTypes,
        };
        await dispatch(updateProduct({ id: parseInt(id, 10), data })).unwrap();
        message.success('Product updated successfully');
      } else {
        const data: CreateProductRequest = {
          code: values.code,
          name: values.name,
          family: values.family,
          customerTypes: values.customerTypes,
          description: values.description,
        };
        await dispatch(createProduct(data)).unwrap();
        message.success('Product created successfully');
      }
      navigate('/products');
    } catch (err: any) {
      console.error('Product save error:', err);
      const errorMessage = extractErrorMessage(err);
      message.error(errorMessage);
    }
  };

  return (
    <div style={{ padding: 24 }}>
      <Button icon={<ArrowLeftOutlined />} onClick={() => navigate('/products')} style={{ marginBottom: 16 }}>
        Back
      </Button>

      <Card title={isEditing ? 'Edit Product' : 'New Product'}>
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Product Code"
                name="code"
                rules={[{ required: !isEditing, message: 'Product code is required' }]}
              >
                <Input disabled={isEditing} placeholder="e.g., ACC-CURRENT-001" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                label="Product Name"
                name="name"
                rules={[{ required: true, message: 'Product name is required' }]}
              >
                <Input placeholder="e.g., Standard Current Account" />
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={24}>
            <Col span={12}>
              <Form.Item
                label="Product Family"
                name="family"
                rules={[{ required: !isEditing, message: 'Product family is required' }]}
              >
                 <Select disabled={isEditing}>
                   <Select.Option value={ProductFamily.Account}>Account</Select.Option>
                   <Select.Option value={ProductFamily.Payment}>Payment</Select.Option>
                   <Select.Option value={ProductFamily.TradeFinance}>Trade Finance</Select.Option>
                 </Select>
              </Form.Item>
            </Col>
           </Row>

           <Row gutter={24}>
             <Col span={12}>
               <Form.Item
                 label="Customer Types"
                 name="customerTypes"
                 rules={[{ required: true, message: 'Please select at least one customer type' }]}
               >
                 <Select mode="multiple" placeholder="Select customer types">
                   <Select.Option value={ProductCustomerType.Corporate}>Corporate</Select.Option>
                   <Select.Option value={ProductCustomerType.Sme}>SME</Select.Option>
                   <Select.Option value={ProductCustomerType.Individual}>Individual</Select.Option>
                 </Select>
               </Form.Item>
             </Col>
           </Row>

           <Row gutter={24}>
             <Col span={24}>
              <Form.Item label="Description" name="description">
                <Input.TextArea rows={4} placeholder="Enter product description" />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item>
            <Button type="primary" htmlType="submit" loading={loading}>
              {isEditing ? 'Update' : 'Create'}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}
