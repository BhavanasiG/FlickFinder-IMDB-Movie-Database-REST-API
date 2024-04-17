package com.flickfinder.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flickfinder.dao.PersonDAO;

import io.javalin.http.Context;

/**
 * TODO: Implement this class
 * Test for Person Controller
 */
class PersonControllerTest {

	private Context ctx;
	
	/**
	 * The person data access object
	 */
	private PersonDAO personDAO;
	
	/**
	 * The person controller
	 */
	private PersonController personController;
	
	@BeforeEach
	void setup() {
		
		// Create a mock of the person class
		personDAO = mock(PersonDAO.class);
		
		// Create a mock of the Context class
		ctx = mock(Context.class);
		
		// Create an instance if the PersonController class and pass the mock object
		personController = new PersonController(personDAO);
	}
	
	/**
	 * Tests the getAllPeople method
	 * Expect to get a list of all people in the database.
	 */
	@Test
	void testGetAllPeople() {
		personController.getAllPeople(ctx);
		try {
			verify(personDAO).getAllPeople();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test that the controller returns a 500 staus code when a database error occurs
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetAllDatabaseError() throws SQLException{
		when(personDAO.getAllPeople()).thenThrow(new SQLException());
		personController.getAllPeople(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test the getPersonById method
	 * Expect to get the person specified by the unique identifier
	 */
	@Test
	void testGetPersonById() {
		when(ctx.pathParam("id")).thenReturn("1");
		personController.getPersonById(ctx);
		try {
			verify(personDAO).getPersonById(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Tests the getMoviesByPersonId
	 */
	@Test
	void testGetMoviesByPersonId() {
		when(ctx.pathParam("id")).thenReturn("1");
		personController.getMoviesStarringPerson(ctx);
		try {
			verify(personDAO).getMoviesByPersonId(1);
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
	void testThrows500ExceptionWhenGetByPersonIdDatabaseError() throws SQLException{
		when(ctx.pathParam("id")).thenReturn("1");
		when(personDAO.getMoviesByPersonId(1)).thenThrow(new SQLException());
		personController.getMoviesStarringPerson(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test a 500 status code is returned when a database error occurs.
	 * @throws SQLException
	 */
	@Test
	void testThrows500ExceptionWhenGetByIdDatabaseError() throws SQLException {
		when(ctx.pathParam("id")).thenReturn("1");
		when(personDAO.getPersonById(1)).thenThrow(new SQLException());
		personController.getPersonById(ctx);
		verify(ctx).status(500);
	}
	
	/**
	 * Test that the controller returns a 404 status code when a person is not found
	 * @throws SQLException
	 */
	@Test
	void testThrows404ExceptionWhenNoPersonFound() throws SQLException {
		when(ctx.pathParam("id")).thenReturn("1");
		when(personDAO.getPersonById(1)).thenReturn(null);
		personController.getPersonById(ctx);
		verify(ctx).status(404);
	}
}