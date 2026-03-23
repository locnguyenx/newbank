# DocumentTypeControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createDocumentType**](#createdocumenttype) | **POST** /api/master-data/document-types | |
|[**deactivateDocumentType**](#deactivatedocumenttype) | **PUT** /api/master-data/document-types/{code}/deactivate | |
|[**getActiveDocumentTypes**](#getactivedocumenttypes) | **GET** /api/master-data/document-types | |
|[**updateDocumentType**](#updatedocumenttype) | **PUT** /api/master-data/document-types/{code} | |

# **createDocumentType**
> DocumentTypeResponse createDocumentType(createDocumentTypeRequest)


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration,
    CreateDocumentTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

let createDocumentTypeRequest: CreateDocumentTypeRequest; //

const { status, data } = await apiInstance.createDocumentType(
    createDocumentTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createDocumentTypeRequest** | **CreateDocumentTypeRequest**|  | |


### Return type

**DocumentTypeResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deactivateDocumentType**
> DocumentTypeResponse deactivateDocumentType()


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.deactivateDocumentType(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**DocumentTypeResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getActiveDocumentTypes**
> Array<DocumentTypeResponse> getActiveDocumentTypes()


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

let category: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getActiveDocumentTypes(
    category
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **category** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<DocumentTypeResponse>**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateDocumentType**
> DocumentTypeResponse updateDocumentType(updateDocumentTypeRequest)


### Example

```typescript
import {
    DocumentTypeControllerApi,
    Configuration,
    UpdateDocumentTypeRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new DocumentTypeControllerApi(configuration);

let code: string; // (default to undefined)
let updateDocumentTypeRequest: UpdateDocumentTypeRequest; //

const { status, data } = await apiInstance.updateDocumentType(
    code,
    updateDocumentTypeRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateDocumentTypeRequest** | **UpdateDocumentTypeRequest**|  | |
| **code** | [**string**] |  | defaults to undefined|


### Return type

**DocumentTypeResponse**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

