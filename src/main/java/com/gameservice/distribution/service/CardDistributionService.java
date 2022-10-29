package com.gameservice.distribution.service;

import com.gameservice.distribution.dto.CardsDistributedDto;
import com.gameservice.distribution.dto.CardsDistributionConfigDto;

public interface CardDistributionService {

    CardsDistributedDto distributeCards(CardsDistributionConfigDto cardsDistributionConfigDto);

}
