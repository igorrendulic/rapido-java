package com.igor.run;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.igor.rest.RestServices;
import com.igor.servlets.MyServlet;
import com.igor.util.Utils;

/**
 * @author Igor Rendulic
 */
public class App 
{
	
	public static Boolean isProduction = false;
	final static Logger log = Logger.getLogger(App.class.getCanonicalName()); 
	
    public static void main( String[] args )
    {
    	// Determining if run locally or from deployed docker version (configured in yaml file)
        isProduction = Utils.isProduction(args);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    	context.setContextPath("/");
    	
    	// webapp part for serving static files and html
    	ResourceHandler resource_handler = new ResourceHandler();
    	resource_handler.setDirectoriesListed(false);
        resource_handler.setWelcomeFiles(new String[]{ "index.html" });
        resource_handler.setResourceBase("src/main/webapp");
        
        // CORS filter (so request can come in from various devices)
        FilterHolder filterHolder = context.addFilter(CrossOriginFilter.class,"/*",EnumSet.allOf(DispatcherType.class));
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM,"*");
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM,"Content-Type,Authorization,X-Forwarded-For,X-Requested-With,Content-Length,Accept,Origin");
        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM,"GET,PUT,POST,DELETE,OPTIONS");
        filterHolder.setAsyncSupported(true);
        
        // response headers
        filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filterHolder.setInitParameter("allowCredentials", "true");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resource_handler,context });
    
        // setting up a server on port 8080
        Server server = new Server(8080);
        server.setHandler(handlers);
        
        // adding a servlet configuration
        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        context.addServlet(new ServletHolder(MyServlet.class), "/myservlet");
        jerseyServlet.setInitOrder(0);

        Map<String,String> initParams = new HashMap<>();
		initParams.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.moxy.json.MoxyJsonFeature,"
				+ RestServices.class.getCanonicalName());
        
        jerseyServlet.setInitParameters(initParams);
        
        try {
        	server.start();
        	server.join();
        	log.info("Server starter");
        } catch (Exception e) {
        	log.log(Level.SEVERE, e.getMessage(), e);
        } finally {
        	log.info("Server stopped");
        	server.destroy();
        }
    }
}
