package com.tlac.rsocketfiddle.service;

import com.tlac.rsocketfiddle.domain.Joke;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RsocketService {
    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

    public void registerClient(RSocketRequester rSocketRequester) {
        rSocketRequester.rsocket()
            .onClose()
            .doFirst(() -> {
                log.info("Client CONNECTED");
                CLIENTS.add(rSocketRequester);
            })
            .doOnError(error -> {
                log.warn("Channel to client CLOSED");
            })
            .doFinally(consumer -> {
                CLIENTS.remove(rSocketRequester);
                log.info("Client DISCONNECTED");
            })
            .subscribe();
    }

    public void sendJokeToClients(Joke joke) {
        CLIENTS.forEach(rSocketRequester ->
            rSocketRequester
                .route("test-route")
                .data(joke)
                .send()
                .subscribe()
        );
    }

}
