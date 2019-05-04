package com.rcastro.customer.management.customermanagement.service;

import com.rcastro.customer.management.customermanagement.domain.CreditCard;
import com.rcastro.customer.management.customermanagement.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository repository;

    public Long nextSequence() {
        List<CreditCard> all = repository.findAll();
        int next = 1;
        if (!CollectionUtils.isEmpty(all)) {
            next = all.size() + 1;
        }
        return Long.valueOf(next);
    }


    public Optional<CreditCard> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<CreditCard> findByCardNumber(String cardNumber) {
        return Optional.ofNullable(repository.findFirstByCardNumber(cardNumber));
    }

    public void save(CreditCard creditCard) {
        repository.save(creditCard);
    }

    public void remove(CreditCard creditCard) {
        repository.delete(creditCard);
    }
}
