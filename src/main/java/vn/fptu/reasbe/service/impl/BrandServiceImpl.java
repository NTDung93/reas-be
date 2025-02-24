package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.entity.Brand;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.BrandRepository;
import vn.fptu.reasbe.service.BrandService;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    @Override
    public Brand getBrandById(Integer id) {
        return brandRepository.getBrandById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
    }
}
