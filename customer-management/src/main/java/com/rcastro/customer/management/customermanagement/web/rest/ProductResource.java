package com.rcastro.customer.management.customermanagement.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rcastro.customer.management.customermanagement.domain.Product;
import com.rcastro.customer.management.customermanagement.event.ActivityProducer;
import com.rcastro.customer.management.customermanagement.service.ProductService;
import com.rcastro.customer.management.customermanagement.service.UserService;
import com.rcastro.customer.management.customermanagement.utils.Pagination;
import com.rcastro.customer.management.customermanagement.web.rest.dto.ProductApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductResource {

    private static Logger log = LoggerFactory.getLogger(ProductResource.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityProducer activityProducer;

    private Gson gson = new GsonBuilder().create();

    @PostMapping("/")
    public ResponseEntity<Void> addProduct(@RequestBody ProductApi productApi) {
        productService.save(new Product(productService.nextSequence(), productApi.getDescription(), productApi.getPrice()));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/")
    public ResponseEntity<Void> deleteProduct(@RequestParam(name = "id") Long id) {
        Optional<Product> item = productService.findById(id);

        if (item.isPresent()) {
            productService.remove(item.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<ProductApi>> listProducts(@Valid Pagination pagination) {


        List<ProductApi> productApis = new ArrayList<>();

        List<Product> products = productService.list(pagination.getPageable());

        if (!CollectionUtils.isEmpty(products)) {

            productApis = products.stream().map(product -> {
                ProductApi productApi = new ProductApi();
                productApi.setId(product.getId());
                productApi.setDescription(product.getDescription());
                productApi.setPrice(product.getPrice());
                return productApi;
            }).collect(Collectors.toList());
        }


        return new ResponseEntity<>(productApis, HttpStatus.OK);
    }


}
