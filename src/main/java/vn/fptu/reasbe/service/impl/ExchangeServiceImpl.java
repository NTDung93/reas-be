package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.fptu.reasbe.model.constant.AppConstants;
import vn.fptu.reasbe.model.dto.exchange.EvidenceExchangeRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestRequest;
import vn.fptu.reasbe.model.dto.exchange.ExchangeRequestResponse;
import vn.fptu.reasbe.model.dto.exchange.ExchangeResponse;
import vn.fptu.reasbe.model.entity.ExchangeHistory;
import vn.fptu.reasbe.model.entity.ExchangeRequest;
import vn.fptu.reasbe.model.entity.Item;
import vn.fptu.reasbe.model.entity.User;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeHistory;
import vn.fptu.reasbe.model.enums.exchange.StatusExchangeRequest;
import vn.fptu.reasbe.model.enums.item.StatusItem;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.ExchangeHistoryRepository;
import vn.fptu.reasbe.repository.ExchangeRequestRepository;
import vn.fptu.reasbe.service.AuthService;
import vn.fptu.reasbe.service.ExchangeService;
import vn.fptu.reasbe.service.ItemService;
import vn.fptu.reasbe.service.UserService;
import vn.fptu.reasbe.utils.common.DateUtils;
import vn.fptu.reasbe.utils.mapper.ExchangeMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl implements ExchangeService {

    private final ItemService itemService;
    private final UserService userService;
    private final AuthService authService;
    private final ExchangeRequestRepository exchangeRequestRepository;
    private final ExchangeHistoryRepository exchangeHistoryRepository;
    private final ExchangeMapper exchangeMapper;

    @Override
    public List<ExchangeResponse> getAllExchangeByStatusOfCurrentUser(StatusExchangeRequest statusRequest, StatusExchangeHistory statusHistory) {
        User user = authService.getCurrentUser();
        if (statusRequest.equals(StatusExchangeRequest.APPROVED)) {
            if (statusHistory == null) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.statusExchangeHistoryNull");
            }
            return exchangeRequestRepository.findByExchangeHistoryStatusAndUser(statusHistory, user)
                    .stream()
                    .map(exchangeMapper::toExchangeResponse)
                    .toList();
        } else {
            return exchangeRequestRepository.findByExchangeRequestStatusAndUser(statusRequest, user)
                    .stream()
                    .map(exchangeMapper::toExchangeResponse)
                    .toList();
        }
    }

    @Override
    public ExchangeResponse getExchangeById(Integer id) {
        ExchangeRequest exchangeRequest = getExchangeRequestById(id);
        return exchangeMapper.toExchangeResponse(exchangeRequest);
    }

    @Override
    public ExchangeRequestResponse createExchangeRequest(ExchangeRequestRequest exchangeRequestRequest) {
        Item sellerItem;
        Item buyerItem;
        User paidByUser;

        validateExchangeRequest(exchangeRequestRequest);

        sellerItem = itemService.getItemById(exchangeRequestRequest.getSellerItemId());
        if (!sellerItem.getStatusItem().equals(StatusItem.AVAILABLE)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.sellerItemNotAvailable");
        }

        ExchangeRequest request = exchangeMapper.toExchangeRequest(exchangeRequestRequest);

        if (exchangeRequestRequest.getBuyerItemId() != null) {
            buyerItem = itemService.getItemById(exchangeRequestRequest.getBuyerItemId());
            if (!buyerItem.getStatusItem().equals(StatusItem.AVAILABLE)) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.buyerItemNotAvailable");
            }
            request.setBuyerItem(buyerItem);
            request.getBuyerItem().setStatusItem(StatusItem.UNAVAILABLE);
        } else {
            if (!sellerItem.isMoneyAccepted()) {
                throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.buyerItemNull");
            }
        }

        paidByUser = userService.getUserById(exchangeRequestRequest.getPaidByUserId());
        setExchangeRequestUserConfirmation(request, Boolean.FALSE);
        request.setSellerItem(sellerItem);
        request.getSellerItem().setStatusItem(StatusItem.UNAVAILABLE);
        request.setPaidBy(paidByUser);
        request.setNumberOfOffer(AppConstants.NUM_OF_OFFER);
        request.setStatusExchangeRequest(StatusExchangeRequest.PENDING);
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeRequestResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeRequestResponse updateExchangeRequestPrice(Integer id, BigDecimal finalPrice) {
        ExchangeRequest request = getExchangeRequestById(id);
        if (request.getNumberOfOffer().equals(0)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.noOfferLeft");
        }
        if (!request.getStatusExchangeRequest().equals(StatusExchangeRequest.PENDING)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeRequestNotPending");
        }
        request.setFinalPrice(finalPrice);
        request.setNumberOfOffer(request.getNumberOfOffer() - 1);
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeRequestResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse reviewExchangeRequest(Integer id, StatusExchangeRequest statusExchangeRequest) {
        ExchangeRequest request = getExchangeRequestById(id);

        if (!request.getSellerItem().getOwner().equals(authService.getCurrentUser())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }
        if (!request.getStatusExchangeRequest().equals(StatusExchangeRequest.PENDING)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeRequestNotPending");
        }
        if (statusExchangeRequest.equals(StatusExchangeRequest.PENDING)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.statusExchangeRequestPendingNotAllowed");
        }

        request.setStatusExchangeRequest(statusExchangeRequest);

        if (statusExchangeRequest.equals(StatusExchangeRequest.APPROVED)) {
            setExchangeRequestUserConfirmation(request, Boolean.TRUE);

            ExchangeHistory exchangeHistory = new ExchangeHistory();
            exchangeHistory.setSellerConfirmation(Boolean.FALSE);
            exchangeHistory.setBuyerConfirmation(Boolean.FALSE);
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.NOT_YET_EXCHANGE);
            request.setExchangeHistory(exchangeHistoryRepository.save(exchangeHistory));
        } else {
            request.setStatusExchangeRequest(statusExchangeRequest);
        }
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse cancelApprovedExchange(Integer id) {
        ExchangeRequest request = getExchangeRequestById(id);

        if (!request.getStatusExchangeRequest().equals(StatusExchangeRequest.APPROVED)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotCancelExchange");
        }
        if (!request.getBuyerItem().getOwner().equals(authService.getCurrentUser())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }
        if (request.getExchangeDate().isBefore(DateUtils.getCurrentDateTime())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeDateBeforeNow");
        }
        if (request.getExchangeHistory() == null) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.exchangeHistoryNull");
        }

        request.getExchangeHistory().setStatusExchangeHistory(StatusExchangeHistory.FAILED);
        request.getSellerItem().setStatusItem(StatusItem.AVAILABLE);
        request.getBuyerItem().setStatusItem(StatusItem.AVAILABLE);
        //TODO: add push notification for resident

        return exchangeMapper.toExchangeResponse(exchangeRequestRepository.save(request));
    }

    @Override
    public ExchangeResponse uploadEvidence(EvidenceExchangeRequest request) {
        User user = authService.getCurrentUser();

        ExchangeHistory exchangeHistory = exchangeHistoryRepository.findById(request.getExchangeHistoryId())
                .orElseThrow(() -> new ResourceNotFoundException("ExchangeHistory", "id", request.getExchangeHistoryId()));

        if (DateUtils.getCurrentDateTime().isBefore(exchangeHistory.getExchangeRequest().getExchangeDate())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.cannotUploadEvidence");
        }

        if (user.equals(exchangeHistory.getExchangeRequest().getSellerItem().getOwner())) {
            exchangeHistory.setSellerConfirmation(Boolean.TRUE);
            exchangeHistory.setSellerItemImageUrl(request.getItemImageUrl());
            exchangeHistory.setSellerTransactionImageUrl(request.getTransactionImageUrl());
            exchangeHistory.setSellerAdditionalNotes(request.getAdditionalNotes());
        } else if ((exchangeHistory.getExchangeRequest().getBuyerItem() != null &&
                user.equals(exchangeHistory.getExchangeRequest().getBuyerItem().getOwner())) ||
                (user.equals(exchangeHistory.getExchangeRequest().getPaidBy()))) {
            exchangeHistory.setBuyerConfirmation(Boolean.TRUE);
            exchangeHistory.setBuyerItemImageUrl(request.getItemImageUrl());
            exchangeHistory.setBuyerTransactionImageUrl(request.getTransactionImageUrl());
            exchangeHistory.setBuyerAdditionalNotes(request.getAdditionalNotes());
        } else {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.userNotAllowed");
        }

        if (Boolean.TRUE.equals(exchangeHistory.getBuyerConfirmation()) &&
                Boolean.TRUE.equals(exchangeHistory.getSellerConfirmation())) {
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.SUCCESSFUL);
        } else {
            exchangeHistory.setStatusExchangeHistory(StatusExchangeHistory.PENDING_EVIDENCE);
        }
        //TODO: add push notification for resident

        exchangeHistory = exchangeHistoryRepository.save(exchangeHistory);
        return exchangeMapper.toExchangeResponse(exchangeHistory.getExchangeRequest());
    }

    @Scheduled(cron = "0 0 0,18 * * *", zone = "Asia/Ho_Chi_Minh")
//    @Scheduled(cron = "0 * * * * *", zone = "Asia/Ho_Chi_Minh")
    public void checkEvidenceForExchange() {
        //TODO: in testing
        LocalDateTime threeDaysAgo = DateUtils.getCurrentDateTime().minusDays(3);

        List<ExchangeRequest> notExchangedExchanges = exchangeRequestRepository.findAllExceedingDateExchanges(threeDaysAgo, StatusExchangeHistory.NOT_YET_EXCHANGE);
        List<ExchangeRequest> pendingEvidenceExchanges = exchangeRequestRepository.findAllExceedingDateExchanges(threeDaysAgo, StatusExchangeHistory.PENDING_EVIDENCE);

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
            log.info("Updated {} not exchanged exchange(s) requests to FAILED.", notExchangedExchanges.size());
        } else {
            log.info("No not exchanged exchanges found.");
        }

        if (!pendingEvidenceExchanges.isEmpty()) {
            pendingEvidenceExchanges.forEach(request -> request.getExchangeHistory()
                    .setStatusExchangeHistory(StatusExchangeHistory.SUCCESSFUL));
            exchangeRequestRepository.saveAll(pendingEvidenceExchanges);
            log.info("Updated {} pending evidence exchange(s) requests to SUCCESSFUL.", pendingEvidenceExchanges.size());
        } else {
            log.info("No pending evidence exchanges found.");
        }
        //TODO: add push notification for resident
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

    private void checkExpiredItemAfterFailedExchange(Item item) {
        if (item.getExpiredTime().isBefore(DateUtils.getCurrentDateTime())) {
            item.setStatusItem(StatusItem.EXPIRED);
            log.info("Item {} expired. Change status to EXPIRED.", item.getId());
        } else {
            item.setStatusItem(StatusItem.AVAILABLE);
        }
    }

    private void setExchangeRequestUserConfirmation(ExchangeRequest request, Boolean bool) {
        request.setSellerConfirmation(bool);
        request.setBuyerConfirmation(bool);
    }
}
