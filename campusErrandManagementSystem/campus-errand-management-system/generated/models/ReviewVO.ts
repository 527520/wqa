/* generated using openapi-typescript-codegen -- do no edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { DeliverymanVOVO } from './DeliverymanVOVO';
import type { UserVO } from './UserVO';
export type ReviewVO = {
    comment?: string;
    id?: number;
    image?: string;
    orderId?: number;
    rating?: number;
    reviewTime?: string;
    reviewed?: DeliverymanVOVO;
    reviewer?: UserVO;
};

