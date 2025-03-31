package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import vn.fptu.reasbe.model.dto.feedback.FeedbackRequest;
import vn.fptu.reasbe.model.dto.feedback.FeedbackResponse;
import vn.fptu.reasbe.model.entity.Feedback;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                UserMapper.class,
                ExchangeHistoryMapper.class
        }
)
public interface FeedbackMapper {
    FeedbackResponse toFeedbackResponse(Feedback feedback);

    Feedback toFeedback(FeedbackRequest request);

    void updateFeedback(@MappingTarget Feedback feedback, FeedbackRequest request);
}
