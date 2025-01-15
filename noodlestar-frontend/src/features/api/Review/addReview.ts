import {AxiosResponse} from 'axios';
import {reviewRequestModel} from "../../model/reviewRequestModel.ts";
import axiosInstance from "../../../Shared/Api/axiosInstance";

export const addReview = async (review: reviewRequestModel): Promise<AxiosResponse<void>> => {
    try {
        return await axiosInstance.post<void>('http://localhost:8080/api/v1/review', review);
    } catch (error) {
        throw new Error(`Failed to review dish: ${error}`);
    }
};
