import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_Recruit_ } from "../models/BaseResponse_List_Recruit_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { RecruitAddRequest } from "../models/RecruitAddRequest";
import type { RecruitSearchRequest } from "../models/RecruitSearchRequest";
import type { RecruitUpdateRequest } from "../models/RecruitUpdateRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class RecruitControllerService {
  /**
   * addRecruit
   * @param recruitAddRequest recruitAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addRecruitUsingPost(
    recruitAddRequest: RecruitAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/recruit/add",
      body: recruitAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAllRecruits
   * @returns BaseResponse_List_Recruit_ OK
   * @throws ApiError
   */
  public static getAllRecruitsUsingGet(): CancelablePromise<BaseResponse_List_Recruit_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/recruit/get/all",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getRecruitsBySearch
   * @param recruitSearchRequest recruitSearchRequest
   * @returns BaseResponse_List_Recruit_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getRecruitsBySearchUsingPost(
    recruitSearchRequest: RecruitSearchRequest
  ): CancelablePromise<BaseResponse_List_Recruit_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/recruit/search",
      body: recruitSearchRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateRecruit
   * @param recruitUpdateRequest recruitUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateRecruitUsingPost(
    recruitUpdateRequest: RecruitUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/recruit/update",
      body: recruitUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateStatus
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateStatusUsingPost2(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/recruit/update/status",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
