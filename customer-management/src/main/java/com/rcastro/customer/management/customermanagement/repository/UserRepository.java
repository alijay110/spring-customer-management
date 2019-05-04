package com.rcastro.customer.management.customermanagement.repository;

import com.rcastro.customer.management.customermanagement.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long>{

}

