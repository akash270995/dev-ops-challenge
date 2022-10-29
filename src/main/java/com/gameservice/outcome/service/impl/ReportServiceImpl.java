package com.gameservice.outcome.service.impl;

import com.gameservice.outcome.dto.PageRequestDto;
import com.gameservice.outcome.repository.GameRepository;
import com.gameservice.outcome.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;

@Transactional
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public Long getTotalRake(PageRequestDto pageRequestDto) {
        int[] fromDateArr = Arrays.stream(pageRequestDto.getFromDate().split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
        int[] toDateArr = Arrays.stream(pageRequestDto.getToDate().split("-"))
                .mapToInt(Integer::parseInt)
                .toArray();
        LocalDateTime fromDate = LocalDateTime.of(fromDateArr[0], fromDateArr[1], fromDateArr[2], fromDateArr[3], fromDateArr[4], fromDateArr[5]);
        LocalDateTime toDate = LocalDateTime.of(toDateArr[0], toDateArr[1], toDateArr[2], toDateArr[3], toDateArr[4], toDateArr[5]);
        return gameRepository.totalRake(fromDate, toDate);
    }

}
