# KycControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**approveKYC**](#approvekyc) | **POST** /api/kyc/{id}/approve | |
|[**getKYCCheckById**](#getkyccheckbyid) | **GET** /api/kyc/{id} | |
|[**initiateKYC**](#initiatekyc) | **POST** /api/kyc | |
|[**rejectKYC**](#rejectkyc) | **POST** /api/kyc/{id}/reject | |
|[**submitDocuments**](#submitdocuments) | **POST** /api/kyc/{id}/documents | |
|[**submitForReview**](#submitforreview) | **POST** /api/kyc/{id}/submit | |
|[**updateKYC**](#updatekyc) | **PUT** /api/kyc/{id} | |

# **approveKYC**
> KYCResponse approveKYC(approveKYCRequest)


### Example

```typescript
import {
    KycControllerApi,
    Configuration,
    ApproveKYCRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)
let approveKYCRequest: ApproveKYCRequest; //

const { status, data } = await apiInstance.approveKYC(
    id,
    approveKYCRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **approveKYCRequest** | **ApproveKYCRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

# **getKYCCheckById**
> KYCResponse getKYCCheckById()


### Example

```typescript
import {
    KycControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.getKYCCheckById(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

# **initiateKYC**
> KYCResponse initiateKYC(initiateKYCRequest)


### Example

```typescript
import {
    KycControllerApi,
    Configuration,
    InitiateKYCRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let initiateKYCRequest: InitiateKYCRequest; //

const { status, data } = await apiInstance.initiateKYC(
    initiateKYCRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **initiateKYCRequest** | **InitiateKYCRequest**|  | |


### Return type

**KYCResponse**

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

# **rejectKYC**
> KYCResponse rejectKYC(rejectKYCRequest)


### Example

```typescript
import {
    KycControllerApi,
    Configuration,
    RejectKYCRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)
let rejectKYCRequest: RejectKYCRequest; //

const { status, data } = await apiInstance.rejectKYC(
    id,
    rejectKYCRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **rejectKYCRequest** | **RejectKYCRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

# **submitDocuments**
> KYCResponse submitDocuments(submitDocumentsRequest)


### Example

```typescript
import {
    KycControllerApi,
    Configuration,
    SubmitDocumentsRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)
let submitDocumentsRequest: SubmitDocumentsRequest; //

const { status, data } = await apiInstance.submitDocuments(
    id,
    submitDocumentsRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **submitDocumentsRequest** | **SubmitDocumentsRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

# **submitForReview**
> KYCResponse submitForReview()


### Example

```typescript
import {
    KycControllerApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)

const { status, data } = await apiInstance.submitForReview(
    id
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

# **updateKYC**
> KYCResponse updateKYC(updateKYCRequest)


### Example

```typescript
import {
    KycControllerApi,
    Configuration,
    UpdateKYCRequest
} from './api';

const configuration = new Configuration();
const apiInstance = new KycControllerApi(configuration);

let id: number; // (default to undefined)
let updateKYCRequest: UpdateKYCRequest; //

const { status, data } = await apiInstance.updateKYC(
    id,
    updateKYCRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateKYCRequest** | **UpdateKYCRequest**|  | |
| **id** | [**number**] |  | defaults to undefined|


### Return type

**KYCResponse**

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

