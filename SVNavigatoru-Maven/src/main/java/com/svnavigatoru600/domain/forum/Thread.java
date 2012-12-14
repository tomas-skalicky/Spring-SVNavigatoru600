package com.svnavigatoru600.domain.forum;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.svnavigatoru600.domain.users.User;
import com.svnavigatoru600.repository.forum.ThreadDao;

public class Thread implements Serializable, Comparable<Thread> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8105812445594624080L;

    @SuppressWarnings("unused")
    private ThreadDao threadDao;

    public void setThreadDao(ThreadDao threadDao) {
        this.threadDao = threadDao;
    }

    private int id;
    private String name;
    @DateTimeFormat(style = "LS")
    private Date creationTime;
    private User author;
    private List<Contribution> contributions;

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
     * Gets the {@link Contribution} which has been saved the latest time among all {@link Contribution}s of
     * this {@link Thread}.
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
     * Sorts according to the last saved {@link Contribution}s returned by the method
     * <code>getLastSavedContribution</code>.
     */
    public int compareTo(Thread t) {
        Contribution thisContribution = this.getLastSavedContribution();
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
}
