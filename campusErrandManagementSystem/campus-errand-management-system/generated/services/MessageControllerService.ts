import type { BaseResponse_List_Map_string_object_ } from "../models/BaseResponse_List_Map_string_object_";
import type { BaseResponse_List_Message_ } from "../models/BaseResponse_List_Message_";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class MessageControllerService {
  /**
   * getChatList
   * @returns BaseResponse_List_Map_string_object_ OK
   * @throws ApiError
   */
  public static getChatListUsingGet(): CancelablePromise<BaseResponse_List_Map_string_object_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/message/informationInquiries",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getMessageList
   * @param counterpartId counterpartId
   * @returns BaseResponse_List_Message_ OK
   * @throws ApiError
   */
  public static getMessageListUsingGet(
    counterpartId: number
  ): CancelablePromise<BaseResponse_List_Message_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/message/list/{counterpartId}",
      path: {
        counterpartId: counterpartId,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
