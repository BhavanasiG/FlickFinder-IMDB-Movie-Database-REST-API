package com.flickfinder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.flickfinder.model.Movie;
import com.flickfinder.model.MovieRating;
import com.flickfinder.model.Person;
import com.flickfinder.util.Database;

/**
 * The Data Access Object for the Movie table.
 * 
 * This class is responsible for getting data from the Movies table in the
 * database.
 * 
 */
public class MovieDAO {

	/**
	 * The connection to the database.
	 */
	private final Connection connection;

	/**
	 * Constructs a SQLiteMovieDAO object and gets the database connection.
	 * 
	 */
	public MovieDAO() {
		Database database = Database.getInstance();
		connection = database.getConnection();
	}

	/**
	 * Returns a list of all movies in the database.
	 * 
	 * @return a list of all movies in the database
	 * @throws SQLException if a database error occurs
	 */

	public List<Movie> getAllMovies() throws SQLException {
		List<Movie> movies = new ArrayList<>();

		Statement statement = connection.createStatement();
		
		// I've set the limit to 10 for development purposes - you should do the same.
		ResultSet rs = statement.executeQuery("select * from movies LIMIT 50");
		
		while (rs.next()) {
			movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getInt("year")));
		}

		return movies;
	}

	/**
	 * Returns the movie with the specified id.
	 * 
	 * @param id the id of the movie
	 * @return the movie with the specified id
	 * @throws SQLException if a database error occurs
	 */
	public Movie getMovieById(int id) throws SQLException {

		String statement = "select * from movies where id = ?";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {

			return new Movie(rs.getInt("id"), rs.getString("title"), rs.getInt("year"));
		}
		
		// return null if the id does not return a movie.

		return null;

	}
	
	/**
	 * Returns the stars associated with the specified movie id
	 * @param id the id of the movie
	 * @return the list of stars associated with the movie id
	 * @throws SQLException if a database error occurs
	 */
	public List<Person> getStarsByMovieId(int id) throws SQLException{
		List<Person> persons = new ArrayList<>();
		
		String statement = "select * from people inner join stars on people.id=stars.person_id where stars.movie_id = ?";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			persons.add(new Person(rs.getInt("id"), rs.getString("name"), rs.getInt("birth")));
		}
		
		return persons;
	}
	
	
	/**
	 * Returns the movies of a specific year, with over 1000 votes and by descending order. The number of movies is limited to 50.
	 * @param year The year from which the highest rated movies should be shown
	 * @return the list of the top 50 rated movies from the year specified
	 * @throws SQLException if a database error occurs
	 */
	public List<MovieRating> getMovieRatingsByYear(int year) throws SQLException{
		List<MovieRating> movies = new ArrayList<>();
		
		String statement = "select * from movies inner join ratings on movies.id=ratings.movie_id where year = ? AND votes>1000 ORDER BY ratings.rating DESC LIMIT 50";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, year);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			movies.add(new MovieRating(rs.getInt("id"), rs.getString("title"), rs.getFloat("rating"), rs.getInt("votes"), rs.getInt("year")));
		}
		System.out.println(movies);
		
		return movies;
	}

}
