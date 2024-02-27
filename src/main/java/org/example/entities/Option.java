package org.example.entities;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Option {

    private String text;
    private int questionId;
    private int optionId;
    public Option() {

    }
    public Option(String text, int questionId, int optionId) {
        setOptionId(optionId);
        this.text = text;
        this.questionId = questionId;
    }
}
