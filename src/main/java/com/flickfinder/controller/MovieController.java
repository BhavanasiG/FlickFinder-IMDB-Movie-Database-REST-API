package com.flickfinder.controller;

import java.sql.SQLException;

import com.flickfinder.dao.MovieDAO;
import com.flickfinder.model.Movie;

import io.javalin.http.Context;

/**
 * The controller for the movie endpoints.
 * 
 * The controller acts as an intermediary between the HTTP routes and the DAO.
 * 
 * As you can see each method in the controller class is responsible for
 * handling a specific HTTP request.
 * 
 * Methods a Javalin Context object as a parameter and uses it to send a
 * response back to the client.
 * We also handle business logic in the controller, such as validating input and
 * handling errors.
 *
 * Notice that the methods don't return anything. Instead, they use the Javalin
 * Context object to send a response back to the client.
 */

public class MovieController {

	/**
	 * The movie data access object.
	 */

	private final MovieDAO movieDAO;

	/**
	 * Constructs a MovieController object and initializes the movieDAO.
	 */
	public MovieController(MovieDAO movieDAO) {
		this.movieDAO = movieDAO;
	}

	/**
	 * Returns a list of all movies in the database.
	 * Limited to 50, if no limit is specified.
	 * 
	 * @param ctx the Javalin context
	 */
	public void getAllMovies(Context ctx) {
		try {
			String limit = ctx.queryParam("limit");
			
			if (limit != null) {
				ctx.json(movieDAO.getAllMoviesByLimit(Integer.parseInt(limit)));
			}
			else {
				ctx.json(movieDAO.getAllMovies());
			}
			
		} catch (SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the movie with the specified id.
	 * 
	 * @param ctx the Javalin context
	 */
	public void getMovieById(Context ctx) {

		int id = Integer.parseInt(ctx.pathParam("id"));
		try {
			Movie movie = movieDAO.getMovieById(id);
			if (movie == null) {
				ctx.status(404);
				ctx.result("Movie not found");
				return;
			}
			ctx.json(movieDAO.getMovieById(id));
		} catch (SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}

	/**
	 * Returns the stars associated with a specific movie
	 * @param ctx the Javalin Context
	 */
	public void getPeopleByMovieId(Context ctx) {
		int id = Integer.parseInt(ctx.pathParam("id"));
		try {
			ctx.json(movieDAO.getStarsByMovieId(id));
		} catch(SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the movie ratings for a given year
	 * A limit on the number of votes and number of movies to be returned can be specified.
	 *  Default value for votes is 1000 and default value for limit is 50
	 * @param ctx the Javalin Context
	 */
	public void getRatingsByYear(Context ctx) {
		int year = Integer.parseInt(ctx.pathParam("year"));
		try {
			String limit = ctx.queryParam("limit");
			String votes = ctx.queryParam("votes");
			
			if (limit != null & votes == null) {
				ctx.json(movieDAO.getMovieRatingsByYearAndLimit(year, Integer.parseInt(limit)));
				
			} else if (votes != null & limit == null) {
				ctx.json(movieDAO.getMovieRatingsByYearAndVoteLimit(year, Integer.parseInt(votes)));
				
			} else if (votes != null & limit != null){
				ctx.json(movieDAO.getMovieRatingsByYearLimitVoteLimit(year, Integer.parseInt(limit), Integer.parseInt(votes)));
			} else {
				ctx.json(movieDAO.getMovieRatingsByYear(year));
			}
		} catch(SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
}