/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.zhj2074.backoffice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * @see org.springframework.web.filter.AbstractRequestLoggingFilter
 * @see org.springframework.web.filter.CommonsRequestLoggingFilter
 */
@Slf4j
@Order(value = 2)
public class LogRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        beforeRequest(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request);
        }
    }

    protected void beforeRequest(HttpServletRequest request) {
        log.debug(createMessage(request));
    }

    protected void afterRequest(HttpServletRequest request) {
    }

    private String createMessage(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(request.getMethod().toUpperCase(Locale.ENGLISH));
        msg.append(" " + request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        return msg.toString();
    }
}
