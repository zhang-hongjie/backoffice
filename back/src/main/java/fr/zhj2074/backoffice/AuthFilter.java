package fr.zhj2074.backoffice;

import fr.zhj2074.backoffice.api.users.UserId;
import fr.zhj2074.backoffice.authentication.MissingTokenException;
import fr.zhj2074.backoffice.authentication.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Slf4j
@Order(value = 1)
public class AuthFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTH_REQUEST_PARAM = "auth_token";
    public static final String USER_REQUEST_ATTRIBUTE = "user_id";
    private static final List<String> SECURED_URIS = newArrayList("/front/api");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (validateAccessToSecuredPath(request.getServletPath())) {
            try {
                String token = extractAccessToken(request);
                UserId userId = getUserId(token);
                MDC.put(MDCInfos.ID_USER_BO, userId.getUserId());
                request.setAttribute(USER_REQUEST_ATTRIBUTE, userId);
                log.debug("User '{}' successfully connected", userId.getUserId());
                chain.doFilter(request, response);
            } catch (MissingTokenException e) {
                log.warn(e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            } catch (Exception e) {
                log.warn(e.getMessage());
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } finally {
                MDC.remove(MDCInfos.ID_USER_BO);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean validateAccessToSecuredPath(String path) {
        return SECURED_URIS.stream().anyMatch(s -> path.startsWith(s));
    }

    private String extractAccessToken(HttpServletRequest request) {
        String accessToken = getAuthorizationHeaderValue(request);
        accessToken = isNotBlank(accessToken) ? accessToken : request.getParameter(AUTH_REQUEST_PARAM);
        if (StringUtils.isBlank(accessToken)) {
            String msg = "No token found for the request from " + request.getRequestURI();
            log.error(msg);
            throw new MissingTokenException(msg);
        }
        return accessToken;
    }

    private String getAuthorizationHeaderValue(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            if (AUTHORIZATION_HEADER.equalsIgnoreCase(header)) {
                String authHeader = request.getHeader(header);
                return StringUtils.removeStart(authHeader, "Bearer ");
            }
        }
        return null;
    }

    private UserId getUserId(String token) {
        WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        TokenAuthentication tokenAuthentication = ctx.getBean(TokenAuthentication.class);
        return tokenAuthentication.validateToken(token);
    }
}
