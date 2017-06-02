package fr.zhj2074.backoffice.monitoring;

import com.codahale.metrics.health.HealthCheck;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.WebApplicationContext;

public class BackofficeDatabaseHealthCheck extends HealthCheck {

    private final JdbcTemplate jdbc;
    private final String dbUrl;

    public BackofficeDatabaseHealthCheck(WebApplicationContext contexte) {
        jdbc = contexte.getBean(JdbcTemplate.class);
        dbUrl = contexte.getEnvironment().getProperty("spring.datasource.url");
    }

    @Override
    protected Result check() throws Exception {
        try {
            String query = "SELECT 1 FROM users";
            jdbc.execute(query);
            return Result.healthy("Succès : base de données backoffice accessible. Base: %s", dbUrl);
        } catch (Exception e) {
            return Result.unhealthy("Erreur : impossible de se connecter à la base backoffice %s." +
                " Vérifier les routes réseau et la config Postgresql. Message: %s", dbUrl, e.getMessage());
        }
    }
}
