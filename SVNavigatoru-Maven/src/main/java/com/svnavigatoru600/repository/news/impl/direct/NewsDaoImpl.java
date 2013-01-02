package com.svnavigatoru600.repository.news.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;
import com.svnavigatoru600.repository.news.impl.NewsField;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class NewsDaoImpl extends NamedParameterJdbcDaoSupport implements NewsDao {

    /**
     * Database table which provides a persistence of {@link News}.
     */
    private static final String TABLE_NAME = PersistedClass.News.getTableName();

    @Override
    public News findById(int newsId) {
        String idColumn = NewsField.id.getColumnName();
        String query = String.format("SELECT * FROM %s n WHERE n.%s = :%s", NewsDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, newsId);

        return this.getNamedParameterJdbcTemplate().queryForObject(query, args, new NewsRowMapper());
    }

    @Override
    public List<News> findAllOrdered(FindAllOrderedArguments arguments) {
        String query = String.format("SELECT * FROM %s n ORDER BY %s %s", NewsDaoImpl.TABLE_NAME, arguments
                .getSortField().getColumnName(), arguments.getSortDirection().getDatabaseCode());

        return this.getJdbcTemplate().query(query, new NewsRowMapper());
    }

    /**
     * Maps properties of the given {@link News} to names of the corresponding database columns.
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
    public void update(News news) {
        String idColumn = NewsField.id.getColumnName();
        String titleColumn = NewsField.title.getColumnName();
        String textColumn = NewsField.text.getColumnName();
        String creationTimeColumn = NewsField.creationTime.getColumnName();
        String lastSaveTimeColumn = NewsField.lastSaveTime.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                NewsDaoImpl.TABLE_NAME, titleColumn, titleColumn, textColumn, textColumn, creationTimeColumn,
                creationTimeColumn, lastSaveTimeColumn, lastSaveTimeColumn, idColumn, idColumn);

        Date now = new Date();
        news.setLastSaveTime(now);
        this.getNamedParameterJdbcTemplate().update(query, this.getNamedParameters(news));
    }

    @Override
    public int save(News news) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(NewsDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(NewsField.id.getColumnName())
                .usingColumns(NewsField.title.getColumnName(), NewsField.text.getColumnName(),
                        NewsField.creationTime.getColumnName(), NewsField.lastSaveTime.getColumnName());

        Date now = new Date();
        news.setCreationTime(now);
        news.setLastSaveTime(now);
        int newId = insert.executeAndReturnKey(this.getNamedParameters(news)).intValue();
        news.setId(newId);
        return newId;
    }

    @Override
    public void delete(News news) {
        String idColumn = NewsField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", NewsDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, news.getId());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
