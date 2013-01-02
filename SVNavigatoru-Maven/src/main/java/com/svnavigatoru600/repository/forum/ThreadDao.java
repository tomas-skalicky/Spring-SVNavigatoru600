package com.svnavigatoru600.repository.forum;

import java.util.List;

import com.svnavigatoru600.domain.forum.Thread;
import com.svnavigatoru600.repository.MapperInterface;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@MapperInterface
public interface ThreadDao {

    /**
     * Returns a {@link Thread} stored in the repository which has the given <code>ID</code>.
     */
    Thread findById(int threadId);

    /**
     * Returns all {@link Thread Threads} stored in the repository.
     */
    List<Thread> loadAll();

    /**
     * Updates the given <code>thread</code> in the repository. The old version of the <code>thread</code>
     * should be already stored there.
     */
    void update(Thread thread);

    /**
     * Stores the given <code>thread</code> to the repository.
     * 
     * @return the generated identifier
     */
    int save(Thread thread);

    /**
     * Deletes the given <code>thread</code> from the repository.
     */
    void delete(Thread thread);
}
