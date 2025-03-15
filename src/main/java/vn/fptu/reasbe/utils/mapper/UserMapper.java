package vn.fptu.reasbe.utils.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import org.springframework.stereotype.Component;
import vn.fptu.reasbe.model.dto.user.CreateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UpdateStaffRequest;
import vn.fptu.reasbe.model.dto.user.UserResponse;
import vn.fptu.reasbe.model.entity.ExchangeRequest;
import vn.fptu.reasbe.model.entity.Feedback;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author ntig
 */
@Mapper(
        collectionMappingStrategy = CollectionMappingStrategy.SETTER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                LocationMapper.class
        }
)
@Component
public interface UserMapper {
    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "numOfExchangedItems", expression = "java(mapNumOfExchangedItems(user))")
    @Mapping(target = "numOfFeedbacks", expression = "java(mapNumOfFeedbacks(user))")
    @Mapping(target = "numOfRatings", expression = "java(mapNumOfRatings(user))")
    UserResponse toUserResponse(User user);

    @Mapping(target = "password", ignore = true)
    User toUser(CreateStaffRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateUser(@MappingTarget User user, UpdateStaffRequest request);

    default Integer mapNumOfExchangedItems(User user) {
        if (user.getItems() == null) return 0;

        return (int) user.getItems().stream()
                .flatMap(item -> Stream.concat(
                        item.getSellerExchangeRequests().stream(),
                        item.getBuyerExchangeRequests().stream()
                ))
                .filter(er -> er.getStatusExchangeRequest() == StatusExchangeRequest.APPROVED)
                .filter(er -> er.getExchangeHistory() != null)
                .filter(er -> er.getExchangeHistory().getStatusExchangeHistory() == StatusExchangeHistory.SUCCESSFUL)
                .count();
    }

    // Only count feedback from seller transactions
    default Integer mapNumOfFeedbacks(User user) {
        if (user.getItems() == null) return 0;

        return user.getItems().stream()
                .flatMap(item -> item.getSellerExchangeRequests().stream()) // Only seller-side exchange requests
                .map(ExchangeRequest::getExchangeHistory)
                .filter(Objects::nonNull)
                .mapToInt(eh -> eh.getFeedbacks() != null ? eh.getFeedbacks().size() : 0)
                .sum();
    }

    // Only get ratings from seller transactions
    default Double mapNumOfRatings(User user) {
        if (user.getItems() == null) return 0.0;

        return user.getItems().stream()
                .flatMap(item -> item.getSellerExchangeRequests().stream()) // Only seller-side exchange requests
                .map(ExchangeRequest::getExchangeHistory)
                .filter(Objects::nonNull)
                .flatMap(eh -> eh.getFeedbacks() != null ? eh.getFeedbacks().stream() : Stream.empty())
                .mapToDouble(Feedback::getRating)
                .average()
                .orElse(0.0);
    }
}
