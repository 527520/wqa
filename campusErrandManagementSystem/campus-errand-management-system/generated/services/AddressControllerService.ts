import type { AddressAddRequest } from "../models/AddressAddRequest";
import type { AddressUpdateRequest } from "../models/AddressUpdateRequest";
import type { BaseResponse_boolean_ } from "../models/BaseResponse_boolean_";
import type { BaseResponse_List_Address_ } from "../models/BaseResponse_List_Address_";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";

export class AddressControllerService {
  /**
   * addAddress
   * @param addressAddRequest addressAddRequest
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
  public static addAddressUsingPost(
    addressAddRequest: AddressAddRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/address/add",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: addressAddRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * deleteAddress
   * @param deleteRequest deleteRequest
   * @returns BaseResponse_boolean_ OK
   * @returns any Created
   * @throws ApiError
   */
  public static deleteAddressUsingPost(
    deleteRequest: DeleteRequest
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/address/delete",
      body: deleteRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }

  /**
   * getAllAddress
   * @param creationTime
   * @param id
   * @param lastAccessedTime
   * @param maxInactiveInterval
   * @param _new
   * @param valueNames
   * @returns BaseResponse_List_Address_ OK
   * @throws ApiError
   */
  public static getAllAddressUsingGet(
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_List_Address_> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/api/address/get",
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
   * updateAddress
   * @param addressUpdateRequest addressUpdateRequest
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
  public static updateAddressUsingPost(
    addressUpdateRequest: AddressUpdateRequest,
    creationTime?: number,
    id?: string,
    lastAccessedTime?: number,
    maxInactiveInterval?: number,
    _new?: boolean,
    valueNames?: Array<string>
  ): CancelablePromise<BaseResponse_boolean_ | any> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/api/address/update",
      query: {
        creationTime: creationTime,
        id: id,
        lastAccessedTime: lastAccessedTime,
        maxInactiveInterval: maxInactiveInterval,
        new: _new,
        valueNames: valueNames,
      },
      body: addressUpdateRequest,
      errors: {
        401: `Unauthorized`,
        403: `Forbidden`,
        404: `Not Found`,
      },
    });
  }
}
