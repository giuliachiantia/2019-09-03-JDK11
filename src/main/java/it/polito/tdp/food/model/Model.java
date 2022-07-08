package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> porzioni;
	
	//variabili di stato della ricorsione
	private double pesoMax;
	private List<String> camminoMax;
	
	public Model() {
		dao= new FoodDao();
	}
	
	public String creaGrafo(int calories){
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//aggiungo i vertici
		this.porzioni=this.dao.getPortionDisplayNames(calories);
		Graphs.addAllVertices(this.grafo, this.porzioni);
		
		//aggiungo i vertici
		List<Adiacenza> archi= new ArrayList<>();
		archi=this.dao.getArchi();
		for (Adiacenza a : archi) {
			if (this.grafo.vertexSet().contains(a.getName1()) && 
					this.grafo.vertexSet().contains(a.getName2())) {
				Graphs.addEdge(this.grafo, a.getName1(), a.getName2(), a.getPeso());
			}
		}
		
		return String.format("Grafo creato (%d vertici, %d archi)\n", 
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public List<String> getVertici(){
		return this.porzioni;
	}
	
	public List<Adiacenza> getAdiacenti(String c){
		List <Adiacenza> list = new ArrayList<>();
		for(String s: Graphs.neighborListOf(this.grafo, c)) {
			DefaultWeightedEdge e=this.grafo.getEdge(c, s);
			int peso= (int) this.grafo.getEdgeWeight(e);
			Adiacenza a= new Adiacenza(c, s, peso);
			list.add(a);
			
		}
		return list;
	
	}
	//[ v1 v2 v3 v4 ]
	/*
	 * soluzione parziale che parte da vertice sorgente passato dal 
	 * controller
	 * livello=lunghezza cammino parziale
	 * terminazione--> cammino ha lunghezza N(cioe n+1 vertici)
	 * funzione da valutare: peso massimo
	 * generazione soluzioni: aggiungere adiacenti che non siano ancora nel cammino
	 * avvio ricorsione: 1 vertice di partenza
	 *   [partenza], livello=1
	 *   [partenza, vertice v] livello=2
	 *   [partenza, v2 , v3, v...., v(n+1)], livello=N+1
	 */
	public void cercaCammino(String partenza, int N) {
		this.camminoMax=null;
		this.pesoMax=0.0;
		
		List<String> parziale= new ArrayList<>();
		parziale.add(partenza);
		
		cerca(parziale, 1, N);
	}
	

	private void cerca(List<String> parziale, int livello, int N){
		if(livello==N+1) {
			double peso=pesoCammino(parziale);
			if (peso>this.pesoMax) {
				this.pesoMax=peso;
				this.camminoMax=new ArrayList<>(parziale);
			}
			return;
		}
		List<String> vicini=Graphs.neighborListOf(this.grafo, parziale.get(livello-1));
		for(String v:vicini) {
			if(!parziale.contains(v)) {
				parziale.add(v);
				cerca(parziale, livello+1, N);
				parziale.remove(parziale.size()-1);
			}
		}
		
}

	private double pesoCammino(List<String> parziale) {
		double peso=0.0;
		for(int i=1; i<parziale.size();i++) {
			double p=this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i-1), parziale.get(i)));
			peso+=p;
		}
		return peso;
	}
	public double getPesoMax() {
		return pesoMax;
	}

	public List<String> getCamminoMax() {
		return camminoMax;
	}
	
}
