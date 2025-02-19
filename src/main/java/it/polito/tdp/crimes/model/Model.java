package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	
	private EventsDao dao;
	private Graph<String, DefaultWeightedEdge> grafo;
	private List<String> percorsoMigliore;
	private int pesoMinore;
	
	public Model() {
		dao = new EventsDao();
	}
	
	public void creaGrafo(String categoria, Integer anno) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo vertici
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, anno));
		
		// aggiungo archi
		for(Arco a: dao.getArchi(categoria, anno)) {
			if(grafo.containsVertex(a.getId1()) && grafo.containsVertex(a.getId2()) && a.getPeso() != 0) {
				Graphs.addEdge(this.grafo, a.getId1(), a.getId2(), a.getPeso());
			}
		}
	}
	
	
	public List<Arco> getArchiMax(){
		List<Arco> result = new ArrayList<>();
		int pesoMax = 0;
		
		for(DefaultWeightedEdge edge: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(edge) > pesoMax) {
				pesoMax = (int) grafo.getEdgeWeight(edge);
			}
		}
		
		for(DefaultWeightedEdge edge: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(edge) == pesoMax) {
				result.add(new Arco(grafo.getEdgeSource(edge), grafo.getEdgeTarget(edge), (int) grafo.getEdgeWeight(edge)));
			}
		}
		
		
		return result;
	}
	
	public List<String> getPercorso(String partenza, String arrivo){
		percorsoMigliore = new ArrayList<>();
		pesoMinore = Integer.MAX_VALUE;
		List<String> parziale = new ArrayList<>();
		parziale.add(partenza);
		cerca(parziale, arrivo, 0);
		return percorsoMigliore;
	}
	
	public void cerca(List<String> parziale, String arrivo, int livello) {
		if(parziale.size() == grafo.vertexSet().size() || parziale.get(parziale.size()-1).equals(arrivo)) {
			if(this.calcolaPeso(parziale) < pesoMinore) {
				pesoMinore = calcolaPeso(parziale);
				percorsoMigliore = new ArrayList<>(parziale);
			}
			return;
		}

		String ultimo = parziale.get(parziale.size()-1);
		List<String> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(String vicino: vicini) {
			if(!parziale.contains(vicino)) {
				parziale.add(vicino);
				cerca(parziale, arrivo, livello+1);
				parziale.remove(vicino);
			}
		}
		
	}
	
	public int calcolaPeso(List<String> parziale) {
		int peso = 0;
		for(int i =0; i< parziale.size()-1; i++) {
			DefaultWeightedEdge e = grafo.getEdge(parziale.get(i), parziale.get(i+1));
			int pesoArco = (int) grafo.getEdgeWeight(e);
			peso += pesoArco;
		}
		return peso;
	}
	
	
	public int nVertici() {
		return grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return grafo.edgeSet().size();
	}
	
	public List<String> getCategorie(){
		return dao.getCategorieReato();
	}
	
	public List<Integer> getAnni(){
		return dao.getAnni();
	}
}
