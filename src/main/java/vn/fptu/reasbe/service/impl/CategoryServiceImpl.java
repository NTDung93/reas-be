package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.dto.category.CategoryResponse;
import vn.fptu.reasbe.model.entity.Category;
import vn.fptu.reasbe.model.enums.category.TypeItem;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.CategoryRepository;
import vn.fptu.reasbe.service.CategoryService;
import vn.fptu.reasbe.utils.mapper.CategoryMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    public List<CategoryResponse> getCategoryListByTypeItem(TypeItem typeItem) {
        List<Category> categories = categoryRepository.findAllByTypeItem(typeItem);
        return categories.stream().map(categoryMapper::toResponse).toList();
    }
}
