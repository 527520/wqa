import type { AddOrderNoteRequest } from "../models/AddOrderNoteRequest";
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_OrderVO_ } from "../models/BaseResponse_OrderVO_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { TaskUpdateStatusRequest } from "../models/TaskUpdateStatusRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class OrderControllerService {
  /**
   * addOrderNote
   * @param addOrderNoteRequest addOrderNoteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static addOrderNoteUsingPost(
    addOrderNoteRequest: AddOrderNoteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/order/add/orderNote",
      body: addOrderNoteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deleteMyOrder
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deleteMyOrderUsingPost(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/order/delete/my",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deliverymanGetOrderVO
   * @returns BaseResponse_OrderVO_ OK
   * @throws ApiError
   */
  public static deliverymanGetOrderVoUsingGet(): CancelablePromise<BaseResponse_OrderVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/order/deliveryman/get/vo",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getOrderVO
   * @param taskId taskId
   * @returns BaseResponse_OrderVO_ OK
   * @throws ApiError
   */
  public static getOrderVoUsingGet(
    taskId?: number
  ): CancelablePromise<BaseResponse_OrderVO_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/order/get/vo",
      query: {
        taskId: taskId,
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
   * @param taskUpdateStatusRequest taskUpdateStatusRequest
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
  public static updateStatusUsingPost1(
    taskUpdateStatusRequest: TaskUpdateStatusRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/order/update/status",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: taskUpdateStatusRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
