package classes.spring6reactive.repositories;

import classes.spring6reactive.config.DatabaseConfig;
import classes.spring6reactive.domain.Beer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

    @Autowired
    private BeerRepository beerRepository;

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(getTestBeer()));
    }

    @Test
    void saveNewBeer(){
        beerRepository.save(getTestBeer()).subscribe(beer -> {
            System.out.println(beer.toString());
        });
    }

    Beer getTestBeer(){
        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .upc("123456")
                .quantityOnHand(200)
                .price(new BigDecimal("22.90"))
                .build();
    }
}