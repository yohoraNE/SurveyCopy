package org.example;

import org.example.Services.UserService;
import org.example.entities.User;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {
        String conString = "jdbc:postgresql://localhost:5432/SURVEY";
        Connection con = null;
        ResultSet rs = null;
        Statement statement = null;
        ArrayList<User> users = new ArrayList<User>();
        User user1 = new User();
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(conString, "postgres", "0000");
            statement = con.createStatement();
            rs = statement.executeQuery("SELECT id,name,surname,username, password FROM users ORDER BY id");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String username = rs.getString("username");
                String password = rs.getString("password");
                User user = new User(name, surname, username, password, id);
                users.add(user);
            }


        } catch (SQLException e) {
            System.out.println("connection server: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("driver not found: " + e.getMessage());
        }

        for (User user : users) {
            System.out.println(user);
        }


        //Initializing preparedStatement object
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Scanner scanner = new Scanner(System.in);
        Statement stat = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(conString, "postgres", "0000");
            // Create a PreparedStatement with a parameterized INSERT query
            stat = con.createStatement();
            resultSet = statement.executeQuery("SELECT id FROM users ORDER BY id");
            preparedStatement = con.prepareStatement("INSERT INTO users (name, surname, username, password) VALUES (?, ?, ?, ?)");

            int rowNum = users.size();

            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            Boolean registered = false;
            for (User user : users) {
                if (user.getUsername().equals(username)) {
                    registered = true;
                    System.out.println("Enter Password:");
                    String enteredPassword = scanner.nextLine();

                    if (user.getPassword().equals(enteredPassword)) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Incorrect password.");

                    }
                    break;
                }
            }

            if (!registered) {
                System.out.println("Enter Name:");
                String name = scanner.nextLine();
                System.out.println("Enter Surname:");
                String surname = scanner.nextLine();
                System.out.println("Create Password:");
                String password = scanner.nextLine();


                preparedStatement.setString(1, name);
                preparedStatement.setString(2, surname);
                preparedStatement.setString(3, username);
                preparedStatement.setString(4, password);

                while(resultSet.next()) {
                    if (resultSet.getString("username") == username){
                        user1 = new User(name, surname, username, password, resultSet.getInt(resultSet.getInt("id")));
                    }
                }

                users.add(user1);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("User registered successfully!");
                } else {
                    System.out.println("Failed to register user.");
                }
            }

        } catch (SQLException e) {
            e.getMessage();
        }
        catch(ClassNotFoundException e) {
            e.getMessage();
        }



        finally {
            // Close resources in a finally block
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            System.out.println("Create survey: 1 " + "\n" + "Participate in a survey: 2");
            int decision = scanner.nextInt();
            UserService userService = new UserService();
            if(decision == 1) {
                userService.createSurvey(user1.getId());
            }
            else if(decision == 2) {
                userService.participateSurvey(user1.getId());
            }
            else {
                throw new InputMismatchException("1 or 2 should be entered");
            }
        }
        catch(InputMismatchException e) {
            System.out.println("Invalid input: " + e.getMessage());
        }
    }
}