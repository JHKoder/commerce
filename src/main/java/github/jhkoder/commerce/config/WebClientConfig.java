package github.jhkoder.commerce.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100_000)
                .responseTimeout(Duration.ofMillis(100_000))
                .doOnConnected(conn -> {
                    conn.addHandlerLast(new ReadTimeoutHandler(100_000, TimeUnit.MILLISECONDS))
                            .addHandlerLast(new WriteTimeoutHandler(100_000, TimeUnit.MILLISECONDS));
                });

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON.toString())
                .defaultHeader(ACCEPT, HttpHeaderValues.APPLICATION_JSON.toString())
                .build();
    }
}