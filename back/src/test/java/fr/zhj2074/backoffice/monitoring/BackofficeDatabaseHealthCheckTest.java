package fr.zhj2074.backoffice.monitoring;

import com.codahale.metrics.health.HealthCheck.Result;
import fr.zhj2074.backoffice.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLTimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class BackofficeDatabaseHealthCheckTest extends IntegrationTest {

    @Mock
    private WebApplicationContext contexteMock;

    @Mock
    private JdbcTemplate jdbcTemplateMock;

    private BackofficeDatabaseHealthCheck backofficeDatabaseHealthCheck;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(contexteMock.getBean(JdbcTemplate.class)).thenReturn(jdbcTemplateMock);
        when(contexteMock.getEnvironment()).thenReturn(new MockEnvironment().withProperty("spring.datasource.url", "urlTest"));
        backofficeDatabaseHealthCheck = new BackofficeDatabaseHealthCheck(contexteMock);
    }

    @Test
    public void batch_is_success() throws Exception {
        Result check = backofficeDatabaseHealthCheck.check();

        verify(jdbcTemplateMock, times(1)).execute(anyString());

        assertThat(check.isHealthy()).isTrue();
    }

    @Test
    public void batch_is_not_success() throws Exception {
        doThrow(SQLTimeoutException.class).when(jdbcTemplateMock).execute(anyString());

        Result check = backofficeDatabaseHealthCheck.check();

        assertThat(check.isHealthy()).isFalse();
    }
}
