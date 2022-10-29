package com.gameservice.outcome.service.impl;

import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.DealsDetailsDto;
import com.gameservice.outcome.dto.DealsSaveDto;
import com.gameservice.outcome.exception.CustomException;
import com.gameservice.outcome.model.DealsModel;
import com.gameservice.outcome.repository.DealsRepository;
import com.gameservice.outcome.service.DealsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealsServiceImpl implements DealsService {

    @Autowired
    private DealsRepository dealsRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public DealsDetailsDto saveDeal(DealsSaveDto dealsSaveDto) {
        DealsModel dealsModel = modelMapper.map(dealsSaveDto, DealsModel.class);
        DealsModel savedDealModel = dealsRepository.save(dealsModel);
        return modelMapper.map(savedDealModel, DealsDetailsDto.class);
    }

    @Override
    public DealsDetailsDto getDealDetails(Long id) {
        Optional<DealsModel> dealsObj = dealsRepository.findById(id);
        if (dealsObj.isPresent()) {
            DealsModel dealsModel = dealsObj.get();
            return modelMapper.map(dealsModel, DealsDetailsDto.class);
        } else {
            throw new CustomException(ResponseMessage.DEALS_FETCH_ERROR);
        }
    }
}
