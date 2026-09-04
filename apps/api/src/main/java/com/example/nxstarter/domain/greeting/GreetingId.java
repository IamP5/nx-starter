package com.example.nxstarter.domain.greeting;

import com.example.nxstarter.domain.shared.Identifier;

/** Identity of a {@link Greeting}: the request hash returned to the caller. */
public record GreetingId(String value) implements Identifier<String> {

    public static GreetingId from(final String value) {
        return new GreetingId(value);
    }
}
