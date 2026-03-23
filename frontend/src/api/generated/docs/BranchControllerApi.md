# BranchControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createBranch**](#createbranch) | **POST** /api/master-data/branches | |
|[**deactivateBranch**](#deactivatebranch) | **PUT** /api/master-data/branches/{code}/deactivate | |
|[**getActiveBranches**](#getactivebranches) | **GET** /api/master-data/branches | |
|[**getBranch**](#getbranch) | **GET** /api/master-data/branches/{code} | |
|[**updateBranch**](#updatebranch) | **PUT** /api/master-data/branches/{code} | |

# **createBranch**
> BranchResponse createBranch(createBranchRequest)


### Example

```typescript
import {
    BranchControllerApi,
    Configuration,
    CreateBranchRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BranchControllerApi(configuration);

let createBranchRequest: CreateBranchRequest; //

const { status, data } = await apiInstance.createBranch(
    createBranchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createBranchRequest** | **CreateBranchRequest**|  | |


### Return type

**BranchResponse**

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

# **deactivateBranch**
> BranchResponse deactivateBranch()


### Example

```typescript
import {
    BranchControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BranchControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.deactivateBranch(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**BranchResponse**

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

# **getActiveBranches**
> Array<BranchResponse> getActiveBranches()


### Example

```typescript
import {
    BranchControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BranchControllerApi(configuration);

let countryCode: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.getActiveBranches(
    countryCode
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **countryCode** | [**string**] |  | (optional) defaults to undefined|


### Return type

**Array<BranchResponse>**

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

# **getBranch**
> BranchResponse getBranch()


### Example

```typescript
import {
    BranchControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new BranchControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.getBranch(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**BranchResponse**

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

# **updateBranch**
> BranchResponse updateBranch(updateBranchRequest)


### Example

```typescript
import {
    BranchControllerApi,
    Configuration,
    UpdateBranchRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new BranchControllerApi(configuration);

let code: string; // (default to undefined)
let updateBranchRequest: UpdateBranchRequest; //

const { status, data } = await apiInstance.updateBranch(
    code,
    updateBranchRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateBranchRequest** | **UpdateBranchRequest**|  | |
| **code** | [**string**] |  | defaults to undefined|


### Return type

**BranchResponse**

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

