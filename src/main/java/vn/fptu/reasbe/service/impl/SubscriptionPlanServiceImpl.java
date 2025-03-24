package vn.fptu.reasbe.service.impl;

import static vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse.getPageable;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
import vn.fptu.reasbe.model.dto.subscriptionplan.SearchSubscriptionPlanRequest;
import vn.fptu.reasbe.model.dto.subscriptionplan.SubscriptionPlanDto;
import vn.fptu.reasbe.model.entity.SubscriptionPlan;
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
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findSubscriptionPlanById(subscriptionPlanDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("error.subscriptionPlan.notFound"));
        mapper.updateEntity(subscriptionPlan, subscriptionPlanDto);
        return mapper.toDto(subscriptionPlanRepository.save(mapper.toEntity(subscriptionPlanDto)));
    }

    @Override
    public SubscriptionPlanDto getSubscriptionPlanById(Integer id) {
        return null;
    }

    @Override
    public Boolean deactivateSubscriptionPlan(Integer id) {
        return null;
    }

    private void validateCreateSubscriptionPlanRequest(SubscriptionPlanDto request){
        if (subscriptionPlanRepository.existsByNameContainsIgnoreCase(request.getName())) {
            throw new ReasApiException(HttpStatus.BAD_REQUEST, "error.subscriptionPlan.nameExist");
        }
    }
}
