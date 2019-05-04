package com.rcastro.customer.management.customermanagement.repository;

import com.rcastro.customer.management.customermanagement.domain.CreditCard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CreditCardRepository extends MongoRepository<CreditCard, Long>{
    CreditCard findFirstByCardNumber(String cardNumber);
}

