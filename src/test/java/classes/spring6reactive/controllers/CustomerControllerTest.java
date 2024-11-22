package classes.spring6reactive.controllers;

import classes.spring6reactive.domain.Customer;
import classes.spring6reactive.model.CustomerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {
    @Autowired
    WebTestClient webClient;

    @Test
    @Order(1)
    void testUpdateCustomer() {
        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(2)
    void testListCustomers(){
        webClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(3)
    void testCreateCustomer() {
        webClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(4)
    void testGetCustomerById(){
        webClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(999)
    void testDeleteCustomer() {
        webClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    Customer getTestCustomer(){
        return Customer.builder()
                .customerName("John Doe")
                .build();
    }
}