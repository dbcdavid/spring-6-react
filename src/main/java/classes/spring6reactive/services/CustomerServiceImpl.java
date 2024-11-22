package classes.spring6reactive.services;

import classes.spring6reactive.mappers.CustomerMapper;
import classes.spring6reactive.model.CustomerDTO;
import classes.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper mapper;

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll().map(mapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer id) {
        return customerRepository.findById(id).map(mapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(mapper.customerDTOToCustomer(customerDTO)).map(mapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    foundCustomer.setCustomerName(customerDTO.getCustomerName());
                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(mapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId)
                .map(foundCustomer -> {
                    if (StringUtils.hasText(customerDTO.getCustomerName()))
                        foundCustomer.setCustomerName(customerDTO.getCustomerName());

                    return foundCustomer;
                }).flatMap(customerRepository::save)
                .map(mapper::customerToCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(Integer id) {
        return customerRepository.deleteById(id);
    }
}
