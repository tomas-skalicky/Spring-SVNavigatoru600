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
     * Returns a {@link Thread} stored in the repository which has the given ID.
     */
    Thread findById(int threadId);

    /**
     * Returns all {@link Thread Threads} stored in the repository.
     */
    List<Thread> loadAll();

    /**
     * Updates the given {@link Thread} in the repository. The old version of this thread should be already
     * stored there.
     */
    void update(Thread thread);

    /**
     * Stores the given {@link Thread} to the repository.
     * 
     * @return The new ID of the given {@link Thread} generated by the repository
     */
    int save(Thread thread);

    /**
     * Deletes the given {@link Thread} from the repository.
     */
    void delete(Thread thread);
}
