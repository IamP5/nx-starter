package com.example.nxstarter.application;

/** Inbound port without input (queries such as "list everything"). */
public abstract class NullaryUseCase<OUT> {

    public abstract OUT execute();

    public <T> T execute(final Presenter<OUT, T> presenter) {
        return presenter.apply(execute());
    }
}
