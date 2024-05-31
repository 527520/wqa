import type { BaseResponse_BecomeDeliverymanVO_ } from "../models/BaseResponse_BecomeDeliverymanVO_";
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_BecomeDeliveryman_ } from "../models/BaseResponse_List_BecomeDeliveryman_";
import type { BaseResponse_string_ } from "../models/BaseResponse_string_";
import type { BecomeDeliverymanUpdateRequest } from "../models/BecomeDeliverymanUpdateRequest";
import type { UserBecomeDeliverymanRequest } from "../models/UserBecomeDeliverymanRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class BecomeDeliverymanControllerService {
  /**
   * becomeDeliveryman
   * @param userBecomeDeliverymanRequest userBecomeDeliverymanRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static becomeDeliverymanUsingPost(
    userBecomeDeliverymanRequest: UserBecomeDeliverymanRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/becomeDeliveryman/add",
      body: userBecomeDeliverymanRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAll
   * @returns BaseResponse_List_BecomeDeliveryman_ OK
   * @throws ApiError
   */
  public static getAllUsingGet(): CancelablePromise<BaseResponse_List_BecomeDeliveryman_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/becomeDeliveryman/get/all",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getProgress
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_BecomeDeliverymanVO_ OK
   * @throws ApiError
   */
  public static getProgressUsingGet(
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_BecomeDeliverymanVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/becomeDeliveryman/get/progress",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateStatus
   * @param becomeDeliverymanUpdateRequest becomeDeliverymanUpdateRequest
   * @returns BaseResponse_string_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateStatusUsingPost(
    becomeDeliverymanUpdateRequest: BecomeDeliverymanUpdateRequest
  ): CancelablePromise<BaseResponse_string_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/becomeDeliveryman/update/status",
      body: becomeDeliverymanUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * uploadIdCard
   * @param file
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_string_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static uploadIdCardUsingPost(
    file?: Blob,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_string_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/becomeDeliveryman/upload/idCard",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      formData: {
        file: file,
      },
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
