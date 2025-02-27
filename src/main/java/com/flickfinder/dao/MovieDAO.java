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
		if (persons.size()>0) {
			return persons;
		}  {
			return null;
		}
		
	}
	
	
	/**
	 * Returns the movies of a specific year, with over 1000 votes and by descending order. The number of movies is limited to 50.
	 * @param year The year from which the highest rated movies should be shown
	 * @return the list of the top 50 rated movies from the year specified
	 * @throws SQLException if a database error occurs
	 */
	public List<MovieRating> getMovieRatingsByYear(int year) throws SQLException{
		List<MovieRating> movies = new ArrayList<>();
		
		String statement = "select id, title, rating, votes, year from movies inner join ratings on movies.id=ratings.movie_id where year = ? AND votes>1000 ORDER BY ratings.rating DESC LIMIT 50";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, year);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			movies.add(new MovieRating(rs.getInt("id"), rs.getString("title"), rs.getFloat("rating"), rs.getInt("votes"), rs.getInt("year")));
		}
		//System.out.println(movies);
		
		if (movies.size() > 0) {
			return movies;
		}  {
			return null;
		}
	}
	
	/**
	 * Returns the specified number of movies
	 * @param limit the number of movies to be listed
	 * @return a list of the specified number of movies
	 * @throws SQLException
	 */
	public List<Movie> getAllMoviesByLimit(long limit) throws SQLException {
		List<Movie> movies = new ArrayList<>();
		
		if (limit < 1) {
			limit = 50;
		}
		if (limit >= 2147483647) {
			limit = 50;
		}

		Statement statement = connection.createStatement();
		
		
		ResultSet rs = statement.executeQuery("select * from movies LIMIT " + limit);
		
		while (rs.next()) {
			movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getInt("year")));
		}

		return movies;
	}
	
	/**
	 * Returns the specific number of ratings
	 * @param year the year of release of movies
	 * @param limit the number of movie ratings to be returned
	 * @return a list of the specified number of movie ratings
	 * @throws SQLException
	 */
	public List<MovieRating> getMovieRatingsByYearAndLimit(int year, long limit) throws SQLException{
		List<MovieRating> movies = new ArrayList<>();
		
		if (limit < 1) {
			limit = 50;
		}
		if (limit >= 2147483647) {
			limit = 50;
		}
		
		String statement = "select id, title, rating, votes, year from movies inner join ratings on movies.id=ratings.movie_id where year = ? AND votes>1000 ORDER BY ratings.rating DESC LIMIT " + limit;
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, year);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			movies.add(new MovieRating(rs.getInt("id"), rs.getString("title"), rs.getFloat("rating"), rs.getInt("votes"), rs.getInt("year")));
		}
		//System.out.println(movies);
		if (movies.size() > 0) {
			return movies;
		}  {
			return null;
		}
	}
	
	/**
	 * Returns the movies with more than the specified number of votes
	 * @param year the year of release of movie
	 * @param limit the number of votes the movie should have more than
	 * @return a list of the number of movie ratings with more votes than the specified number of votes
	 * @throws SQLException
	 */
	public List<MovieRating> getMovieRatingsByYearAndVoteLimit(int year, long votes) throws SQLException{
		List<MovieRating> movies = new ArrayList<>();
		
		if (votes < 0) { //someone might want to see list of movie with any number of votes
			votes = 1000;
		}
		if (votes >= 2147483647) {
			votes = 1000;
		}
		
		String statement = "select id, title, rating, votes, year from movies inner join ratings on movies.id=ratings.movie_id where year = ? AND votes>" + votes +" ORDER BY ratings.rating DESC LIMIT 50";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, year);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			movies.add(new MovieRating(rs.getInt("id"), rs.getString("title"), rs.getFloat("rating"), rs.getInt("votes"), rs.getInt("year")));
		}
		//System.out.println(movies);
		if (movies.size() > 0) {
			return movies;
		}  {
			return null;
		}
		
	}
	
	/**
	 * Returns the specified number of movies with more than the specified number of votes.
	 * If limit or votes are invalid numbers, then they will set to default values.
	 * @param year The year of release of the movies
	 * @param limit the number of movies to be returned
	 * @param votes the number of votes the movie rating should have more than
	 * @return a list containing the specified number of movie ratings contains more than the number of votes specified
	 * @throws SQLException
	 */
	public List<MovieRating> getMovieRatingsByYearLimitVoteLimit(int year, long limit, long votes) throws SQLException{
		List<MovieRating> movies = new ArrayList<>();
		
		if (limit < 1) {
			limit = 50;
		}
		if (limit >= 2147483647) {
			limit = 50;
		}
		if (votes < 0) {
			votes = 1000;
		}
		if (votes >= 2147483647) {
			votes = 1000;
		}
		
		String statement = "select id, title, rating, votes, year from movies inner join ratings on movies.id=ratings.movie_id where year = ? AND votes>" + votes +" ORDER BY ratings.rating DESC LIMIT " + limit;
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, year);
		ResultSet rs = ps.executeQuery();
		
		while (rs.next()) {
			movies.add(new MovieRating(rs.getInt("id"), rs.getString("title"), rs.getFloat("rating"), rs.getInt("votes"), rs.getInt("year")));
		}
		//System.out.println(movies);
		if (movies.size() > 0) {
			return movies;
		}  {
			return null;
		}
		
	}

}
