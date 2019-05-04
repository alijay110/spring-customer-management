package com.rcastro.customer.management.customermanagement.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rcastro.customer.management.customermanagement.domain.Activity;
import com.rcastro.customer.management.customermanagement.domain.CreditCard;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.CreditCardService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.utils.Pagination;
import com.rcastro.customer.management.customermanagement.web.rest.dto.CreditCardApi;
import com.rcastro.customer.management.customermanagement.web.rest.dto.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    private UserService userService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private ActivityProducer activityProducer;


    @PutMapping("/wallet/add")
    public ResponseEntity<Void> add(@RequestHeader(name = "user-id") Long userId,
                                    @RequestBody @Valid List<CreditCardApi> wallet) throws JsonProcessingException {

        Optional<User> user = userService.findUser(userId);
        if (!user.isPresent() || wallet.stream().anyMatch(c -> !c.isValid())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.isPresent()) {
            long count = CollectionUtils.isEmpty(user.get().getWallet()) ? 0 : user.get().getWallet().stream().count();
            StringBuilder messageCreditCard = new StringBuilder();
            List<CreditCard> creditCards = wallet.stream().map(w -> {

                Optional<CreditCard> result = creditCardService.findByCardNumber(w.getCardNumber());
                if (result.isPresent()) {
                    return result.get();
                }

                final CreditCard creditCard = new CreditCard();
                creditCard.setId(count + 1);
                creditCard.setCardNumber(w.getCardNumber());
                creditCard.setName(w.getName());
                creditCard.setCvv(w.getCvv());
                creditCard.setExpiration(w.getExpiration());
                creditCardService.save(creditCard);
                messageCreditCard.append(creditCard.getCardNumber());
                messageCreditCard.append(creditCard.getCvv());
                messageCreditCard.append(creditCard.getName());
                return creditCard;
            }).collect(Collectors.toList());
            user.get().setWallet(creditCards);

            activityProducer.producer(new Activity(user.get(), "User has added credit cards to wallet:" + messageCreditCard, LocalDateTime.now()));

            userService.update(user.get());
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/wallet/id/{id}/remove")
    public ResponseEntity<Void> remove(@RequestHeader(name = "user-id") Long userId,
                                       @PathVariable Long id) throws JsonProcessingException {

        Optional<User> user = userService.findUser(userId);
        if (user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (user.isPresent()) {
            Optional<CreditCard> creditCard = user.get().getWallet().stream().filter(w -> w.getId().equals(id)).findFirst();
            if (creditCard.isPresent()) {
                user.get().getWallet().remove(creditCard.get());
                userService.update(user.get());
                activityProducer.producer(new Activity(user.get(), "User has added credit cards to wallet:" + creditCard.get().toString(), LocalDateTime.now()));
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createUser(@RequestBody UserApi userApi) throws JsonProcessingException {
        userService.create(userApi.getName(), userApi.getBirthday());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserApi> getUser(@PathVariable(name = "id") Long id) {
        Optional<User> user = userService.findUser(id);

        if (user.isPresent()) {
            final UserApi userApi = new UserApi();
            userApi.setId(user.get().getId());
            userApi.setBirthday(user.get().getBirthday());
            userApi.setName(user.get().getName());
            userApi.setWallet(user.get().getWallet().stream().map(w -> {
                CreditCardApi creditCardApi = new CreditCardApi();
                creditCardApi.setId(w.getId());
                creditCardApi.setCardNumber(w.getCardNumber());
                creditCardApi.setName(w.getName());
                creditCardApi.setCvv(w.getCvv());
                creditCardApi.setExpiration(w.getExpiration());
                return creditCardApi;
            }).collect(Collectors.toList()));

            return new ResponseEntity<>(userApi, HttpStatus.OK);

        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<UserApi>> listUsers(@Valid Pagination pagination) {
        List<User> users = userService.list(pagination.getPageable());

        List<UserApi> userApis = new ArrayList<>();
        if (!CollectionUtils.isEmpty(users)) {

            userApis = users.stream().map(user -> {
                final UserApi userApi = new UserApi();
                userApi.setId(user.getId());
                userApi.setBirthday(user.getBirthday());
                userApi.setName(user.getName());

                if (!CollectionUtils.isEmpty(user.getWallet())) {
                    userApi.setWallet(user.getWallet().stream().map(w -> {
                        CreditCardApi creditCardApi = new CreditCardApi();
                        creditCardApi.setId(w.getId());
                        creditCardApi.setCardNumber(w.getCardNumber());
                        creditCardApi.setName(w.getName());
                        creditCardApi.setCvv(w.getCvv());
                        creditCardApi.setExpiration(w.getExpiration());
                        return creditCardApi;
                    }).collect(Collectors.toList()));
                }
                return userApi;
            }).collect(Collectors.toList());
        }

        return new ResponseEntity<>(userApis, HttpStatus.OK);
    }


}
