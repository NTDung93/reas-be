package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.feedback.FeedbackRequest;
import vn.fptu.reasbe.model.dto.feedback.FeedbackResponse;

public interface FeedbackService {
    BaseSearchPaginationResponse<FeedbackResponse> getAllFeedbackOfUser(int pageNo, int pageSize, String sortBy, String sortDir, Integer userId, Integer rating);

    FeedbackResponse viewFeedbackDetail(Integer feedbackId);

    FeedbackResponse createFeedback(FeedbackRequest feedbackRequest);

    FeedbackResponse updateFeedback(FeedbackRequest feedbackRequest);

    Boolean deleteFeedback(Integer feedbackId);
}
