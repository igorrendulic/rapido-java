package com.igor.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.igor.model.Ping;

/**
 * @author Igor Rendulic
 */
@Path("/api")
public class RestServices {
	
	@GET
	@Produces("application/json")
	@Path("/ping")
	public Ping ping(@QueryParam("echo") String echo) {
		
		return new Ping(LocalDateTime.now(ZoneId.of("UTC")).toEpochSecond(ZoneOffset.UTC) + "", echo);
	}
}
