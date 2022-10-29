package com.gameservice.outcome.service;

import com.gameservice.outcome.dto.DealsDetailsDto;
import com.gameservice.outcome.dto.DealsSaveDto;

public interface DealsService {

    DealsDetailsDto saveDeal(DealsSaveDto dealsSaveDto);

    DealsDetailsDto getDealDetails(Long id);

}
