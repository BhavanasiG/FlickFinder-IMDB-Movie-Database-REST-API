package com.flickfinder.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.flickfinder.model.Movie;
import com.flickfinder.model.Person;
import com.flickfinder.util.Database;

/**
 * TODO: Implement this class
 * The Data Access Object for the People table.
 * 
 * This class is responsible for getting data from the People table in the
 * database.
 * 
 */
public class PersonDAO {

	// for the must have requirements, you will need to implement the following
	// methods:	
	// - getAllPeople()
	// - getPersonById(int id)
	// you will add further methods for the more advanced tasks; however, ensure your have completed 
	// the must have requirements before you start these.  
	
	/**
	 * The connection to the database
	 */
	private final Connection connection;
	
	/**
	 * Constructs a SQLitePersonDAO object and gets the database connection.
	 */
	public PersonDAO() {
		Database database = Database.getInstance();
		connection = database.getConnection();
	}
	
	/**
	 * Returns a list of people in the database (limited to 50)
	 * 
	 * @return a list of people in the database
	 * @throws SQLException if a database error occurs
	 */
	
	public List<Person> getAllPeople() throws SQLException {
		List<Person> persons = new ArrayList<>();
		
		Statement statement = connection.createStatement();
		
		ResultSet rs = statement.executeQuery("select * from people LIMIT 50");
		
		while (rs.next()) {
			persons.add(new Person(rs.getInt("id"), rs.getString("name"), rs.getInt("birth")));
		}
		
		return persons;
		
	}
	
	/**
	 * Returns the movie with the specified id.
	 * 
	 * @param id the id of the person
	 * @return the person with the specified id
	 * @throws SQLException if a database error occurs
	 */
	public Person getPersonById(int id) throws SQLException {
		
		String statement = "select * from people where id = ?";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			return new Person(rs.getInt("id"), rs.getString("name"), rs.getInt("birth"));
		}
		
		return null;
	}
	
	/**
	 * Returns the movies associated with a specific star
	 * @param id the id of the person
	 * @return the list of movies associated with the person
	 * @throws SQLException if a database error occurs
	 */
	public List<Movie> getMoviesByPersonId(int id) throws SQLException{
		List<Movie> movies = new ArrayList<>();
		
		String statement = "select * from movies inner join stars on movies.id=stars.movie_id where stars.person_id = ?";
		PreparedStatement ps = connection.prepareStatement(statement);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		
		
		while (rs.next()) {
			movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getInt("year")));
		}
		
		if (movies.size() > 0) {
			return movies;
		}  {
			return null;
		}
		
	}
	
	/**
	 * Returns the specified number of people
	 * @param limit the number of people to be returned
	 * @return a list of the specified number of people
	 * @throws SQLException
	 */
	public List<Person> getAllPeopleByLimit(int limit) throws SQLException{
		List<Person> people = new ArrayList<>();
		if (limit < 1) {
			limit = 50;
		}
		if (limit >= 2147483647) {
			limit = 50;
		}
		
		Statement statement = connection.createStatement();
		
		ResultSet rs = statement.executeQuery("select * from people LIMIT " + limit);
		
		while (rs.next()) {
			people.add(new Person(rs.getInt("id"), rs.getString("name"), rs.getInt("birth")));
		}
		
		return people;
	}
}
