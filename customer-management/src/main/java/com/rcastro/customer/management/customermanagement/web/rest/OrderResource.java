package com.rcastro.customer.management.customermanagement.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rcastro.customer.management.customermanagement.domain.*;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.CreditCardService;
import com.rcastro.customer.management.customermanagement.service.OrderService;
import com.rcastro.customer.management.customermanagement.service.ProductService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.utils.Pagination;
import com.rcastro.customer.management.customermanagement.web.rest.dto.CreditCardApi;
import com.rcastro.customer.management.customermanagement.web.rest.dto.ItemApi;
import com.rcastro.customer.management.customermanagement.web.rest.dto.OrderApi;
import com.rcastro.customer.management.customermanagement.web.rest.dto.ProductApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/order")
public class OrderResource {

    private static Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private ActivityProducer activityProducer;

    private Gson gson = new GsonBuilder().create();

    @PostMapping("/")
    public ResponseEntity<Void> create(@RequestHeader(name = "user-id") Long userId, @RequestBody @Valid OrderApi orderApi) throws JsonProcessingException {

        try {
            Optional<User> user = userService.findUser(userId);
            final Optional<CreditCard> creditCardOptional = creditCardService.findByCardNumber(orderApi.getCreditCard().getCardNumber());
            if (!user.isPresent() || !creditCardOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            List<Item> items = (List<Item>) orderApi.getItems().stream().map(itemApi -> {
                Optional<Product> product = productService.findById(itemApi.getProductApi().getId());

                if (product.isPresent()) {
                    Item item = new Item(product.get(), itemApi.getQuantity());
                    return Optional.ofNullable(item);
                }

                return Optional.empty();
            }).filter(item -> item.isPresent()).map(item -> item.get()).collect(Collectors.toList());

            final Order order = new Order(orderService.nextSequence(), user.get(), items, LocalDateTime.now(), creditCardOptional.get());
            orderService.save(order);
            activityProducer.producer(new Activity(user.get(), "User make an order:" + gson.toJson(orderApi), LocalDateTime.now()));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JsonProcessingException e) {
            log.error("Error to send event of activity");
        } catch (Exception e) {
            log.error("Generic error.");
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping
    public ResponseEntity<List<OrderApi>> get(@RequestHeader(name = "user-id") Long userId, @Valid Pagination pagination) {

        Optional<User> user = userService.findUser(userId);
        if (!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        final List<Order> orders = orderService.listOrdersByUser(user.get(), pagination.getPageable());
        if (!CollectionUtils.isEmpty(orders)) {
            final List<OrderApi> orderApis = orders.stream().map(order -> {
                OrderApi orderApi = new OrderApi();
                orderApi.setId(order.getId());
                orderApi.setUserId(order.getUser().getId());
                orderApi.setTotal(order.getTotal());

                CreditCardApi creditCardApi = new CreditCardApi();
                creditCardApi.setExpiration(order.getCreditCard().getExpiration());
                creditCardApi.setCardNumber(order.getCreditCard().getCardNumber());
                creditCardApi.setCvv(order.getCreditCard().getCvv());
                creditCardApi.setName(order.getCreditCard().getName());
                creditCardApi.setId(order.getCreditCard().getId());
                orderApi.setCreditCard(creditCardApi);

                orderApi.setItems(order.getItems().stream().map(item -> {
                    ItemApi itemApi = new ItemApi();
                    itemApi.setQuantity(item.getQuantity());

                    ProductApi productApi = new ProductApi();
                    productApi.setDescription(item.getProduct().getDescription());
                    productApi.setPrice(item.getProduct().getPrice());
                    productApi.setId(item.getProduct().getId());

                    itemApi.setProductApi(productApi);
                    return itemApi;
                }).collect(Collectors.toList()));

                return orderApi;
            }).collect(Collectors.toList());
            return new ResponseEntity<>(orderApis, HttpStatus.OK);
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }


}
