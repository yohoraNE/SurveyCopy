package org.example.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class Survey {
    private int id;
    private static int id_gen = 1;
    private int user_id;
    private String title;
    private String description;
    private ArrayList<Question> questions;

    public Survey() {
        questions = new ArrayList<Question>();
    }
    public void addQuestion(Question question) {this.questions.add(question);}

    public Survey(int user_id, String title, String description, int survey_id) {
        this();
        setId(survey_id);
        setUser_id(user_id);
        setTitle(title);
        setDescription(description);}

    @Override
    public String toString() {
        return "Survey{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
