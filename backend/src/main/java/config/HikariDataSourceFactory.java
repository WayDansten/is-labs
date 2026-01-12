package config;

import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;

@ApplicationScoped
@NoArgsConstructor
public class HikariDataSourceFactory {
    public static HikariDataSource create() {
        HikariConfig hc = new HikariConfig();
        Properties props = HikariPropertiesLoader.loadProperties();
        setProperties(hc, props);
        return new HikariDataSource(hc);
    }

    private static void setProperties(HikariConfig hc, Properties props) {
        hc.setJdbcUrl(props.getProperty("hikari.jdbc.url"));
        hc.setUsername(props.getProperty("hikari.jdbc.username"));
        hc.setPassword(props.getProperty("hikari.jdbc.password"));
        hc.setDriverClassName(props.getProperty("hikari.jdbc.driver"));
        
        hc.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.pool.minimumIdle")));
        hc.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.pool.maximumPoolSize")));
        hc.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.pool.connectionTimeout")));
        hc.setIdleTimeout(Long.parseLong(props.getProperty("hikari.pool.idleTimeout")));
    }
}