package com.rcastro.customer.management.customermanagement.main.java.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.CustomerManagementApplication;
import com.rcastro.customer.management.customermanagement.domain.*;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.CreditCardService;
import com.rcastro.customer.management.customermanagement.service.OrderService;
import com.rcastro.customer.management.customermanagement.service.ProductService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.web.rest.OrderResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = OrderResource.class)
@ContextConfiguration(classes = CustomerManagementApplication.class)
public class OrderResourceTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected UserService userService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ActivityProducer activityProducer;

    @MockBean
    private CreditCardService creditCardService;


    @Test
    public void shouldAddOrder() throws Exception {
        // given
        String body = "{\"userId\":\"1\",\"items\":[{\"productApi\":{\"id\":1},\"quantity\":2}],\"creditCard\":{\"cardNumber\":\"4000806378397691\"}}\\r\\n";
        Product product = createProduct("Bag", new BigDecimal(15));
        User user = createUser("Niko", LocalDate.of(2000, 03, 5));
        CreditCard creditCard = createCreditCard();

        // when
        when(productService.findById(1l)).thenReturn(Optional.of(product));
        when(userService.findUser(1l)).thenReturn(Optional.of(user));
        when(creditCardService.findByCardNumber("4000806378397691")).thenReturn(Optional.of(creditCard));

        // then
        mockMvc.perform(post("/api/order/")
                .header("user-id", 1l)
                .content(body)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    public void shouldReturnOrder() throws Exception {
        // given
        User user = createUser("Niko", LocalDate.of(2000, 03, 5));
        CreditCard creditCard = createCreditCard();

        Item item = new Item(createProduct("Bag", new BigDecimal(15)),10);
        List<Item> items = new ArrayList<>();
        Order order = new Order(1l, user,items, LocalDateTime.now(), creditCard);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        // when
        when(userService.findUser(1l)).thenReturn(Optional.of(user));

        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "_id"));
        when(orderService.listOrdersByUser(user,pageable)).thenReturn(orders);

        // then
        String contentAsString = mockMvc.perform(get("/api/order")
                .header("user-id", 1l)
                .param("size", "10")
                .param("page", "0")
                .param("sort", "DESC")
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        Assertions.assertThat(contentAsString).isEqualTo("[{\"id\":1,\"userId\":1,\"items\":[],\"creditCard\":{\"id\":1,\"cardNumber\":\"************7691\",\"cvv\":\"***\",\"name\":\"Niko\",\"expiration\":\"2040-05-01\",\"valid\":false},\"total\":0}]");
    }

    private Product createProduct(String description, BigDecimal price) {
        return new Product(1l, description, price);
    }

    private User createUser(String name, LocalDate birthday) {
        User user = new User();
        user.setName(name);
        user.setId(1l);
        user.setBirthday(birthday);
        return user;
    }

    private CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("4000806378397691");
        creditCard.setCvv("111");
        creditCard.setExpiration(LocalDate.of(2040,05,1));
        creditCard.setName("Niko");
        creditCard.setId(1l);
        return creditCard;
    }

}
