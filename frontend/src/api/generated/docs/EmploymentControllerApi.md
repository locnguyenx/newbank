# EmploymentControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**bulkUploadEmployees**](#bulkuploademployees) | **POST** /api/employments/bulk | |
|[**createEmployment**](#createemployment) | **POST** /api/employments | |
|[**getEmploymentById**](#getemploymentbyid) | **GET** /api/employments/{id} | |

# **bulkUploadEmployees**
> BulkUploadResult bulkUploadEmployees(body)


### Example

```typescript
import {
    EmploymentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmploymentControllerApi(configuration);

let employerId: number; // (default to undefined)
let body: string; //

const { status, data } = await apiInstance.bulkUploadEmployees(
    employerId,
    body
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **body** | **string**|  | |
| **employerId** | [**number**] |  | defaults to undefined|


### Return type

**BulkUploadResult**

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

# **createEmployment**
> EmploymentResponse createEmployment(createEmploymentRequest)


### Example

```typescript
import {
    EmploymentControllerApi,
    Configuration,
    CreateEmploymentRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new EmploymentControllerApi(configuration);

let createEmploymentRequest: CreateEmploymentRequest; //

const { status, data } = await apiInstance.createEmployment(
    createEmploymentRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createEmploymentRequest** | **CreateEmploymentRequest**|  | |


### Return type

**EmploymentResponse**

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

# **getEmploymentById**
> EmploymentResponse getEmploymentById()


### Example

```typescript
import {
    EmploymentControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new EmploymentControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getEmploymentById(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**EmploymentResponse**

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

