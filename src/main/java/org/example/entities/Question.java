package org.example.entities;

import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.lang.reflect.Array;

@Getter
@Setter

public class Question extends Survey {
    private int survey_id;
    private int questionId;
    private String text;
    private ArrayList<Option> options;


    public Question() {
        options = new ArrayList<Option>();
    }

    public Question(int surveyId, String text, int questionId) {
        this.survey_id = surveyId;
        setQuestionId(questionId);
        setText(text);
    }

    public void addOption(Option option) {
        options.add(option);
    }

}
