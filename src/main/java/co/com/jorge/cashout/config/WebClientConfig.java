package co.com.jorge.cashout.config;

import co.com.jorge.cashout.exceptions.Error400Exception;
import co.com.jorge.cashout.services.interfaces.IPaymentRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Value("${webclient.base-url}")
    private String baseUrl;

    @Bean
    public IPaymentRestClient paymentRestClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
          .builderFor(WebClientAdapter.create(webClient))
          .build();
        return factory.createClient(IPaymentRestClient.class);
    }

    @Bean
    public WebClient createWebClient(WebClient.Builder builder) {
        return builder.baseUrl(baseUrl)
          .defaultStatusHandler(HttpStatusCode::is4xxClientError,
            clientResponse -> clientResponse.bodyToMono(String.class)
            .flatMap(error -> Mono.error(new Error400Exception(error))))
          .defaultStatusHandler(HttpStatusCode::is5xxServerError, clientResponse ->
            clientResponse.bodyToMono(String.class)
              .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)))
              .flatMap(error -> Mono.error(new RuntimeException(error))))
          .build();
    }
}
