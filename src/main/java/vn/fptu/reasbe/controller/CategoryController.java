package vn.fptu.reasbe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.fptu.reasbe.model.dto.category.CategoryResponse;
import vn.fptu.reasbe.model.enums.category.TypeItem;
import vn.fptu.reasbe.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategory(@RequestParam TypeItem typeItem) {
        return ResponseEntity.ok(categoryService.getCategoryListByTypeItem(typeItem));
    }
}
