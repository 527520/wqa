/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DeliverymanVOVO } from './DeliverymanVOVO';
import type { TaskVO } from './TaskVO';
export type OrderVO = {
    completionTime?: string;
    deliveryman?: DeliverymanVOVO;
    estimatedCompletionTime?: string;
    fetchAddress?: string;
    id?: number;
    orderNote?: string;
    orderStatus?: string;
    orderTime?: string;
    shippingAddress?: string;
    task?: TaskVO;
};

