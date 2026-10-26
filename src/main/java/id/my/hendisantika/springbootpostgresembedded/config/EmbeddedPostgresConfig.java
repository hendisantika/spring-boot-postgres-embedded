package id.my.hendisantika.springbootpostgresembedded.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * Starts an embedded PostgreSQL server when the application boots and exposes it as the {@link DataSource}.
 */
@Configuration(proxyBeanMethods = false)
public class EmbeddedPostgresConfig {

    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres(@Value("${embedded.postgres.port:0}") int port) throws IOException {
        EmbeddedPostgres.Builder builder = EmbeddedPostgres.builder();
        if (port > 0) {
            builder.setPort(port);
        }
        return builder.start();
    }

    @Bean
    public DataSource dataSource(EmbeddedPostgres embeddedPostgres) {
        return embeddedPostgres.getPostgresDatabase();
    }
}
