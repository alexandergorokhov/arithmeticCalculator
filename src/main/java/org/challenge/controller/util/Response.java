package org.challenge.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response {
    private long id;
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
