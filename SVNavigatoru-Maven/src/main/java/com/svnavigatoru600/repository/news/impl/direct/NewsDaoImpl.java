package com.svnavigatoru600.repository.news.impl.direct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;
import com.svnavigatoru600.repository.news.impl.NewsField;

public class NewsDaoImpl extends SimpleJdbcDaoSupport implements NewsDao {

    private static final String TABLE_NAME = PersistedClass.News.getTableName();

    @Override
    public News findById(int newsId) {
        String query = String.format("SELECT * FROM %s n WHERE n.%s = ?", NewsDaoImpl.TABLE_NAME,
                NewsField.id.getColumnName());
        return this.getSimpleJdbcTemplate().queryForObject(query, new NewsRowMapper(), newsId);
    }

    @Override
    public List<News> findAllOrdered(FindAllOrderedArguments arguments) {
        String query = String.format("SELECT * FROM %s n ORDER BY %s %s", NewsDaoImpl.TABLE_NAME, arguments
                .getSortField().getColumnName(), arguments.getSortDirection().getDatabaseCode());
        return this.getSimpleJdbcTemplate().query(query, new NewsRowMapper());
    }

    @Override
    public void update(News news) {
        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                NewsDaoImpl.TABLE_NAME, NewsField.title.getColumnName(), NewsField.text.getColumnName(),
                NewsField.creationTime.getColumnName(), NewsField.lastSaveTime.getColumnName(),
                NewsField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, news.getTitle(), news.getText(), news.getCreationTime(),
                news.getLastSaveTime(), news.getId());
    }

    /**
     * Used during the save of the given <code>news</code>.
     */
    private Map<String, Object> getNamedParameters(News news) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(NewsField.id.getColumnName(), news.getId());
        parameters.put(NewsField.title.getColumnName(), news.getTitle());
        parameters.put(NewsField.text.getColumnName(), news.getText());
        parameters.put(NewsField.creationTime.getColumnName(), news.getCreationTime());
        parameters.put(NewsField.lastSaveTime.getColumnName(), news.getLastSaveTime());
        return parameters;
    }

    @Override
    public int save(News news) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(NewsDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(NewsField.id.getColumnName())
                .usingColumns(NewsField.title.getColumnName(), NewsField.text.getColumnName(),
                        NewsField.creationTime.getColumnName(), NewsField.lastSaveTime.getColumnName());

        return insert.executeAndReturnKey(this.getNamedParameters(news)).intValue();
    }

    @Override
    public void delete(News news) {
        String query = String.format("DELETE FROM %s WHERE %s = ?", NewsDaoImpl.TABLE_NAME,
                NewsField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, news.getId());
    }
}
