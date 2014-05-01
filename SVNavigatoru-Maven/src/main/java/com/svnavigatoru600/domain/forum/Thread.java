package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.service.forum.ThreadService;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class Thread implements Serializable, Comparable<Thread> {

    private static final long serialVersionUID = -8105812445594624080L;

    @SuppressWarnings("unused")
    private ThreadService threadService;

    @Inject
    public void setThreadService(ThreadService threadService) {
        this.threadService = threadService;
    }

    private int id;
    private String name;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    private User author;
    private List<Contribution> contributions;

    /**
     * Initialises no property.
     */
    public Thread() {
    }

    /**
     * Initialises thread's name, author and contributions. Other properties are not touched.
     */
    public Thread(String name, User author, List<Contribution> contributions) {
        this.name = name;
        this.author = author;
        this.contributions = contributions;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationTime() {
        return this.creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public User getAuthor() {
        return this.author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Contribution> getContributions() {
        return this.contributions;
    }

    public void setContributions(List<Contribution> contributions) {
        this.contributions = contributions;
    }

    /**
     * Sorts {@link Thread Threads} according to their last saved {@link Contribution Contributions} returned
     * by the {@link #getLastSavedContribution() getLastSavedContribution} method.
     */
    @Override
    public int compareTo(Thread t) {
        Contribution thisContribution = getLastSavedContribution();
        if (thisContribution == null) {
            return 1;
        }
        Contribution tContribution = t.getLastSavedContribution();
        if (tContribution == null) {
            return -1;
        }

        // Sorts in the descending order; hence sign `-`.
        return -thisContribution.getLastSaveTime().compareTo(tContribution.getLastSaveTime());
    }

    /**
     * Gets the {@link Contribution} which has been saved the latest time among all {@link Contribution
     * Contributions} of this {@link Thread}.
     */
    public Contribution getLastSavedContribution() {
        Contribution lastSavedContribution = null;
        for (Contribution contribution : this.contributions) {
            boolean isBetter = (lastSavedContribution == null)
                    || (contribution.getLastSaveTime().after(lastSavedContribution.getLastSaveTime()));
            if (isBetter) {
                lastSavedContribution = contribution;
            }
        }
        return lastSavedContribution;
    }

    /**
     * Gets a {@link Map} which for each input {@link Thread} contains a {@link Contribution} which has the
     * highest {@link Contribution#getLastSaveTime() lastSaveTime} and which belongs to that thread. Moreover,
     * the method finds the author of such a contribution.
     */
    public static Map<Thread, Contribution> getLastSavedContributions(List<Thread> threads) {
        Map<Thread, Contribution> lastSavedContributions = new HashMap<Thread, Contribution>();

        for (Thread thread : threads) {
            lastSavedContributions.put(thread, thread.getLastSavedContribution());
        }
        return lastSavedContributions;
    }

    @Override
    public String toString() {
        return new StringBuilder("[id=").append(this.id).append(", name=").append(this.name)
                .append(", creationTime=").append(this.creationTime).append(", author=").append(this.author)
                .append("]").toString();
    }

    private Thread(Builder builder) {
        this.threadService = builder.threadService;
        this.id = builder.id;
        this.name = builder.name;
        this.creationTime = builder.creationTime;
        this.author = builder.author;
        this.contributions = builder.contributions;
    }

    /**
     * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
     */
    public static class Builder {

        private ThreadService threadService;
        private int id;
        private String name;
        private Date creationTime;
        private User author;
        private List<Contribution> contributions;

        public Builder withThreadService(ThreadService threadService) {
            this.threadService = threadService;
            return this;
        }

        public Builder withId(int id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withCreationTime(Date creationTime) {
            this.creationTime = creationTime;
            return this;
        }

        public Builder withAuthor(User author) {
            this.author = author;
            return this;
        }

        public Builder withContributions(List<Contribution> contributions) {
            this.contributions = contributions;
            return this;
        }

        public Thread build() {
            return new Thread(this);
        }
    }
}
