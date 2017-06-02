package fr.zhj2074.backoffice.monitoring;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheck.Result;
import com.codahale.metrics.json.HealthCheckModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Strings;
import fr.zhj2074.backoffice.exception.AppException;
import lombok.Value;
import net.sf.ehcache.util.NamedThreadFactory;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.toList;

/**
 * Custom HealthCheckServlet used to give access to the Spring context to the healthChecks
 */
public class HealthCheckServlet extends HttpServlet {

    private static final int HEALTH_CHECK_TASK_TIMEOUT_IN_SECONDS = 30;
    private static final TimeUnit HEALTH_CHECK_TASK_TIMEOUT_UNIT = TimeUnit.SECONDS;
    private ExecutorService executor;
    private ObjectMapper mapper;
    private Map<String, HealthCheck> healthChecks;

    public HealthCheckServlet() {
        // Needed for the Servlet container
    }

    @VisibleForTesting
    HealthCheckServlet(ExecutorService executor, Map<String, HealthCheck> healthChecks, ObjectMapper objectMapper) {
        this.executor = executor;
        this.healthChecks = healthChecks;
        mapper = objectMapper;
    }

    private static boolean isAllHealthy(Map<String, Result> results) {
        for (Result result : results.values()) {
            if (!result.isHealthy()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        executor = Executors.newFixedThreadPool(10, new NamedThreadFactory("monitoring"));
        mapper = new ObjectMapper().registerModule(new HealthCheckModule());
        createHealthChecks();
    }

    @Override
    public void destroy() {
        if (executor != null) {
            executor.shutdownNow();
        }
        super.destroy();
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortedMap<String, Result> results = runHealthChecks();
        resp.setContentType("application/json");
        resp.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
        if (results.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
        } else {
            if (isAllHealthy(results)) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }

        try (OutputStream output = resp.getOutputStream()) {
            getWriter(req).writeValue(output, results);
        }
    }

    private ObjectWriter getWriter(HttpServletRequest request) {
        return Boolean.parseBoolean(request.getParameter("pretty")) ?
            mapper.writerWithDefaultPrettyPrinter() : mapper.writer();
    }

    private SortedMap<String, Result> runHealthChecks() {
        List<HealthCheckTask> tasks = healthChecks
            .entrySet()
            .stream()
            .map(entry -> new HealthCheckTask(entry.getKey(), entry.getValue()))
            .collect(toList());

        try {
            SortedMap<String, Result> results = new TreeMap<>();
            List<Future<Result>> futures = executor.invokeAll(tasks, HEALTH_CHECK_TASK_TIMEOUT_IN_SECONDS, HEALTH_CHECK_TASK_TIMEOUT_UNIT);
            for (int i = 0; i < futures.size(); i++) {
                Future<Result> future = futures.get(i);
                HealthCheckTask invokedCallable = tasks.get(i);
                String healthCheckName = invokedCallable.getName();

                try {
                    results.put(healthCheckName, future.get());
                } catch (CancellationException ignored) {
                    results.put(healthCheckName,
                        Result.unhealthy("Erreur : le test '" + healthCheckName + "' n'a pas répondu avant " +
                            (long) HEALTH_CHECK_TASK_TIMEOUT_IN_SECONDS + " " + HEALTH_CHECK_TASK_TIMEOUT_UNIT));
                } catch (InterruptedException ignored) {
                    Thread.interrupted();
                    results.put(healthCheckName,
                        Result.unhealthy("Erreur : le test '" + healthCheckName + "' a été annulé."));
                } catch (ExecutionException e) {
                    results.put(healthCheckName,
                        Result.unhealthy("Erreur : le test '" + healthCheckName + "' a échoué pour une raison " +
                            "inconnue. Message: " + e.getMessage(), ExceptionUtils.getStackTrace(e)));
                }
            }

            return Collections.unmodifiableSortedMap(results);
        } catch (InterruptedException e) {
            Thread.interrupted();
            throw new AppException("Monitoring interrompu", e);
        }
    }

    private void createHealthChecks() {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        healthChecks = new TreeMap<>();
        AtomicInteger count = new AtomicInteger();
        healthChecks.put(counterString(count) + " - DBPANO - Accès à la base de données backoffice", new BackofficeDatabaseHealthCheck(ctx));
    }

    private String counterString(AtomicInteger count) {
        return Strings.padStart(String.valueOf(count.incrementAndGet()), 2, '0');
    }

    @Value
    private static class HealthCheckTask implements Callable<Result> {
        private final String name;
        private final HealthCheck healthCheck;

        @Override
        public Result call() throws Exception {
            return healthCheck.execute();
        }
    }
}
