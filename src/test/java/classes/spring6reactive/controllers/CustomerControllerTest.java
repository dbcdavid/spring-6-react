package classes.spring6reactive.controllers;

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
    void testUpdateCustomerNotFound() {
        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, -1)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(2)
    void testUpdateCustomerBadRequest() {
        CustomerDTO customerDTO = getTestCustomer();
        customerDTO.setCustomerName("");

        webClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testPatchCustomerNotFound() {
        webClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, -1)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(3)
    void testListCustomers(){
        webClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(4)
    void testCreateCustomer() {
        webClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(getTestCustomer()), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    void testCreateCustomerBadRequest() {
        CustomerDTO customerDTO = getTestCustomer();
        customerDTO.setCustomerName("");

        webClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(customerDTO), CustomerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @Order(5)
    void testGetCustomerById(){
        webClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    void testGetCustomerByIdNotFound(){
        webClient.get().uri(CustomerController.CUSTOMER_PATH_ID, -1)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @Order(999)
    void testDeleteCustomer() {
        webClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void testDeleteCustomerNotFound() {
        webClient.delete().uri(CustomerController.CUSTOMER_PATH_ID, -1)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNotFound();
    }

    CustomerDTO getTestCustomer(){
        return CustomerDTO.builder()
                .customerName("John Doe")
                .build();
    }
}