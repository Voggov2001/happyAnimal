package com.coderiders.happyanimal.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@NoArgsConstructor
public class MessageFromScheduler {
    private String content;

    public MessageFromScheduler(String content) {
        this.content = content;
    }

}
