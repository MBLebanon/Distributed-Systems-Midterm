package com.journaldev.jersey.mobiledevelopmentprojectapi.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**
 *
 * @author
 */
@Path("jakartaee10")
public class JakartaEE10Resource {

    @GET
    public Response ping() {
        return Response
                .ok("i am mobile api.")
                .build();
    }

    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/retrieveRecipes")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response retrieveRecipes(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        String searchTerm = jsonNode.get("searchTerm").asText();

        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(Recipe.retrieveRecipes(searchTerm).toString())
                .build();

    }
    
    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/likeRecipe")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response likeRecipe(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        String username = null;
        String title = null;

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        username = jsonNode.get("username").asText();
        title = jsonNode.get("title").asText();
        

        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(Recipe.likeRecipe(username, title))
                .build();

    }

    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/addComment")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response addComment(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        String title = null;
        String comment = null;
        String username = null;
        

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        comment = jsonNode.get("comment").asText();
        title = jsonNode.get("title").asText();
        username = jsonNode.get("username").asText();

        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(Comment.addComment(username, title, comment))
                .build();

    }

    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/addNewRecipe")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response addNewRecipe(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        String title = null;
        org.json.JSONArray stepsData = new org.json.JSONArray();
        
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        title = jsonNode.get("recipeTitle").asText();
        stepsData = new org.json.JSONArray(jsonNode.get("stepsData").toString());

        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(Recipe.addNewRecipe(title, stepsData))
                .build();

    }

    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/register")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response register(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        String username = null;
        String password = null;

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        username = jsonNode.get("username").asText();
        password = jsonNode.get("password").asText();

        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(User.register(username, password))
                .build();
    }

    @jakarta.ws.rs.POST
    @jakarta.ws.rs.Path("/login")
    @jakarta.ws.rs.Produces(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    @jakarta.ws.rs.Consumes(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
    public jakarta.ws.rs.core.Response login(String data, @jakarta.ws.rs.core.Context jakarta.ws.rs.core.HttpHeaders headers) throws SQLException, IOException, NoSuchAlgorithmException {

        String username = null;
        String password = null;

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(data);
        username = jsonNode.get("username").asText();
        password = jsonNode.get("password").asText();

        String functionResponse = null;

        functionResponse = User.login(username, password);

        if (null != functionResponse)

            switch (functionResponse) {
                case "Username or Password Wrong!":
                    return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                            .entity("Username or Password Wrong!")
                            .build();
                case "Access Denied!":
                    return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                            .entity("Access Denied!")
                            .build();
                case "Error processing data!":
                    return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("Error processing data!")
                            .build();
                case "No data provided!":
                    return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.BAD_REQUEST)
                            .entity("No data provided!")
                            .build();
                default:
                    break;
            }
        return jakarta.ws.rs.core.Response.status(jakarta.ws.rs.core.Response.Status.OK)
                .entity(functionResponse)
                .build();

    }
}
