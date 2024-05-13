package github.jhkoder.commerce.flatform.local.ep.item.repository;

import github.jhkoder.commerce.flatform.local.ep.item.domain.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    public static final String INSERT_SQL = "INSERT INTO items " +
            " (id, barcode, brand, brand_certification, maker, origin) " +
            " VALUES (items_id_seq.NEXTVAL, ?, ?, ?, ?, ?) ";
    public static final String selectIdMax = "SELECT MAX(id) FROM items";

    public List<Long> fetchInsert(List<Item> itemList) {

        Long lastImageId = jdbcTemplate.queryForObject(selectIdMax, Long.class);
        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Item item = itemList.get(i);
                ps.setString(1, item.getBarcode());
                ps.setString(2, item.getBrand());
                ps.setInt(3, item.getBrandCertification().jdbcInsert());
                ps.setString(4, item.getMaker());
                ps.setString(5, item.getOrigin());
            }

            @Override
            public int getBatchSize() {
                return itemList.size();
            }
        });

        return getLongs(lastImageId, jdbcTemplate, selectIdMax);
    }

    public List<Long> getLongs(Long lastImageId, JdbcTemplate jdbcTemplate, String selectIdMax) {

        Long lastInsertedId = jdbcTemplate.queryForObject(selectIdMax, Long.class);

        List<Long> ids = new ArrayList<>();
        for (Long i = lastImageId; i < lastInsertedId; i += 50) {
            ids.add(i);
        }
        return ids;
    }
}
