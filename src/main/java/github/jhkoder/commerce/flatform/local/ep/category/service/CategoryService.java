package github.jhkoder.commerce.flatform.local.ep.category.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryAddRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryChangeRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.request.CategoryUpdateRequest;
import github.jhkoder.commerce.flatform.local.ep.category.service.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        List<Category> categoryList = categoryRepository.findAll();
        List<Category> top = categoryList.stream().filter(category -> category.getLevel() == CategoryLevel.TOP).toList();
        List<CategoryResponse> response = new ArrayList<>();
        for (Category category : top) {
            response.add(new CategoryResponse(category.getName(), category.getId(), mapToResponse(categoryList)));
        }
        return response;
    }


    private List<CategoryResponse.MiddleCategory> mapToResponse(List<Category> categoryList) {
        return categoryList.stream()
                .map(category -> new CategoryResponse.MiddleCategory(
                        category.getName(),
                        category.getId(),
                        mapToSmallCategory(category.getChild())
                ))
                .collect(Collectors.toList());
    }

    private List<CategoryResponse.SmallCategory> mapToSmallCategory(List<Category> categoryList) {
        return categoryList.stream()
                .map(category -> new CategoryResponse.SmallCategory(
                        category.getName(),
                        category.getId()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void add(CategoryAddRequest request) {
        if (request.isTop()) {
            Category top = categoryRepository.findById(request.topId())
                    .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_TOP_ID));
            top.updateCategory(new Category(request.name(), request.level()));
            return;
        }
        categoryRepository.save( new Category(request.name(), CategoryLevel.TOP));
    }

    @Transactional
    public void update(CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(request.id())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));
        category.updateName(request.name());
    }

    @Transactional
    public void delete(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));

        if (category.getLevel() == CategoryLevel.BOTTOM) {
            categoryRepository.delete(category);
        } else if (category.getLevel() == CategoryLevel.MID) {
            categoryRepository.deleteAllByIdInBatch(filterParentId(category));
        } else if (category.getLevel() == CategoryLevel.TOP) {
            for (Category mid : category.getChild()) {
                for (Category bottom : mid.getChild()) {
                    categoryRepository.deleteAllByIdInBatch(filterParentId(bottom));
                }

                categoryRepository.deleteAllByIdInBatch(filterParentId(mid));
            }
            categoryRepository.deleteAllByIdInBatch(filterParentId(category));
        }
    }

    @Transactional
    public void move(CategoryChangeRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ApiException(ErrorCode.CATEGORY_NOT_FOUND));

        Optional<Category> top = categoryRepository.findById(request.topId());

        if(top.isPresent()){
            moveChildCategory(request,top.get(),category);
        }else{
            moveTopCategory(request,category);
        }
    }

    private void moveChildCategory(CategoryChangeRequest request, Category top , Category category ){
        int index = categoryListGetIndex(request.categoryId(), top.getChild());
        if (request.up() >= 1) {
            if (index + request.up() > top.getChild().size()) {
                throw new ApiException(ErrorCode.CATEGORY_MOVE_UP_INDEX_OUT);
            }
            category.childChangeIndex(index, index + request.up());
        } else if (request.down() >= 1) {
            if (index - request.down() <= 0) {
                throw new ApiException(ErrorCode.CATEGORY_MOVE_DOWN_INDEX_OUT);
            }
            category.childChangeIndex(index, index - request.down());
        } else {
            throw new ApiException(ErrorCode.CATEGORY_CHANGE_NOT_MOVE);
        }
    }

    private void moveTopCategory(CategoryChangeRequest request, Category category){
        Optional<List<Category>> optionalCategories = categoryRepository.findByLevel(CategoryLevel.TOP);

        if(optionalCategories.isPresent()){
            List<Category> categoryList = optionalCategories.get();
            int index = categoryListFindIndex(categoryList,category);
            int toIndex = toIndexGet(index,request.up(),request.down());
            if(index != -1 && toIndex >= 0 && toIndex < categoryList.size()){
                Category toCategory = categoryList.get(toIndex);
                Long temp = toCategory.getId();
                toCategory.updateId(category.getId());
                category.updateId(temp);
            }
        }
    }

    private int categoryListFindIndex(List<Category> categoryList ,Category category ){
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getId().equals(category.getId())) {
                return i;
            }
        }
        return -1;
    }

    private int toIndexGet(int index,int up, int down) {
        if(up >= 1){
            return index+up;
        }
        return index-down;
    }

    private int categoryListGetIndex(Long id, List<Category> list) {
        return IntStream.range(0, list.size())
                .filter(i -> list.get(i).getId().equals(id))
                .findFirst()
                .orElse(-1);
    }

    private List<Long> filterParentId(Category category) {
        return category.getChild().stream()
                .map(Category::getId)
                .toList();
    }
}
