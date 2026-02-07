package com.nsteuerberg.gametracker.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

public class ProblemDetailBuilder {
    public static ProblemDetail build(HttpStatus status, String detail, String title, String uri) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(URI.create(uri));
        return problem;
    }
}
