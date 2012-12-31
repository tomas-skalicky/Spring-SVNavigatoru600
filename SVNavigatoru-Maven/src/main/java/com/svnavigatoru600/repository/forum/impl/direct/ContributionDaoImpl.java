package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.repository.forum.impl.FindAllContributionsOrderedWithLimitArguments;
import com.svnavigatoru600.repository.forum.impl.FindAllContributionsOrderedWithThreadIdArguments;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ContributionDaoImpl extends SimpleJdbcDaoSupport implements ContributionDao {

    private static final String TABLE_NAME = PersistedClass.Contribution.getTableName();
    protected ThreadDao threadDao;
    protected UserDao userDao;

    @Inject
    public void setThreadDao(ThreadDao threadDao) {
        this.threadDao = threadDao;
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of the given
     * <code>contribution</code>.
     */
    private void populateThreadAndAuthor(Contribution contribution) {
        // "true" means that the User (the Thread) will be loaded without
        // associated authorities (contributions).
        boolean lazy = true;

        int threadId = contribution.getThread().getId();
        contribution.setThread(((ThreadDaoImpl) this.threadDao).findById(threadId, lazy));

        String authorUsername = contribution.getAuthor().getUsername();
        contribution.setAuthor(((UserDaoImpl) this.userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of all the given
     * <code>contributions</code>.
     */
    private void populateThreadAndAuthor(List<Contribution> contributions) {
        for (Contribution contribution : contributions) {
            this.populateThreadAndAuthor(contribution);
        }
    }

    @Override
    public Contribution findById(int contributionId) {
        String query = String.format("SELECT * FROM %s c WHERE c.%s = ?", ContributionDaoImpl.TABLE_NAME,
                ContributionField.id.getColumnName());
        Contribution contribution = this.getSimpleJdbcTemplate().queryForObject(query,
                new ContributionRowMapper(), contributionId);

        this.populateThreadAndAuthor(contribution);
        return contribution;
    }

    @Override
    public List<Contribution> find(int threadId) {
        String query = String.format("SELECT * FROM %s c WHERE c.%s = ?", ContributionDaoImpl.TABLE_NAME,
                ContributionField.threadId.getColumnName());
        return this.getSimpleJdbcTemplate().query(query, new ContributionRowMapper(), threadId);
    }

    @Override
    public List<Contribution> findAllOrdered(FindAllContributionsOrderedWithLimitArguments arguments) {
        String query = String.format("SELECT * FROM %s c ORDER BY c.%s %s", ContributionDaoImpl.TABLE_NAME,
                arguments.getSortField().getColumnName(), arguments.getSortDirection().getDatabaseCode());
        List<Contribution> contributions = this.getSimpleJdbcTemplate().query(query,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    @Override
    public List<Contribution> findAllOrdered(FindAllContributionsOrderedWithThreadIdArguments arguments) {
        String query = String.format("SELECT * FROM %s c WHERE c.%s = ? ORDER BY c.%s %s",
                ContributionDaoImpl.TABLE_NAME, ContributionField.threadId.getColumnName(), arguments
                        .getSortField().getColumnName(), arguments.getSortDirection().getDatabaseCode());
        List<Contribution> contributions = this.getSimpleJdbcTemplate().query(query,
                new ContributionRowMapper(), arguments.getThreadId());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    @Override
    public void update(Contribution contribution) {
        Date now = new Date();
        contribution.setLastSaveTime(now);

        String query = String.format("UPDATE %s SET %s = ?, %s = ?, %s = ?, %s = ?, %s = ? WHERE %s = ?",
                ContributionDaoImpl.TABLE_NAME, ContributionField.threadId.getColumnName(),
                ContributionField.text.getColumnName(), ContributionField.creationTime.getColumnName(),
                ContributionField.lastSaveTime.getColumnName(),
                ContributionField.authorUsername.getColumnName(), ContributionField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, contribution.getThread().getId(), contribution.getText(),
                contribution.getCreationTime(), contribution.getLastSaveTime(),
                contribution.getAuthor().getUsername(), contribution.getId());
    }

    /**
     * Used during the save of the given <code>contribution</code>.
     */
    private Map<String, Object> getNamedParameters(Contribution contribution) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ContributionField.id.getColumnName(), contribution.getId());
        parameters.put(ContributionField.threadId.getColumnName(), contribution.getThread().getId());
        parameters.put(ContributionField.text.getColumnName(), contribution.getText());
        parameters.put(ContributionField.creationTime.getColumnName(), contribution.getCreationTime());
        parameters.put(ContributionField.lastSaveTime.getColumnName(), contribution.getLastSaveTime());
        parameters.put(ContributionField.authorUsername.getColumnName(), contribution.getAuthor()
                .getUsername());
        return parameters;
    }

    @Override
    public int save(Contribution contribution) {
        Date now = new Date();
        contribution.setCreationTime(now);
        contribution.setLastSaveTime(now);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(this.getDataSource())
                .withTableName(ContributionDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ContributionField.id.getColumnName())
                .usingColumns(ContributionField.threadId.getColumnName(),
                        ContributionField.text.getColumnName(),
                        ContributionField.creationTime.getColumnName(),
                        ContributionField.lastSaveTime.getColumnName(),
                        ContributionField.authorUsername.getColumnName());

        return insert.executeAndReturnKey(this.getNamedParameters(contribution)).intValue();
    }

    @Override
    public void delete(Contribution contribution) {
        String query = String.format("DELETE FROM %s WHERE %s = ?", ContributionDaoImpl.TABLE_NAME,
                ContributionField.id.getColumnName());
        this.getSimpleJdbcTemplate().update(query, contribution.getId());
    }
}
