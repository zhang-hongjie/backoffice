package fr.zhj2074.backoffice.monitoring;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.json.HealthCheckModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;

public class HealthCheckServletTest {

    private ExecutorService executor = mock(ExecutorService.class);
    private Map<String, HealthCheck> healthChecks = new HashMap<>(1);
    private ObjectMapper mapper = new ObjectMapper();
    private HealthCheckServlet servlet = new HealthCheckServlet(executor, healthChecks, mapper);

    @Before
    public void configureMapper() throws Exception {
        mapper.registerModule(new HealthCheckModule());
    }

    @Test
    public void success() throws Exception {
        Future future = mock(Future.class);
        given(future.get()).willReturn(Result.healthy());
        given(executor.invokeAll(any(List.class), anyLong(), any(TimeUnit.class))).willReturn(singletonList(future));
        healthChecks.put("aHealthCheck - don't care: only the result of the future is important", new AnyHealthCheck());

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
    }

    @Test
    public void exception_in_healthcheck() throws Exception {
        Future future = mock(Future.class);
        given(future.get()).willThrow(new ExecutionException(new RuntimeException("TEST EXCEPTION - IGNORE ME")));
        given(executor.invokeAll(any(List.class), anyLong(), any(TimeUnit.class))).willReturn(singletonList(future));
        healthChecks.put("aHealthCheck - don't care: only the result of the future is important", null);

        MockHttpServletResponse response = new MockHttpServletResponse();
        servlet.doGet(new MockHttpServletRequest(), response);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private static class AnyHealthCheck extends HealthCheck {
        @Override
        protected Result check() throws Exception {
            return null;
        }
    }
}
