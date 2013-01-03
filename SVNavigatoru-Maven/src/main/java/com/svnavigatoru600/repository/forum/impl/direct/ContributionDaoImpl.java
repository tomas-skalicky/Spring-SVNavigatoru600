package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.svnavigatoru600.domain.forum.Contribution;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ContributionField;
import com.svnavigatoru600.repository.impl.PersistedClass;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;
import com.svnavigatoru600.service.util.OrderType;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ContributionDaoImpl extends NamedParameterJdbcDaoSupport implements ContributionDao {

    /**
     * Database table which provides a persistence of {@link Contribution Contributions}.
     */
    private static final String TABLE_NAME = PersistedClass.Contribution.getTableName();
    private ThreadDao threadDao;
    private UserDao userDao;

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
        String idColumn = ContributionField.id.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s", ContributionDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, contributionId);

        Contribution contribution = this.getNamedParameterJdbcTemplate().queryForObject(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contribution);
        return contribution;
    }

    @Override
    public List<Contribution> findAll(int threadId) {
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s", ContributionDaoImpl.TABLE_NAME,
                threadIdColumn, threadIdColumn);

        Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        return this.getNamedParameterJdbcTemplate().query(query, args, new ContributionRowMapper());
    }

    /**
     * @param maxResultSize
     *            NOT USED YET
     */
    @Override
    public List<Contribution> findAllOrdered(ContributionField sortField, OrderType sortDirection,
            int maxResultSize) {
        String query = String.format("SELECT * FROM %s c ORDER BY c.%s %s", ContributionDaoImpl.TABLE_NAME,
                sortField.getColumnName(), sortDirection.getDatabaseCode());

        List<Contribution> contributions = this.getJdbcTemplate().query(query, new ContributionRowMapper());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    @Override
    public List<Contribution> findAllOrdered(int threadId, ContributionField sortField,
            OrderType sortDirection) {
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s ORDER BY c.%s %s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, sortField.getColumnName(),
                sortDirection.getDatabaseCode());

        Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        List<Contribution> contributions = this.getNamedParameterJdbcTemplate().query(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    /**
     * Maps properties of the given {@link Contribution} to names of the corresponding database columns.
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
    public void update(Contribution contribution) {
        Date now = new Date();
        contribution.setLastSaveTime(now);

        String idColumn = ContributionField.id.getColumnName();
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String textColumn = ContributionField.text.getColumnName();
        String creationTimeColumn = ContributionField.creationTime.getColumnName();
        String lastSaveTimeColumn = ContributionField.lastSaveTime.getColumnName();
        String authorUsernameColumn = ContributionField.authorUsername.getColumnName();
        String query = String.format(
                "UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, textColumn, textColumn,
                creationTimeColumn, creationTimeColumn, lastSaveTimeColumn, lastSaveTimeColumn,
                authorUsernameColumn, authorUsernameColumn, idColumn, idColumn);

        this.getNamedParameterJdbcTemplate().update(query, this.getNamedParameters(contribution));
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
        String idColumn = ContributionField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", ContributionDaoImpl.TABLE_NAME,
                idColumn, idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, contribution.getId());

        this.getNamedParameterJdbcTemplate().update(query, args);
    }
}
