import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_DeliverymanVO_ } from "../models/BaseResponse_DeliverymanVO_";
import type { BaseResponse_List_Deliveryman_ } from "../models/BaseResponse_List_Deliveryman_";
import type { BaseResponse_List_DeliverymanVOResponse_ } from "../models/BaseResponse_List_DeliverymanVOResponse_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { DeliverymanUpdateRequest } from "../models/DeliverymanUpdateRequest";
import type { DeliverymanVOVO } from "../models/DeliverymanVOVO";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class DeliverymanControllerService {
  /**
   * deleteById
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deleteByIdUsingPost(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/deliveryman/delete",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAll
   * @returns BaseResponse_List_Deliveryman_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getAllUsingPost(): CancelablePromise<
    BaseResponse_List_Deliveryman_ | any
  > {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/deliveryman/get/all",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAllInline
   * @returns BaseResponse_List_DeliverymanVOResponse_ OK
   * @throws ApiError
   */
  public static getAllInlineUsingGet(): CancelablePromise<BaseResponse_List_DeliverymanVOResponse_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/deliveryman/get/allInline",
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getDeliverymanImage
   * @param deliveryman deliveryman
   * @returns BaseResponse_DeliverymanVO_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static getDeliverymanImageUsingPost(
    deliveryman: DeliverymanVOVO
  ): CancelablePromise<BaseResponse_DeliverymanVO_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/deliveryman/image",
      body: deliveryman,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * updateById
   * @param deliverymanUpdateRequest deliverymanUpdateRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static updateByIdUsingPost(
    deliverymanUpdateRequest: DeliverymanUpdateRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/deliveryman/update",
      body: deliverymanUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
