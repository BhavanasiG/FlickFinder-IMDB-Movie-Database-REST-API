package com.flickfinder.model;

/**
 * Represents a movierating object that stores the rating and associated to a specific movie identified by its unique identifier.
 */
public class MovieRating extends Movie {
	
	private float rating;
	private int votes;

	/**
	 * 
	 * @param id the unique identifier of the movie
	 * @param title the title of the movie
	 * @param rating the rating of the movie
	 * @param votes the number of votes given to the movie
	 * @param year the release year of the movie
	 */
	public MovieRating(int id, String title, float rating, int votes, int year) {
		super (id, title, year);
		this.rating = rating;
		this.votes = votes;
		
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Returns the rating of the movie
	 * @return the rating of the movie
	 */
	public float getRating() {
		return rating;
	}
	
	/**
	 * Returns the number of votes for the movie
	 * @return the number of votes for the movie
	 */
	public int getVotes() {
		return votes;
	}
	
	/**
	 * Sets the rating of the movie
	 * @param rating the rating to set the movie
	 */
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	/**
	 * Sets the number of votes of the movie
	 * @param votes the number of votes to set the movie
	 */
	public void setVotes (int votes) {
		this.votes = votes;
	}

}
