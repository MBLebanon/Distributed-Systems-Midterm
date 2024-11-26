/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.journaldev.jersey.mobiledevelopmentprojectapi.resources;

import com.journaldev.jersey.mobiledevelopmentprojectapi.DataBaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author MBLebanon
 */
public class Recipe {

    public static String addNewRecipe(String title, org.json.JSONArray stepsData) {
        String insertQuery = "INSERT INTO recipes (title, steps_data) VALUES (?, ?);";
        try (Connection connection = DataBaseConnection.connect()) {
            try (java.sql.PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setString(1, title);
                insertStatement.setObject(2, stepsData, java.sql.Types.OTHER);

                int rowsAffected = insertStatement.executeUpdate();

                if (rowsAffected == 1)
                    return "New recipe added successfully";
                else
                    return "Failed to add new recipe";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "An error occurred while adding new recipe";
        }

    }
    
     public static String likeRecipe(String username, String title) {
         System.out.println(username+" "+title);
        // SQL queries
        String selectQuery = "SELECT likes FROM recipes WHERE title = ?";
        String updateQuery = "UPDATE recipes SET likes = ? WHERE title = ?";

        try (Connection connection = DataBaseConnection.connect();
             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
             PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

            // Set the title parameter for the select query
            selectStatement.setString(1, title);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Extract the current likes array from the result set
                String likesJSON = resultSet.getString("likes");
                JSONArray currentLikes = new JSONArray(likesJSON);

                // Check if the username is already present in the likes array
                boolean alreadyLiked = false;
                for (int i = 0; i < currentLikes.length(); i++) {
                    if (currentLikes.getString(i).equals(username)) {
                        alreadyLiked = true;
                        break;
                    }
                }

                if (alreadyLiked) {
                    return "Already Liked";
                } else {
                    // Add the username to the likes array
                    currentLikes.put(username);

                    // Set the likes parameter for the update query
                    updateStatement.setObject(1, currentLikes,java.sql.Types.OTHER);
                    updateStatement.setString(2, title);
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        return "Liked";
                    } else {
                        return "Failed to update likes";
                    }
                }
            } else {
                return "Recipe Not Found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error";
        } catch (JSONException e) {
            e.printStackTrace();
            return "JSON Error";
        }
    }

    public static JSONArray retrieveRecipes(String searchTerm) {
        JSONArray jsonArray = new JSONArray();
        String selectQuery;
        if (!searchTerm.isEmpty())
            selectQuery = "SELECT title, steps_data, comments, likes FROM recipes WHERE title ILIKE ? OR SOUNDEX(title) = SOUNDEX(?);";
        else
            selectQuery = "SELECT title, steps_data, comments, likes FROM recipes;";

        try (Connection connection = DataBaseConnection.connect(); PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            if (!searchTerm.isEmpty()) {
                selectStatement.setString(1, "%" + searchTerm + "%");
                selectStatement.setString(2, searchTerm);

            }

            try (ResultSet resultSet = selectStatement.executeQuery()) {
                while (resultSet.next()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", resultSet.getString("title"));
                    jsonObject.put("data", resultSet.getString("steps_data"));
                    JSONArray likesArray = new org.json.JSONArray(resultSet.getString("likes"));
                    jsonObject.put("likes", likesArray.length());
    String commentsJSON = resultSet.getString("comments");

    StringBuilder recordsBuilder = new StringBuilder();

    JSONArray commentsArray = new JSONArray(commentsJSON);

    for (int i = 0; i < commentsArray.length(); i++) {
        String comment = commentsArray.getString(i);
        recordsBuilder.append(comment);
        if (i < commentsArray.length() - 1) {
            recordsBuilder.append("\n");
        }
    }
    String allRecords = recordsBuilder.toString();
                    jsonObject.put("comments", allRecords);
                    jsonArray.put(jsonObject);
                }
            }
        } catch (SQLException | JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

}
