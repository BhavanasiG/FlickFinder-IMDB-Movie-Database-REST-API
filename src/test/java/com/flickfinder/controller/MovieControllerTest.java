package com.flickfinder.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flickfinder.dao.MovieDAO;

import io.javalin.http.Context;

/**
 * Test for the Movie Controller.
 */

class MovieControllerTest {

	/**
	 *
	 * The context object, later we will mock it.
	 */
	private Context ctx;

	/**
	 * The movie data access object.
	 */
	private MovieDAO movieDAO;

	/**
	 * The movie controller.
	 */

	private MovieController movieController;

	@BeforeEach
	void setUp() {
		// We create a mock of the MovieDAO class.
		movieDAO = mock(MovieDAO.class);
		// We create a mock of the Context class.
		ctx = mock(Context.class);

		// We create an instance of the MovieController class and pass the mock object
		movieController = new MovieController(movieDAO);
	}

	/**
	 * Tests the getAllMovies method.
	 * We expect to get a list of all movies in the database.
	 */

	@Test
	void testGetAllMovies() {
		movieController.getAllMovies(ctx);
		try {
			verify(movieDAO).getAllMovies();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test that the controller returns a 500 status code when a database error
	 * occurs
	 * 
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetAllDatabaseError() throws SQLException {
		when(movieDAO.getAllMovies()).thenThrow(new SQLException());
		movieController.getAllMovies(ctx);
		verify(ctx).status(500);
	}

	/**
	 * Tests the getMovieById method.
	 * We expect to get the movie with the specified id.
	 */

	@Test
	void testGetMovieById() {
		when(ctx.pathParam("id")).thenReturn("1");
		movieController.getMovieById(ctx);
		try {
			verify(movieDAO).getMovieById(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test a 500 status code is returned when a database error occurs.
	 * 
	 * @throws SQLException
	 */

	@Test
	void testThrows500ExceptionWhenGetByIdDatabaseError() throws SQLException {
		when(ctx.pathParam("id")).thenReturn("1");
		when(movieDAO.getMovieById(1)).thenThrow(new SQLException());
		movieController.getMovieById(ctx);
		verify(ctx).status(500);
	}

	/**
	 * Test that the controller returns a 404 status code when a movie is not found
	 * or
	 * database error.
	 * 
	 * @throws SQLException
	 */

	@Test
	void testThrows404ExceptionWhenNoMovieFound() throws SQLException {
		when(ctx.pathParam("id")).thenReturn("1");
		when(movieDAO.getMovieById(1)).thenReturn(null);
		movieController.getMovieById(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Test a 400 status code is returned for invalid id parameter
	 * @throws SQLException
	 */
	@Test
	void testThrows400ExceptionWhenInvalidMovieId() throws SQLException{
		when(ctx.pathParam("id")).thenReturn("0");
		movieController.getMovieById(ctx);
		verify(ctx).status(400);
	}
	
	/**
	 * Test a 400 status code is returned for invalid id parameter
	 * @throws SQLException
	 */
	@Test
	void testThrows400ExceptionWhenInvalidMovieId2() throws SQLException{
		when(ctx.pathParam("id")).thenReturn("abc");
		movieController.getMovieById(ctx);
		verify(ctx).status(400);
	}

	/**
	 * Tests the getStarsByMovie
	 */
	@Test
	void testGetStarsByMovieId() {
		when(ctx.pathParam("id")).thenReturn("1");
		movieController.getPeopleByMovieId(ctx);
		try {
			verify(movieDAO).getStarsByMovieId(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test a 500 status code is returned when a database error occurs.
	 * 
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetByMovieIdDatabaseError() throws SQLException {
		when(ctx.pathParam("id")).thenReturn("1");
		when(movieDAO.getStarsByMovieId(1)).thenThrow(new SQLException());
		movieController.getPeopleByMovieId(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test that the controller returns a 404 status code when a star is not found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenNoStarsFound() throws SQLException{
		when(ctx.pathParam("id")).thenReturn("1");
		when(movieDAO.getStarsByMovieId(1)).thenReturn(null);
		movieController.getPeopleByMovieId(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Tests the getMovieRatingsByYear
	 */
	@Test
	void testGetMovieRatingsByYear() {
		when(ctx.pathParam("year")).thenReturn("1994");
		movieController.getRatingsByYear(ctx);
		try {
			verify(movieDAO).getMovieRatingsByYear(1994);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test a 500 status code is returned when a database error occurs.
	 * 
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMovieRatingsByYearDatabaseError() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(movieDAO.getMovieRatingsByYear(1994)).thenThrow(new SQLException());
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test a 400 status code is returned if invalid year.
	 * @throws SQLException
	 */
	@Test
	void testThrows400ExceptionWhenGetMovieRatingsByInvalidYear() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("2028");
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(400);
	}
	
	/**
	 * Test a 400 status code is returned if invalid year.
	 * @throws SQLException
	 */
	@Test
	void testThrows400ExceptionWhenGetMovieRatingsByInvalidYear2() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("-123");
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(400);
	}
	
	/**
	 * Test a 400 status code is returned if invalid year.
	 * @throws SQLException
	 */
	@Test
	void testThrows400ExceptionWhenGetMovieRatingsByInvalidYear3() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("abc");
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(400);
	}
	
	/**
	 * Test a 404 status code is returned if no movie ratings are found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenNoMovieRatingsFound() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("9");
		when(movieDAO.getMovieRatingsByYear(9)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Tests the getAllMovies, but also specifies a limit on the number of movies
	 */
	@Test
	void testGetAllMoviesByLimit() {
		when (ctx.queryParam("limit")).thenReturn("1");
		movieController.getAllMovies(ctx);
		try {
			verify(movieDAO).getAllMoviesByLimit(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests a 500 status code is shown when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMoviesByLimitDatabaseError() throws SQLException{
		when(ctx.queryParam("limit")).thenReturn("1");
		when(movieDAO.getAllMoviesByLimit(1)).thenThrow(new SQLException());
		movieController.getAllMovies(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Tests the getAllMovies, but also specifies a limit on the number of movies
	 */
	@Test
	void testGetAllMoviesByLimit2() {
		when (ctx.queryParam("limit")).thenReturn("2");
		movieController.getAllMovies(ctx);
		try {
			verify(movieDAO).getAllMoviesByLimit(2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests a 500 status code is shown when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMoviesByLimit2DatabaseError() throws SQLException{
		when(ctx.queryParam("limit")).thenReturn("2");
		when(movieDAO.getAllMoviesByLimit(2)).thenThrow(new SQLException());
		movieController.getAllMovies(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Tests the getAllMoviesRatings, but also specifies a limit on the number of movie ratings
	 */
	@Test
	void testGetAllMovieRatingsByLimit1() {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("limit")).thenReturn("2");
		movieController.getRatingsByYear(ctx);
		try {
			verify(movieDAO).getMovieRatingsByYearAndLimit(1994, 2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests a 500 status code is shown when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMovieRatingsByLimitDatabaseError() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("limit")).thenReturn("2");
		when(movieDAO.getMovieRatingsByYearAndLimit(1994, 2)).thenThrow(new SQLException());
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test a 404 status code is returned if no movie ratings are found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenNoMovieRatingsFoundWithLimit() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("9");
		when(ctx.queryParam("limit")).thenReturn("2");
		when(movieDAO.getMovieRatingsByYearAndLimit(9, 2)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Tests the getAllMovieRatings, but also specifies a minimum votes
	 */
	@Test
	void testGetAllMovieRatingsByVotes() {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("100");
		movieController.getRatingsByYear(ctx);
		try {
			verify(movieDAO).getMovieRatingsByYearAndVoteLimit(1994, 100);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests a 500 status code is shown when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMovieRatingsByVoteLimitDatabaseError() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("100");
		when(movieDAO.getMovieRatingsByYearAndVoteLimit(1994, 100)).thenThrow(new SQLException());
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test that the controller returns a 404 status code when no movie/movie rating is found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenGetMovieRatingsByTooHighVotes() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("1000000");
		when(movieDAO.getMovieRatingsByYearAndVoteLimit(1994,1000000)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Test a 404 status code is returned if no movie ratings are found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenNoMovieRatingsFoundWithInvalidYearAndValidVotes() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("9");
		when(ctx.queryParam("votes")).thenReturn("1000");
		when(movieDAO.getMovieRatingsByYearAndVoteLimit(9, 1000)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Tests the getAllMovieRatings, but also specifies a limit on number of movies and minimum number of votes
	 */
	@Test
	void testGetAllMovieRatingsByLimitAndVotes() {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("100");
		when(ctx.queryParam("limit")).thenReturn("2");
		movieController.getRatingsByYear(ctx);
		try {
			verify(movieDAO).getMovieRatingsByYearLimitVoteLimit(1994, 2, 100);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests a 500 status code is shown when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetMovieRatingsByLimitAndVoteLimitDatabaseError() throws SQLException {
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("100");
		when(ctx.queryParam("limit")).thenReturn("2");
		when(movieDAO.getMovieRatingsByYearLimitVoteLimit(1994, 2, 100)).thenThrow(new SQLException());
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test that the controller returns a 404 status code when no movie/movie rating is found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenGetMovieRatingsByLimitAndVoteLimitByTooHighVotes() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("10000000");
		when(ctx.queryParam("limit")).thenReturn("2");
		when(movieDAO.getMovieRatingsByYearLimitVoteLimit(1994, 2, 10000000)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
	
	/**
	 * Test that the controller returns a 404 status code when no movie/movie rating is found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenGetMovieRatingsByLimitAndVoteLimitByTooHighVotes2() throws SQLException{
		when(ctx.pathParam("year")).thenReturn("1994");
		when(ctx.queryParam("votes")).thenReturn("10000000");
		when(ctx.queryParam("limit")).thenReturn("-2");
		when(movieDAO.getMovieRatingsByYearAndVoteLimit(1994, 10000000)).thenReturn(null);
		movieController.getRatingsByYear(ctx);
		verify(ctx).status(404);
	}
}