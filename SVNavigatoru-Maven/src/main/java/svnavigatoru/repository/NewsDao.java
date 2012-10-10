package svnavigatoru.repository;

import java.util.List;

import svnavigatoru.domain.News;
import svnavigatoru.service.util.OrderType;

public interface NewsDao {

	/**
	 * Returns a {@link News} stored in the repository which has the given
	 * <code>ID</code>.
	 */
	public News findById(int newsId);

	/**
	 * Returns all {@link News} stored in the repository arranged according to
	 * their values of the <code>attribute</code> in the given
	 * <code>order</code>.
	 */
	public List<News> findOrdered(String attribute, OrderType order);

	/**
	 * Updates the given <code>news</code> in the repository. The old version of
	 * the <code>news</code> should be already stored there.
	 */
	public void update(News news);

	/**
	 * Stores the given <code>news</code> to the repository.
	 * 
	 * @return the generated identifier
	 */
	public int save(News news);

	/**
	 * Deletes the given <code>news</code> from the repository.
	 */
	public void delete(News news);
}
