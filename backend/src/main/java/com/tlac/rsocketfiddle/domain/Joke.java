package com.tlac.rsocketfiddle.domain;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Joke {
    private Integer position;
    private String joke;
}
