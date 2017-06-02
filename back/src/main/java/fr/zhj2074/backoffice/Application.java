package fr.zhj2074.backoffice;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.zhj2074.backoffice.monitoring.HealthCheckServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.Filter;

@SpringBootApplication
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class Application extends SpringBootServletInitializer {

    @Autowired
    private Environment env;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    @Bean
    @Profile("dev")
    public Filter logRequestFilter() {
        return new LogRequestFilter();
    }

    @Bean
    public Filter authFilter() {
        return new AuthFilter();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        return mapper;
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(new ApiInfoBuilder()
                .title("backoffice")
                .description("Application des chargés de clientèle")
                .version(env.getProperty("info.build.version"))
                .build())
            .select()
            .build();
    }

    @Bean
    public FilterRegistrationBean tuckeyRegistrationBean() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new UrlRewriteFilter());
        registrationBean.addInitParameter("modRewriteConf", "true");
        registrationBean.addInitParameter("modRewriteConfText",
                "RewriteRule ^/front/api/ /api/ [L]\n" +
                "RewriteRule ^/front(/[^\\.]*)*$ /index.html [L]\n" +
                "RewriteRule ^/front/ / [L]\n");
        return registrationBean;
    }

    @Bean
    public LdapTemplate ldapTemplate(@Value("${ldap.url}") String ldapUrl,
                                     @Value("${ldap.userdn}") String ldapUserDn,
                                     @Value("${ldap.password}") String ldapPassword) throws Exception {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setUserDn(ldapUserDn);
        contextSource.setPassword(ldapPassword);
        contextSource.afterPropertiesSet();
        LdapTemplate ldapTemplate = new LdapTemplate(contextSource);
        ldapTemplate.afterPropertiesSet();
        return ldapTemplate;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new HealthCheckServlet(), "/monitoring");
    }
}
