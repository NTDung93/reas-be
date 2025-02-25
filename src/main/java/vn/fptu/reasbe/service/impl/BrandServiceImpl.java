package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.brand.BrandResponse;
import vn.fptu.reasbe.model.entity.Brand;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.BrandRepository;
import vn.fptu.reasbe.service.BrandService;
import vn.fptu.reasbe.utils.mapper.BrandMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public Brand getBrandById(Integer id) {
        return brandRepository.getBrandById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
    }

    @Override
    public List<BrandResponse> getBrandList() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream().map(brandMapper::toResponse).toList();
    }
}
