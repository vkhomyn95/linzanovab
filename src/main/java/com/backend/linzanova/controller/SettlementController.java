package com.backend.linzanova.controller;

import com.backend.linzanova.entity.settlement.MethodProperties;
import com.backend.linzanova.entity.settlement.Settlement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class SettlementController {

    @GetMapping(value = "/api/settlements")
    public String getSettlementsByName(@RequestParam("name") String name) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.novaposhta.ua/v2.0/json/";
//        Create mapper to map Settlement and convert to object
        ObjectMapper mapper = new ObjectMapper();
        Settlement settlement = new Settlement(new MethodProperties(name,"",1, 10), "searchSettlements");
        String requestJson = mapper.writeValueAsString(settlement);
        System.out.println(requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);

        return answer;
    }

    @GetMapping(value = "/api/warehouses")
    public String getWarehouses(@RequestParam("city") String city) throws JsonProcessingException {
        System.out.println(city);
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.novaposhta.ua/v2.0/json/";
//        Create mapper to map Settlement and convert to object
        ObjectMapper mapper = new ObjectMapper();
        Settlement settlement = new Settlement(new MethodProperties("", city,1, 500), "getWarehouses");
        String requestJson = mapper.writeValueAsString(settlement);
        System.out.println(requestJson);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        String answer = restTemplate.postForObject(url, entity, String.class);
        System.out.println(answer);

        return answer;
    }
}
