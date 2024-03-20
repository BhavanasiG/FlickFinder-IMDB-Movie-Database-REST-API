package com.flickfinder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 
 * TODO: Implement this class
 * Test for the Person model.
 * 
 */

class PersonTest {

	/**
	 * The person object to be tested.
	 */

	private Person person;
	
	/**
	 * Set up the person object before each test.
	 */
	@BeforeEach
	public void setUp() {
		person = new Person (1, "Keanu Reeves", 1964);
	}
	
	/**
	 * Test the person object is created with the correct values.
	 */
	@Test
	public void testPersonCreated() {
		assertEquals(1, person.getId());
		assertEquals("Keanu Reeves", person.getName());
		assertEquals(1964, person.getBirth());
	}
	
	/**
	 * Test the person object is created with the correct values.
	 */
	@Test
	public void testPersonSetters() {
		person.setId(2);
		person.setName("Cillian Murphy");
		person.setBirth(1976);
		assertEquals(2, person.getId());
		assertEquals("Cillian Murphy", person.getName());
		assertEquals(1976, person.getBirth());
	}
}