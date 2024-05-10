package com.flickfinder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.flickfinder.util.Database;
import com.flickfinder.util.Seeder;

import io.javalin.Javalin;

/**
 * These are our integration tests.
 * We are testing the application as a whole, including the database.
 */
class IntegrationTests {

	/**
	 * The Javalin app.*
	 */
	Javalin app;

	/**
	 * The seeder object.
	 */
	Seeder seeder;

	/**
	 * The port number. Try and use a different port number from your main
	 * application.
	 */
	int port = 6000;

	/**
	 * The base URL for our test application.
	 */
	String baseURL = "http://localhost:" + port;

	/**
	 * Bootstraps the application before each test.
	 */
	@BeforeEach
	void setUp() {
		var url = "jdbc:sqlite::memory:";
		seeder = new Seeder(url);
		Database.getInstance(seeder.getConnection());
		app = AppConfig.startServer(port);
	}

	/**
	 * Test that the application retrieves a list of all movies.
	 * Notice how we are checking the actual content of the list.
	 * At this higher level, we are not concerned with the implementation details.
	 */

	@Test
	void retrieves_a_list_of_all_movies() {
		given().when().get(baseURL + "/movies").then().assertThat().statusCode(200). // Assuming a successful
												// response returns HTTP
												// 200
				body("id", hasItems(1, 2, 3, 4, 5))
				.body("title", hasItems("The Shawshank Redemption", "The Godfather",
						"The Godfather: Part II", "The Dark Knight", "12 Angry Men"))
				.body("year", hasItems(1994, 1972, 1974, 2008, 1957));
	}

	/**
	 * Test that the application retreives a single movie
	 */
	@Test
	void retrieves_a_single_movie_by_id() {

		given().when().get(baseURL + "/movies/1").then().assertThat().statusCode(200). // Assuming a successful
												// response returns HTTP
												// 200
				body("id", equalTo(1))
				.body("title", equalTo("The Shawshank Redemption"))
				.body("year", equalTo(1994));
	}
	
	/**
	 * Test that the application shows the valid error result
	 * when no movie is found.
	 */
	@Test
	void retrieves_a_invalid_movie_by_id() {

		given().when().get(baseURL + "/movies/190").then().assertThat().statusCode(404). // Assuming a successful
												// response returns HTTP
												// 404
				body(equalTo("Movie not found"));
	}
	
	/**
	 * Checking content of list of people
	 */
	@Test
	void retrieves_a_list_of_all_people() {
		given().when().get(baseURL + "/people").then().assertThat().statusCode(200).
			
			body("id", hasItems(1, 2, 3, 4, 5))
			.body("name", hasItems("Tim Robbins", "Morgan Freeman", "Christopher Nolan", 
					"Al Pacino", "Henry Fonda"))
			.body("birth", hasItems(1958, 1937, 1970, 1940, 1905));
	}
	
	/**
	 * Checking content of single person
	 */
	@Test
	void retrieves_a_single_person_by_id() {
given().when().get(baseURL + "/people/1").then().assertThat().statusCode(200).
		
		body("id", equalTo(1))
		.body("name", equalTo("Tim Robbins"))
		.body("birth", equalTo(1958));
		
	}
	
	/**
	 * Checking invalid webpage shown
	 */
	@Test
	void retrieves_a_single_person_by_invalid_id() {
given().when().get(baseURL + "/people/1234").then().assertThat().statusCode(404).
		
		body(equalTo("Person not found"));
		
	}

	/**
	 * Checking content of list of stars of a specified movie
	 */
	@Test
	void retrieves_a_list_of_stars_of_movie_id() {
		given().when().get(baseURL + "/movies/1/stars").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2))
		.body("name", hasItems("Tim Robbins", "Morgan Freeman"))
		.body("birth", hasItems(1958, 1937));
	}
	
	/**
	 * Checking content of list of stars of a invalid movie
	 */
	@Test
	void retrieves_a_list_of_stars_of_invalid_movie_id() {
		given().when().get(baseURL + "/movies/1234/stars").then().assertThat().statusCode(404).
		
		body(equalTo("Star(s) not found"));
	}
	
	/**
	 * Checking the content of the list of movies of a specified star
	 */
	@Test
	void retrieves_a_list_of_movies_of_person_id() {
		given().when().get(baseURL + "/people/4/movies").then().assertThat().statusCode(200).
		
		body("id", hasItems(2, 3))
		.body("title", hasItems("The Godfather", "The Godfather: Part II"))
		.body("year", hasItems(1972, 1974));
	}
	
	/**
	 * Checking the content of the list of movies of a invalid star
	 */
	@Test
	void retrieves_a_list_of_movies_of_invalid_person_id() {
		given().when().get(baseURL + "/people/400/movies").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of movie ratings of a specified year
	 */
	@Test
	void retrieves_a_list_of_movie_ratings_of_year() {
		given().when().get(baseURL + "/movies/ratings/2008").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of movie ratings of a invalid year
	 */
	@Test
	void retrieves_a_list_of_movie_ratings_of_invalid_year() {
		given().when().get(baseURL + "/movies/ratings/0").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of movie ratings of a invalid year
	 */
	@Test
	void retrieves_a_list_of_movie_ratings_of_invalid_year2() {
		given().when().get(baseURL + "/movies/ratings/abc").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	/**
	 * Checking the content of the list of movie ratings of a future year
	 */
	@Test
	void retrieves_a_list_of_movie_ratings_of_future_year() {
		given().when().get(baseURL + "/movies/ratings/2028").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of people with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_people_with_limit() {
		given().when().get(baseURL + "/people?limit=3").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("name", hasItems("Tim Robbins", "Morgan Freeman", "Christopher Nolan"))
		.body("birth", hasItems(1958, 1937, 1970));
	}
	
	/**
	 * Checking the content of the list of people with an invalid limit.
	 */
	@Test
	void retrieves_a_list_of_people_with_invalid_limit() {
		given().when().get(baseURL + "/people?limit=-9").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("name", hasItems("Tim Robbins", "Morgan Freeman", "Christopher Nolan"))
		.body("birth", hasItems(1958, 1937, 1970));
	}
	
	/**
	 * Checking the content of the list of people with an invalid limit.
	 */
	@Test
	void retrieves_a_list_of_people_with_invalid_limit2() {
		given().when().get(baseURL + "/people?limit=abc").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("name", hasItems("Tim Robbins", "Morgan Freeman", "Christopher Nolan"))
		.body("birth", hasItems(1958, 1937, 1970));
	}
	
	/**
	 * Checking the content of the list of people with an invalid limit.
	 */
	@Test
	void retrieves_a_list_of_people_with_invalid_limit3() {
		given().when().get(baseURL + "/people?limit=10000000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("name", hasItems("Tim Robbins", "Morgan Freeman", "Christopher Nolan"))
		.body("birth", hasItems(1958, 1937, 1970));
	}
	
	/**
	 * Checking the content of the list of movies with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_movies_with_limit() {
		given().when().get(baseURL + "/movies?limit=3").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("title", hasItems("The Shawshank Redemption", "The Godfather", "The Godfather: Part II"))
		.body("year", hasItems(1994, 1972, 1974));
	}
	
	/**
	 * Checking the content of the list of movies with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_movies_with_invalid_limit() {
		given().when().get(baseURL + "/movies?limit=-0").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("title", hasItems("The Shawshank Redemption", "The Godfather", "The Godfather: Part II"))
		.body("year", hasItems(1994, 1972, 1974));
	}
	
	/**
	 * Checking the content of the list of movies with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_movies_with_invalid_limit2() {
		given().when().get(baseURL + "/movies?limit=abc").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("title", hasItems("The Shawshank Redemption", "The Godfather", "The Godfather: Part II"))
		.body("year", hasItems(1994, 1972, 1974));
	}
	
	/**
	 * Checking the content of the list of movies with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_movies_with_invalid_limit3() {
		given().when().get(baseURL + "/movies?limit=1000000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(1, 2, 3))
		.body("title", hasItems("The Shawshank Redemption", "The Godfather", "The Godfather: Part II"))
		.body("year", hasItems(1994, 1972, 1974));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query3() {
		given().when().get(baseURL + "/movies/ratings/2008?votes=10000000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query4() {
		given().when().get(baseURL + "/movies/ratings/2008?votes=-ab1").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query5() {
		given().when().get(baseURL + "/movies/ratings/0?votes=10000000000").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query6() {
		given().when().get(baseURL + "/movies/ratings/-173?votes=-ab1").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with a minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_vote_query() {
		given().when().get(baseURL + "/movies/ratings/2008?votes=1000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with a minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_vote_query2() {
		given().when().get(baseURL + "/movies/ratings/2028?votes=1000000").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query() {
		given().when().get(baseURL + "/movies/ratings/2028?votes=10000000000").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of ratings of a specific year with an invalid minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_vote_query2() {
		given().when().get(baseURL + "/movies/ratings/2028?votes=-ab1").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_query() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2008?limit=3").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_query_invalid() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/0?limit=3").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_query_invalid2() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/adboub?limit=3").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with a limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_query5() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2028?limit=3").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_limit_query() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2008?limit=-09a").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_limit_query2() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2008?limit=10000000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_limit_query3() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2028?limit=-09a").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of rating of a specific year with an invalid limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_invalid_limit_query4() {// limit specified is 3, but the seeder database has only 1 entry for 2008
														// so should only return one record.
		given().when().get(baseURL + "/movies/ratings/2028?limit=10000000000").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of ratings with a specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query() {
		given().when().get(baseURL + "/movies/ratings/2008?limit=3&votes=1000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings with a specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query2() {
		given().when().get(baseURL + "/movies/ratings/0?limit=3&votes=1000000").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of ratings with a specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query3() {
		given().when().get(baseURL + "/movies/ratings/acouv?limit=3&votes=1000000").then().assertThat().statusCode(400).
		
		body(equalTo("Invalid year"));
	}
	
	/**
	 * Checking the content of the list of ratings with an invalid, specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query_invalid() {
		given().when().get(baseURL + "/movies/ratings/2028?limit=3&votes=1000000").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of ratings with an invalid, specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query_invalid2() {
		given().when().get(baseURL + "/movies/ratings/2008?limit=0a-bc&votes=1000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings with an invalid, specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query_invalid3() {
		given().when().get(baseURL + "/movies/ratings/2008?limit=3&votes=10000000").then().assertThat().statusCode(404).
		
		body(equalTo("Movie(s) not found"));
	}
	
	/**
	 * Checking the content of the list of ratings with an invalid, specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query_invalid4() {
		given().when().get(baseURL + "/movies/ratings/2008?limit=10000000000&votes=1000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings with an invalid, specific year with limit and minimum votes specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_limit_and_vote_query_invalid5() {
		given().when().get(baseURL + "/movies/ratings/2008?limit=3&votes=10000000000").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Checking the content of the list of ratings with a specific year with minimum votes and limit specified.
	 */
	@Test
	void retrieves_a_list_of_ratings_with_vote_and_limit_query() { // checking result is same, even if queries are swapped around in URL
		given().when().get(baseURL + "/movies/ratings/2008?votes=1000000&limit=3").then().assertThat().statusCode(200).
		
		body("id", hasItems(4))
		.body("title", hasItems("The Dark Knight"))
		.body("rating", hasItems(8.8f))
		.body("votes", hasItems(2000000))
		.body("year", hasItems(2008));
	}
	
	/**
	 * Tears down the application after each test.
	 * We want to make sure that each test runs in isolation.
	 */
	@AfterEach
	void tearDown() {
		seeder.closeConnection();
		app.stop();
	}

}
