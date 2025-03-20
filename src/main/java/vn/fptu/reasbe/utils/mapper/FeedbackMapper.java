package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.feedback.FeedbackRequest;
import vn.fptu.reasbe.model.dto.feedback.FeedbackResponse;
import vn.fptu.reasbe.model.entity.Feedback;

@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                UserMapper.class,
                ExchangeMapper.class
        }
)
@Component
public interface FeedbackMapper {
    FeedbackResponse toFeedbackResponse(Feedback feedback);

    Feedback toFeedback(FeedbackRequest feedbackResponse);

    void updateFeedback(@MappingTarget Feedback feedback, FeedbackRequest feedbackResponse);
}
