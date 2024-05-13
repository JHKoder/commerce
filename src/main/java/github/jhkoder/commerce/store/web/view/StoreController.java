package github.jhkoder.commerce.store.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/store")
public class StoreController {

    @GetMapping
    public String store(){
        return "store";
    }

    @GetMapping("/product")
    public String product(){
        return "product";
    }
}
