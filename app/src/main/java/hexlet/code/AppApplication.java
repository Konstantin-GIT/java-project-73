package hexlet.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@SpringBootApplication
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

/*
    @Bean(name = "datasource1")
    @ConfigurationProperties("database1.datasource")
    @Primary
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }
/*
    @Bean(name = "datasource2")
    @ConfigurationProperties("database2.datasource")
    public DataSource dataSource2(){
        return DataSourceBuilder.create().build();
    }*/
}
