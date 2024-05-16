package github.jhkoder.commerce.flatform.local.ep.itemproduct.repository;


import github.jhkoder.commerce.flatform.local.ep.itemproduct.domain.ItemProduct;
import github.jhkoder.commerce.image.domain.Images;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ItemProductJdbcRepository {
    private final JdbcTemplate jdbcTemplate;
    public static final String INSERT_SQL = "INSERT INTO item_product" +
            " (id, user_id, category_id, item_id, main_image_id, name, price,normal_price, order_mode, rental_info, click_count," +
            " review_count, minimum_purchase_quantity, option_detail, gender, delivery_price, shipping_setting," +
            " fast_delivery, regular_delivery, dawn_delivery, isbn, stock) " +
            "VALUES (item_product_id_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String selectIdMax = "SELECT MAX(id) FROM item_product";
    public static final String INSERT_LINKS_SQL =
            "INSERT INTO item_product_links (item_product_id, links_id) " +
                    "VALUES (?, ?)";

    public void fetchInsert(List<ItemProduct> itemProductList) {
        Long lastId;
        try {
            lastId= jdbcTemplate.queryForObject(selectIdMax, Long.class);
        }catch (Exception e){
            lastId =0L;
        }

        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ItemProduct product = itemProductList.get(i);
                ps.setLong(1, product.getUser().getId());
                ps.setLong(2, product.getCategory().getId());
                ps.setLong(3, product.getItem().getId());
                ps.setLong(4, product.getMainImage().getId());
                ps.setString(5, product.getName());
                ps.setInt(6, product.getPrice());
                ps.setInt(7, product.getNormalPrice());
                ps.setInt(8, product.getOrderMode().jdbcInsert());
                ps.setString(9, product.getRentalInfo());
                ps.setInt(10, product.getClickCount());
                ps.setInt(11, product.getReviewCount());
                ps.setInt(12, product.getMinimumPurchaseQuantity());
                ps.setString(13, product.getOptionDetail());
                ps.setInt(14, product.getGender().jdbcInsert());
                ps.setInt(15, product.getDeliveryPrice());
                ps.setString(16, product.getShippingSetting());
                ps.setInt(17, product.getFastDelivery().jdbcInsert());
                ps.setInt(18, product.getRegularDelivery().jdbcInsert());
                ps.setInt(19, product.getDawnDelivery().jdbcInsert());
                ps.setString(20, product.getIsbn());
                ps.setInt(21, product.getStock());
            }

            @Override
            public int getBatchSize() {
                return itemProductList.size();
            }
        });

        List<Long> ids = getLongs(lastId, jdbcTemplate, selectIdMax);
        jdbcTemplate.batchUpdate(INSERT_LINKS_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long itemProductId = ids.get(i);
                ps.setLong(1,  ids.get(i));
                ps.setLong(2, itemProductList.get(i).getLinks().get(0).getId());
            }

            @Override
            public int getBatchSize() {
                return ids.size();
            }
        });

    }


    public List<Long> getLongs(Long lastId, JdbcTemplate jdbcTemplate, String selectIdMax) {
        Long lastInsertedId = jdbcTemplate.queryForObject(selectIdMax, Long.class);
        List<Long> ids = new ArrayList<>();
        for (Long i = lastId; i < lastInsertedId; i += 50) {
            ids.add(i);
        }
        return ids;
    }
}
