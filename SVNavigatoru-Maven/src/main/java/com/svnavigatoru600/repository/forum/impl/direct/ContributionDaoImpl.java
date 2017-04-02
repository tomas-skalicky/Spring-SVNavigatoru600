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

import com.svnavigatoru600.domain.forum.ForumContribution;
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
@Repository("contributionDao")
public class ContributionDaoImpl extends NamedParameterJdbcDaoSupport implements ContributionDao {

    /**
     * Database table which provides a persistence of {@link ForumContribution Contributions}.
     */
    private static final String TABLE_NAME = PersistedClass.Contribution.getTableName();

    @Inject
    private ThreadDao threadDao;

    @Inject
    private UserDao userDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public ContributionDaoImpl(DataSource dataSource) {
        super();
        setDataSource(dataSource);
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of the given <code>contribution</code>.
     */
    private void populateThreadAndAuthor(ForumContribution contribution) {
        // "true" means that the User (the Thread) will be loaded without
        // associated authorities (contributions).
        boolean lazy = true;

        int threadId = contribution.getThread().getId();
        contribution.setThread(((ThreadDaoImpl) this.threadDao).findById(threadId, lazy));

        String authorUsername = contribution.getAuthor().getUsername();
        contribution.setAuthor(((UserDaoImpl) this.userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of all the given <code>contributions</code>.
     */
    private void populateThreadAndAuthor(List<ForumContribution> contributions) {
        for (ForumContribution contribution : contributions) {
            this.populateThreadAndAuthor(contribution);
        }
    }

    @Override
    public ForumContribution findById(int contributionId) {
        String idColumn = ContributionField.id.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s", ContributionDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, contributionId);

        ForumContribution contribution = getNamedParameterJdbcTemplate().queryForObject(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contribution);
        return contribution;
    }

    @Override
    public List<ForumContribution> findAll(int threadId) {
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s", ContributionDaoImpl.TABLE_NAME,
                threadIdColumn, threadIdColumn);

        Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        return getNamedParameterJdbcTemplate().query(query, args, new ContributionRowMapper());
    }

    @Override
    public List<ForumContribution> findAllOrdered(ContributionField sortField, OrderType sortDirection, int maxResultSize) {
        String query = String.format("SELECT * FROM %s c ORDER BY c.%s %s", ContributionDaoImpl.TABLE_NAME,
                sortField.getColumnName(), sortDirection.getDatabaseCode());

        List<ForumContribution> contributionsFromDb = getJdbcTemplate().query(query, new ContributionRowMapper());
        List<ForumContribution> filteredContributions = contributionsFromDb.subList(0,
                Math.min(contributionsFromDb.size(), maxResultSize));

        this.populateThreadAndAuthor(filteredContributions);
        return filteredContributions;
    }

    @Override
    public List<ForumContribution> findAllOrdered(int threadId, ContributionField sortField, OrderType sortDirection) {
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s ORDER BY c.%s %s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, sortField.getColumnName(),
                sortDirection.getDatabaseCode());

        Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        List<ForumContribution> contributions = getNamedParameterJdbcTemplate().query(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    /**
     * Maps properties of the given {@link ForumContribution} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(ForumContribution contribution) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(ContributionField.id.getColumnName(), contribution.getId());
        parameters.put(ContributionField.threadId.getColumnName(), contribution.getThread().getId());
        parameters.put(ContributionField.text.getColumnName(), contribution.getText());
        parameters.put(ContributionField.creationTime.getColumnName(), contribution.getCreationTime());
        parameters.put(ContributionField.lastSaveTime.getColumnName(), contribution.getLastSaveTime());
        parameters.put(ContributionField.authorUsername.getColumnName(), contribution.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public void update(ForumContribution contribution) {
        String idColumn = ContributionField.id.getColumnName();
        String threadIdColumn = ContributionField.threadId.getColumnName();
        String textColumn = ContributionField.text.getColumnName();
        String lastSaveTimeColumn = ContributionField.lastSaveTime.getColumnName();
        String authorUsernameColumn = ContributionField.authorUsername.getColumnName();
        String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, textColumn, textColumn,
                lastSaveTimeColumn, lastSaveTimeColumn, authorUsernameColumn, authorUsernameColumn, idColumn, idColumn);

        Date now = new Date();
        contribution.setLastSaveTime(now);
        getNamedParameterJdbcTemplate().update(query, getNamedParameters(contribution));
    }

    @Override
    public int save(ForumContribution contribution) {
        Date now = new Date();
        contribution.setCreationTime(now);
        contribution.setLastSaveTime(now);
        return saveWithoutTimeSetup(contribution);
    }

    /**
     * Does the save as {@link ContributionDaoImpl#save(ForumContribution)}, but does not sets
     * {@link ForumContribution#getCreationTime() creation time} and {@link ForumContribution#getLastSaveTime() last save time} of
     * the given {@link ForumContribution}.
     * 
     * @return The new ID of the given {@link ForumContribution} generated by the repository
     */
    int saveWithoutTimeSetup(ForumContribution contribution) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource()).withTableName(ContributionDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ContributionField.id.getColumnName())
                .usingColumns(ContributionField.threadId.getColumnName(), ContributionField.text.getColumnName(),
                        ContributionField.creationTime.getColumnName(), ContributionField.lastSaveTime.getColumnName(),
                        ContributionField.authorUsername.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(contribution)).intValue();
    }

    @Override
    public void delete(ForumContribution contribution) {
        String idColumn = ContributionField.id.getColumnName();
        String query = String.format("DELETE FROM %s WHERE %s = :%s", ContributionDaoImpl.TABLE_NAME, idColumn,
                idColumn);

        Map<String, Integer> args = Collections.singletonMap(idColumn, contribution.getId());

        getNamedParameterJdbcTemplate().update(query, args);
    }
}
