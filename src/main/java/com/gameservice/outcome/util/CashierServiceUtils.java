package com.gameservice.outcome.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gameservice.outcome.constant.CashierServiceEndpoints;
import com.gameservice.outcome.constant.ResponseMessage;
import com.gameservice.outcome.dto.BalanceDto;
import com.gameservice.outcome.exception.CustomException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class CashierServiceUtils {

    public BalanceDto getBalance(Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = CashierServiceEndpoints.GET_BALANCE_URL + userId;
        Map<String, Object> responseMap;
        try {
            responseMap = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Map<String, Object>>() {
            }).getBody();
            if (responseMap == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new CustomException(ResponseMessage.CASHIER_SERVICE_FETCH_BALANCE_ERROR);
        }
        if (responseMap.get("success") != null && !((boolean) responseMap.get("success"))) {
            throw new CustomException(ResponseMessage.CASHIER_SERVICE_FETCH_BALANCE_ERROR);
        }
        return new ObjectMapper().convertValue(responseMap.get("body"), BalanceDto.class);
    }

    public void updateBalance(String requestBody) {
        RestTemplate restTemplate = new RestTemplate();
        String url = CashierServiceEndpoints.UPDATE_BALANCE_URL;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> entity = new HttpEntity<>(requestBody, httpHeaders);
        Map<String, Object> responseMap;
        try {
            responseMap = restTemplate.exchange(url, HttpMethod.PUT, entity, new ParameterizedTypeReference<Map<String, Object>>() {
            }).getBody();
            if (responseMap == null) {
                throw new Exception();
            }
        } catch (Exception e) {
            throw new CustomException(ResponseMessage.CASHIER_SERVICE_UPDATE_BALANCE_ERROR);
        }
        if (responseMap.get("success") != null && !((boolean) responseMap.get("success"))) {
            throw new CustomException(ResponseMessage.CASHIER_SERVICE_UPDATE_BALANCE_ERROR);
        }
    }

}
