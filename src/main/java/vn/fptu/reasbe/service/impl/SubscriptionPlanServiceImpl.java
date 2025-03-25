package vn.fptu.reasbe.service.impl;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
import vn.fptu.reasbe.model.enums.core.StatusEntity;
import vn.fptu.reasbe.model.exception.ReasApiException;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.SubscriptionPlanRepository;
import vn.fptu.reasbe.service.SubscriptionPlanService;
import vn.fptu.reasbe.utils.mapper.SubscriptionPlanMapper;

/**
 *
 * @author dungnguyen
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper mapper;

    @Override
    public List<SubscriptionPlanDto> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public BaseSearchPaginationResponse<SubscriptionPlanDto> searchSubscriptionPlanPagination(int pageNo, int pageSize, String sortBy, String sortDir, SearchSubscriptionPlanRequest request) {
        return BaseSearchPaginationResponse.of(subscriptionPlanRepository.searchSubscriptionPlanPagination(request, getPageable(pageNo, pageSize, sortBy, sortDir))
                .map(mapper::toDto));
    }

    @Override
    public SubscriptionPlanDto createSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto) {
        validateCreateSubscriptionPlanRequest(subscriptionPlanDto);
        return mapper.toDto(subscriptionPlanRepository.save(mapper.toEntity(subscriptionPlanDto)));
    }

    @Override
    public SubscriptionPlanDto updateSubscriptionPlan(SubscriptionPlanDto subscriptionPlanDto) {
        SubscriptionPlan subscriptionPlan = getSubscriptionPlanByPlanId(subscriptionPlanDto.getId());
        if (subscriptionPlan.getStatusEntity() == StatusEntity.INACTIVE) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.subscriptionPlan.inactive");
        }
        validateUpdateSubscriptionPlanRequest(subscriptionPlanDto);
        mapper.updateEntity(subscriptionPlan, subscriptionPlanDto);
        return mapper.toDto(subscriptionPlanRepository.save(subscriptionPlan));
    }

    @Override
    public SubscriptionPlan getSubscriptionPlanByPlanId(Integer id) {
        return subscriptionPlanRepository.findSubscriptionPlanById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.subscriptionPlan.notFound"));
    }

    @Override
    public Boolean deactivateSubscriptionPlan(Integer id) {
        SubscriptionPlan subscriptionPlan = getSubscriptionPlanByPlanId(id);
        if (subscriptionPlan.getStatusEntity() == StatusEntity.INACTIVE) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.subscriptionPlan.inactive");
        }
        subscriptionPlan.setStatusEntity(StatusEntity.INACTIVE);
        subscriptionPlanRepository.save(subscriptionPlan);
        return true;
    }

    private void validateCreateSubscriptionPlanRequest(SubscriptionPlanDto request){
        if (subscriptionPlanRepository.existsByNameContainsIgnoreCaseAndStatusEntityEquals(request.getName(), StatusEntity.ACTIVE)) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.subscriptionPlan.nameExist");
        }
    }

    private void validateUpdateSubscriptionPlanRequest(SubscriptionPlanDto request){
        if (subscriptionPlanRepository.existsByNameContainsIgnoreCaseAndStatusEntityEqualsAndIdIsNot(request.getName(), StatusEntity.ACTIVE, request.getId())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.subscriptionPlan.nameExist");
        }
    }
}
