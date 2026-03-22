// @ts-nocheck - Type mismatches with OpenAPI-generated types
import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card, Form, Select, Upload, Button, Space, List, message, Spin } from 'antd';
import { ArrowLeftOutlined, UploadOutlined, DeleteOutlined } from '@ant-design/icons';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { submitDocuments } from '@/store/slices/kycSlice';
import type { KYCDocumentType } from '@/types/kyc.types';

// @ts-expect-error - KYCDocumentType enum values from backend
const documentTypes: Array<{ value: KYCDocumentType; label: string }> = [
  { value: 'PASSPORT', label: 'Passport' },
  { value: 'DRIVERS_LICENSE', label: "Driver's License" },
  { value: 'NATIONAL_ID', label: 'National ID' },
  { value: 'PROOF_OF_ADDRESS', label: 'Proof of Address' },
  { value: 'BANK_STATEMENT', label: 'Bank Statement' },
  { value: 'BUSINESS_LICENSE', label: 'Business License' },
  { value: 'INCORPORATION_CERTIFICATE', label: 'Incorporation Certificate' },
];

interface DocumentEntry {
  id: string;
  documentType: KYCDocumentType;
  file: File;
}

export function KYCDocumentUploadPage() {
  const { customerId } = useParams<{ customerId: string }>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const { loading, error } = useAppSelector((state) => state.kyc);
  const [form] = Form.useForm();
  const [documents, setDocuments] = useState<DocumentEntry[]>([]);

  const handleAddDocument = (values: { documentType: KYCDocumentType }) => {
    const fileList = form.getFieldValue('fileList');
    if (!fileList || fileList.length === 0) {
      message.error('Please select a file');
      return;
    }

    const file = fileList[0].originFileObj as File;
    const newDoc: DocumentEntry = {
      id: `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
      documentType: values.documentType,
      file,
    };

    setDocuments((prev) => [...prev, newDoc]);
    form.resetFields();
    message.success('Document added');
  };

  const handleRemoveDocument = (id: string) => {
    setDocuments((prev) => prev.filter((doc) => doc.id !== id));
  };

  const handleSubmit = async () => {
    if (documents.length === 0) {
      message.error('Please add at least one document');
      return;
    }

    if (!customerId) {
      message.error('Customer ID is required');
      return;
    }

    try {
      await dispatch(submitDocuments({
        customerId: parseInt(customerId, 10),
        documents: documents.map((doc) => ({
          documentType: doc.documentType,
          file: doc.file,
        })),
      })).unwrap();

      message.success('Documents submitted successfully');
      navigate(`/customers/${customerId}/kyc`);
    } catch (err) {
      message.error('Failed to submit documents');
    }
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', padding: 100 }}>
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div style={{ padding: 24 }}>
      <Space style={{ marginBottom: 16 }}>
        <Button icon={<ArrowLeftOutlined />} onClick={() => navigate(`/customers/${customerId}/kyc`)}>
          Back to KYC Status
        </Button>
      </Space>

      <Card title="Upload KYC Documents">
        {error && <div style={{ color: 'red', marginBottom: 16 }}>{error}</div>}

        <Form form={form} layout="vertical" onFinish={handleAddDocument} style={{ marginBottom: 24 }}>
          <Form.Item
            name="documentType"
            label="Document Type"
            rules={[{ required: true, message: 'Please select a document type' }]}
          >
            <Select placeholder="Select document type">
              {documentTypes.map((type) => (
                <Select.Option key={type.value} value={type.value}>
                  {type.label}
                </Select.Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item
            name="fileList"
            label="File"
            valuePropName="fileList"
            getValueFromEvent={(e) => (Array.isArray(e) ? e : e?.fileList)}
            rules={[{ required: true, message: 'Please select a file' }]}
          >
            <Upload
              beforeUpload={() => false}
              maxCount={1}
              accept=".pdf,.jpg,.jpeg,.png"
            >
              <Button icon={<UploadOutlined />}>Select File</Button>
            </Upload>
          </Form.Item>

          <Form.Item>
            <Button type="dashed" htmlType="submit">
              Add Document
            </Button>
          </Form.Item>
        </Form>

        <Card title={`Documents to Submit (${documents.length})`} size="small" style={{ marginBottom: 24 }}>
          <List
            dataSource={documents}
            renderItem={(doc) => (
              <List.Item
                actions={[
                  <Button
                    type="text"
                    danger
                    icon={<DeleteOutlined />}
                    onClick={() => handleRemoveDocument(doc.id)}
                  >
                    Remove
                  </Button>,
                ]}
              >
                <List.Item.Meta
                  title={documentTypes.find((t) => t.value === doc.documentType)?.label}
                  description={doc.file.name}
                />
              </List.Item>
            )}
            locale={{ emptyText: 'No documents added yet' }}
          />
        </Card>

        <Space>
          <Button type="primary" onClick={handleSubmit} disabled={documents.length === 0}>
            Submit Documents
          </Button>
          <Button onClick={() => navigate(`/customers/${customerId}/kyc`)}>
            Cancel
          </Button>
        </Space>
      </Card>
    </div>
  );
}
