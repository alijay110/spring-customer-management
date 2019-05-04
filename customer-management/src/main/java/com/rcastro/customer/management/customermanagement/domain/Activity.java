package com.rcastro.customer.management.customermanagement.domain;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document
public class Activity {

    @Field
    private final User user;

    @Field
    private final String description;

    @Field
    private final LocalDateTime created;

    public Activity(User user, String description, LocalDateTime created) {
        this.user = user;
        this.description = description;
        this.created = created;
    }

    public User getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "user=" + user +
                ", description='" + description + '\'' +
                ", created=" + created +
                '}';
    }
}
