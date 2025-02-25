package vn.fptu.reasbe.service;

import vn.fptu.reasbe.model.dto.category.CategoryResponse;
import vn.fptu.reasbe.model.entity.Category;
import vn.fptu.reasbe.model.enums.category.TypeItem;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Integer id);

    List<CategoryResponse> getCategoryListByTypeItem(TypeItem typeItem);
}
