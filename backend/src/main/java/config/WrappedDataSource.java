package config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WrappedDataSource implements DataSource {
    private final HikariDataSource hikariDataSource;
 
    public WrappedDataSource() {
 
        HikariConfig hc = new HikariConfig();
        Properties props = loadProperties();

        hc.setJdbcUrl(props.getProperty("hikari.jdbc.url"));
        hc.setUsername(props.getProperty("hikari.jdbc.username"));
        hc.setPassword(props.getProperty("hikari.jdbc.password"));
        hc.setDriverClassName(props.getProperty("hikari.jdbc.driver"));
        
        hc.setMinimumIdle(Integer.parseInt(props.getProperty("hikari.pool.minimumIdle")));
        hc.setMaximumPoolSize(Integer.parseInt(props.getProperty("hikari.pool.maximumPoolSize")));
        hc.setConnectionTimeout(Long.parseLong(props.getProperty("hikari.pool.connectionTimeout")));
        hc.setIdleTimeout(Long.parseLong(props.getProperty("hikari.pool.idleTimeout")));
 
        hikariDataSource = new HikariDataSource(hc);
    }
    
    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("hikari.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find hikari.properties");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
        return props;
    }
 
    @Override
    public Connection getConnection() throws SQLException {
        return hikariDataSource.getConnection();
    }
 
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return hikariDataSource.getConnection(username, password);
    }
 
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return hikariDataSource.getLogWriter();
    }
 
    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        hikariDataSource.setLogWriter(out);
    }
 
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        hikariDataSource.setLoginTimeout(seconds);
    }
 
    @Override
    public int getLoginTimeout() throws SQLException {
        return hikariDataSource.getLoginTimeout();
    }
 
    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return hikariDataSource.getParentLogger();
    }
 
    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return hikariDataSource.unwrap(iface);
    }
 
    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return hikariDataSource.isWrapperFor(iface);
    }
}
