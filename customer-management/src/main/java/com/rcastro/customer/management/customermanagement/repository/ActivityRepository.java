package com.rcastro.customer.management.customermanagement.repository;

import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface  ActivityRepository extends MongoRepository<Activity, Long>{

    Page<Activity> findByUserId(Long userId, Pageable pageable);


}

