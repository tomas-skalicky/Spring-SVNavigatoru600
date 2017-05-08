package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class ForumThread implements Serializable, Comparable<ForumThread> {

    private static final long serialVersionUID = 1L;
    public static final String CLASS_FULL_NAME = "com.svnavigatoru600.domain.forum.ForumThread";

    private int id;
    private String name;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    private User author;
    private List<ForumContribution> contributions;

    /**
     * Sorts {@link ForumThread Threads} according to their last saved {@link ForumContribution Contributions} returned
     * by the {@link #getLastSavedContribution() getLastSavedContribution} method.
     */
    @Override
    public int compareTo(final ForumThread t) {
        final ForumContribution thisContribution = getLastSavedContribution();
        if (thisContribution == null) {
            return 1;
        }
        final ForumContribution tContribution = t.getLastSavedContribution();
        if (tContribution == null) {
            return -1;
        }

        // Sorts in the descending order; hence sign `-`.
        return -thisContribution.getLastSaveTime().compareTo(tContribution.getLastSaveTime());
    }

    /**
     * Gets the {@link ForumContribution} which has been saved the latest time among all {@link ForumContribution
     * Contributions} of this {@link ForumThread}.
     */
    public ForumContribution getLastSavedContribution() {
        ForumContribution lastSavedContribution = null;
        for (final ForumContribution contribution : contributions) {
            final boolean isBetter = (lastSavedContribution == null)
                    || (contribution.getLastSaveTime().after(lastSavedContribution.getLastSaveTime()));
            if (isBetter) {
                lastSavedContribution = contribution;
            }
        }
        return lastSavedContribution;
    }

    /**
     * Gets a {@link Map} which for each input {@link ForumThread} contains a {@link ForumContribution} which has the
     * highest {@link ForumContribution#getLastSaveTime() lastSaveTime} and which belongs to that thread. Moreover, the
     * method finds the author of such a contribution.
     */
    public static Map<ForumThread, ForumContribution> getLastSavedContributions(final List<ForumThread> threads) {
        final Map<ForumThread, ForumContribution> lastSavedContributions = new HashMap<>();

        for (final ForumThread thread : threads) {
            lastSavedContributions.put(thread, thread.getLastSavedContribution());
        }
        return lastSavedContributions;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(final Date creationTime) {
        this.creationTime = creationTime;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(final User author) {
        this.author = author;
    }

    public List<ForumContribution> getContributions() {
        return contributions;
    }

    public void setContributions(final List<ForumContribution> contributions) {
        this.contributions = contributions;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Thread [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", creationTime=");
        builder.append(creationTime);
        builder.append(", author=");
        builder.append(author);
        builder.append(", contributions=");
        builder.append(contributions);
        builder.append("]");
        return builder.toString();
    }

}
