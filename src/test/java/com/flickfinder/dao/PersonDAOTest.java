package com.flickfinder.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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