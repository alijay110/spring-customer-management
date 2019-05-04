package com.rcastro.customer.management.customermanagement.service;

import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository repository;

    @Autowired
    private ActivityProducer activityProducer;

    public List<Activity> findActivities(User user, Pageable pageable) {
        final List<Activity> activities = new ArrayList<>();
        Page<Activity> all = repository.findByUserId(user.getId(), pageable);
        if (all.getTotalElements() > 0) {
            activities.addAll(all.getContent());
        }
        return activities;
    }

    public void save(Activity activity) {
        repository.save(activity);
    }

}
