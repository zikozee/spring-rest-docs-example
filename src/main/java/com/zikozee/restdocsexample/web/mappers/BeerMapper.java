package com.zikozee.restdocsexample.web.mappers;

import com.zikozee.restdocsexample.domain.Beer;
import com.zikozee.restdocsexample.web.model.BeerDto;
import org.mapstruct.Mapper;

/**
 * Created by jt on 2019-05-25.
 */
@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto BeerToBeerDto(Beer beer);

    Beer BeerDtoToBeer(BeerDto dto);
}
