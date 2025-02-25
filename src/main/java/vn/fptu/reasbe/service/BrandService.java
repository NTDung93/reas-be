package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.brand.BrandResponse;
import vn.fptu.reasbe.model.entity.Brand;

import java.util.List;

public interface BrandService {
    Brand getBrandById(Integer id);

    List<BrandResponse> getBrandList();
}
