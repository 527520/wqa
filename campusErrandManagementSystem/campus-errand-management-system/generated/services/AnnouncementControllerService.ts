import type { AnnouncementAddRequest } from "../models/AnnouncementAddRequest";
import type { AnnouncementUpdateRequest } from "../models/AnnouncementUpdateRequest";
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_Announcement_ } from "../models/BaseResponse_List_Announcement_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class AnnouncementControllerService {
  /**
   * addAnnouncement
   * @param announcementAddRequest announcementAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addAnnouncementUsingPost(
    announcementAddRequest: AnnouncementAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/announcement/add",
      body: announcementAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deleteAnnouncement
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deleteAnnouncementUsingPost(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/announcement/delete",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getLatestFive
   * @returns BaseResponse_List_Announcement_ OK
   * @throws ApiError
   */
  public static getLatestFiveUsingGet(): CancelablePromise<BaseResponse_List_Announcement_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/announcement/get/latestFive",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateAnnouncement
   * @param announcementUpdateRequest announcementUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateAnnouncementUsingPost(
    announcementUpdateRequest: AnnouncementUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/announcement/update",
      body: announcementUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
