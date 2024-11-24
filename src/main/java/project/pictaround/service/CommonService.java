package project.pictaround.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.pictaround.domain.Category;
import project.pictaround.dto.request.CategoryRequestDto;
import project.pictaround.dto.response.CategoryResponseDto;
import project.pictaround.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommonService {

    private final CategoryRepository categoryRepository;

    public CommonService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void saveCategory(CategoryRequestDto requestDto) {
        categoryRepository.save(CategoryRequestDto.toEntity(requestDto));
    }

    public List<CategoryResponseDto> findCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponseDto> categoriesDto = new ArrayList<>();

        for (Category category : categories) {
            categoriesDto.add(CategoryResponseDto.of(category));
        }

        return categoriesDto;
    }
}
