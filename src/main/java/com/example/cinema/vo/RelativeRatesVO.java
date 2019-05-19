package com.example.cinema.vo;

import java.util.List;

import com.example.cinema.po.Movie;

public class RelativeRatesVO {
	/**
	 * 所有的电影
	 */
	private List<Movie> movies;
	
	/**
	 * 电影对应的相对上座率
	 */
	private double[] rates;
	
	public RelativeRatesVO() {
		
	}
	
	public void setMovies(List<Movie> movies) {
		this.movies=movies;
	}
	
	public List<Movie> getMovies(){
		return this.movies;
	}
	
	public void setRates(double[] rates) {
		this.rates=rates;
	}
	
	public double[] getRates() {
		return this.rates;
	}
	
}
