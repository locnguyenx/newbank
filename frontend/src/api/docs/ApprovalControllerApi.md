# ApprovalControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**approve1**](#approve1) | **POST** /api/limits/approvals/{id}/approve | |
|[**getPendingApprovals**](#getpendingapprovals) | **GET** /api/limits/approvals/pending | |
|[**reject1**](#reject1) | **POST** /api/limits/approvals/{id}/reject | |

# **approve1**
> approve1()


### Example

```typescript
import {
    ApprovalControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ApprovalControllerApi(configuration);

let id: number; // (default to undefined)
let username: string; // (default to undefined)

const { status, data } = await apiInstance.approve1(
    id,
    username
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|
| **username** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getPendingApprovals**
> Array<ApprovalRequestResponse> getPendingApprovals()


### Example

```typescript
import {
    ApprovalControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ApprovalControllerApi(configuration);

let page: number; // (optional) (default to 0)
let size: number; // (optional) (default to 20)

const { status, data } = await apiInstance.getPendingApprovals(
    page,
    size
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **page** | [**number**] |  | (optional) defaults to 0|
| **size** | [**number**] |  | (optional) defaults to 20|


### Return type

**Array<ApprovalRequestResponse>**

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

# **reject1**
> reject1(approvalActionRequest)


### Example

```typescript
import {
    ApprovalControllerApi,
    Configuration,
    ApprovalActionRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ApprovalControllerApi(configuration);

let id: number; // (default to undefined)
let username: string; // (default to undefined)
let approvalActionRequest: ApprovalActionRequest; //

const { status, data } = await apiInstance.reject1(
    id,
    username,
    approvalActionRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **approvalActionRequest** | **ApprovalActionRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|
| **username** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

