package com.backend.linzanova.entity.settlement;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Settlement {
    private String modelName = "Address";
    private String calledMethod;
    private MethodProperties methodProperties;
    private String apiKey = "4584049bf3694c4d7df1a00888974d8c";

    public Settlement(MethodProperties methodProperties, String calledMethod) {
        this.calledMethod = calledMethod;
        this.methodProperties = methodProperties;
    }
}
