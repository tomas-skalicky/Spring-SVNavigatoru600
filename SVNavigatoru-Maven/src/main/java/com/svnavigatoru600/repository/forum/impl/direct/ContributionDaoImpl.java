package com.svnavigatoru600.repository.forum.impl.direct;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.domain.forum.ForumContribution;
import com.svnavigatoru600.repository.AbstractDaoImpl;
import com.svnavigatoru600.repository.QueryUtil;
import com.svnavigatoru600.repository.forum.ContributionDao;
import com.svnavigatoru600.repository.forum.ThreadDao;
import com.svnavigatoru600.repository.forum.impl.ContributionFieldEnum;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.repository.users.impl.direct.UserDaoImpl;
import com.svnavigatoru600.service.util.OrderTypeEnum;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Repository("contributionDao")
@Transactional
public class ContributionDaoImpl extends AbstractDaoImpl implements ContributionDao {

    /**
     * Database table which provides a persistence of {@link ForumContribution Contributions}.
     */
    private static final String TABLE_NAME = "contributions";

    /**
     * NOTE: Cannot be injected via constructor since otherwise "unresolvable circular reference".
     */
    @Inject
    private ThreadDao threadDao;

    private final UserDao userDao;

    /**
     * NOTE: Added because of the final setter.
     */
    @Inject
    public ContributionDaoImpl(final UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of the given <code>contribution</code>.
     */
    private void populateThreadAndAuthor(final ForumContribution contribution) {
        // "true" means that the User (the Thread) will be loaded without
        // associated authorities (contributions).
        final boolean lazy = true;

        final int threadId = contribution.getThread().getId();
        contribution.setThread(((ThreadDaoImpl) threadDao).findById(threadId, lazy));

        final String authorUsername = contribution.getAuthor().getUsername();
        contribution.setAuthor(((UserDaoImpl) userDao).findByUsername(authorUsername, lazy));
    }

    /**
     * Populates the <code>thread</code> and <code>author</code> property of all the given <code>contributions</code>.
     */
    private void populateThreadAndAuthor(final List<ForumContribution> contributions) {
        for (final ForumContribution contribution : contributions) {
            this.populateThreadAndAuthor(contribution);
        }
    }

    @Override
    public ForumContribution findById(final int contributionId) {
        final String idColumn = ContributionFieldEnum.ID.getColumnName();
        final String query = QueryUtil.selectQuery(ContributionDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, contributionId);

        final ForumContribution contribution = getNamedParameterJdbcTemplate().queryForObject(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contribution);
        return contribution;
    }

    @Override
    public List<ForumContribution> findByThreadId(final int threadId) {
        final String threadIdColumn = ContributionFieldEnum.THREAD_ID.getColumnName();
        final String query = QueryUtil.selectQuery(ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn);

        final Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        return getNamedParameterJdbcTemplate().query(query, args, new ContributionRowMapper());
    }

    @Override
    public List<ForumContribution> findAllOrdered(final ContributionFieldEnum sortField,
            final OrderTypeEnum sortDirection, final int maxResultSize) {
        final String query = String.format("SELECT * FROM %s c ORDER BY c.%s %s", ContributionDaoImpl.TABLE_NAME,
                sortField.getColumnName(), sortDirection.getDatabaseCode());

        final List<ForumContribution> contributionsFromDb = getJdbcTemplate().query(query, new ContributionRowMapper());
        final List<ForumContribution> filteredContributions = contributionsFromDb.subList(0,
                Math.min(contributionsFromDb.size(), maxResultSize));

        this.populateThreadAndAuthor(filteredContributions);
        return filteredContributions;
    }

    @Override
    public List<ForumContribution> findAllOrdered(final int threadId, final ContributionFieldEnum sortField,
            final OrderTypeEnum sortDirection) {
        final String threadIdColumn = ContributionFieldEnum.THREAD_ID.getColumnName();
        final String query = String.format("SELECT * FROM %s c WHERE c.%s = :%s ORDER BY c.%s %s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, sortField.getColumnName(),
                sortDirection.getDatabaseCode());

        final Map<String, Integer> args = Collections.singletonMap(threadIdColumn, threadId);

        final List<ForumContribution> contributions = getNamedParameterJdbcTemplate().query(query, args,
                new ContributionRowMapper());

        this.populateThreadAndAuthor(contributions);
        return contributions;
    }

    /**
     * Maps properties of the given {@link ForumContribution} to names of the corresponding database columns.
     */
    private Map<String, Object> getNamedParameters(final ForumContribution contribution) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put(ContributionFieldEnum.ID.getColumnName(), contribution.getId());
        parameters.put(ContributionFieldEnum.THREAD_ID.getColumnName(), contribution.getThread().getId());
        parameters.put(ContributionFieldEnum.TEXT.getColumnName(), contribution.getText());
        parameters.put(ContributionFieldEnum.CREATION_TIME.getColumnName(), contribution.getCreationTime());
        parameters.put(ContributionFieldEnum.LAST_SAVE_TIME.getColumnName(), contribution.getLastSaveTime());
        parameters.put(ContributionFieldEnum.AUTHOR_USERNAME.getColumnName(), contribution.getAuthor().getUsername());
        return parameters;
    }

    @Override
    public void update(final ForumContribution contribution) {
        final String idColumn = ContributionFieldEnum.ID.getColumnName();
        final String threadIdColumn = ContributionFieldEnum.THREAD_ID.getColumnName();
        final String textColumn = ContributionFieldEnum.TEXT.getColumnName();
        final String lastSaveTimeColumn = ContributionFieldEnum.LAST_SAVE_TIME.getColumnName();
        final String authorUsernameColumn = ContributionFieldEnum.AUTHOR_USERNAME.getColumnName();
        final String query = String.format("UPDATE %s SET %s = :%s, %s = :%s, %s = :%s, %s = :%s WHERE %s = :%s",
                ContributionDaoImpl.TABLE_NAME, threadIdColumn, threadIdColumn, textColumn, textColumn,
                lastSaveTimeColumn, lastSaveTimeColumn, authorUsernameColumn, authorUsernameColumn, idColumn, idColumn);

        final Date now = new Date();
        contribution.setLastSaveTime(now);
        doUpdate(query, getNamedParameters(contribution));
    }

    @Override
    public int save(final ForumContribution contribution) {
        final Date now = new Date();
        contribution.setCreationTime(now);
        contribution.setLastSaveTime(now);
        return saveWithoutTimeSetup(contribution);
    }

    /**
     * Does the save as {@link ContributionDaoImpl#save(ForumContribution)}, but does not sets
     * {@link ForumContribution#getCreationTime() creation time} and {@link ForumContribution#getLastSaveTime() last
     * save time} of the given {@link ForumContribution}.
     *
     * @return The new ID of the given {@link ForumContribution} generated by the repository
     */
    int saveWithoutTimeSetup(final ForumContribution contribution) {
        final SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
                .withTableName(ContributionDaoImpl.TABLE_NAME)
                .usingGeneratedKeyColumns(ContributionFieldEnum.ID.getColumnName())
                .usingColumns(ContributionFieldEnum.THREAD_ID.getColumnName(),
                        ContributionFieldEnum.TEXT.getColumnName(), ContributionFieldEnum.CREATION_TIME.getColumnName(),
                        ContributionFieldEnum.LAST_SAVE_TIME.getColumnName(),
                        ContributionFieldEnum.AUTHOR_USERNAME.getColumnName());

        return insert.executeAndReturnKey(getNamedParameters(contribution)).intValue();
    }

    @Override
    public void delete(final ForumContribution contribution) {
        final String idColumn = ContributionFieldEnum.ID.getColumnName();
        final String query = QueryUtil.deleteQuery(ContributionDaoImpl.TABLE_NAME, idColumn, idColumn);

        final Map<String, Integer> args = Collections.singletonMap(idColumn, contribution.getId());

        doUpdate(query, args);
    }
}
