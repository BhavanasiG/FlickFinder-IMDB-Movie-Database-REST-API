package com.flickfinder.model;

/**
 * A person in the movie database.
 * 
 * @TODO: Implement this class
 */
public class Person {

	// - Add your code here: use the MovieDAO.java as an example
	// - Check the ERD and database schema in the docs folder
	// (./docs/database_schema.md) to ensure each column in the People table
	// has an attribute in the model. (DELETE THIS COMMENT WHEN DONE)

	private int id;
	private String name;
	private int birth;
	
	
	/**
	 * Constructs a Movie object with the specified id, name, and birth.
	 * 
	 * @param id the unique identifier of the movie
	 * @param name the name of the person
	 * @param birth the birth year of the person
	 */
	public Person(int id, String name, int birth) {
		this.id = id;
		this.name = name;
		this.birth = birth;
	}
	
	/**
	 * Returns the unique identifier of the movie.
	 * 
	 * @return the id of the movie
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets the unique identifier of the person.
	 * 
	 * @param id the id to set to the person
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns the name of the person.
	 * 
	 * @return the name of the person
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the person
	 * 
	 * @param name the name of the person
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the birth year of the person
	 * 
	 * @return the birth year of the person
	 */
	public int getBirth() {
		return birth;
	}
	
	/**
	 * Sets the birth year of the person.
	 * 
	 * @param birth the birth year to set.
	 */
	public void setBirth(int birth) {
		this.birth = birth;
	}
	
	
	/**
	 * Returns a string representation of the Person object.
	 * 
	 * @return a string representation of the Person object.
	 */
	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", birth=" + birth +"]";
	}
}
