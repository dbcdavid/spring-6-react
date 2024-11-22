package classes.spring6reactive.mappers;

import classes.spring6reactive.domain.Beer;
import classes.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    BeerDTO beerToBeerDTO(Beer beer);

    Beer beerDTOToBeer(BeerDTO beerDTO);
}
