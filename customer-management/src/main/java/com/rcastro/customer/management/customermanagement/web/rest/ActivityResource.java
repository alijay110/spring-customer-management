package com.rcastro.customer.management.customermanagement.web.rest;

import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.service.ActivityService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.utils.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activity")
public class ActivityResource {

    private static Logger log = LoggerFactory.getLogger(ActivityResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public ResponseEntity<List<Activity>> get(@RequestHeader(name = "user-id") Long userId, @Valid Pagination pagination) {

        Optional<User> user = userService.findUser(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final List<Activity> activities = activityService.findActivities(user.get(), pagination.getPageable());
        if (!CollectionUtils.isEmpty(activities)) {
            return new ResponseEntity<>(activities, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


}
