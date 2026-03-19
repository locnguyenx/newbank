import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { Upload, Button, Progress, Card, Typography, Alert, Space } from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import type { UploadFile } from 'antd';
import { useAppDispatch, useAppSelector } from '@/hooks/useRedux';
import { bulkUpload, clearUploadResult } from '@/store/slices/employmentSlice';
import { BulkUploadResults } from './BulkUploadResults';

const { Title, Text } = Typography;

export function BulkUploadPage() {
  const { customerId } = useParams<{ customerId: string }>();
  const dispatch = useAppDispatch();
  const { uploadProgress, uploadResult, uploadLoading, error } = useAppSelector(
    (state) => state.employment
  );
  const [fileList, setFileList] = useState<UploadFile[]>([]);

  const handleUpload = () => {
    const file = fileList[0]?.originFileObj;
    if (!file || !customerId) return;

    dispatch(
      bulkUpload({
        customerId: Number(customerId),
        file,
        onProgress: () => {},
      })
    );
  };

  const handleReset = () => {
    setFileList([]);
    dispatch(clearUploadResult());
  };

  return (
    <div style={{ padding: 24 }}>
      <Title level={2} style={{ margin: 0, marginBottom: 24 }}>
        Bulk Upload Employees
      </Title>

      <Card style={{ marginBottom: 24 }}>
        <Space direction="vertical" style={{ width: '100%' }} size="large">
          <Text>
            Upload a CSV file with employee data. The file should include columns: employeeName,
            employeeId, department, position, startDate, salary, currency.
          </Text>

          <Upload
            fileList={fileList}
            onChange={({ fileList: files }) => setFileList(files)}
            beforeUpload={() => false}
            accept=".csv"
            maxCount={1}
          >
            <Button icon={<UploadOutlined />} disabled={uploadLoading}>
              Select CSV File
            </Button>
          </Upload>

          {uploadLoading && (
            <div>
              <Text>Uploading...</Text>
              <Progress percent={uploadProgress} status="active" />
            </div>
          )}

          {error && <Alert type="error" message={error} showIcon closable />}

          <Space>
            <Button
              type="primary"
              onClick={handleUpload}
              disabled={fileList.length === 0 || uploadLoading}
              loading={uploadLoading}
            >
              Upload
            </Button>
            <Button onClick={handleReset} disabled={uploadLoading}>
              Reset
            </Button>
          </Space>
        </Space>
      </Card>

      {uploadResult && <BulkUploadResults result={uploadResult} />}
    </div>
  );
}
