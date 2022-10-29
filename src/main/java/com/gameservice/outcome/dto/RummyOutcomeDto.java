package com.gameservice.outcome.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

import java.util.List;

@Data
public class RummyOutcomeDto {

    private List<RummyGenerateOutcomeDto> scoreBoard;
    private EnumConstants.SPLIT_TYPE splitType = EnumConstants.SPLIT_TYPE.NONE;

}
