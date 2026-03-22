# ChannelControllerApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createChannel**](#createchannel) | **POST** /api/master-data/channels | |
|[**deactivateChannel**](#deactivatechannel) | **PUT** /api/master-data/channels/{code}/deactivate | |
|[**getActiveChannels**](#getactivechannels) | **GET** /api/master-data/channels | |
|[**updateChannel**](#updatechannel) | **PUT** /api/master-data/channels/{code} | |

# **createChannel**
> ChannelResponse createChannel(createChannelRequest)


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration,
    CreateChannelRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let createChannelRequest: CreateChannelRequest; //

const { status, data } = await apiInstance.createChannel(
    createChannelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **createChannelRequest** | **CreateChannelRequest**|  | |


### Return type

**ChannelResponse**

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

# **deactivateChannel**
> ChannelResponse deactivateChannel()


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let code: string; // (default to undefined)

const { status, data } = await apiInstance.deactivateChannel(
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **code** | [**string**] |  | defaults to undefined|


### Return type

**ChannelResponse**

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

# **getActiveChannels**
> Array<ChannelResponse> getActiveChannels()


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

const { status, data } = await apiInstance.getActiveChannels();
```

### Parameters
This endpoint does not have any parameters.


### Return type

**Array<ChannelResponse>**

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

# **updateChannel**
> ChannelResponse updateChannel(updateChannelRequest)


### Example

```typescript
import {
    ChannelControllerApi,
    Configuration,
    UpdateChannelRequest
} from '@openapitools/openapi-typescript-axios';

const configuration = new Configuration();
const apiInstance = new ChannelControllerApi(configuration);

let code: string; // (default to undefined)
let updateChannelRequest: UpdateChannelRequest; //

const { status, data } = await apiInstance.updateChannel(
    code,
    updateChannelRequest
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **updateChannelRequest** | **UpdateChannelRequest**|  | |
| **code** | [**string**] |  | defaults to undefined|


### Return type

**ChannelResponse**

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

