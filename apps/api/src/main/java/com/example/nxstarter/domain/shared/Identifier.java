package com.example.nxstarter.domain.shared;

/**
 * Identity of an {@link Entity}. Implemented by records named {@code <Aggregate>Id} that live next
 * to their aggregate.
 */
public interface Identifier<T> {

    T value();
}
