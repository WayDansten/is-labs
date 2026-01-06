package config;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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

        hc.setJdbcUrl("jdbc:postgresql://localhost:5432/postgres");
        hc.setUsername("postgres");
        hc.setPassword("12345");
        hc.setDriverClassName("org.postgresql.Driver");

        hc.setMinimumIdle(5);  
        hc.setMaximumPoolSize(20);
        hc.setConnectionTimeout(30000);
        hc.setIdleTimeout(600000);
 
        hikariDataSource = new HikariDataSource(hc);
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
