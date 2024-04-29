package com.flickfinder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for the Movie Rating Model
 * 
 * 
 */
public class MovieRatingTest {
	
	/**
	 * The movie rating object to be tested.
	 */
	private MovieRating movieRating;

	/**
	 * Set up the movie rating object before each test.
	 */
	@BeforeEach
	public void setUp() {
		movieRating = new MovieRating(1, "The Matrix", 8.7f, 1895415, 1999);
	}
	
	/**
	 * Test the movie rating object is created with the correct values
	 */
	@Test
	public void testMovieRatingCreated() {
		assertEquals(1,movieRating.getId());
		assertEquals("The Matrix", movieRating.getTitle());
		assertEquals(8.7f, movieRating.getRating());
		assertEquals(1895415, movieRating.getVotes());
		assertEquals(1999, movieRating.getYear());
	}
	
	/**
	 * Test the movie rating object is created with the correct values.
	 */
	@Test
	public void testMovieRatingSetters() {
		movieRating.setId(2);
		movieRating.setTitle("The Matrix Reloaded");
		movieRating.setRating(7.2f);
		movieRating.setVotes(592953);
		movieRating.setYear(2003);
		assertEquals(2, movieRating.getId());
		assertEquals("The Matrix Reloaded", movieRating.getTitle());
		assertEquals(7.2f, movieRating.getRating());
		assertEquals(592953, movieRating.getVotes());
		assertEquals(2003, movieRating.getYear());
	}

}
