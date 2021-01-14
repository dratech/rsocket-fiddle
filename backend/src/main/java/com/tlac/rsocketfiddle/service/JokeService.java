package com.tlac.rsocketfiddle.service;

import com.tlac.rsocketfiddle.domain.Joke;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class JokeService {

    private final RsocketService rsocketService;
    private final ConcurrentHashMap<Integer, String> jokes = new ConcurrentHashMap<>();
    private final Random random = new Random();

    @PostConstruct
    public void setup() {
        jokes.put(0, "Very joke much funny");
    }

    public Mono<Joke> addJoke(String joke) {
        return Mono.defer(() -> {
            var pos = jokes.size();
            jokes.put(pos, joke);
            log.info("Added joke: {} to pos: {}", joke, pos);
            return Mono.just(new Joke(pos, joke));
        });
    }

    public Flux<Joke> streamAllJoke() {
        return Flux.create(sink ->
            jokes.forEach((pos, joke) -> sink.next(new Joke(pos, joke)))
        );
    }

    public Mono<Joke> pushRandomJoke() {
        return Mono.just(random.nextInt(jokes.size()))
            .map(pos -> new Joke(pos, jokes.get(pos)))
            .doOnNext(joke -> rsocketService.sendJokeToClients(joke));
    }

}
