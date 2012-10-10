package svnavigatoru.service.forum.contributions;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class DeleteContribution {

	@PreAuthorize("hasPermission(#contributionId, 'svnavigatoru.domain.forum.Contribution', 'delete')")
	public void canDelete(int contributionId) {
	}
}
