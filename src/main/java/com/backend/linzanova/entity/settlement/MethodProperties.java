package com.backend.linzanova.entity.settlement;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MethodProperties {
    @JsonProperty("CityName")
    private String CityName;
    @JsonProperty("CityRef")
    private String CityRef;
    @JsonProperty("Page")
    private int Page;
    @JsonProperty("Limit")
    private int Limit;

    public MethodProperties(String cityName, String cityRef, int page, int limit) {
        CityRef = cityRef;
        CityName = cityName;
        Page = page;
        Limit = limit;
    }
}
