package com.actuator;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.spring.jdbc.DataSourceMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Collection;

@Configuration
@SpringBootApplication
public class ActuatorExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActuatorExampleApplication.class, args);

	}

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Collection<DataSourcePoolMetadataProvider> metadataProviders;

    @Autowired
    private Environment env;

    @Autowired
    private MeterRegistry influxMeterRegistry;

    @Bean
    public DataSourceMetrics dataSourceMetrics(DataSource ds, Collection<DataSourcePoolMetadataProvider> poolMetadata) {
        return new DataSourceMetrics(dataSource, metadataProviders, "data.source", Tags.zip());
    }

    @Bean
    public MeterBinder processMemoryMetrics() {
        return new ProcessMemoryMetrics();
    }

    @Bean
    public MeterBinder processThreadMetrics() {
        return new ProcessThreadMetrics();
    }

}
