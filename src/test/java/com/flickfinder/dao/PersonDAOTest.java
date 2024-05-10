package com.flickfinder.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flickfinder.model.Movie;
import com.flickfinder.model.Person;
import com.flickfinder.util.Database;
import com.flickfinder.util.Seeder;

/**
 * TODO: Implement this class
 * Test for the Person Data Access Object.
 */
class PersonDAOTest {
	
	/**
	 * The person data access object.
	 */
	
	private PersonDAO personDAO;
	
	/**
	 * Seeder
	 */
	
	Seeder seeder;
	
	/**
	 * Sets up the in-memory database connection and creates the tables.
	 */
	
	@BeforeEach
	void setUp() {
		var url = "jdbc:sqlite::memory:";
		seeder = new Seeder(url);
		Database.getInstance(seeder.getConnection());
		personDAO = new PersonDAO();
	}
	
	/**
	 * Tests the getAllPeople method
	 */
	@Test
	void testGetAllPeople() {
		try {
			List<Person> people = personDAO.getAllPeople();
			assertEquals(5, people.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the getPersonById method with an invalid id, Null should be returned.
	 */
	@Test
	void testGetMovieByIdInvalidId() {
		try {
			Person person = personDAO.getPersonById(1000);
			assertEquals(null, person);
			
			Person person2 = personDAO.getPersonById(0);
			assertEquals(null, person2);
			
			Person person3 = personDAO.getPersonById(-5);
			assertEquals(null, person3);
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the getMoviesbyPersonId method
	 * We expect to get the list of movies specified by the person id
	 */
	@Test
	void testGetMoviesbyPersonId() {
		try {
			List<Movie> movies = personDAO.getMoviesByPersonId(1);
			assertEquals(1, movies.size());
			
			List<Movie> movies2 = personDAO.getMoviesByPersonId(2);
			assertEquals(1, movies2.size());
			
			List<Movie> movies3 = personDAO.getMoviesByPersonId(4);
			assertEquals(2, movies3.size());
			
			List<Movie> movies4 = personDAO.getMoviesByPersonId(5);
			assertEquals(1, movies4.size());
			
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getMoviesByStarId method with an invalid id
	 */
	@Test
	void testGetMoviesByInvalidPersonId() {
		try {
			List<Movie> movies = personDAO.getMoviesByPersonId(3);
			assertEquals (null, movies);
			
			List<Movie> movies2 = personDAO.getMoviesByPersonId(-5);
			assertEquals (null, movies2);
			
			List<Movie> movies3 = personDAO.getMoviesByPersonId(0);
			assertEquals (null, movies3);
			
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getAllPeopleByLimit()
	 * We expect to get a list of people of the specified length
	 */
	@Test
	void testGetAllPeopleByLimit() {
		try {
			List<Person> people = personDAO.getAllPeopleByLimit(1);
			assertEquals(1, people.size());
			
			List<Person> people2 = personDAO.getAllPeopleByLimit(2);
			assertEquals(2, people2.size());
			
			List<Person> people3 = personDAO.getAllPeopleByLimit(3);
			assertEquals(3, people3.size());
			
			List<Person> people4 = personDAO.getAllPeopleByLimit(4);
			assertEquals(4, people4.size());
			
			List<Person> people5 = personDAO.getAllPeopleByLimit(5);
			assertEquals(5, people5.size());
		} catch (SQLException e) {
			fail("SQLException thrown");
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the getAllPeopleByLimit by an invalid limit
	 */
	@Test
	void testGetAllPeopleByInvalidLimit() {
		try {
			List<Person> people = personDAO.getAllPeopleByLimit(-1);
			assertEquals(5, people.size());
			
			List<Person> people2 = personDAO.getAllPeopleByLimit(0);
			assertEquals(5, people2.size());
			
			List<Person> people3 = personDAO.getAllPeopleByLimit(2147483647);
			assertEquals(5, people3.size());
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