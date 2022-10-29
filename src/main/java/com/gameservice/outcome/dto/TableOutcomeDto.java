package com.gameservice.outcome.dto;

import com.gameservice.outcome.constant.EnumConstants;
import lombok.Data;

@Data
public class TableOutcomeDto extends RummyOutcomeDto {

    private EnumConstants.OUTCOME_TYPE outcomeType;

}
