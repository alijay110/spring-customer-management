package com.rcastro.customer.management.customermanagement.main.java.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.CustomerManagementApplication;
import com.rcastro.customer.management.customermanagement.domain.CreditCard;
import com.rcastro.customer.management.customermanagement.domain.User;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.CreditCardService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.web.rest.UserResource;
import com.rcastro.customer.management.customermanagement.web.rest.dto.CreditCardApi;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UserResource.class)
@ContextConfiguration(classes = CustomerManagementApplication.class)
public class UserResourceTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected UserService userService;

    @MockBean
    private CreditCardService creditCardService;

    @MockBean
    private ActivityProducer activityProducer;

    @Test
    public void shouldCreateUser() throws Exception {
        // given
        String userBody = "{\"name\":\"Niko\", \"birthday\":\"2001-05-03\"}";
        User user = createUser("Niko", LocalDate.of(2001, 05, 03));

        // when
        when(userService.create("Niko", LocalDate.of(2001, 05, 03))).thenReturn(user);

        // then
        mockMvc.perform(post("/api/user/")
                .content(userBody)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin", password = "adminPass")
    public void shouldReturnFoundUser() throws Exception {

        // given
        User user = createUser("Niko", LocalDate.of(2000,05,02));

        // when
        when(userService.findUser(1l)).thenReturn(Optional.of(user));

        // then
        String contentAsString = mockMvc.perform(get("/api/user/id/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(contentAsString).isEqualTo("{\"id\":1,\"name\":\"Niko\",\"birthday\":\"2000-05-02\",\"wallet\":null}");

    }

    @Test
    public void shouldAddCreditCardToUser() throws Exception {

        // given
        User user = createUser("Niko", LocalDate.of(2000,05,02));
        CreditCardApi creditCardApi = new CreditCardApi();
        creditCardApi.setCardNumber("5566666");
        creditCardApi.setCvv("111");
        creditCardApi.setExpiration(LocalDate.of(2040,05,1));
        creditCardApi.setName("Niko");
        creditCardApi.setId(1l);

        List<CreditCardApi> wallet = new ArrayList<>();
        wallet.add(creditCardApi);
        String requestBody = mapper.writeValueAsString(wallet);

        // When
        when(userService.findUser(1l)).thenReturn(Optional.of(user));

        // then
        mockMvc.perform(put("/api/user/wallet/add")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .header("user-id",1l).content(requestBody))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void shouldDeleteCreditCardToUser() throws Exception {

        // given
        User user = createUser("Niko", LocalDate.of(2000,05,02));
        CreditCard creditCard = createCreditCard();

        List<CreditCard> wallet = new ArrayList<>();
        wallet.add(creditCard);
        user.setWallet(wallet);

        // When
        when(userService.findUser(1l)).thenReturn(Optional.of(user));

        // then
        mockMvc.perform(put("/api/user/wallet/id/1/remove")
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
                .header("user-id",1l))
                .andExpect(status().isOk());
    }

    private CreditCard createCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setCardNumber("5566666");
        creditCard.setCvv("111");
        creditCard.setExpiration(LocalDate.of(2040,05,1));
        creditCard.setName("Niko");
        creditCard.setId(1l);
        return creditCard;
    }

    private User createUser(String name, LocalDate birthday) {
        User user = new User();
        user.setName(name);
        user.setId(1l);
        user.setBirthday(birthday);
        return user;
    }
}
