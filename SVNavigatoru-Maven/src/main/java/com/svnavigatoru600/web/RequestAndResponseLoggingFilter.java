package com.svnavigatoru600.web;

import static com.svnavigatoru600.common.constants.CommonConstants.NEW_LINE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.svnavigatoru600.common.constants.CommonConstants;

/**
 * Similar to {@link org.springframework.web.filter.AbstractRequestLoggingFilter}, however this class enables logging of
 * request payload before the request has been processed. The Spring class doesn't support such a functionality.
 *
 * It's not possible to use {@link org.springframework.web.servlet.HandlerInterceptor} since you cannot pass further a
 * different request object than you have got.
 *
 * @author Tomas Skalicky
 * @since 29.03.2017
 */
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    /**
     * NOTE: If an incoming request is POST and its type is "application/x-www-form-urlencoded", this wrapper parses the
     * cached payload to retrieve parameter names and values. The reason is that controller methods like
     * {@link com.svnavigatoru600.web.news.EditNewsController#processSubmittedForm(int, com.svnavigatoru600.viewmodel.news.EditNews, org.springframework.validation.BindingResult, org.springframework.web.bind.support.SessionStatus, HttpServletRequest)}
     * are using {@link org.springframework.web.bind.annotation.ModelAttribute} annotation which expects properly
     * populated parameter names and values. After configuring {@link RequestAndResponseLoggingFilter} in web.xml as
     * HTTP filter, parameter names and values were not populated properly and
     * {@link org.springframework.web.util.WebUtils#getParametersStartingWith(javax.servlet.ServletRequest, String)}
     * returned an empty {@link java.util.Map}.
     *
     * @author Tomas Skalicky
     * @since 01.04.2017
     */
    @VisibleForTesting
    class RequestWrapper extends HttpServletRequestWrapper {
        private final byte[] cachedPayload;
        private final Map<String, String[]> parameterMap = new HashMap<>();

        public RequestWrapper(final HttpServletRequest request) {
            super(request);
            cachedPayload = retrievePayload(request);
        }

        private byte[] retrievePayload(final HttpServletRequest request) {
            try {
                final ServletInputStream inputStream = request.getInputStream();
                if (inputStream == null) {
                    return new byte[0];
                }

                try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    final byte[] byteBuffer = new byte[1024];
                    int readByteCount = 0;
                    while ((readByteCount = inputStream.read(byteBuffer)) > 0) {
                        outputStream.write(byteBuffer, 0, readByteCount);
                    }
                    return outputStream.toByteArray();
                }
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cachedPayload);
            final ServletInputStream servletInputStream = new ServletInputStream() {
                boolean finished = false;

                @Override
                public int read() throws IOException {
                    final int nextByte = byteArrayInputStream.read();
                    if (!finished && nextByte == -1) {
                        finished = true;
                    }
                    return nextByte;
                }

                @Override
                public boolean isFinished() {
                    return finished;
                }

                @Override
                public boolean isReady() {
                    throw new RuntimeException("came with Tomcat 8.5, unsupported method");
                }

                @Override
                public void setReadListener(final ReadListener listener) {
                    throw new RuntimeException("came with Tomcat 8.5, unsupported method");
                }
            };
            return servletInputStream;
        }

        @Override
        public String getParameter(final String name) {
            extractRequestParameters();
            final String[] parameterValues = getParameterValues(name);
            if (parameterValues == null || parameterValues.length == 0) {
                return null;
            } else if (parameterValues.length == 1) {
                return parameterValues[0];
            } else {
                throw new IllegalStateException();
            }
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            extractRequestParameters();
            return super.getParameterMap();
        }

        @Override
        public Enumeration<String> getParameterNames() {
            extractRequestParameters();
            return Iterators.asEnumeration(parameterMap.keySet().iterator());
        }

        @Override
        public String[] getParameterValues(final String name) {
            extractRequestParameters();
            return parameterMap.get(name);
        }

        private void extractRequestParameters() {
            if (isFormPost() && parameterMap.size() == 0) {
                readRequestParametersFromCachedPayload();
            }
        }

        private boolean isFormPost() {
            final String contentType = getContentType();
            return (contentType != null && contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    && HttpMethod.POST.matches(getMethod()));
        }

        public boolean isMultipartFormPost() {
            final String contentType = getContentType();
            return (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)
                    && HttpMethod.POST.matches(getMethod()));
        }

        private void readRequestParametersFromCachedPayload() {
            if (parameterMap.size() == 0) {
                parameterMap.putAll(retrieveRequestParametersFromPayload(getCachedPayloadString()));
            }
        }

        @VisibleForTesting
        Map<String, String[]> retrieveRequestParametersFromPayload(final String input) {

            final Map<String, String[]> resultMap = new HashMap<>();

            try {
                final String[] parameterNameValuePairs = input.split("&");
                for (final String parameterNameValuePair : parameterNameValuePairs) {
                    final String[] parameter = parameterNameValuePair.split("=");
                    Assert.isTrue(parameter.length <= 2, "It is a parameter name-value pair where value may be empty.");

                    final String parameterName = parameter[0];
                    // Cached payload is still encoded.
                    final String decodedParameterValue = parameter.length == 2
                            ? java.net.URLDecoder.decode(parameter[1], CommonConstants.DEFAULT_CHARSET.name()) : null;

                    // Hides passwords.
                    final String parameterValueToLog = PASSWORD_LOG_IN_PARAMETER_NAME.equals(parameterName)
                            ? PASSWORD_PLACEHOLDER : decodedParameterValue;
                    logger.info("parameter name=" + parameterName + ", value=" + parameterValueToLog);

                    resultMap.put(parameterName, new String[] { decodedParameterValue });
                }
            } catch (final UnsupportedEncodingException ex) {
                throw new IllegalStateException(ex);
            }

            return resultMap;
        }

        private String getCachedPayloadString() {
            return new String(cachedPayload, CommonConstants.DEFAULT_CHARSET);
        }
    }

    /*
     * Copyright 2002-2012 the original author or authors.
     *
     * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
     * with the License. You may obtain a copy of the License at
     *
     * http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
     * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     * the specific language governing permissions and limitations under the License.
     */
    /**
     * Delegating implementation of {@link javax.servlet.ServletOutputStream}.
     *
     * <p>
     * Used by MockHttpServletResponse; typically not directly used for testing application controllers.
     *
     * @author Juergen Hoeller
     * @since 1.0.2
     */
    public class DelegatingServletOutputStream extends ServletOutputStream {

        private final OutputStream targetStream;

        /**
         * Create a DelegatingServletOutputStream for the given target stream.
         *
         * @param targetStream
         *            the target stream (never {@code null})
         */
        public DelegatingServletOutputStream(final OutputStream targetStream) {
            Assert.notNull(targetStream, "Target OutputStream must not be null");
            this.targetStream = targetStream;
        }

        /**
         * Return the underlying target stream (never {@code null}).
         */
        public final OutputStream getTargetStream() {
            return targetStream;
        }

        @Override
        public void write(final int b) throws IOException {
            targetStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            super.flush();
            targetStream.flush();
        }

        @Override
        public void close() throws IOException {
            super.close();
            targetStream.close();
        }

        @Override
        public boolean isReady() {
            throw new RuntimeException("came with Tomcat 8.5, unsupported method");
        }

        @Override
        public void setWriteListener(final WriteListener listener) {
            throw new RuntimeException("came with Tomcat 8.5, unsupported method");
        }

    }

    /**
     * Helps to write the response payload to both the default and one other output stream at the same time.
     */
    private class ResponseWrapper extends HttpServletResponseWrapper {

        private final ByteArrayOutputStream responsePayloadOutputStream = new ByteArrayOutputStream();

        private ResponseWrapper(final HttpServletResponse response) {
            super(response);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return new DelegatingServletOutputStream(
                    new TeeOutputStream(super.getOutputStream(), responsePayloadOutputStream));
        }

        private boolean isJson() {
            return getContentType() != null && getContentType().contains(MediaType.APPLICATION_JSON_VALUE);
        }
    }

    private static final String REQUEST_PREFIX = " >> ";
    private static final String RESPONSE_PREFIX = " << ";

    private static final List<String> IGNORED_URI_SUFFIXES = ImmutableList.of(
            CommonConstants.CSS_FILE_EXTENSION_WITH_DOT, CommonConstants.JAVASCRIPT_FILE_EXTENSION_WITH_DOT,
            CommonConstants.PNG_FILE_EXTENSION_WITH_DOT, CommonConstants.JPG_FILE_EXTENSION_WITH_DOT,
            CommonConstants.JPEG_FILE_EXTENSION_WITH_DOT);

    private static final String PASSWORD_LOG_IN_PARAMETER_NAME = "j_password";
    private static final String PASSWORD_PLACEHOLDER = "******************************";
    private static final Pattern PASSWORD_LOG_IN_PARAMETER_MATCHER = Pattern
            .compile(PASSWORD_LOG_IN_PARAMETER_NAME + "=[^\\&]*");

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {

        final RequestWrapper requestWrapper = new RequestWrapper(request);
        beforeRequest(requestWrapper);

        final ResponseWrapper responseWrapper = new ResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            afterRequest(requestWrapper, responseWrapper);
        }
    }

    private void beforeRequest(final RequestWrapper request) {
        if (checkIfShouldBeLogged(request)) {

            final String message = buildBeforeMessage(request);
            logger.info(message);
        }
    }

    private void afterRequest(final RequestWrapper request, final ResponseWrapper response) {
        if (checkIfShouldBeLogged(request)) {

            final String message = buildAfterMessage(request, response);

            final HttpStatus httpStatus = HttpStatus.valueOf(response.getStatus());
            if (httpStatus.is5xxServerError()) {
                logger.error(message);
            } else if (httpStatus.is4xxClientError()) {
                logger.warn(message);
            } else {
                logger.info(message);
            }
        }
    }

    private boolean checkIfShouldBeLogged(final RequestWrapper request) {
        for (final String ignoredSuffix : IGNORED_URI_SUFFIXES) {
            if (request.getRequestURI().endsWith(ignoredSuffix)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Constructs a message to write to the log before the request.
     */
    private String buildBeforeMessage(final RequestWrapper request) {
        final StringBuilder message = new StringBuilder(NEW_LINE);
        appendToRequestMessage(message, buildRequestIdentificationString(request));
        appendToRequestMessage(message, "client", request.getRemoteAddr());
        appendToRequestMessage(message, "user", request.getRemoteUser());
        appendHeadersToMessage(message, request);

        if (!request.isMultipartFormPost() && request.cachedPayload != null && request.cachedPayload.length > 0) {
            message.append(NEW_LINE);

            // Hides passwords.
            final String payloadWithHiddenPassword = hideLogInPasswordInPayload(request.getCachedPayloadString());
            appendToRequestMessage(message, "payload", payloadWithHiddenPassword);
        }

        return message.toString();
    }

    @VisibleForTesting
    String hideLogInPasswordInPayload(final String payload) {
        return PASSWORD_LOG_IN_PARAMETER_MATCHER.matcher(payload)
                .replaceFirst(PASSWORD_LOG_IN_PARAMETER_NAME + "=" + PASSWORD_PLACEHOLDER);
    }

    private void appendHeadersToMessage(final StringBuilder message, final RequestWrapper request) {
        final HttpHeaders headers = new ServletServerHttpRequest(request).getHeaders();
        final List<String> headerNames = Lists.newArrayList(headers.keySet());
        Collections.sort(headerNames);

        for (final String headerName : headerNames) {
            final List<String> headerValues = headers.getValuesAsList(headerName);
            if (headerValues.isEmpty()) {
                continue;
            }
            final String value = headerValues.size() == 1 ? headerValues.get(0) : headerValues.toString();
            appendToRequestMessage(message, headerName, value);
        }
    }

    /**
     * Constructs a message to write to the log after the request has been processed.
     */
    private String buildAfterMessage(final HttpServletRequest request, final ResponseWrapper response) {
        final StringBuilder message = new StringBuilder(NEW_LINE);
        appendToResponseMessage(message, buildRequestIdentificationString(request));
        appendToResponseMessage(message, "status", Integer.valueOf(response.getStatus()).toString());

        if (response.isJson()) {
            message.append(NEW_LINE);
            appendToResponseMessage(message, "payload",
                    new String(response.responsePayloadOutputStream.toByteArray(), CommonConstants.DEFAULT_CHARSET));
        }

        return message.toString();
    }

    private StringBuilder appendToRequestMessage(final StringBuilder message, final CharSequence value) {
        return appendToRequestMessage(message, "", value);
    }

    private StringBuilder appendToRequestMessage(final StringBuilder message, final CharSequence key,
            final CharSequence value) {
        return appendToMessage(message, REQUEST_PREFIX, key, value);
    }

    private StringBuilder appendToResponseMessage(final StringBuilder message, final CharSequence value) {
        return appendToResponseMessage(message, "", value);
    }

    private StringBuilder appendToResponseMessage(final StringBuilder message, final CharSequence key,
            final CharSequence value) {
        return appendToMessage(message, RESPONSE_PREFIX, key, value);
    }

    private StringBuilder appendToMessage(final StringBuilder message, final CharSequence prefix,
            final CharSequence key, final CharSequence value) {
        if (value == null || value.length() == 0) {
            return message;
        }

        // @formatter:off
        return message.append(prefix)
                .append(decorateKeyForMessage(key))
                .append(value)
                .append(NEW_LINE);
        // @formatter:on
    }

    private CharSequence decorateKeyForMessage(final CharSequence key) {
        if (StringUtils.isNotBlank(key)) {
            return key + ": ";
        } else {
            return key;
        }
    }

    private StringBuilder buildRequestIdentificationString(final HttpServletRequest request) {
        // @formatter:off
        final StringBuilder message = new StringBuilder()
                .append(request.getMethod())
                .append(' ')
                .append(request.getRequestURI());
        // @formatter:on

        final String queryString = request.getQueryString();
        if (queryString != null) {
            message.append('?').append(queryString);
        }
        final HttpSession session = request.getSession(false);
        if (session != null) {
            message.append(";jsessionid=").append(session.getId());
        }
        return message;
    }

}
