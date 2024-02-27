package org.example.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response extends Option{
    private int response_id;
    private static int id_gen = 1;
    private int survey_id;
    private int question_id;
    private int user_id;
    private int answer;
    public Response () {
        this.response_id = id_gen++;
    }
    public Response(int survey_id, int question_id, int user_id, int answer) {
        this();
        setSurvey_id(survey_id);
        setQuestion_id(question_id);
        setUser_id(user_id);
        setAnswer(answer);
    }
}
