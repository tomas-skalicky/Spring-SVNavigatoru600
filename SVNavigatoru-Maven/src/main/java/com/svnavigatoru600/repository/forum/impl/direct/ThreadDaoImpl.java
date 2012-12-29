package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;

public class ThreadDaoImpl extends SimpleJdbcDaoSupport implements ThreadDao {

    private static final String TABLE_NAME = "threads";
    protected ContributionDao contributionDao;
    protected UserDao userDao;

    @Autowired
    public void setContributionDao(final ContributionDao contributionDao) {
        this.contributionDao = contributionDao;
    }

    @Autowired
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
        thread.setContributions(this.contributionDao.find(thread.getId()));
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
                ThreadRowMapper.getColumn("id"));
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
                ThreadDaoImpl.TABLE_NAME, ThreadRowMapper.getColumn("name"),
                ThreadRowMapper.getColumn("creationTime"), ThreadRowMapper.getColumn("authorUsername"),
                ThreadRowMapper.getColumn("id"));
        this.getSimpleJdbcTemplate().update(query, thread.getName(), thread.getCreationTime(),
                thread.getAuthor().getUsername(), thread.getId());
    }

    /**
     * Used during the save of the given <code>thread</code>.
     */
    private Map<String, Object> getNamedParameters(final Thread thread) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ThreadRowMapper.getColumn("id"), thread.getId());
        parameters.put(ThreadRowMapper.getColumn("name"), thread.getName());
        parameters.put(ThreadRowMapper.getColumn("creationTime"), thread.getCreationTime());
        parameters.put(ThreadRowMapper.getColumn("authorUsername"), thread.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public int save(final Thread thread) {
        final Date now = new Date();
        thread.setCreationTime(now);

        final String idColumn = ThreadRowMapper.getColumn("id");

        final SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(ThreadDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(idColumn)
                .usingColumns(ThreadRowMapper.getColumn("name"), ThreadRowMapper.getColumn("creationTime"),
                        ThreadRowMapper.getColumn("authorUsername"));

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
                ThreadRowMapper.getColumn("id"));
        this.getSimpleJdbcTemplate().update(query, thread.getId());
    }
}
