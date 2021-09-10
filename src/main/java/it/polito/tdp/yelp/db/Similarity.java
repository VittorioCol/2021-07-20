package it.polito.tdp.yelp.db;

import it.polito.tdp.yelp.model.User;

public class Similarity {
	User u1;
	User u2;
	double similarity;
	public Similarity(User u1, User u2, double similarity) {
		super();
		this.u1 = u1;
		this.u2 = u2;
		this.similarity = similarity;
	}
	public User getU1() {
		return u1;
	}
	public void setU1(User u1) {
		this.u1 = u1;
	}
	public User getU2() {
		return u2;
	}
	public void setU2(User u2) {
		this.u2 = u2;
	}
	public double getSimilarity() {
		return similarity;
	}
	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
	
}
