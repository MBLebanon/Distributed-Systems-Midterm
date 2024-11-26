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

/**
 *
 * @author MBLebanon
 */
public class Comment {

    public static String addComment(String username, String title, String comment) throws SQLException {
        try {
            if(comment.isEmpty())
                return "Empty Comment";
            Connection connection = DataBaseConnection.connect();

            // Selecting comments JSON array for the specified recipe
            String selectQuery = "SELECT comments FROM recipes WHERE title = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setString(1, title);
            ResultSet resultSet = selectStatement.executeQuery();

            JSONArray commentsArray;
            if (resultSet.next()) {
                String commentsJSON = resultSet.getString("comments");
                if (commentsJSON == null || commentsJSON.isEmpty())
                    commentsArray = new JSONArray();
                else
                    commentsArray = new JSONArray(commentsJSON);

                // Check if the comment already exists
                String commentToCheck = username + ": " + comment;
                for (int i = 0; i < commentsArray.length(); i++)
                    if (commentsArray.getString(i).equals(commentToCheck))
                        return "Comment already made";

                // Add the comment
                commentsArray.put(commentToCheck);

                // Update the comments in the database
                String updateQuery = "UPDATE recipes SET comments = ? WHERE title = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                updateStatement.setObject(1, commentsArray,java.sql.Types.OTHER);
                updateStatement.setString(2, title);
                updateStatement.executeUpdate();

                return "Commented on " + title;
            } else
                return "Recipe not found";
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error occurred";
        }
    }
}
