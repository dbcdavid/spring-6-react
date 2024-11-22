package classes.spring6reactive.services;

import classes.spring6reactive.mappers.BeerMapper;
import classes.spring6reactive.model.BeerDTO;
import classes.spring6reactive.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerMapper mapper;
    private final BeerRepository beerRepository;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(mapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getBeerById(Integer id) {
        return beerRepository.findById(id).map(mapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(mapper.beerDTOToBeer(beerDTO)).map(mapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer id, BeerDTO beerDTO) {
        return beerRepository.findById(id)
                .map(foundBeer -> {
                    foundBeer.setBeerName(beerDTO.getBeerName());
                    foundBeer.setPrice(beerDTO.getPrice());
                    foundBeer.setBeerStyle(beerDTO.getBeerStyle());
                    foundBeer.setUpc(beerDTO.getUpc());
                    foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(mapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer id, BeerDTO beerDTO) {
        return beerRepository.findById(id)
                .map(foundBeer -> {
                    if (StringUtils.hasText(beerDTO.getBeerName()))
                        foundBeer.setBeerName(beerDTO.getBeerName());

                    if (beerDTO.getPrice() != null)
                        foundBeer.setPrice(beerDTO.getPrice());

                    if (StringUtils.hasText(beerDTO.getBeerStyle()))
                        foundBeer.setBeerStyle(beerDTO.getBeerStyle());

                    if (StringUtils.hasText(beerDTO.getUpc()))
                        foundBeer.setUpc(beerDTO.getUpc());

                    if (beerDTO.getQuantityOnHand() != null)
                        foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());

                    return foundBeer;
                }).flatMap(beerRepository::save)
                .map(mapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer id) {
        return beerRepository.deleteById(id);
    }
}
