package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.domain.forum.ForumThread;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ThreadFieldEnum;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("threadDao")
@Transactional
public class ThreadDaoImpl extends NamedParameterJdbcDaoSupport implements ThreadDao {

    /**
     * Database table which provides a persistence of {@link ForumThread Threads}.
     */
    private static final String TABLE_NAME = "threads";

    private final ContributionDao contributionDao;
    private final UserDao userDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public ThreadDaoImpl(final DataSource dataSource, final ContributionDao contributionDao, final UserDao userDao) {
        setDataSource(dataSource);
        this.contributionDao = contributionDao;
        this.userDao = userDao;
    }

    @Override
    public ForumThread findById(final int threadId) {
        return this.findById(threadId, false);
    }

    /**
     * Populates the <code>contributions</code> property of the given <code>thread</code>.
     */
    private void populateContributions(final ForumThread thread) {
        thread.setContributions(contributionDao.findByThreadId(thread.getId()));
    }

    /**
     * Populates the <code>contributions</code> property of all the given <code>threads</code>.
     */
    private void populateContributions(final List<ForumThread> threads) {
        for (final ForumThread thread : threads) {
            this.populateContributions(thread);
        }
    }

    /**
     * Populates the <code>author</code> property of the given <code>thread</code>.
     */
    private void populateAuthor(final ForumThread thread) {
        // "true" means that the User will be loaded without associated
        // authorities.
        final boolean lazy = true;

        final String authorUsername = thread.getAuthor().getUsername();
        thread.setAuthor(((UserDaoImpl) userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>author</code> property of all the given <code>threads</code>.
     */
    private void populateAuthor(final List<ForumThread> threads) {
        for (final ForumThread thread : threads) {
            this.populateAuthor(thread);
        }
    }

    /**
     * @param lazy
     *            If <code>true</code>, {@link ForumContribution contributions} of the desired {@link ForumThread} will
     *            not be populated.
     */
    public ForumThread findById(final int threadId, final boolean lazy) {
        final String idColumn = ThreadFieldEnum.ID.getColumnName();
        final String query = QueryUtil.selectQuery(ThreadDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, threadId);

        final ForumThread thread = getNamedParameterJdbcTemplate().queryForObject(query, args, new ThreadRowMapper());

        if (!lazy) {
            this.populateContributions(thread);
        }
        this.populateAuthor(thread);
        return thread;
    }

    @Override
    public List<ForumThread> loadAll() {
        final String query = String.format("SELECT * FROM %s t", ThreadDaoImpl.TABLE_NAME);

        final List<ForumThread> threads = getJdbcTemplate().query(query, new ThreadRowMapper());

        this.populateContributions(threads);
        this.populateAuthor(threads);
        return threads;
    }

    /**
     * Maps properties of the given {@link ForumThread} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final ForumThread thread) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ThreadFieldEnum.ID.getColumnName(), thread.getId());
        parameters.put(ThreadFieldEnum.NAME.getColumnName(), thread.getName());
        parameters.put(ThreadFieldEnum.CREATION_TIME.getColumnName(), thread.getCreationTime());
        parameters.put(ThreadFieldEnum.AUTHOR_USERNAME.getColumnName(), thread.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public void update(final ForumThread thread) {
        final String idColumn = ThreadFieldEnum.ID.getColumnName();
        final String nameColumn = ThreadFieldEnum.NAME.getColumnName();
        final String authorUsernameColumn = ThreadFieldEnum.AUTHOR_USERNAME.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s", ThreadDaoImpl.TABLE_NAME,
                nameColumn, nameColumn, authorUsernameColumn, authorUsernameColumn, idColumn, idColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(thread));
    }

    @Override
    public int save(final ForumThread thread) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(ThreadDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ThreadFieldEnum.ID.getColumnName()).usingColumns(ThreadFieldEnum.NAME.getColumnName(),
                        ThreadFieldEnum.CREATION_TIME.getColumnName(), ThreadFieldEnum.AUTHOR_USERNAME.getColumnName());

        final Date now = new Date();
        thread.setCreationTime(now);
        final int threadId = insert.executeAndReturnKey(getNamedParameters(thread)).intValue();
        thread.setId(threadId);

        // NOTE: explicit save of the thread's contributions.
        final ContributionDaoImpl castedContributionDao = (ContributionDaoImpl) contributionDao;
        for (final ForumContribution contribution : thread.getContributions()) {
            contribution.setCreationTime(now);
            contribution.setLastSaveTime(now);
            castedContributionDao.saveWithoutTimeSetup(contribution);
        }

        return threadId;
    }

    @Override
    public void delete(final ForumThread thread) {
        final String idColumn = ThreadFieldEnum.ID.getColumnName();
        final String query = QueryUtil.deleteQuery(ThreadDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, thread.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
