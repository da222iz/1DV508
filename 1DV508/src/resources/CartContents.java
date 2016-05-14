package resources;

import movies.Movie;

public class CartContents {
	private Movie movie;
	private int number;
	
	public CartContents(Movie movie, int number) {
		this.movie = movie;
		this.number = number;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	
}
