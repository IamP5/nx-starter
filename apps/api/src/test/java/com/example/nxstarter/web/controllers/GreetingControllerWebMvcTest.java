package com.example.nxstarter.web.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.nxstarter.application.greeting.CreateGreeting;
import com.example.nxstarter.domain.exceptions.DomainException;
import com.example.nxstarter.domain.validation.Error;
import com.example.nxstarter.infrastructure.configuration.GlobalExceptionHandler;
import com.example.nxstarter.infrastructure.configuration.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Controller slice test: the use case is mocked, security and the exception mapping are real. */
@WebMvcTest(GreetingController.class)
@Import({SecurityConfiguration.class, GlobalExceptionHandler.class})
class GreetingControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    // CALLS_REAL_METHODS keeps the concrete execute(input, presenter) overload working, so the
    // controller's presenter path runs against the stubbed abstract execute(input).
    @MockitoBean(answers = Answers.CALLS_REAL_METHODS)
    private CreateGreeting createGreeting;

    @Test
    void returnsGreetingResponse() throws Exception {
        when(createGreeting.execute(any(CreateGreeting.Input.class)))
                .thenReturn(new Output("Hello, Tuba, from api", "abc"));

        mockMvc.perform(get("/api/greeting").param("name", "Tuba"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello, Tuba, from api"))
                .andExpect(jsonPath("$.hash").value("abc"));
    }

    @Test
    void mapsDomainExceptionToUnprocessableEntity() throws Exception {
        when(createGreeting.execute(any(CreateGreeting.Input.class)))
                .thenThrow(DomainException.with(new Error("name", "'name' should not be empty")));

        mockMvc.perform(get("/api/greeting").param("name", " "))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.errors[0].property").value("name"));
    }

    private record Output(String message, String hash) implements CreateGreeting.Output {}
}
