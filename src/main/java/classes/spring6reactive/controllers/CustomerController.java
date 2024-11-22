package classes.spring6reactive.controllers;

import classes.spring6reactive.model.CustomerDTO;
import classes.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    final static String CUSTOMER_PATH = "/api/v2/customer";
    final static String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    private final CustomerService customerService;

    @DeleteMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> deleteExistingCustomer(@PathVariable("customerId") Integer customerId){
        customerService.deleteCustomerById(customerId).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> patchExistingCustomer(@PathVariable("customerId") Integer customerId,
                                           @Validated @RequestBody CustomerDTO customerDTO){
        customerService.patchCustomer(customerId, customerDTO).subscribe();

        return ResponseEntity.ok().build();
    }

    @PutMapping(CUSTOMER_PATH_ID)
    ResponseEntity<Void> updateExistingCustomer(@PathVariable("customerId") Integer customerId,
                                            @Validated @RequestBody CustomerDTO customerDTO){
        customerService.updateCustomer(customerId, customerDTO).subscribe();

        return ResponseEntity.noContent().build();
    }

    @PostMapping(CUSTOMER_PATH)
    ResponseEntity<Void> createNewCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        AtomicInteger atomicInteger = new AtomicInteger();

        customerService.saveNewCustomer(customerDTO).subscribe(savedDTO -> {
            atomicInteger.set(savedDTO.getId());
        });

        return ResponseEntity.created(UriComponentsBuilder
                        .fromHttpUrl("http://localhost:8080" + CUSTOMER_PATH + "/" + atomicInteger.get())
                        .build().toUri())
                .build();
    }

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable Integer customerId){
        log.info("Get customer by id {}", customerId);
        return customerService.getCustomerById(customerId);
    }
}
