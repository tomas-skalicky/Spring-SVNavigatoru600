package com.svnavigatoru600.repository.forum;

import java.util.List;

import com.svnavigatoru600.domain.forum.Thread;

public interface ThreadDao {

    /**
     * Returns a {@link Thread} stored in the repository which has the given <code>ID</code>.
     */
    public Thread findById(int threadId);

    /**
     * Returns all {@link Thread}s stored in the repository.
     */
    public List<Thread> loadAll();

    /**
     * Updates the given <code>thread</code> in the repository. The old version of the <code>thread</code>
     * should be already stored there.
     */
    public void update(Thread thread);

    /**
     * Stores the given <code>thread</code> to the repository.
     * 
     * @return the generated identifier
     */
    public int save(Thread thread);

    /**
     * Deletes the given <code>thread</code> from the repository.
     */
    public void delete(Thread thread);
}
