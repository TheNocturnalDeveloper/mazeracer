module nl.fontys.se3.presentation {
    requires nl.fontys.se3.logic;
    requires io.javalin;
    requires j2html;
    requires com.fasterxml.jackson.databind;
    requires jackson.annotations;
    requires org.eclipse.jetty.websocket.api;
    requires io.swagger.v3.core;
    requires io.swagger.v3.oas.models;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.module.kotlin; //optional?
    requires java.xml.bind;
    requires java.activation;
    requires org.eclipse.jetty.server;
    requires javax.servlet.api;
    requires nl.fontys.se3.data; //needed for session extraction from httpsession

    opens nl.fontys.se3.presentation.models to  com.fasterxml.jackson.databind;

}