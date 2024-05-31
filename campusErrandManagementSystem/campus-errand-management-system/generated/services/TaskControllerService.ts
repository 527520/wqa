import type { AssignTaskRequest } from "../models/AssignTaskRequest";
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_Task_ } from "../models/BaseResponse_List_Task_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { TaskAddRequest } from "../models/TaskAddRequest";
import type { TaskReceiveRequest } from "../models/TaskReceiveRequest";
import type { TaskUpdateRequest } from "../models/TaskUpdateRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class TaskControllerService {
  /**
   * addTask
   * @param taskAddRequest taskAddRequest
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
  public static addTaskUsingPost(
    taskAddRequest: TaskAddRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/task/add",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: taskAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * assignTask
   * @param assignTaskRequest assignTaskRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static assignTaskUsingPost(
    assignTaskRequest: AssignTaskRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/task/assign",
      body: assignTaskRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * delete
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
  public static deleteUsingPost(
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
      url: "/api/task/delete",
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
   * getAllOrderForDeliveryman
   * @returns BaseResponse_List_Task_ OK
   * @throws ApiError
   */
  public static getAllOrderForDeliverymanUsingGet(): CancelablePromise<BaseResponse_List_Task_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/task/deliveryman/getAllOrder",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getTaskByDeliveryman
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_List_Task_ OK
   * @throws ApiError
   */
  public static getTaskByDeliverymanUsingGet(
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_List_Task_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/task/get/byDeliveryman",
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
   * getTask
   * @param status status
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_List_Task_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getTaskUsingPost(
    status: string,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_List_Task_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/task/get/byStatus",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: status,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * receiveTask
   * @param taskReceiveRequest taskReceiveRequest
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
  public static receiveTaskUsingPost(
    taskReceiveRequest: TaskReceiveRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/task/receive",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: taskReceiveRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * update
   * @param taskUpdateRequest taskUpdateRequest
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
  public static updateUsingPost(
    taskUpdateRequest: TaskUpdateRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/task/update",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: taskUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
