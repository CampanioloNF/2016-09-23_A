package it.polito.tdp.gestionale.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.gestionale.db.DidatticaDAO;

public class Model {

	private List<Corso> corsi;
	private List<Studente> studenti;
	private DidatticaDAO didatticaDAO;
	
	//puo risultare utile avere delle idMap
	private Map<Integer, Studente> studentiMap;
	private Map<String, Corso> corsoMap;
	
	private Graph<Nodo, DefaultEdge> grafo;

	public Model() {

		didatticaDAO = new DidatticaDAO();
		studentiMap = new HashMap<Integer, Studente>();
		corsoMap = new HashMap<String, Corso>();
		
		corsi = new ArrayList<Corso>(didatticaDAO.getTuttiICorsi(corsoMap));
		studenti = new ArrayList<Studente>(didatticaDAO.getTuttiStudenti(studentiMap));
		
		this.creaGrafo();
		
	}

	private void creaGrafo() {
	
		this.grafo = new SimpleGraph<>(DefaultEdge.class);
		//aggiungo tutti i vertici 
		Graphs.addAllVertices(grafo, studenti);
		Graphs.addAllVertices(grafo, corsi);
		//devo aggiungere gli archi e per questo chiedo al dao 
		didatticaDAO.loadEdges(grafo, corsoMap, studentiMap);
		
		System.out.println("Grafo creato :" +grafo.vertexSet().size() +" - "+grafo.edgeSet().size());
		
	}

	public String getCorsiFrequentati() {
		
		if(grafo!=null) {
			
		    String ris = "";
			
		    //il grado di ogni studente è il numero di corsi che frequenta
		    for(Nodo n : grafo.vertexSet()) {
		    	if(n instanceof Studente) {
		    	//s	int num_corsi = grafo.de
		    	}
		    }
		    
		}
		
		return "";
	}
	
	
}
