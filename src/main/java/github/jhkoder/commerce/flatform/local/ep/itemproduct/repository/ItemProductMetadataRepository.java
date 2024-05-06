package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;

import github.jhkoder.commerce.common.entity.Gender;
import github.jhkoder.commerce.flatform.local.ep.category.domain.Category;
import github.jhkoder.commerce.flatform.local.ep.category.domain.CategoryLevel;
import github.jhkoder.commerce.flatform.local.ep.category.repository.CategoryRepository;
import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import github.jhkoder.commerce.flatform.local.ep.item.repository.ItemRepository;
import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import github.jhkoder.commerce.image.repository.ImageJpaRepository;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemProductMetadataRepository {
    private final PlatformTransactionManager transactionManager;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemProductRepository productRepository;
    private final ImageJpaRepository imageRepository;
    private final ItemRepository itemRepository;


    public final Map<Long, Map<Integer, ProductMetaDtoKey>> keys = new HashMap<>();
    public final ProductMetaDto metaDto = new ProductMetaDto();

    private static final int itemMax = 35_000;
    private static int count = 0;

    @PostConstruct
    void createMetadata(){
        Random random = new Random();
        List<User> sellers = findBySeller();
        List<Category> categories = categoryRepository.findAll();
        List<Category> categoryTops = categories.stream()
                .filter(category -> category.getLevel().equals(CategoryLevel.TOP))
                .toList();
        List<Category> topRandomCategory = randomTopCategory(categoryTops);
        Map<Long, String> images = imageIdToPath();
        long startTimeAll = System.currentTimeMillis();
        int index = 3;

        for (User users : sellers) {
            addMetadata(users, index, topRandomCategory, images, random);
        }

        log.info(" 상품 Meta data 입력 완료 ====  " + (System.currentTimeMillis() - startTimeAll) + " ms");
    }

    public void addMetadata(User seller, int index, List<Category> topRandomCategory, Map<Long, String> images, Random random) {
        log.info("[" + ++count + "] user - ");
        int price = getPrice(random);
        given(seller, index, price, topRandomCategory, images, random);

        long startTime = System.currentTimeMillis();
        List<Images> itemImages = saveImage(metaDto.getItemImages(keys.get(seller.getId())));
        List<Images> links = saveImage(metaDto.getLinks(keys.get(seller.getId())));
        log(startTime, count, "img");

        startTime = System.currentTimeMillis();
        List<List<Images>> contextImg = givenContextImg(links);
        List<Item> items = saveItem(givenItem(itemImages, seller, index, price));
        log(startTime, count, "item");

        startTime = System.currentTimeMillis();
        saveItemProduct(givenProduct(seller, items, contextImg, price, random));
        log(startTime, count, "product");

        clear(seller);
    }

    @Transactional
    public List<User> findBySeller() {
        log.info("meta data 생성 index 조회");
        Optional<User> user = userRepository.findByUserId("TEST_1_1");
        if (user.isEmpty()) {
            log.info("계정 생성 [SELLER] .. 1_500 ");
            return createUser();
        }
        log.info("계정 불러 오기 [SELLER ... 1_500");
        return userRepository.findByUserIdStartingWith("TEST_1_");
    }

    @Transactional
    public List<Images> saveImage(List<Images> images) {
        return imageRepository.saveAll(images);
    }

    @Transactional
    public List<Item> saveItem(List<Item> items) {
        return itemRepository.saveAll(items);
    }

    @Transactional
    public void saveItemProduct(List<ItemProduct> itemProductList) {
        productRepository.saveAll(itemProductList);
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
        for (int i = 1; i <= 1500; i++) {
            String name = "TEST_1_" + i;
            User seller = userRepository.save(new User(name, name, passwordEncoder.encode("qwer1234"),
                    "test" + name + "@test.com", "[TEST] 010-1234-1234 " + name, Gender.MAN, Role.SELLER));
            sellers.add(seller);
        }
        return sellers;
    }

    private void given(User seller, int index, int price, List<Category> topRandomCategory, Map<Long, String> images, Random random) {
        for (int i = 0; i < itemMax; i++) {
            Category category = topRandomCategory.get(random.nextInt(topRandomCategory.size()));
            String path = images.get(category.getId());
            Images productImg = new Images(seller, path);
            Item item = createItem(productImg, index, category, price); //not saave
            List<Images> links = new ArrayList<>();
            links.add(new Images(seller, images.get(random.nextLong(images.size()))));
            links.add(new Images(seller, images.get(random.nextLong(images.size()))));
            links.add(new Images(seller, images.get(random.nextLong(images.size()))));

            ItemProductMetadataRepository.ProductMetaDtoKey key = new ItemProductMetadataRepository.ProductMetaDtoKey(seller.getId(), i);
            saveKey(seller, i, key);
            ItemProductMetadataRepository.ProductMetaDtoData data = new ItemProductMetadataRepository.ProductMetaDtoData(productImg, item, category, links);
            saveMetaData(key, data);
        }
    }


    private List<ItemProduct> givenProduct(
            User seller, List<Item> items, List<List<Images>> contextImg, int price, Random random) {
        List<ItemProduct> itemProductList = new ArrayList<>();
        for (int i = 0; i < itemMax; i++) {
            ItemProductMetadataRepository.ProductMetaDtoData data = metaDto.getData().get(keys.get(seller.getId()).get(i));
            ItemProduct.ProductMetadata metadata = new ItemProduct.ProductMetadata(
                    seller.getId(),
                    data.getCategory().getId(),
                    items.get(i).getId(),
                    contextImg.get(i)
                            .stream()
                            .map(Images::getId)
                            .collect(Collectors.toList())
            );
            itemProductList.add(createProduct(metadata, price, random));
        }

        itemProductList.forEach(ItemProduct::metadataUp);
        return itemProductList;
    }

    private List<Item> givenItem(List<Images> itemImages, User seller, int index, int price) {
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < itemMax; i++) {
            ItemProductMetadataRepository.ProductMetaDtoData data = metaDto.getData().get(keys.get(seller.getId()).get(i));
            data.setItem(createItem(itemImages.get(i), index, data.getCategory(), price));
            items.add(data.getItem());
        }
        return items;
    }

    private List<List<Images>> givenContextImg(List<Images> links) {
        List<List<Images>> contextImg = new ArrayList<>();
        for (int i = 0; i < links.size(); i += 3) {
            List<Images> linkGroup = new ArrayList<>();
            for (int j = i; j < Math.min(i + 3, links.size()); j++) {
                linkGroup.add(links.get(j));
            }
            contextImg.add(linkGroup);
        }
        return contextImg;
    }

    private int getPrice(Random random) {
        int randomNumber = random.nextInt(1500000 - 1000) + 1000;
        return (randomNumber / 10) * 10;
    }

    private String arrow(int index) {
        return "=".repeat(Math.max(0, index)) + ">";
    }

    private Item createItem(Images img, int startIndex, Category category, int price) {
        return new Item(img, "TestItem_" + startIndex + "_" + category.getName(), price,
                "TestMaker", "TestOrigin", "TestBrand",
                true, "TestBarcode");
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

        return new ItemProduct(mate, price, orderMode, rentalInfo,
                clickCount, reviewCount, minimumPurchaseQuantity, optionDetail, gender, deliveryPrice,
                shippingSetting, fastDelivery, regularDelivery, dawnDelivery, isbn, stock);
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
        private Item item;
        private Category category;
        private List<Images> links;

        public ProductMetaDtoData(Images itemImg, Item item, Category category, List<Images> links) {
            this.itemImg = itemImg;
            this.item = item;
            this.category = category;
            this.links = links;
        }
    }

    public record ProductMetaDtoKey(Long key_user, int key_index) {
    }

    private Map<Long, String> imageIdToPath() {
        Map<Long, String> images = new HashMap<>();
        Long[] keys = {276L, 241L, 132L, 498L, 372L, 691L, 167L, 469L, 530L, 338L, 306L, 106L, 63L, 50L, 406L, 199L, 597L, 566L, 437L, 628L, 716L, 659L, 1L};
        String[] paths = {"test/animal.jpg", "test/baby.jpg", "test/beauty.png", "test/car_1.jpg", "test/digital.jpg", "test/eco.png", "test/furniture.jpg", "test/health.jpg", "test/hobbies.jpg", "test/home.png", "test/kitchen.jpg", "test/man_fashion.jpg", "test/woman_fashion.jpg", "test/maple.jpg", "test/notebook_1.jpg", "test/product_food.jpg", "test/refurbishment.jpg", "test/rent.jpg", "test/sport.jpg", "test/store.jpg", "test/sub.png", "test/travel.jpg", "test/trip.jpg"};

        for (int i = 0; i < keys.length; i++) {
            images.put(keys[i], paths[i]);
        }
        return images;
    }

    private List<Category> randomTopCategory(List<Category> categories) {
        List<Category> categoryList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(categories.size());
            categoryList.add(categories.get(index));
        }
        return categoryList;
    }
}
