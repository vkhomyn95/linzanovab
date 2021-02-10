package com.backend.linzanova.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage.location")
@Getter
@Setter
public class StorageProperties {

    private String lens;
    private String solutions;
    private String offers;
    private String drops;

}
