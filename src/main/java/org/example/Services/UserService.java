package org.example.Services;

import org.example.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;

public class UserService {
    ArrayList<Survey> surveys = new ArrayList<Survey>();
    public void createSurvey(int user_id) {
        String conString = "jdbc:postgresql://localhost:5432/SURVEY";
        Connection con = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement2 = null;
        PreparedStatement preparedStatement3 = null;

        Scanner scanner = new Scanner(System.in);
        ResultSet rs = null;
        Statement statement = null;
        Statement stmnt = null;

        try {
            con = DriverManager.getConnection(conString, "postgres", "0000");
            statement = con.createStatement();
            rs = statement.executeQuery("SELECT survey_id, title, description, created_at, user_id FROM surveys ORDER BY survey_id");

            Class.forName("org.postgresql.Driver");

            preparedStatement = con.prepareStatement("INSERT INTO surveys (title, description) VALUES (?, ?)");


            System.out.println("Enter survey title:");
            String title = scanner.nextLine();
            System.out.println("Enter survey description:");
            String description = scanner.nextLine();


            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);

            Survey survey1 = new Survey();
            while(rs.next()) {
                if(rs.isLast()) {
                    survey1 = new Survey(user_id, title, description, rs.getInt("survey_id"));
                }
            }



            surveys.add(survey1);

            stmnt = con.createStatement();
            ResultSet rsQuestion = stmnt.executeQuery("SELECT question_id, survey_id, question_text FROM questions ORDER BY question_id");
            ResultSet rsOption = statement.executeQuery("SELECT option_id, question_id, option_text FROM options ORDER BY option_id");
            preparedStatement2 = con.prepareStatement("INSERT INTO questions (survey_id, question_text) VALUES (?, ?)");
            preparedStatement3 = con.prepareStatement("INSERT INTO options (question_id, option_text) VALUES(?, ?)");
            System.out.println("How many questions you want to add?");
            int questionNumber = scanner.nextInt();
            Question question = new Question();
            scanner.nextLine();
            for (int i = 1; i <= questionNumber; i++) {
                System.out.println("Your question number " + i + " is?");
                String text = scanner.nextLine();

                preparedStatement2.setInt(1, survey1.getId());
                preparedStatement2.setString(2, text);
                preparedStatement2.executeUpdate();

                int question_id = 0;
                while(rsQuestion.next()) {
                    if(rsQuestion.isLast()){
                        question_id = rsQuestion.getInt("question_id");
                    }
                }
                while(rsQuestion.next()) {
                    if (rsQuestion.isLast()) {
                        question = new Question(survey1.getId(), text, rsQuestion.getInt("question_id"));
                    }
                }

                for(int j = 1; j <= 4; j++){
                    System.out.println("Your option number " + j + ":");
                    String optionText = scanner.nextLine();

                    preparedStatement3.setInt(1, question_id);
                    preparedStatement3.setString(2, optionText);
                    preparedStatement3.addBatch();
                    while(rsOption.next()) {
                        if(rsOption.isLast()) {
                            Option option = new Option(optionText, question_id, rsOption.getInt("option_id"));
                            question.addOption(option);
                        }
                    }
                }
                preparedStatement3.executeBatch();

                preparedStatement2.setInt(1, survey1.getId());
                preparedStatement2.setString(2, text);

                survey1.addQuestion(question);
            }
            int rowsAffected = preparedStatement.executeUpdate();
            preparedStatement3.executeUpdate();
            preparedStatement2.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Survey created successfully!");
            } else {
                System.out.println("Failed to create Survey.");
            }


        }
        catch(SQLException e){
            System.out.println("connection server: " + e.getMessage());
        } catch(ClassNotFoundException e){
            System.out.println("driver not found: " + e.getMessage());
        }finally {
            try {
                if (con != null)
                    con.close();
                if (rs != null)
                    rs.close();
                if (statement != null)
                    statement.close();
                if (preparedStatement != null)
                    preparedStatement.close();
                if (preparedStatement2 != null)
                    preparedStatement2.close();
                if (preparedStatement3 != null)
                    preparedStatement3.close();
            } catch (SQLException e) {
                System.out.println("could not close connection: " + e.getMessage());
            }
        }
    }
    public void participateSurvey(int user_id) {
        String conString = "jdbc:postgresql://localhost:5432/SURVEY";
        Connection con = null;
        Scanner scanner = new Scanner(System.in);
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<Survey> surveysTable = new ArrayList<Survey>();
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(conString, "postgres", "0000");
            statement = con.createStatement();
            rs = statement.executeQuery("SELECT survey_id, title, description, user_id FROM surveys ORDER BY survey_id");

            PreparedStatement ps = con.prepareStatement("SELECT count(*) FROM questions WHERE survey_id = ?");

            while (rs.next()) {
                int survey_id = rs.getInt("survey_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                int userId = rs.getInt("user_id");
                Survey survey = new Survey(userId, title, description, survey_id);
                PreparedStatement preparedStatement2 = con.prepareStatement("SELECT question_id, survey_id, question_text FROM questions WHERE survey_id = ?");
                preparedStatement2.setInt(1, survey_id);
                ResultSet questionsRs = preparedStatement2.executeQuery();
                while(questionsRs.next()) {
                    int question_id = questionsRs.getInt("question_id");
                    int question_survey_id = questionsRs.getInt("survey_id");
                    String question_text = questionsRs.getString("question_text");
                    PreparedStatement preparedStatement3 = con.prepareStatement("SELECT question_id, option_id, option_text FROM options WHERE question_id = ?");
                    preparedStatement3.setInt(1, question_id);
                    ResultSet optionRs = preparedStatement3.executeQuery();

                    Question question = new Question();

                    while(optionRs.next()) {
                        int option_question_id = optionRs.getInt("question_id");
                        int option_id = optionRs.getInt("option_id");
                        String option_text = optionRs.getString("option_text");

                        Option option = new Option(option_text, option_question_id, option_id);
                        question.addOption(option);
                    }
                    question = new Question(question_survey_id, question_text, question_id);

                    survey.addQuestion(question);
                }
                surveysTable.add(survey);
            }

            for (Survey survey : surveysTable) {
                System.out.println(survey);
                System.out.println(survey.getQuestions());
            }

            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO responses (response_id, survey_id, question_id, user_id, answer) VALUES (?, ?, ?, ?, ?)");
            System.out.println("Which survey you want to answer? (Enter survey_id) ");
            int selectedSurveyId = scanner.nextInt();

            ArrayList<Response> responsesList = new ArrayList<Response>();
            for (Survey survey : surveysTable) {
                if (survey.getId() == selectedSurveyId) {
                    System.out.println(survey.getTitle());
                    ps.setInt(1, selectedSurveyId);
                    ResultSet resultSet = ps.executeQuery();
                    for (Question question : survey.getQuestions()) {
                        System.out.println(question.getText());
                        System.out.println("1. " + question.getOptions().get(1).getText());
                        System.out.println("2. " + question.getOptions().get(2).getText());
                        System.out.println("3. " + question.getOptions().get(3).getText());
                        System.out.println("4. " + question.getOptions().get(4).getText());
                        System.out.print("Your answer:");
                        int answer = scanner.nextInt();
                        Response response = new Response(selectedSurveyId, question.getQuestionId(), user_id, answer);
                        responsesList.add(response);
                        preparedStatement.setInt(1, response.getResponse_id());
                        preparedStatement.setInt(2, response.getSurvey_id());
                        preparedStatement.setInt(3, response.getQuestion_id());
                        preparedStatement.setInt(4, response.getUser_id());
                        preparedStatement.setInt(5, response.getAnswer());
                    }
                }
            }

        }
        catch (SQLException e) {
            System.out.println("connection server: "+ e.getMessage());
        }
        catch (ClassNotFoundException e) {
            System.out.println("Class not found: " + e.getMessage());
        }

    }


}
