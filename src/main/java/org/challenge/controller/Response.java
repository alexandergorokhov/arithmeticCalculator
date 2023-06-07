package org.challenge.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response {
    private int id;
    private String type;
    private String result;

    private int statusCode;

    @Override
    public String toString() {
        return "Response{" +
            "id=" + id +
            ", type='" + type +
            ", result='" + result +
            ", statusCode=" + statusCode +
            '}';
    }
}
