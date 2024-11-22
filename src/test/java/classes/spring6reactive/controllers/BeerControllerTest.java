package classes.spring6reactive.controllers;

import classes.spring6reactive.model.BeerDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    @Autowired
    WebTestClient webClient;

    @Test
    @Order(1)
    void testUpdateBeer() {
        webClient.put().uri(BeerController.BEER_PATH_ID, 1)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    @Order(2)
    void testListBeers(){
        webClient.get().uri(BeerController.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody().jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(3)
    void testCreateBeer() {
        webClient.post().uri(BeerController.BEER_PATH)
                .body(Mono.just(getTestBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().location("http://localhost:8080/api/v2/beer/4");
    }

    @Test
    @Order(4)
    void testGetBeerById(){
        webClient.get().uri(BeerController.BEER_PATH_ID, 1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody(BeerDTO.class);
    }

    @Test
    @Order(999)
    void testDeleteBeer() {
        webClient.delete().uri(BeerController.BEER_PATH_ID, 1)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    BeerDTO getTestBeer(){
        return BeerDTO.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .upc("123456")
                .quantityOnHand(200)
                .price(new BigDecimal("22.90"))
                .build();
    }
}