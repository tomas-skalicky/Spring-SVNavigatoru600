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

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ThreadField;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("threadDao")
public class ThreadDaoImpl extends NamedParameterJdbcDaoSupport implements ThreadDao {

    /**
     * Database table which provides a persistence of {@link Thread Threads}.
     */
    private static final String TABLE_NAME = PersistedClass.Thread.getTableName();

    @Inject
    private ContributionDao contributionDao;

    @Inject
    private UserDao userDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public ThreadDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    @Override
    public Thread findById(int threadId) {
        return this.findById(threadId, false);
    }

    /**
     * Populates the <code>contributions</code> property of the given <code>thread</code>.
     */
    private void populateContributions(Thread thread) {
        thread.setContributions(this.contributionDao.findAll(thread.getId()));
    }

    /**
     * Populates the <code>contributions</code> property of all the given <code>threads</code>.
     */
    private void populateContributions(List<Thread> threads) {
        for (Thread thread : threads) {
            this.populateContributions(thread);
        }
    }

    /**
     * Populates the <code>author</code> property of the given <code>thread</code>.
     */
    private void populateAuthor(Thread thread) {
        // "true" means that the User will be loaded without associated
        // authorities.
        boolean lazy = true;

        String authorUsername = thread.getAuthor().getUsername();
        thread.setAuthor(((UserDaoImpl) this.userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>author</code> property of all the given <code>threads</code>.
     */
    private void populateAuthor(List<Thread> threads) {
        for (Thread thread : threads) {
            this.populateAuthor(thread);
        }
    }

    /**
     * @param lazy
     *            If <code>true</code>, {@link Contribution contributions} of the desired {@link Thread} will
     *            not be populated.
     */
    public Thread findById(int threadId, boolean lazy) {
        String idColumn = ThreadField.id.getColumnName();
        String query = String.format("SELECT * FROM %s t WHERE t.%s = :%s", ThreadDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, threadId);

        Thread thread = getNamedParameterJdbcTemplate().queryForObject(query, args, new ThreadRowMapper());

        if (!lazy) {
            this.populateContributions(thread);
        }
        this.populateAuthor(thread);
        return thread;
    }

    @Override
    public List<Thread> loadAll() {
        String query = String.format("SELECT * FROM %s t", ThreadDaoImpl.TABLE_NAME);

        List<Thread> threads = getJdbcTemplate().query(query, new ThreadRowMapper());

        this.populateContributions(threads);
        this.populateAuthor(threads);
        return threads;
    }

    /**
     * Maps properties of the given {@link Thread} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(Thread thread) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ThreadField.id.getColumnName(), thread.getId());
        parameters.put(ThreadField.name.getColumnName(), thread.getName());
        parameters.put(ThreadField.creationTime.getColumnName(), thread.getCreationTime());
        parameters.put(ThreadField.authorUsername.getColumnName(), thread.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public void update(Thread thread) {
        String idColumn = ThreadField.id.getColumnName();
        String nameColumn = ThreadField.name.getColumnName();
        String authorUsernameColumn = ThreadField.authorUsername.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s WHERE %s = :%s",
                ThreadDaoImpl.TABLE_NAME, nameColumn, nameColumn, authorUsernameColumn, authorUsernameColumn,
                idColumn, idColumn);

        getNamedParameterJdbcTemplate().update(query, getNamedParameters(thread));
    }

    @Override
    public int save(Thread thread) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(ThreadDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ThreadField.id.getColumnName())
                .usingColumns(ThreadField.name.getColumnName(), ThreadField.creationTime.getColumnName(),
                        ThreadField.authorUsername.getColumnName());

        Date now = new Date();
        thread.setCreationTime(now);
        int threadId = insert.executeAndReturnKey(getNamedParameters(thread)).intValue();
        thread.setId(threadId);

        // NOTE: explicit save of the thread's contributions.
        ContributionDaoImpl castedContributionDao = (ContributionDaoImpl) this.contributionDao;
        for (Contribution contribution : thread.getContributions()) {
            contribution.setCreationTime(now);
            contribution.setLastSaveTime(now);
            castedContributionDao.saveWithoutTimeSetup(contribution);
        }

        return threadId;
    }

    @Override
    public void delete(Thread thread) {
        String idColumn = ThreadField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", ThreadDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, thread.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
