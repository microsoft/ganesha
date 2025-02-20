package com.microsoft.ganesha.config;

import com.microsoft.ganesha.constant.APIConstants;
import com.microsoft.ganesha.exception.CustomWebClientException;
import com.microsoft.ganesha.filter.WebClientFilter;
import com.microsoft.ganesha.response.ErrorResponse;

import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import javax.net.ssl.SSLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    @Value("${webclient.connection.timeout}")
    private String webClientConnectionTimeOut;

    @Value("${webclient.read.timeout}")
    private String webClientReadTimeOut;

    @Value("${webclient.write.timeout}")
    private String webClientWriteTimeOut;

    @Value("${httpclient.max-connections}")
    private String httpClientMaxConnections;

    @Value("${httpclient.max-idle-time}")
    private String httpClientMaxIdleTime;

    @Value("${httpclient.max-life-time}")
    private String httpClientMaxLifeTime;

    @Value("${httpclient.pending-acq-timeout}")
    private String httpClientPendingAcqTimeout;

    @Value("${httpclient.evict-in-background}")
    private String httpClientEvictInBackground;

    @Value("${SetLocalEnvFlag}")
    private boolean setLocalEnvFlag;

    private final WebClientFilter webClientFilter;

    public WebClientConfig(WebClientFilter webClientFilter) {
        this.webClientFilter = webClientFilter;
    }


    /**
     * Creates connection pool for http client
     *
     * @return ConnectionProvider
     */
    @Bean
    public ConnectionProvider createConnectionProvider() {
        return ConnectionProvider.builder(APIConstants.HTTP_CLIENT_CONNECTION_PROVIDER_FIXED)
                .maxConnections(Integer.parseInt(httpClientMaxConnections))
                .maxIdleTime(Duration.ofMillis(Long.parseLong(httpClientMaxIdleTime)))
                .maxLifeTime(Duration.ofMillis(Long.parseLong(httpClientMaxLifeTime)))
                .pendingAcquireTimeout(Duration.ofMillis(Long.parseLong(httpClientPendingAcqTimeout)))
                .evictInBackground(Duration.ofMillis(Long.parseLong(httpClientEvictInBackground)))
                .build();
    }

    @Bean
    public SslContext createSSLContext() throws SSLException {
        if (!setLocalEnvFlag) {
            return SslContextBuilder.forClient().build();
        } else {
            return SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }
    }

    /**
     * Creates HttpClient
     *
     * @param connectionProvider
     * @return HttpClient
     * @throws CustomWebClientException
     */
    @Bean
    public HttpClient createHttpClient(ConnectionProvider connectionProvider, SslContext sslContext)
            throws CustomWebClientException {
        HttpClient httpClient = null;

        try {
            httpClient = HttpClient.create(connectionProvider)
                    .secure(t -> t.sslContext(sslContext))
                    .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                            Integer.parseInt(webClientConnectionTimeOut))
                    .doOnConnected(conn -> conn
                            .addHandlerLast(new ReadTimeoutHandler(Integer.parseInt(webClientReadTimeOut),
                                    TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(Integer.parseInt(webClientWriteTimeOut),
                                    TimeUnit.MILLISECONDS)));
        } catch (Exception ex) {
            LOGGER.error("Error Occurred while creating HttpClient, Error Message :: {}"
                    + "Exception :: {}", ex.getMessage(), ex);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .errorTitle(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorMessage(ex.getMessage())
                    .build();
            throw new CustomWebClientException(errorResponse);
        }
        return httpClient;
    }

    /**
     * Creates Webclient bean
     *
     * @param httpClient
     * @return WebClient
     */
    @Bean
    public WebClient createWebClient(HttpClient httpClient) {
        WebClient webClient = null;
        LOGGER.info("Initializing WebClient bean inside createWebClient.");

        try {
            ReactorClientHttpConnector connector = new ReactorClientHttpConnector(httpClient);
            webClient = WebClient.builder()
                    .clientConnector(connector)
                    .filter(webClientFilter.requestFilter())
                    .filter(webClientFilter.responseFilter())
                    .build();

            LOGGER.info("Webclient initialized successfully.");

        } catch (Exception ex) {
            LOGGER.error("Error Occurred inside createWebClient(), Error Message :: {}, "
                    + "Exception :: {}", ex.getMessage(), ex);

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .errorTitle(HttpStatus.INTERNAL_SERVER_ERROR)
                    .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .errorMessage(ex.getMessage())
                    .build();
            throw new CustomWebClientException(errorResponse);
        }
        return webClient;
    }
}