package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
public class ThreadDaoImpl extends SimpleJdbcDaoSupport implements ThreadDao {

    private static final String TABLE_NAME = PersistedClass.Thread.getTableName();
    protected ContributionDao contributionDao;
    protected UserDao userDao;

    @Inject
    public void setContributionDao(final ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @Inject
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Thread findById(final int threadId) {
        return this.findById(threadId, false);
    }

    /**
     * Populates the <code>contributions</code> property of the given <code>thread</code>.
     */
    private void populateContributions(final Thread thread) {
        thread.setContributions(this.contributionDao.findAll(thread.getId()));
    }

    /**
     * Populates the <code>contributions</code> property of all the given <code>threads</code>.
     */
    private void populateContributions(final List<Thread> threads) {
        for (Thread thread : threads) {
            this.populateContributions(thread);
        }
    }

    /**
     * Populates the <code>author</code> property of the given <code>thread</code>.
     */
    private void populateAuthor(final Thread thread) {
        // "true" means that the User will be loaded without associated
        // authorities.
        final boolean lazy = true;

        final String authorUsername = thread.getAuthor().getUsername();
        thread.setAuthor(((UserDaoImpl) this.userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>author</code> property of all the given <code>threads</code>.
     */
    private void populateAuthor(final List<Thread> threads) {
        for (Thread thread : threads) {
            this.populateAuthor(thread);
        }
    }

    /**
     * @param lazy
     *            {@link Contribution}s of the desired {@link Thread} will not be loaded.
     */
    public Thread findById(final int threadId, final boolean lazy) {
        final String query = String.format("SELECT * FROM %s t WHERE t.%s = ?", ThreadDaoImpl.TABLE_NAME,
                ThreadField.id.getColumnName());
        final Thread thread = this.getSimpleJdbcTemplate().queryForObject(query, new ThreadRowMapper(),
                threadId);

        if (!lazy) {
            this.populateContributions(thread);
        }
        this.populateAuthor(thread);
        return thread;
    }

    @Override
    public List<Thread> loadAll() {
        final String query = String.format("SELECT * FROM %s t", ThreadDaoImpl.TABLE_NAME);
        final List<Thread> threads = this.getSimpleJdbcTemplate().query(query, new ThreadRowMapper());

        this.populateContributions(threads);
        this.populateAuthor(threads);
        return threads;
    }

    @Override
    public void update(final Thread thread) {
        final String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ? WHERE %s = ?",
                ThreadDaoImpl.TABLE_NAME, ThreadField.name.getColumnName(),
                ThreadField.creationTime.getColumnName(), ThreadField.authorUsername.getColumnName(),
                ThreadField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, thread.getName(), thread.getCreationTime(),
                thread.getAuthor().getUsername(), thread.getId());
    }

    /**
     * Used during the save of the given <code>thread</code>.
     */
    private Map<String, Object> getNamedParameters(final Thread thread) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ThreadField.id.getColumnName(), thread.getId());
        parameters.put(ThreadField.name.getColumnName(), thread.getName());
        parameters.put(ThreadField.creationTime.getColumnName(), thread.getCreationTime());
        parameters.put(ThreadField.authorUsername.getColumnName(), thread.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public int save(final Thread thread) {
        final Date now = new Date();
        thread.setCreationTime(now);

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(ThreadDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ThreadField.id.getColumnName())
                .usingColumns(ThreadField.name.getColumnName(), ThreadField.creationTime.getColumnName(),
                        ThreadField.authorUsername.getColumnName());

        final int threadId = insert.executeAndReturnKey(this.getNamedParameters(thread)).intValue();
        thread.setId(threadId);

        // NOTE: explicit save of the thread's contributions.
        for (Contribution contribution : thread.getContributions()) {
            contribution.setCreationTime(now);
            contribution.setLastSaveTime(now);
            this.contributionDao.save(contribution);
        }

        return threadId;
    }

    @Override
    public void delete(final Thread thread) {
        final String query = String.format("DELETE FROM %s WHERE %s = ?", ThreadDaoImpl.TABLE_NAME,
                ThreadField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, thread.getId());
    }
}
