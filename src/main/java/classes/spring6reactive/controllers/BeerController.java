package classes.spring6reactive.controllers;

import classes.spring6reactive.model.BeerDTO;
import classes.spring6reactive.services.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
@AllArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    public final BeerService beerService;

    @DeleteMapping(BEER_PATH_ID)
    ResponseEntity<Void> deleteExistingBeer(@PathVariable("beerId") Integer beerId){
        beerService.deleteBeerById(beerId).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(BEER_PATH_ID)
    ResponseEntity<Void> patchExistingBeer(@PathVariable("beerId") Integer beerId,
                                            @Validated @RequestBody BeerDTO beerDTO){
        beerService.patchBeer(beerId, beerDTO).subscribe();

        return ResponseEntity.ok().build();
    }

    @PutMapping(BEER_PATH_ID)
    ResponseEntity<Void> updateExistingBeer(@PathVariable("beerId") Integer beerId,
                                            @Validated @RequestBody BeerDTO beerDTO){
        beerService.updateBeer(beerId, beerDTO).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PostMapping(BEER_PATH)
    ResponseEntity<Void> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {
        AtomicInteger atomicInteger = new AtomicInteger();

        beerService.saveNewBeer(beerDTO).subscribe(savedDTO -> {
            atomicInteger.set(savedDTO.getId());
        });

        return ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl("http://localhost:8080" + BEER_PATH + "/" + atomicInteger.get())
                        .build().toUri())
                .build();
    }

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable Integer beerId){
        log.info("Get beer by id {}", beerId);
        return beerService.getBeerById(beerId);
    }
}
