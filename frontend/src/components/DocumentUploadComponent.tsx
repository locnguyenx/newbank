import { useState } from 'react';
import { Upload, Button, List, Select, message } from 'antd';
import { UploadOutlined, DeleteOutlined } from '@ant-design/icons';
import type { AuthorizationDocumentType } from '@/types/authorization.types';

const documentTypeOptions: Array<{ value: AuthorizationDocumentType; label: string }> = [
  { value: 'ID_DOCUMENT', label: 'ID Document' },
  { value: 'POWER_OF_ATTORNEY_LETTER', label: 'Power of Attorney Letter' },
  { value: 'BOARD_RESOLUTION', label: 'Board Resolution' },
  { value: 'SPECIMEN_SIGNATURE', label: 'Specimen Signature' },
  { value: 'OTHER', label: 'Other' },
];

interface PendingDocument {
  id: string;
  documentType: AuthorizationDocumentType;
  file: File;
}

interface DocumentUploadComponentProps {
  onDocumentsChange: (documents: Array<{ documentType: AuthorizationDocumentType; file: File }>) => void;
  disabled?: boolean;
  accept?: string;
}

export function DocumentUploadComponent({
  onDocumentsChange,
  disabled = false,
  accept = '.pdf,.jpg,.jpeg,.png',
}: DocumentUploadComponentProps) {
  const [documents, setDocuments] = useState<PendingDocument[]>([]);
  const [selectedType, setSelectedType] = useState<AuthorizationDocumentType | null>(null);

  const handleAddDocument = (file: File) => {
    if (!selectedType) {
      message.error('Please select a document type first');
      return false;
    }

    const newDoc: PendingDocument = {
      id: `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`,
      documentType: selectedType,
      file,
    };

    const updated = [...documents, newDoc];
    setDocuments(updated);
    onDocumentsChange(updated.map((d) => ({ documentType: d.documentType, file: d.file })));
    setSelectedType(null);
    message.success('Document added');
    return false;
  };

  const handleRemoveDocument = (id: string) => {
    const updated = documents.filter((doc) => doc.id !== id);
    setDocuments(updated);
    onDocumentsChange(updated.map((d) => ({ documentType: d.documentType, file: d.file })));
  };

  return (
    <div>
      <div style={{ display: 'flex', gap: 16, marginBottom: 16, alignItems: 'flex-end' }}>
        <div style={{ flex: 1 }}>
          <label style={{ display: 'block', marginBottom: 4, fontWeight: 500 }}>Document Type</label>
          <Select
            placeholder="Select document type"
            value={selectedType}
            onChange={(value) => setSelectedType(value)}
            style={{ width: '100%' }}
            disabled={disabled}
            options={documentTypeOptions.map((opt) => ({ value: opt.value, label: opt.label }))}
          />
        </div>
        <Upload
          beforeUpload={handleAddDocument}
          showUploadList={false}
          accept={accept}
          disabled={disabled || !selectedType}
        >
          <Button icon={<UploadOutlined />} disabled={disabled || !selectedType}>
            Add File
          </Button>
        </Upload>
      </div>

      <List
        dataSource={documents}
        size="small"
        bordered
        renderItem={(doc) => {
          const typeLabel = documentTypeOptions.find((t) => t.value === doc.documentType)?.label;
          return (
            <List.Item
              actions={[
                <Button
                  type="text"
                  danger
                  icon={<DeleteOutlined />}
                  onClick={() => handleRemoveDocument(doc.id)}
                  disabled={disabled}
                >
                  Remove
                </Button>,
              ]}
            >
              <List.Item.Meta title={typeLabel} description={doc.file.name} />
            </List.Item>
          );
        }}
        locale={{ emptyText: 'No documents added' }}
      />
    </div>
  );
}
