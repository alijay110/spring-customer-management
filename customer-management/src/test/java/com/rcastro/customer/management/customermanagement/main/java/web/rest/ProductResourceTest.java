package com.rcastro.customer.management.customermanagement.main.java.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rcastro.customer.management.customermanagement.CustomerManagementApplication;
import com.rcastro.customer.management.customermanagement.domain.Product;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.ProductService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.web.rest.ProductResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProductResource.class)
@ContextConfiguration(classes = CustomerManagementApplication.class)
public class ProductResourceTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected ProductService productService;

    @MockBean
    protected UserService userService;

    @MockBean
    private ActivityProducer activityProducer;

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin", password = "adminPass")
    public void shouldAddProduct() throws Exception {
        // given
        String body = "{\"description\":\"Bag\", \"price\":\"15.0\"}";
        Product product = createProduct("Bag", new BigDecimal(15));

        // when
        when(productService.nextSequence()).thenReturn(1l);

        // then
        mockMvc.perform(post("/api/product/")
                .content(body)
                .contentType(APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }


    @Test
    @WithMockUser(roles = "ADMIN", username = "admin", password = "adminPass")
    public void shouldReturnFoundProduct() throws Exception {

        // given
        Product product = createProduct("Bag", new BigDecimal(15));
        List<Product> products = new ArrayList<>();
        products.add(product);

        // when
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "_id"));
        when(productService.list(pageable)).thenReturn(products);

        // then
        String contentAsString = mockMvc.perform(get("/api/product")
                .accept(MediaType.APPLICATION_JSON)
                .param("size", "10")
                .param("page", "0")
                .param("sort", "DESC")
        )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Assertions.assertThat(contentAsString).isEqualTo("[{\"id\":1,\"description\":\"Bag\",\"price\":15}]");
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin", password = "adminPass")
    public void shouldDeleteProduct() throws Exception {

        // given
        Product product = createProduct("Bag", new BigDecimal(15));
        // when
        when(productService.findById(1l)).thenReturn(Optional.of(product));

        // then
        mockMvc.perform(delete("/api/product/")
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "1"))
                .andExpect(status().isOk());

    }

    private Product createProduct(String description, BigDecimal price) {
        return new Product(1l, description, price);
    }
}
