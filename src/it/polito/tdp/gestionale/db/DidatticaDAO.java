package it.polito.tdp.gestionale.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.gestionale.model.Corso;
import it.polito.tdp.gestionale.model.Nodo;
import it.polito.tdp.gestionale.model.Studente;

public class DidatticaDAO {

	/*
	 * Ottengo tutti gli studenti iscritti al Corso
	 */
	public void getStudentiIscrittiAlCorso(Corso corso, Map<Integer, Studente> mapStudenti) {
		final String sql = "SELECT studente.matricola FROM iscrizione, studente WHERE iscrizione.matricola=studente.matricola AND codins=?";

		List<Studente> studentiIscrittiAlCorso = new ArrayList<Studente>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, corso.getCodins());

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				int matricola = rs.getInt("matricola");
				Studente studente = mapStudenti.get(matricola);
				if (studente != null) {
					studentiIscrittiAlCorso.add(studente);
				} else {
					System.out.println("ERRORE! Lo studente non è presente!");
				}
			}

			corso.setStudenti(studentiIscrittiAlCorso);

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Ottengo tutti i corsi salvati nel Db
	 */
	public List<Corso> getTuttiICorsi(Map<String, Corso> corsoMap) {

		final String sql = "SELECT * FROM corso";

		List<Corso> corsi = new LinkedList<Corso>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Corso s = new Corso(rs.getString("codins"), rs.getInt("crediti"), rs.getString("nome"), rs.getInt("pd"));
				if(!corsoMap.containsKey(s.getCodins())) {
				
					corsoMap.put(s.getCodins(), s);
					corsi.add(s);
				}
			}

			return corsi;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	/*
	 * Ottengo tutti gli studenti salvati nel Db
	 */
	public List<Studente> getTuttiStudenti(Map<Integer, Studente> studentiMap) {

		final String sql = "SELECT * FROM studente";

		List<Studente> studenti = new LinkedList<Studente>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Studente s = new Studente(rs.getInt("matricola"), rs.getString("cognome"), rs.getString("nome"), rs.getString("CDS"));
				if(!studentiMap.containsKey(s.getMatricola())) {
				    studentiMap.put(s.getMatricola(), s);
					studenti.add(s);}
			}

			return studenti;

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
	}

	public void loadEdges(Graph<Nodo, DefaultEdge> grafo, Map<String, Corso> corsoMap,
			Map<Integer, Studente> studentiMap) {

		String sql = "SELECT * FROM iscrizione";
	
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				if(studentiMap.containsKey(rs.getInt("matricola")) && corsoMap.containsKey(rs.getString("codins"))) {
					
					Studente s  = studentiMap.get(rs.getInt("matricola"));
					Corso c = corsoMap.get(rs.getString("codins"));
					
					c.aggiungiStudente(s);
					s.aggiungiCorso(c);
					
				    grafo.addEdge(s, c);
					
				}
				
			}

			

		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
	}

}
