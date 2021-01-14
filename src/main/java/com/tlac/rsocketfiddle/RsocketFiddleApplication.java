package com.tlac.rsocketfiddle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketStrategies;

@SpringBootApplication
public class RsocketFiddleApplication {

	@Bean
	@Primary
	public RSocketStrategies rSocketStrategies() {
		return RSocketStrategies.builder()
			.encoders(encoders -> encoders.add(new Jackson2JsonEncoder()))
			.decoders(decoders -> decoders.add(new Jackson2JsonDecoder()))
			.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(RsocketFiddleApplication.class, args);
	}

}
