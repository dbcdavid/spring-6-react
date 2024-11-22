package classes.spring6reactive.repositories;

import classes.spring6reactive.config.DatabaseConfig;
import classes.spring6reactive.domain.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(getTestCustomer()));
    }

    @Test
    void saveNewCustomer(){
        customerRepository.save(getTestCustomer()).subscribe(customer -> {
            System.out.println(customer.toString());
        });
    }

    Customer getTestCustomer(){
        return Customer.builder()
                .customerName("John Doe")
                .build();
    }
}