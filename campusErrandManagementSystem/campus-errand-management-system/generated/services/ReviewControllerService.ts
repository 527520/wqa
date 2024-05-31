import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_ReviewVO_ } from "../models/BaseResponse_List_ReviewVO_";
import type { BaseResponse_string_ } from "../models/BaseResponse_string_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { DeliverymanVOVO } from "../models/DeliverymanVOVO";
import type { ReviewAddRequest } from "../models/ReviewAddRequest";
import type { ReviewSearchRequest } from "../models/ReviewSearchRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class ReviewControllerService {
  /**
   * addReview
   * @param reviewAddRequest reviewAddRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addReviewUsingPost(
    reviewAddRequest: ReviewAddRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/review/add",
      body: reviewAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deleteComments
   * @param deleteRequest deleteRequest
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deleteCommentsUsingPost(
    deleteRequest: DeleteRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/review/delete",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAllComments
   * @returns BaseResponse_List_ReviewVO_ OK
   * @throws ApiError
   */
  public static getAllCommentsUsingGet(): CancelablePromise<BaseResponse_List_ReviewVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/review/get/all",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getDeliveryComments
   * @param deliveryman deliveryman
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_List_ReviewVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getDeliveryCommentsUsingPost(
    deliveryman: DeliverymanVOVO,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_List_ReviewVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/review/get/deliveryman/all",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: deliveryman,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * viewMyAllComments
   * @returns BaseResponse_List_ReviewVO_ OK
   * @throws ApiError
   */
  public static viewMyAllCommentsUsingGet(): CancelablePromise<BaseResponse_List_ReviewVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/review/get/my/all",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getBySearchComments
   * @param reviewSearchRequest reviewSearchRequest
   * @returns BaseResponse_List_ReviewVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getBySearchCommentsUsingPost(
    reviewSearchRequest: ReviewSearchRequest
  ): CancelablePromise<BaseResponse_List_ReviewVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/review/get/search",
      body: reviewSearchRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * uploadImage
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
  public static uploadImageUsingPost(
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
      url: "/api/review/upload/image",
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
