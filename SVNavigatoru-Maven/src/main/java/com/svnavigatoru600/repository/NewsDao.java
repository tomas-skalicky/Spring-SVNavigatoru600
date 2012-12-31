package com.svnavigatoru600.repository;

import java.util.List;

import com.svnavigatoru600.domain.News;
import com.svnavigatoru600.repository.news.impl.FindAllOrderedArguments;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface NewsDao {

    /**
     * Returns a {@link News} stored in the repository which has the given <code>ID</code>.
     */
    News findById(int newsId);

    /**
     * Returns all {@link News} stored in the repository arranged according to their values of the given
     * <code>arguments</code>.
     */
    List<News> findAllOrdered(FindAllOrderedArguments arguments);

    /**
     * Updates the given <code>news</code> in the repository. The old version of the <code>news</code> should
     * be already stored there.
     */
    void update(News news);

    /**
     * Stores the given <code>news</code> to the repository.
     * 
     * @return the generated identifier
     */
    int save(News news);

    /**
     * Deletes the given <code>news</code> from the repository.
     */
    void delete(News news);
}
