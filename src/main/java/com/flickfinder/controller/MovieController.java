package com.flickfinder.controller;

import java.sql.SQLException;
import java.util.List;

import com.flickfinder.dao.MovieDAO;
import com.flickfinder.model.Movie;
import com.flickfinder.model.MovieRating;
import com.flickfinder.model.Person;

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
	 * Limited to 50, if no limit is specified, or, an invalid limit.
	 * 
	 * @param ctx the Javalin context
	 */
	public void getAllMovies(Context ctx) {
		try {
			String limit = ctx.queryParam("limit");
			
			if (limit != null) {
				if (limit.matches("[0-9]+")) {
					ctx.json(movieDAO.getAllMoviesByLimit(Integer.parseInt(limit)));
				} else {
					ctx.json(movieDAO.getAllMovies());
				}
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
		if (!ctx.pathParam("id").matches("[0-9]+") | Integer.parseInt(ctx.pathParam("id"))<1) {
			ctx.status(400);
			ctx.result("Invalid id");
			return;
		}
		
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
		if (!ctx.pathParam("id").matches("[0-9]+")) {
			ctx.status(404);
			ctx.result("Invalid id");
			return;
		}
		int id = Integer.parseInt(ctx.pathParam("id"));
		try {
			List<Person> stars = movieDAO.getStarsByMovieId(id);
			if (stars == null) {
				ctx.status(404);
				ctx.result("Star(s) not found");
				return;
			}
			ctx.json(stars);
		} catch(SQLException e) {
			ctx.status(500);
			ctx.result("Database error");
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the movie ratings for a given year, a check is made to see if year is valid.
	 * A limit on the number of votes and number of movies to be returned can be specified.
	 * If either limit or both are invalid, it routes to default result.
	 *  Default value for votes is 1000 and default value for limit is 50
	 * @param ctx the Javalin Context
	 */
	public void getRatingsByYear(Context ctx) {
		//System.out.println("Param: " + ctx.pathParam("year"));
		boolean validYear = ctx.pathParam("year").matches("[0-9]+") && Integer.parseInt(ctx.pathParam("year")) <= 2024;
		
		if(!ctx.pathParam("year").matches("[0-9]+") | validYear == false) {
			ctx.status(400);
			ctx.result("Invalid year");
			return;
		}
		
		else if (ctx.pathParam("year").matches("[0-9]+")) {
			int year = Integer.parseInt(ctx.pathParam("year"));
			try {
				String limit = ctx.queryParam("limit");
				//System.out.println(limit);
				String votes = ctx.queryParam("votes");
				//System.out.println(votes);
				
				if (limit != null & votes == null) {
					if (limit.matches("[0-9]+")) {
						ctx.json(movieDAO.getMovieRatingsByYearAndLimit(year, Integer.parseInt(limit)));
					} else {
						ctx.json(movieDAO.getMovieRatingsByYear(year));
					}
					
				} else if (votes != null & limit == null) {
					if (votes.matches("[0-9]+")) {
						List<MovieRating> ratings = movieDAO.getMovieRatingsByYearAndVoteLimit(year, Integer.parseInt(votes));
						if (ratings == null) {
							ctx.status(404);
							ctx.result("Movie(s) not found");
							return;
						}
						ctx.json(ratings);
					} else {
						ctx.json(movieDAO.getMovieRatingsByYear(year));
					}
					
				} else if (votes != null & limit != null){ // to check both the url has to be in the form of /movies/ratings/{year}?query1=[int]&query2=[int]
					if (votes.matches("[0-9]+") && limit.matches("[0-9]+")) {
						List<MovieRating> ratings = movieDAO.getMovieRatingsByYearLimitVoteLimit(year, Integer.parseInt(limit), Integer.parseInt(votes));
						if (ratings == null) {
							ctx.status(404);
							ctx.result("Movie(s) not found");
							return;
						}
						ctx.json(ratings);
					} else {
						ctx.json(movieDAO.getMovieRatingsByYear(year));
					}
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
}