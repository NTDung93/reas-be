package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.exchange.EvidenceExchangeRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.entity.ExchangeHistory;
import vn.fptu.reasbe.model.entity.ExchangeRequest;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.enums.user.RoleName;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.ExchangeHistoryRepository;
import vn.fptu.reasbe.repository.ExchangeRequestRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.ExchangeService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.service.VectorStoreService;
import vn.fptu.reasbe.utils.common.DateUtils;
import vn.fptu.reasbe.utils.mapper.ExchangeRequestMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    private final ItemService itemService;
    private final UserService userService;
    private final AuthService authService;
    private final VectorStoreService vectorStoreService;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ExchangeHistoryRepository exchangeHistoryRepository;
    private final ExchangeRequestMapper exchangeMapper;

    @Override
    public BaseSearchPaginationResponse<ExchangeResponse> getAllExchangeByStatusOfCurrentUser(int pageNo, int pageSize, String sortBy, String sortDir,
                                                                                              StatusExchangeRequest statusRequest, StatusExchangeHistory statusHistory) {
        User user = authService.getCurrentUser();
        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);
        if (statusRequest.equals(StatusExchangeRequest.APPROVED)) {
            if (statusHistory == null) {
                return BaseSearchPaginationResponse.of(exchangeRequestRepository.findByExchangeHistoryStatusInAndUser(
                                List.of(StatusExchangeHistory.NOT_YET_EXCHANGE, StatusExchangeHistory.PENDING_EVIDENCE), user, pageable)
                        .map(exchangeMapper::toExchangeResponse));
            } else {
                return BaseSearchPaginationResponse.of(exchangeRequestRepository.findByExchangeHistoryStatusAndUser(statusHistory, user, pageable)
                        .map(exchangeMapper::toExchangeResponse));
            }
        } else {
            return BaseSearchPaginationResponse.of(exchangeRequestRepository.findByExchangeRequestStatusAndUser(statusRequest, user, pageable)
                    .map(exchangeMapper::toExchangeResponse));
        }
    }

    @Override
    public BaseSearchPaginationResponse<ExchangeResponse> getAllExchangeHistoryOfUser(int pageNo, int pageSize, String sortBy, String sortDir, Integer userId) {
        User resident = userService.getUserById(userId);

        if (!resident.getRole().getName().equals(RoleName.ROLE_RESIDENT)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotResident");
        }

        Pageable pageable = getPageable(pageNo, pageSize, sortBy, sortDir);

        return BaseSearchPaginationResponse.of(exchangeRequestRepository.findByExchangeRequestStatusAndUser(StatusExchangeRequest.APPROVED, resident, pageable)
                .map(exchangeMapper::toExchangeResponse));
    }

    @Override
    public ExchangeResponse getExchangeById(Integer id) {
        User user = authService.getCurrentUser();
        ExchangeRequest request = getExchangeRequestById(id);

        if (user.getRole().getName().equals(RoleName.ROLE_RESIDENT)) {
            boolean isSeller = request.getSellerItem().getOwner().equals(user);
            boolean isBuyer = request.getBuyerItem() != null && request.getBuyerItem().getOwner().equals(user);
            boolean isPayer = request.getPaidBy().equals(user);

            if (!(isSeller || isBuyer || isPayer)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
            }
        }

        return exchangeMapper.toExchangeResponse(request);
    }

    @Override
    public ExchangeResponse createExchangeRequest(ExchangeRequestRequest exchangeRequestRequest) {
        validateExchangeRequest(exchangeRequestRequest);

        Item sellerItem = itemService.getItemById(exchangeRequestRequest.getSellerItemId());
        if (!sellerItem.getStatusItem().equals(StatusItem.AVAILABLE)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.sellerItemNotAvailable");
        }

        if (sellerItem.getMethodExchanges().stream()
                .noneMatch(method -> method.equals(exchangeRequestRequest.getMethodExchange()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.methodExchangeNotMatch");
        }

        ExchangeRequest request = exchangeMapper.toExchangeRequest(exchangeRequestRequest);

        if (exchangeRequestRequest.getBuyerItemId() != null) {
            Item buyerItem = itemService.getItemById(exchangeRequestRequest.getBuyerItemId());
            if (!buyerItem.getStatusItem().equals(StatusItem.AVAILABLE)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.buyerItemNotAvailable");
            }
            if (sellerItem.getOwner().equals(buyerItem.getOwner())) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.sameOwnerWithSellerAndBuyerItem");
            }

            request.setBuyerItem(buyerItem);
        } else {
            if (!sellerItem.isMoneyAccepted()) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.buyerItemNull");
            }
        }

        User paidByUser = userService.getUserById(exchangeRequestRequest.getPaidByUserId());

        request.setSellerItem(sellerItem);
        request.setPaidBy(paidByUser);
        request.setNumberOfOffer(AppConstants.NUM_OF_OFFER);
        request.setStatusExchangeRequest(StatusExchangeRequest.PENDING);

        if (sellerItem.getPrice().equals(BigDecimal.ZERO)) {
            request.setSellerConfirmation(Boolean.TRUE);
            request.setBuyerConfirmation(Boolean.TRUE);
        } else {
            request.setBuyerConfirmation(Boolean.FALSE);
            request.setSellerConfirmation(Boolean.FALSE);
        }
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeRequestResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse updateExchangeRequestPrice(Integer id, BigDecimal finalPrice) {
        User user = authService.getCurrentUser();

        ExchangeRequest request = getExchangeRequestById(id);

        if (request.getNumberOfOffer().equals(0)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.noOfferLeft");
        }

        checkIfExchangeIsPending(request);

        request.setFinalPrice(finalPrice);
        request.setNumberOfOffer(request.getNumberOfOffer() - 1);

        //Checking and changing status for confirmation from both user
        if (request.getBuyerItem().getOwner().equals(user)) {
            if (request.getBuyerConfirmation().equals(Boolean.TRUE)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.waitForOtherUserConfirmation");
            }
            request.setSellerConfirmation(Boolean.FALSE);
            request.setBuyerConfirmation(Boolean.TRUE);
        } else if (request.getSellerItem().getOwner().equals(user)) {
            if (request.getSellerConfirmation().equals(Boolean.TRUE)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.waitForOtherUserConfirmation");
            }
            request.setSellerConfirmation(Boolean.TRUE);
            request.setBuyerConfirmation(Boolean.FALSE);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        //TODO: add push notification for resident

        return exchangeMapper.toExchangeRequestResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse reviewExchangeRequest(Integer id, StatusExchangeRequest statusExchangeRequest) {
        ExchangeRequest request = getExchangeRequestById(id);

        if (!request.getSellerItem().getOwner().equals(authService.getCurrentUser())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        checkIfExchangeIsPending(request);

        if (statusExchangeRequest.equals(StatusExchangeRequest.PENDING)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.statusExchangeRequestPendingNotAllowed");
        }

        request.setStatusExchangeRequest(statusExchangeRequest);

        if (statusExchangeRequest.equals(StatusExchangeRequest.APPROVED)) {
            if (request.getSellerConfirmation().equals(Boolean.FALSE) || request.getBuyerConfirmation().equals(Boolean.FALSE)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.notYetConfirmFinalPrice");
            }
            request.getSellerItem().setStatusItem(StatusItem.UNAVAILABLE);

            List<Item> deletedItemsFromVectorStore = new ArrayList<>();
            deletedItemsFromVectorStore.add(request.getSellerItem());

            if (request.getBuyerItem() != null) {
                request.getBuyerItem().setStatusItem(StatusItem.UNAVAILABLE);
                deletedItemsFromVectorStore.add(request.getBuyerItem());
            }

            ExchangeHistory exchangeHistory = new ExchangeHistory();
            exchangeHistory.setSellerConfirmation(Boolean.FALSE);
            exchangeHistory.setBuyerConfirmation(Boolean.FALSE);
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.NOT_YET_EXCHANGE);

            cancelOtherExchangeRequests(request.getSellerItem(), request.getBuyerItem());

            vectorStoreService.deleteItem(deletedItemsFromVectorStore);

            request.setExchangeHistory(exchangeHistoryRepository.save(exchangeHistory));
        } else {
            request.setStatusExchangeRequest(statusExchangeRequest);
        }
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse cancelExchange(Integer id) {
        ExchangeRequest request = getExchangeRequestById(id);

        if ((request.getBuyerItem() != null && !request.getBuyerItem().getOwner().equals(authService.getCurrentUser())) ||
                (!request.getPaidBy().equals(authService.getCurrentUser()))) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        if (request.getStatusExchangeRequest().equals(StatusExchangeRequest.PENDING)) {
            return exchangeMapper.toExchangeResponse(cancelExchangeRequest(request));
        } else if (request.getStatusExchangeRequest().equals(StatusExchangeRequest.APPROVED)) {
            return exchangeMapper.toExchangeResponse(cancelApprovedExchange(request));
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotCancelExchange");
        }
    }

    @Override
    public ExchangeResponse confirmNegotiatedPrice(Integer id) {
        User user = authService.getCurrentUser();

        ExchangeRequest request = getExchangeRequestById(id);

        checkIfExchangeIsPending(request);

        if (request.getSellerItem().getOwner().equals(user)) { //checking if the current user is the seller
            request.setSellerConfirmation(Boolean.TRUE);
        } else if ((request.getBuyerItem() != null &&  //checking if the current user is the buyer or paid by -> upload on buyer (paid by) side
                request.getBuyerItem().getOwner().equals(user)) ||
                (request.getPaidBy().equals(user))) {
            request.setBuyerConfirmation(Boolean.TRUE);
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        return exchangeMapper.toExchangeResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse uploadEvidence(EvidenceExchangeRequest request) {
        User user = authService.getCurrentUser();

        ExchangeHistory exchangeHistory = exchangeHistoryRepository.findById(request.getExchangeHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExchangeHistory", "id", request.getExchangeHistoryId()));

        if (DateUtils.getCurrentDateTime().isBefore(exchangeHistory.getExchangeRequest().getExchangeDate())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.notPassExchangeDateYet");
        }

        if (user.equals(exchangeHistory.getExchangeRequest().getSellerItem().getOwner())) { //checking if the current user is the seller -> upload on seller side
            exchangeHistory.setSellerConfirmation(Boolean.TRUE);
            exchangeHistory.setSellerImageUrl(request.getImageUrl());
            exchangeHistory.setSellerAdditionalNotes(request.getAdditionalNotes());
        } else if ((exchangeHistory.getExchangeRequest().getBuyerItem() != null &&  //checking if the current user is the buyer or paid by -> upload on buyer (paid by) side
                user.equals(exchangeHistory.getExchangeRequest().getBuyerItem().getOwner())) ||
                (user.equals(exchangeHistory.getExchangeRequest().getPaidBy()))) {
            exchangeHistory.setBuyerConfirmation(Boolean.TRUE);
            exchangeHistory.setBuyerImageUrl(request.getImageUrl());
            exchangeHistory.setBuyerAdditionalNotes(request.getAdditionalNotes());
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        if (Boolean.TRUE.equals(exchangeHistory.getBuyerConfirmation()) &&
                Boolean.TRUE.equals(exchangeHistory.getSellerConfirmation())) {
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.SUCCESSFUL);
            exchangeHistory.getExchangeRequest().getSellerItem().setStatusItem(StatusItem.SOLD);
            if (exchangeHistory.getExchangeRequest().getBuyerItem() != null) {
                exchangeHistory.getExchangeRequest().getBuyerItem().setStatusItem(StatusItem.SOLD);
            }
        } else {
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.PENDING_EVIDENCE);
        }
        //TODO: add push notification for resident

        exchangeHistory = exchangeHistoryRepository.save(exchangeHistory);
        return exchangeMapper.toExchangeResponse(exchangeHistory.getExchangeRequest());
    }

    @Override
    public Integer getNumberOfSuccessfulExchanges(Integer month, Integer year) {
        LocalDateTime startDate = DateUtils.getStartOfSpecificMonth(month, year);
        LocalDateTime endDate = DateUtils.getEndOfSpecificMonth(month, year);
        return exchangeHistoryRepository.countByCreationDateBetweenAndStatusExchangeHistoryAndStatusEntity(startDate, endDate, StatusExchangeHistory.SUCCESSFUL, StatusEntity.ACTIVE);
    }

    @Override
    public Integer getNumberOfSuccessfulExchangesOfUser(Integer month, Integer year) {
        User user = authService.getCurrentUser();
        return exchangeHistoryRepository.getNumberOfSuccessfulExchangesOfUser(month, year, user.getId());
    }

    @Scheduled(cron = "0 0 0,18 * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkEvidenceForExchange() {
        LocalDateTime threeDaysAgo = DateUtils.getCurrentDateTime().minusDays(3);

        List<ExchangeRequest> notExchangedExchanges = exchangeRequestRepository.findAllExceedingDateExchanges(threeDaysAgo, StatusExchangeHistory.NOT_YET_EXCHANGE);
        List<ExchangeRequest> pendingEvidenceExchanges = exchangeRequestRepository.findAllExceedingDateExchanges(threeDaysAgo, StatusExchangeHistory.PENDING_EVIDENCE);
        List<ExchangeRequest> pendingExchanges = exchangeRequestRepository.findAllByStatusExchangeRequestAndExchangeDateAfter(StatusExchangeRequest.PENDING, DateUtils.getCurrentDateTime());

        notExchangedExchangesCronJob(notExchangedExchanges);

        pendingEvidenceExchangesCronJob(pendingEvidenceExchanges);

        pendingExchangeRequestsCronJob(pendingExchanges);

        //TODO: add push notification for resident
    }

    private ExchangeRequest cancelExchangeRequest(ExchangeRequest request) {
        request.setStatusExchangeRequest(StatusExchangeRequest.CANCELLED);
        //TODO: add push notification for resident

        return exchangeRequestRepository.save(request);
    }

    private ExchangeRequest cancelApprovedExchange(ExchangeRequest request) {
        if (request.getExchangeDate().isBefore(DateUtils.getCurrentDateTime())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeDateBeforeNow");
        }
        if (request.getExchangeHistory() == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeHistoryNull");
        }

        request.getExchangeHistory().setStatusExchangeHistory(StatusExchangeHistory.FAILED);
        request.getSellerItem().setStatusItem(StatusItem.AVAILABLE);
        request.getBuyerItem().setStatusItem(StatusItem.AVAILABLE);

        List<Item> items = new ArrayList<>();
        items.add(request.getSellerItem());
        if (request.getBuyerItem() != null) {
            items.add(request.getBuyerItem());
        }
        vectorStoreService.addNewItem(items);

        //TODO: add push notification for resident

        return exchangeRequestRepository.save(request);
    }

    private ExchangeRequest getExchangeRequestById(Integer id) {
        return exchangeRequestRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("ExchangeRequest", "Id", id));
    }

    private void validateExchangeRequest(ExchangeRequestRequest exchangeRequestRequest) {
        if (exchangeRequestRequest.getExchangeDate().isBefore(DateUtils.getCurrentDateTime()) ||
                exchangeRequestRequest.getExchangeDate().isEqual(DateUtils.getCurrentDateTime())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeDateBeforeNow");
        }
        if (exchangeRequestRequest.getSellerItemId().equals(exchangeRequestRequest.getBuyerItemId())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.duplicateSellerAndBuyerItem");
        }
    }

    private void pendingExchangeRequestsCronJob(List<ExchangeRequest> pendingExchanges) {
        if (!pendingExchanges.isEmpty()) {
            pendingExchanges.forEach(request -> request.setStatusExchangeRequest(StatusExchangeRequest.PENDING));
            exchangeRequestRepository.saveAll(pendingExchanges);
            log.info("Updated {} pending exchange request(s) to CANCELLED.", pendingExchanges.size());
        } else {
            log.info("No pending exchanges found.");
        }
    }

    private void pendingEvidenceExchangesCronJob(List<ExchangeRequest> pendingEvidenceExchanges) {
        if (!pendingEvidenceExchanges.isEmpty()) {
            pendingEvidenceExchanges.forEach(request -> {
                request.getExchangeHistory().setStatusExchangeHistory(StatusExchangeHistory.SUCCESSFUL);
                request.getSellerItem().setStatusItem(StatusItem.SOLD);
                if (request.getBuyerItem() != null) {
                    request.getBuyerItem().setStatusItem(StatusItem.SOLD);
                }
            });
            exchangeRequestRepository.saveAll(pendingEvidenceExchanges);
            log.info("Updated {} pending evidence exchange request(s) to SUCCESSFUL.", pendingEvidenceExchanges.size());
        } else {
            log.info("No pending evidence exchanges found.");
        }
    }

    private void notExchangedExchangesCronJob(List<ExchangeRequest> notExchangedExchanges) {
        if (!notExchangedExchanges.isEmpty()) {
            notExchangedExchanges.forEach(request -> {
                request.getExchangeHistory().setStatusExchangeHistory(StatusExchangeHistory.FAILED);

                Item sellerItem = request.getSellerItem();
                Item buyerItem = request.getBuyerItem();

                checkExpiredItemAfterFailedExchange(sellerItem);
                if (buyerItem != null) {
                    checkExpiredItemAfterFailedExchange(buyerItem);
                }
            });
            exchangeRequestRepository.saveAll(notExchangedExchanges);
            log.info("Updated {} not exchanged exchange request(s) to FAILED.", notExchangedExchanges.size());
        } else {
            log.info("No not exchanged exchanges found.");
        }
    }

    private void checkExpiredItemAfterFailedExchange(Item item) {
        if (item.getExpiredTime().isBefore(DateUtils.getCurrentDateTime())) {
            item.setStatusItem(StatusItem.EXPIRED);
            log.info("Item {} expired. Change status to EXPIRED.", item.getId());
        } else {
            item.setStatusItem(StatusItem.AVAILABLE);
            vectorStoreService.addNewItem(List.of(item));
        }
    }

    private void checkIfExchangeIsPending(ExchangeRequest request) {
        if (!request.getStatusExchangeRequest().equals(StatusExchangeRequest.PENDING)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeRequestNotPending");
        }
    }

    private void cancelOtherExchangeRequests(Item sellerItem, Item buyerItem) {
        List<ExchangeRequest> requests = exchangeRequestRepository
                .findAllByStatusAndSellerItemOrBuyerItem(StatusExchangeRequest.PENDING, sellerItem, buyerItem);

        for (ExchangeRequest relatedRequest : requests) {
            relatedRequest.setStatusExchangeRequest(StatusExchangeRequest.CANCELLED);
        }

        exchangeRequestRepository.saveAll(requests);
    }
}
