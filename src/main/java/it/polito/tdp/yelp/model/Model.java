package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.Similarity;
import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	YelpDao dao=new YelpDao();
	Graph<User,DefaultWeightedEdge> grafo;
	
	public void CreaGrafo(int n, Integer anno) {
		grafo=new SimpleWeightedGraph<User,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getAllUtentiCheHannoFattoXReviews(n).values() );
		for(Similarity s:dao.getAllSimilarity(n, anno)) {
			Graphs.addEdgeWithVertices(grafo, s.getU1(), s.getU2(), s.getSimilarity());
		}
	}
	
	public Set<User> VerticiGrafo(){
		return grafo.vertexSet();
	}
	
	public Set<DefaultWeightedEdge> ArchiGrafo(){
		return grafo.edgeSet();
	}
	
	public List<User> UtentiConMaggiorGradoDiSimilarità(User utente){
		List<User> utenti= new ArrayList<User>();
		double grado=0;
		for(DefaultWeightedEdge a:grafo.edgesOf(utente)) 
		{
			if(grafo.getEdgeWeight(a)>grado) 
			{
				grado=grafo.getEdgeWeight(a);
				utenti= new ArrayList<User>();
				if(grafo.getEdgeTarget(a).equals(utente)) 
				{
					utenti.add(grafo.getEdgeSource(a));
				}
				else 
				{
					utenti.add(grafo.getEdgeTarget(a));
				}
			}
			else if(grafo.getEdgeWeight(a)==grado) 
			{
				if(grafo.getEdgeTarget(a).equals(utente)) 
				{
					utenti.add(grafo.getEdgeSource(a));
				}
				else 
				{
					utenti.add(grafo.getEdgeTarget(a));
				}
			}
		}
		return utenti;
	}
}
