package com.github.microtweak.jbx4j.descriptor.persistence.entities;

import javax.persistence.Entity;

@Entity
public class Country extends BaseEntity {

    private String name;

    private String language;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

}
