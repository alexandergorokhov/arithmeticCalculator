package org.challenge.controller.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class ResponsePageable {
    private long id;
    private String type;
    private Page result;

    private int statusCode;

    @Override
    public String toString() {
        return "{" +
            "id:" + id +
            ", type:'" + type +
            ", result:'" + result +
            ", statusCode:" + statusCode +
            '}';
    }
}
