package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.item.repository.ItemJdbcRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.repository.ImageJdbcRepository;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemProductMetadataRepository {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final ItemProductJdbcRepository productJdbcRepository;
    private final ItemJdbcRepository itemJdbcRepository;
    private final ImageJdbcRepository imageJdbcRepository;


    public final Map<Long, Map<Integer, ProductMetaDtoKey>> keys = new HashMap<>();
    public final ProductMetaDto metaDto = new ProductMetaDto();
    private static final int itemMax = 20_001;
    private static int count = 0;

//    @PostConstruct // 메타 데이터 생성시 주석 제거
    void createMetadata() {
        Random random = new Random();
        List<User> sellers = findBySeller();
        List<Category> categories = categoryRepository.findAll();
        List<Category> categoryBottom = categories.stream()
                .filter(category -> category.getLevel().equals(CategoryLevel.BOTTOM))
                .toList();

        long startTimeAll = System.currentTimeMillis();
        for (User users : sellers) {
            List<Category> randomCategory = randomTopCategory(categoryBottom);
            addMetadata(users, randomCategory, random);
        }

        log.info(" 상품 Meta data 입력 완료 ====  time " + (System.currentTimeMillis() - startTimeAll) + " ms");
    }

    public void addMetadata(User seller, List<Category> randomCategory, Random random) {
        log.info("[" + ++count + "] user add item- ");
        given(seller, randomCategory, random);

        List<Long> itemImages = saveImage(metaDto.getItemImages(keys.get(seller.getId())));
        List<Long> links = saveImage(metaDto.getLinks(keys.get(seller.getId())));
        List<List<Long>> contextImg = givenContextImg(links);
        List<Long> item_Ids = saveItem(givenItem());

        saveItemProduct(givenProduct(seller, item_Ids, itemImages, contextImg, random));

        clear(seller);
    }

    @Transactional
    public List<User> findBySeller() {
        log.info("meta data 생성 index 조회");
        Optional<User> user = userRepository.findByUserId("TEST_1_1");
        if (user.isEmpty()) {
            log.info("계정 생성 [SELLER] .. 834 ");
            return createUser();
        }
        log.info("계정 불러 오기 [SELLER ... 834");
        return userRepository.findByUserIdStartingWith("TEST_1_");
    }

    @Transactional
    public List<Long> saveImage(List<Images> images) {
        return imageJdbcRepository.fetchInsert(images);
    }

    @Transactional
    public List<Long> saveItem(List<Item> items) {
        return itemJdbcRepository.fetchInsert(items);
    }

    @Transactional
    public void saveItemProduct(List<ItemProduct> itemProductList) {
        productJdbcRepository.fetchInsert(itemProductList);
    }

    private void clear(User seller) {
        metaDto.clear(keys.get(seller.getId()));
        keys.remove(seller.getId());
    }

    private void log(long startTime, int userIndex, String name) {
        log.info("[" + userIndex + "] " + name + " save " + (System.currentTimeMillis() - startTime) + " ms");
    }


    private void saveKey(User user, int index, ProductMetaDtoKey key) {
        keys.computeIfAbsent(user.getId(), k -> new HashMap<>())
                .put(index, key);
    }

    private void saveMetaData(ProductMetaDtoKey key, ProductMetaDtoData data) {
        metaDto.add(key, data);
    }

    private List<User> createUser() {
        List<User> sellers = new ArrayList<>();
        for (int i = 1; i < 834; i++) {
            String name = "TEST_1_" + i;
            User seller = userRepository.save(new User(name, name, passwordEncoder.encode("qwer1234"),
                    "test" + name + "@test.com", "[TEST] 010-1234-1234 " + name, Gender.MAN, Role.SELLER));
            sellers.add(seller);
        }
        return sellers;
    }

    private void given(User seller, List<Category> randomCategory, Random random) {
        for (int i = 0; i < itemMax; i++) {
            Category category = randomCategory.get(random.nextInt(randomCategory.size()));
            String path = randomImagePath(random);
            Images productImg = new Images(seller, path);
            int price = getPrice(random);
            String categoryPath = category.getPath();

            List<Images> links = new ArrayList<>();
            links.add(new Images(seller, randomImagePath(random)));
            links.add(new Images(seller, randomImagePath(random)));
            links.add(new Images(seller, randomImagePath(random)));

            ProductMetaDtoKey key = new ProductMetaDtoKey(seller.getId(), i);
            saveKey(seller, i, key);
            ProductMetaDtoData data = new ProductMetaDtoData(productImg, category, links, price, categoryPath);
            saveMetaData(key, data);
        }
    }

    private List<ItemProduct> givenProduct(
            User seller, List<Long> items, List<Long> mainImg, List<List<Long>> contextImg, Random random) {
        List<ItemProduct> itemProductList = new ArrayList<>();
        for (int i = 0; i < itemMax - 1; i++) {
            ProductMetaDtoData data = metaDto.getData().get(keys.get(seller.getId()).get(i));
            ItemProduct.ProductMetadata metadata = new ItemProduct.ProductMetadata(
                    seller.getId(),
                    data.getCategory().getId(),
                    data.getCategoryPath(),
                    data.getCategory().getName(),
                    items.get(i),
                    mainImg.get(i),
                    contextImg.get(i)
            );
            ItemProduct itemProduct = createProduct(metadata, data.getPrice(), random);
            itemProduct.metadataUp();
            itemProductList.add(itemProduct);
        }
        return itemProductList;
    }

    private List<Item> givenItem() {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < itemMax - 1; i++) {
            items.add(createItem());
        }
        return items;
    }

    private List<List<Long>> givenContextImg(List<Long> links) {
        List<List<Long>> contextImg = new ArrayList<>();
        for (int i = 0; i < links.size(); i += 3) {
            List<Long> linkGroup = new ArrayList<>();
            for (int j = i; j < Math.min(i + 3, links.size()); j++) {
                linkGroup.add(links.get(i));
            }
            contextImg.add(linkGroup);
        }
        return contextImg;
    }
    /*
    819ms, 309ms, 447ms
    1595ms
    1.6초
     */

    private int getPrice(Random random) {
        int randomNumber = random.nextInt(1500000 - 1000) + 1000;
        return (randomNumber / 10) * 10;
    }

    private String arrow(int index) {
        return "=".repeat(Math.max(0, index)) + ">";
    }

    private Item createItem() {
        return new Item("TestMaker", "TestOrigin", "TestBrand", true, "TestBarcode");
    }

    private static String randomName(Random random, String categoryName) {
        return "TestItem_" + random.nextInt(1000) + "_" + random.nextInt(9999) + categoryName;
    }

    private static ItemProduct createProduct(ItemProduct.ProductMetadata mate, int price, Random random) {
        boolean orderMode = true;
        String rentalInfo = "TestRentalInfo";
        int clickCount = random.nextInt(1_500);
        int reviewCount = random.nextInt(1_500);
        int minimumPurchaseQuantity = 1;
        String optionDetail = "TestOptionDetail";
        Gender gender = Gender.MAN;
        int deliveryPrice = 2_500;
        String shippingSetting = "TestShippingSetting";
        boolean fastDelivery = random.nextBoolean();
        boolean regularDelivery = random.nextBoolean();
        boolean dawnDelivery = random.nextBoolean();
        String isbn = "TestISBN";
        int stock = random.nextInt(15000 - 10) + 10;

        return new ItemProduct(mate, randomName(random, mate.getCategoryName()), price, discount(price), orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
    }

    public static int discount(int price) {

        double discountAmount = price * (10 / 100.0); // 할인액 계산
        double discountedPrice = price - discountAmount; // 할인된 가격 계산
        return (int) Math.round(discountedPrice); // 반올림하여 정수로 변환하여 반환
    }

    @Data
    public static class ProductMetaDto {
        private final Map<ProductMetaDtoKey, ProductMetaDtoData> data = new HashMap<>();

        public void add(ProductMetaDtoKey key, ProductMetaDtoData data) {
            this.data.put(key, data);
        }

        public List<Images> getItemImages(Map<Integer, ProductMetaDtoKey> key) {
            List<Images> itemImages = new ArrayList<>();
            for (int i = 0; i < itemMax; i++) {
                ProductMetaDtoData data = this.data.get(key.get(i));
                itemImages.add(data.getItemImg());
            }
            return itemImages;
        }

        public List<Images> getLinks(Map<Integer, ProductMetaDtoKey> key) {
            List<Images> links = new ArrayList<>();

            for (int i = 0; i < itemMax; i++) {
                ProductMetaDtoData data = this.data.get(key.get(i));
                links.addAll(data.getLinks());
            }
            return links;
        }

        public void clear(Map<Integer, ProductMetaDtoKey> key) {
            key.values().forEach(data::remove);
            System.gc();
        }
    }

    @Data
    public static class ProductMetaDtoData {
        private Images itemImg;
        private Category category;
        private int price;
        private String categoryPath;

        private List<Images> links;

        public ProductMetaDtoData(Images itemImg, Category category, List<Images> links, int price, String categoryPath) {
            this.itemImg = itemImg;
            this.category = category;
            this.links = links;
            this.price = price;
            this.categoryPath = categoryPath;

        }

    }

    public record ProductMetaDtoKey(Long key_user, int key_index) {
    }

    private String randomImagePath(Random random) {
        return "test/image-" + random.nextInt(457) + ".jpg";
    }

    private List<Category> randomTopCategory(List<Category> categories) {
        List<Category> categoryList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int index = random.nextInt(categories.size());
            categoryList.add(categories.get(index));
        }
        return categoryList;
    }
}
