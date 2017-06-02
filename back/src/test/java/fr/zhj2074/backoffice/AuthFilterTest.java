package fr.zhj2074.backoffice;


import fr.zhj2074.backoffice.api.users.UserId;
import fr.zhj2074.backoffice.authentication.TokenAuthentication;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AuthFilterTest {

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private TokenAuthentication validator = mock(TokenAuthentication.class);
    private FilterChain chain = mock(FilterChain.class);
    private Environment env = mock(Environment.class);
    private AuthFilter filter = new AuthFilter();

    @Before
    public void mockSpring() throws Exception {
        WebApplicationContext springContext = mock(WebApplicationContext.class);
        given(springContext.getBean(TokenAuthentication.class)).willReturn(validator);
        when(springContext.getEnvironment()).thenReturn(env);

        MockServletContext servletContext = new MockServletContext();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, springContext);
        filter.setServletContext(servletContext);
    }

    @Test
    public void success_with_header() throws Exception {
        given(validator.validateToken("mytoken")).willReturn(new UserId("005"));
        request.addHeader("Authorization", "Bearer mytoken");
        request.setServletPath("/front/api");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(request.getAttribute("userId"));
    }

    @Test
    public void success_with_header_case_insensitive() throws Exception {
        given(validator.validateToken("mytoken")).willReturn(new UserId("005"));
        request.addHeader("aUthoRIzaTION", "Bearer mytoken");
        request.setServletPath("/front/api");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(request.getAttribute("userId"));
    }

    @Test
    public void success_with_request_param() throws Exception {
        given(validator.validateToken("mytoken")).willReturn(new UserId("005"));
        request.addParameter("auth_token", "mytoken");
        request.setServletPath("/front/api");

        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
        assertThat(request.getAttribute("userId"));
    }

    @Test
    public void failure() throws Exception {
        request.setServletPath("/front/api");
        filter.doFilterInternal(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);

        verifyZeroInteractions(validator);
    }

    @Test
    public void access_with_api() throws Exception {
        request.setServletPath("/tada/front/api");
        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    public void free_access() throws Exception {
        request.setServletPath("/tada");
        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(request, response);
    }
}
