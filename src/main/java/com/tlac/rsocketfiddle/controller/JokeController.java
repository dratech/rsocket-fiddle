package com.tlac.rsocketfiddle.controller;

import com.tlac.rsocketfiddle.domain.Joke;
import com.tlac.rsocketfiddle.service.JokeService;
import com.tlac.rsocketfiddle.service.RsocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class JokeController {

    private final JokeService jokeService;
    private final RsocketService rsocketService;

    @ConnectMapping("joke")
    void connect(RSocketRequester rSocketRequester) {
        rsocketService.registerClient(rSocketRequester);
    }

    @MessageMapping("joke.add")
    Mono<Joke> requestResponse(String joke) {
        return jokeService.addJoke(joke);
    }

    @MessageMapping("joke.stream")
    Flux<Joke> requestStream() {
        return jokeService.streamAllJoke();
    }

    @PostMapping("/push")
    public ResponseEntity<Mono<Joke>> pushRandomJoke() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(jokeService.pushRandomJoke());
    }

}
