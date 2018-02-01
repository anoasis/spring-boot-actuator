package com.actuator;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.spring.jdbc.DataSourceMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Collection;

/**
 * Created by kangj on 1/31/2018.
 */
@Configuration
public class MicrometerConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Collection<DataSourcePoolMetadataProvider> metadataProviders;

    @Autowired
    private Environment env;

    @Autowired
    private MeterRegistry influxMeterRegistry;

    @PostConstruct
    public void setCommonTags() {
        influxMeterRegistry.config().commonTags("env", "IPE", "app", "poc-actuator");
    }

    @PostConstruct
    private void instrumentDataSource() {
        new DataSourceMetrics(
                dataSource,
                metadataProviders,
                "data.source", // base metric name
                Tags.zip("stack", env.acceptsProfiles("prod") ? "prod" : "test")
        ).bindTo(influxMeterRegistry);
    }

    @Bean
    public DataSourceMetrics dataSourceMetrics(DataSource ds, Collection<DataSourcePoolMetadataProvider> poolMetadata) {
        return new DataSourceMetrics(dataSource, metadataProviders, "data.source", Tags.zip());
    }
}
