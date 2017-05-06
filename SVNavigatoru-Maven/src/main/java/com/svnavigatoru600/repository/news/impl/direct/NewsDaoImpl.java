package com.svnavigatoru600.repository.news.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.NewsDao;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;
import com.svnavigatoru600.repository.news.impl.NewsFieldEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("newsDao")
@Transactional
public class NewsDaoImpl extends AbstractDaoImpl implements NewsDao {

    /**
     * Database table which provides a persistence of {@link News}.
     */
    private static final String TABLE_NAME = "news";

    @Override
    public News findById(final int newsId) {
        final String idColumn = NewsFieldEnum.ID.getColumnName();
        final String query = QueryUtil.selectQuery(NewsDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, newsId);

        return getNamedParameterJdbcTemplate().queryForObject(query, args, new NewsRowMapper());
    }

    @Override
    public List<News> findAllOrdered(final FindAllOrderedArguments arguments) {
        final String query = String.format("SELECT * FROM %s n ORDER BY %s %s", NewsDaoImpl.TABLE_NAME,
                arguments.getSortField().getColumnName(), arguments.getSortDirection().getDatabaseCode());

        return getJdbcTemplate().query(query, new NewsRowMapper());
    }

    /**
     * Maps properties of the given {@link News} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final News news) {
        final Map<String, Object> parameters = Maps.newHashMap();
        parameters.put(NewsFieldEnum.ID.getColumnName(), news.getId());
        parameters.put(NewsFieldEnum.TITLE.getColumnName(), news.getTitle());
        parameters.put(NewsFieldEnum.TEXT.getColumnName(), news.getText());
        parameters.put(NewsFieldEnum.CREATION_TIME.getColumnName(), news.getCreationTime());
        parameters.put(NewsFieldEnum.LAST_SAVE_TIME.getColumnName(), news.getLastSaveTime());
        return parameters;
    }

    @Override
    public void update(final News news) {
        final String idColumn = NewsFieldEnum.ID.getColumnName();
        final String titleColumn = NewsFieldEnum.TITLE.getColumnName();
        final String textColumn = NewsFieldEnum.TEXT.getColumnName();
        final String lastSaveTimeColumn = NewsFieldEnum.LAST_SAVE_TIME.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                NewsDaoImpl.TABLE_NAME, titleColumn, titleColumn, textColumn, textColumn, lastSaveTimeColumn,
                lastSaveTimeColumn, idColumn, idColumn);

        final Date now = new Date();
        news.setLastSaveTime(now);
        getNamedParameterJdbcTemplate().update(query, getNamedParameters(news));
    }

    @Override
    public int save(final News news) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(NewsDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(NewsFieldEnum.ID.getColumnName()).usingColumns(NewsFieldEnum.TITLE.getColumnName(),
                        NewsFieldEnum.TEXT.getColumnName(), NewsFieldEnum.CREATION_TIME.getColumnName(),
                        NewsFieldEnum.LAST_SAVE_TIME.getColumnName());

        final Date now = new Date();
        news.setCreationTime(now);
        news.setLastSaveTime(now);
        final int newId = insert.executeAndReturnKey(getNamedParameters(news)).intValue();
        news.setId(newId);
        return newId;
    }

    @Override
    public void delete(final News news) {
        final String idColumn = NewsFieldEnum.ID.getColumnName();
        final String query = QueryUtil.deleteQuery(NewsDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, news.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
