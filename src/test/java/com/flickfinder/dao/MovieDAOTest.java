package com.flickfinder.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flickfinder.model.Movie;
import com.flickfinder.model.MovieRating;
import com.flickfinder.model.Person;
import com.flickfinder.util.Database;
import com.flickfinder.util.Seeder;

/**
 * Test for the Movie Data Access Object.
 * This uses an in-memory database for testing purposes.
 */

class MovieDAOTest {

	/**
	 * The movie data access object.
	 */

	private MovieDAO movieDAO;

	/**
	 * Seeder
	 */

	Seeder seeder;

	/**
	 * Sets up the database connection and creates the tables.
	 * We are using an in-memory database for testing purposes.
	 * This gets passed to the Database class to get a connection to the database.
	 * As it's a singleton class, the entire application will use the same
	 * connection.
	 */
	@BeforeEach
	void setUp() {
		var url = "jdbc:sqlite::memory:";
		seeder = new Seeder(url);
		Database.getInstance(seeder.getConnection());
		movieDAO = new MovieDAO();

	}

	/**
	 * Tests the getAllMovies method.
	 * We expect to get a list of all movies in the database.
	 * We have seeded the database with 5 movies, so we expect to get 5 movies back.
	 * At this point, we avoid checking the actual content of the list.
	 */
	@Test
	void testGetAllMovies() {
		try {
			List<Movie> movies = movieDAO.getAllMovies();
			assertEquals(5, movies.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the getMovieById method.
	 * We expect to get the movie with the specified id.
	 */
	@Test
	void testGetMovieById() {
		Movie movie;
		try {
			movie = movieDAO.getMovieById(1);
			assertEquals("The Shawshank Redemption", movie.getTitle());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the getMovieById method with an invalid id. Null should be returned.
	 */
	@Test
	void testGetMovieByIdInvalidId() {
		// write an assertThrows for a SQLException

		try {
			Movie movie = movieDAO.getMovieById(1000);
			assertEquals(null, movie);
			
			Movie movie2 = movieDAO.getMovieById(-10);
			assertEquals(null, movie2);
			
			Movie movie3 = movieDAO.getMovieById(0);
			assertEquals(null, movie3);
			
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}

	}
	
	/**
	 * Tests the getStarsByMovieId method
	 * We expect to get the list of stars in the movie specified by the movie id
	 */
	
	@Test
	void testGetPeopleByMovieId() {
		try {
			List<Person> people = movieDAO.getStarsByMovieId(1);
			assertEquals (2, people.size());
			
			List<Person> people2 = movieDAO.getStarsByMovieId(2);
			assertEquals (1, people2.size());
			
			List<Person> people3 = movieDAO.getStarsByMovieId(3);
			assertEquals (1, people3.size());
			
			List<Person> people5 = movieDAO.getStarsByMovieId(5);
			assertEquals (1, people5.size());
			
		} catch (SQLException e){
			fail("SQLException thrown");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Test the getStarsByMovieId method with an invalid id.
	 */
	@Test
	void testGetPeopleByInvalidMovieId() {
		try {
			List<Person> people = movieDAO.getStarsByMovieId(4);
			assertEquals (null, people);
			
			List<Person> people2 = movieDAO.getStarsByMovieId(-3);
			assertEquals (null, people2);
			
			List<Person> people3 = movieDAO.getStarsByMovieId(0);
			assertEquals (null, people3);
			
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the getMovieRatingsByYear
	 * We expect to get the list of movies and their rating specified by the year of release
	 */
	@Test
	void testGetMovieRatingsByYear() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYear(1994);
			assertEquals(1, movies.size());
			
			List<MovieRating> movies2 = movieDAO.getMovieRatingsByYear(1972);
			assertEquals(1, movies2.size());
			
			List<MovieRating> movies3 = movieDAO.getMovieRatingsByYear(1974);
			assertEquals(1, movies3.size());
			
			List<MovieRating> movies4 = movieDAO.getMovieRatingsByYear(2008);
			assertEquals(1, movies4.size());
			
			List<MovieRating> movies5 = movieDAO.getMovieRatingsByYear(1957);
			assertEquals(1, movies5.size());
		} catch(SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getMovieRatingsByYear with an invalid year
	 */
	@Test
	void testGetMovieRatingsByInvalidYear() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYear(2028);
			assertEquals(null, movies);
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getAllMoviesByLimit
	 * We expect to get a list of movies of the specified length
	 */
	@Test
	void testGetAllMoviesByLimit() {
		try {
			List<Movie> movies = movieDAO.getAllMoviesByLimit(1);
			assertEquals(1, movies.size());
			
			List<Movie> movies2 = movieDAO.getAllMoviesByLimit(2);
			assertEquals(2, movies2.size());
			
			List<Movie> movies3 = movieDAO.getAllMoviesByLimit(3);
			assertEquals(3, movies3.size());
			
			List<Movie> movies4 = movieDAO.getAllMoviesByLimit(4);
			assertEquals(4, movies4.size());
			
			List<Movie> movies5 = movieDAO.getAllMoviesByLimit(5);
			assertEquals(5, movies5.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getAllMoviesByLimit with an invalid limit
	 */
	@Test
	void testGetAllMoviesByInvalidLimit() {
		try {
			List<Movie> movies = movieDAO.getAllMoviesByLimit(-1);
			assertEquals(5, movies.size());
		} catch (SQLException e) {
			fail("SQLexception thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the limit for get movie ratings
	 */
	@Test
	void testGetAllMovieRatingsByLimit() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearAndLimit(1994, 1);
			assertEquals(1, movies.size());
			
//			List<MovieRating> movie2 = movieDAO.getMovieRatingsByYearAndLimit(1994, 3);
//			assertEquals(3, movies.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getMovieRatingsByYearAndLimit with an invalid limits/years
	 */
	@Test
	void testGetAllMovieRatingsByInvalidLimit() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearAndLimit(1994, -1);
			assertEquals(1, movies.size());
			
			List<MovieRating> movies4 = movieDAO.getMovieRatingsByYearAndLimit(1994, 2147483647);
			assertEquals(1, movies4.size());
			
			List<MovieRating> movies2 = movieDAO.getMovieRatingsByYearAndLimit(2028, 2);
			assertEquals(null, movies2);
			
			List<MovieRating> movies3 = movieDAO.getMovieRatingsByYearAndLimit(2028, -1);
			assertEquals(null, movies3);
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the vote minimum for get movie ratings
	 */
	@Test
	void testGetMovieRatingsByVotes() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearAndVoteLimit(1994, 2000000);
			assertEquals(1, movies.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/***
	 * Test the get movie ratings with too high of a vote minimum
	 */
	@Test
	void testGetMovieRatingsByTooHighVotes() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearAndVoteLimit(1994, 2300000);
			assertEquals(null, movies);
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the get movie ratings with an invalid vote minimum
	 */
	@Test
	void testGetMovieRatingsByInvalidVotes() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearAndVoteLimit(1994, -1);
			assertEquals(1, movies.size());
			List<MovieRating> movies2 = movieDAO.getMovieRatingsByYearAndVoteLimit(1994, 2147483647);
			assertEquals(1, movies2.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getMovieRatingsByYearLimitVoteLimit
	 */
	@Test
	void testGetMovieRatingsByYearLimitVoteLimit() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearLimitVoteLimit(1972, 2, 1000000);
			assertEquals(1, movies.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getMovieRatingsByYearLimitVoteLimit with invalid parameters
	 */
	@Test
	void testGetMovieRatingsByYearByInvalidLimitVoteLimit() {
		try {
			List<MovieRating> movies = movieDAO.getMovieRatingsByYearLimitVoteLimit(1972, -5, 1000000);
			assertEquals(1, movies.size());
			
			List<MovieRating> movies7 = movieDAO.getMovieRatingsByYearLimitVoteLimit(1972, -5, 2147483647);
			assertEquals(1, movies7.size());
			
			List<MovieRating> movies2 = movieDAO.getMovieRatingsByYearLimitVoteLimit(1972, 2, 2000000);
			assertEquals(null, movies2);
			
			List<MovieRating> movies3 = movieDAO.getMovieRatingsByYearLimitVoteLimit(1972, 0, 2000000);
			assertEquals(null, movies3);
			
			List<MovieRating> movies4 = movieDAO.getMovieRatingsByYearLimitVoteLimit(2028, 2, 1000000);
			assertEquals(null, movies4);
			
			List<MovieRating> movies5 = movieDAO.getMovieRatingsByYearLimitVoteLimit(2028, -5, 1000000);
			assertEquals(null, movies5);
			
			List<MovieRating> movies6 = movieDAO.getMovieRatingsByYearLimitVoteLimit(2028, 0, 2000000);
			assertEquals(null, movies6);
			
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}

	@AfterEach
	void tearDown() {
		seeder.closeConnection();
	}

}