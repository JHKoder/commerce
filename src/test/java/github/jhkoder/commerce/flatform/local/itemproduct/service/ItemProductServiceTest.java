package github.jhkoder.commerce.flatform.local.itemproduct.service;

import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.ProductService;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.request.ProductCategoryPageRequest;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.service.response.ProductsCategoryPageResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
public class ItemProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @Transactional
    void targetCategorySelect() {
        long startTime = System.currentTimeMillis();
        Long categoryId = 241L;

        Page<ProductsCategoryPageResponse> responses = productService
                .findCategoryPage("132/152/", new ProductCategoryPageRequest(categoryId,
                        30, 1345268901));

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println(" time: " + elapsedTime + " milliseconds");
        System.out.println(responses.getContent().size());
        //   3천만 건  0.003 ~ 1초
    }
}

