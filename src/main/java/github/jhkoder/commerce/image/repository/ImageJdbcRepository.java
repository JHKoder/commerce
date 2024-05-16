package github.jhkoder.commerce.image.repository;

import github.jhkoder.commerce.image.domain.Images;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final String INSERT_SQL = "INSERT INTO images (user_id, path, id) " +
            "VALUES (?, ?, images_id_seq.NEXTVAL)";
    private static final String selectIdMax = "SELECT MAX(id) FROM images";

    public List<Long> fetchInsert(List<Images> imagesList) {
        Long lastImageId;
        try {
            lastImageId = jdbcTemplate.queryForObject(selectIdMax, Long.class);
        } catch (Exception e) {
            lastImageId = 0L;
        }

        jdbcTemplate.batchUpdate(INSERT_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Images image = imagesList.get(i);
                ps.setLong(1, image.getUser().getId());
                ps.setString(2, image.getPath());
            }

            @Override
            public int getBatchSize() {
                return imagesList.size();
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