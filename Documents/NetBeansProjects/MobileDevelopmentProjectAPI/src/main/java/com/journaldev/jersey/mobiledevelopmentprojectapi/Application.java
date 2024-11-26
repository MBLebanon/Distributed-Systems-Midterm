/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.journaldev.jersey.mobiledevelopmentprojectapi;

/**
 *
 * @author MBLebanon
 */
@jakarta.ws.rs.ApplicationPath("/api/*")
public class Application extends org.glassfish.jersey.server.ResourceConfig {

    public Application() {
        packages("com.journaldev.jersey.mobiledevelopmentprojectapi.resources");
        register(org.glassfish.jersey.server.mvc.MvcFeature.class);
        register(org.glassfish.jersey.server.mvc.jsp.JspMvcFeature.class);

    }

}
