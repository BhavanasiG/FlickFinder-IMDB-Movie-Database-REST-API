package com.flickfinder.controller;

import java.sql.SQLException;
import java.util.List;

import com.flickfinder.dao.PersonDAO;
import com.flickfinder.model.Movie;
import com.flickfinder.model.Person;

import io.javalin.http.Context;

/**
 * The controller for the people endpoints.
 */

public class PersonController {

	// to complete the must-have requirements you need to add the following methods:
	// getAllPeople
	// getPersonById
	// you will add further methods for the more advanced tasks; however, ensure your have completed 
	// the must have requirements before you start these.  
	
	/**
	 * The people data access object.
	 */

	private final PersonDAO personDAO;
	
	/**
	 * Constructs a PersonController object and initalizes the personDAO. 
	 *
	 */
	
	public PersonController(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}
	
	/**
	 * Returns a list of people in the database.
	 * Limited to 50, if no limit is specified, or an invalid limit.
	 * 
	 * @param ctx
	 */
	
	public void getAllPeople(Context ctx) {
		try {
			String limit = ctx.queryParam("limit");
			
			if (limit != null) {
				if ((limit.matches("[0-9]+")) && (limit.length()<10)) {
					ctx.json(personDAO.getAllPeopleByLimit(Integer.parseInt(limit)));
					return;
				}  {
					ctx.json(personDAO.getAllPeople());
					return;
				}
				
			} 
			 {
				ctx.json(personDAO.getAllPeople());
				return;
			}
			
		} catch (SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the person with the specified id.
	 * 
	 * @param ctx
	 */
	
	public void getPersonById(Context ctx) {
		boolean valid = (ctx.pathParam("id").matches("[0-9]+") && ctx.pathParam("id").length()<10 && Integer.parseInt(ctx.pathParam("id"))>=1);
		if (!valid) {
			ctx.status(400);
			ctx.result("Invalid id");
			return;
		}
		
		int id = Integer.parseInt(ctx.pathParam("id"));
		try {
			Person person = personDAO.getPersonById(id);
			if (person == null) {
				ctx.status(404);
				ctx.result("Person not found");
				return;
			}
			ctx.json(personDAO.getPersonById(id));
		} catch (SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the movies associated with a specific star
	 * @param ctx the Javalin Context
	 */
	public void getMoviesStarringPerson(Context ctx) {
		boolean valid = (ctx.pathParam("id").matches("[0-9]+") && ctx.pathParam("id").length()<10 && Integer.parseInt(ctx.pathParam("id"))>=1);
		if (!valid) {
			ctx.status(400);
			ctx.result("Invalid id");
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		try {
			List<Movie> movies = personDAO.getMoviesByPersonId(id);
			if (movies == null) {
				ctx.status(404);
				ctx.result("Movie(s) not found");
				return;
			}  {
				ctx.json(movies);
				return;
			}
		} catch(SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
}