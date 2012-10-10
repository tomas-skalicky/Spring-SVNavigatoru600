package svnavigatoru.service.forum.threads;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DeleteThread {

	@PreAuthorize("hasPermission(#threadId, 'svnavigatoru.domain.forum.Thread', 'delete')")
	public void canDelete(int threadId) {
	}
}
