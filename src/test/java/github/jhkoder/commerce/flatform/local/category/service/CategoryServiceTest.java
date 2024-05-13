package github.jhkoder.commerce.flatform.local.category.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQueryFactory;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryDslRepository;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private  JPAQueryFactory factory;
    @Autowired
    private CategoryDslRepository dslRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

}
