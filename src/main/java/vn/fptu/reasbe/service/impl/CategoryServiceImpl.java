package vn.fptu.reasbe.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.fptu.reasbe.model.entity.Category;
import vn.fptu.reasbe.model.exception.ResourceNotFoundException;
import vn.fptu.reasbe.repository.CategoryRepository;
import vn.fptu.reasbe.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}
