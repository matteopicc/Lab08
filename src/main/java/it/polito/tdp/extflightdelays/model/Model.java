package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport,DefaultWeightedEdge>grafo;
	private Map<Integer,Airport> idMap;
	private ExtFlightDelaysDAO dao;
	
	public Model() {
		idMap = new HashMap<Integer,Airport>();
		dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(idMap); // carica tutti gli aeroporti in idMAp
	}
	
	public void creaGrafo(int distaMedia) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, idMap.values());//aggiungere vertici al grafo
		
		//archi
		for(Rotta rotta : dao.getRotte(idMap, distaMedia)) {
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getA1(), rotta.getA2()); // prendere un arco
			
			if(edge == null) {	
				Graphs.addEdge(grafo, rotta.getA1(), rotta.getA2(), rotta.getPeso()); // se non esiste, aggiungerlo al grafo
				
			}else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + rotta.getPeso())/2;
				grafo.setEdgeWeight(edge, newPeso);
				
			}
			
			
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Rotta>getRotte(){
		List<Rotta> rotte = new ArrayList<Rotta>(); 
		
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			rotte.add(new Rotta(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),this.grafo.getEdgeWeight(e)));
		}
		return rotte;
	}
	

}
