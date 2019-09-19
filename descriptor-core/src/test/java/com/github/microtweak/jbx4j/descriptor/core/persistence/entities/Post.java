package com.github.microtweak.jbx4j.descriptor.core.persistence.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Post extends BaseEntity {

    private String title;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postedAt;

    @ManyToMany
    @JoinTable(name = "post_tag")
    private Set<Tag> tags = new LinkedHashSet<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(Date postedAt) {
        this.postedAt = postedAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }
}
