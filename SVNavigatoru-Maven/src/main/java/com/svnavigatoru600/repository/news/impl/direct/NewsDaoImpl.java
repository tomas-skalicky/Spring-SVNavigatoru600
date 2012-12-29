package com.svnavigatoru600.repository.news.impl.direct;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.service.util.OrderType;

public class NewsDaoImpl extends SimpleJdbcDaoSupport implements NewsDao {

    private static final String TABLE_NAME = "news";

    @Override
    public News findById(int newsId) {
        String query = String.format("SELECT * FROM %s n WHERE n.%s = ?", NewsDaoImpl.TABLE_NAME,
                NewsRowMapper.getColumn("id"));
        return this.getSimpleJdbcTemplate().queryForObject(query, new NewsRowMapper(), newsId);
    }

    @Override
    public List<News> findOrdered(String attribute, OrderType order) {
        String query = String.format("SELECT * FROM %s n ORDER BY %s %s", NewsDaoImpl.TABLE_NAME,
                NewsRowMapper.getColumn(attribute), order.getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new NewsRowMapper());
    }

    @Override
    public void update(News news) {
        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                NewsDaoImpl.TABLE_NAME, NewsRowMapper.getColumn("title"), NewsRowMapper.getColumn("text"),
                NewsRowMapper.getColumn("creationTime"), NewsRowMapper.getColumn("lastSaveTime"),
                NewsRowMapper.getColumn("id"));
        this.getSimpleJdbcTemplate().update(query, news.getTitle(), news.getText(), news.getCreationTime(),
                news.getLastSaveTime(), news.getId());
    }

    /**
     * Used during the save of the given <code>news</code>.
     */
    private Map<String, Object> getNamedParameters(News news) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(NewsRowMapper.getColumn("id"), news.getId());
        parameters.put(NewsRowMapper.getColumn("title"), news.getTitle());
        parameters.put(NewsRowMapper.getColumn("text"), news.getText());
        parameters.put(NewsRowMapper.getColumn("creationTime"), news.getCreationTime());
        parameters.put(NewsRowMapper.getColumn("lastSaveTime"), news.getLastSaveTime());
        return parameters;
    }

    @Override
    public int save(News news) {
        String idColumn = NewsRowMapper.getColumn("id");

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(NewsDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(idColumn)
                .usingColumns(NewsRowMapper.getColumn("title"), NewsRowMapper.getColumn("text"),
                        NewsRowMapper.getColumn("creationTime"), NewsRowMapper.getColumn("lastSaveTime"));

        Map<String, Object> keys = insert.executeAndReturnKeyHolder(this.getNamedParameters(news)).getKeys();
        // The generated identified is not under the given idColumn, though the
        // ID should be there. Instead, the ID is mapped under "GENERATED_KEY".
        // For more info, see
        // http://forum.springsource.org/showthread.php?91014-Fetching-auto-generated-primary-key-value-after-insert
        // return (Integer) keys.get(idColumn);
        news.setId(((Long) keys.get("GENERATED_KEY")).intValue());
        return news.getId();
    }

    @Override
    public void delete(News news) {
        String idProperty = NewsRowMapper.getColumn("id");
        String query = String.format("DELETE FROM %s WHERE %s = ?", NewsDaoImpl.TABLE_NAME, idProperty);
        this.getSimpleJdbcTemplate().update(query, news.getId());
    }
}
