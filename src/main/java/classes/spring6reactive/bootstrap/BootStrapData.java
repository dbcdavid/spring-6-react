package classes.spring6reactive.bootstrap;

import classes.spring6reactive.domain.Beer;
import classes.spring6reactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();

        beerRepository.count().subscribe(count -> {
            System.out.println("Count is: " + count);
        });
    }

    private void loadBeerData(){
        beerRepository.count().subscribe(count -> {
            if (count == 0) {
                Beer beer1 = Beer.builder()
                        .beerName("Galaxy Cat")
                        .beerStyle("PALE ALE")
                        .upc("12356")
                        .price(new BigDecimal("12.99"))
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .quantityOnHand(122)
                        .build();

                Beer beer2 = Beer.builder()
                        .beerName("Crank")
                        .beerStyle("PALE ALE")
                        .upc("12356222")
                        .price(new BigDecimal("11.99"))
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .quantityOnHand(392)
                        .build();

                Beer beer3 = Beer.builder()
                        .beerName("Sunshine City")
                        .beerStyle("IPA")
                        .upc("12356")
                        .price(new BigDecimal("13.99"))
                        .createdDate(LocalDateTime.now())
                        .lastModifiedDate(LocalDateTime.now())
                        .quantityOnHand(144)
                        .build();

                beerRepository.save(beer1).subscribe();
                beerRepository.save(beer2).subscribe();
                beerRepository.save(beer3).subscribe();
            }
        });
    }
}
