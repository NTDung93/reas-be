package vn.fptu.reasbe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.fptu.reasbe.model.dto.brand.BrandResponse;
import vn.fptu.reasbe.service.BrandService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/brand")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrand() {
        return ResponseEntity.ok(brandService.getBrandList());
    }
}
