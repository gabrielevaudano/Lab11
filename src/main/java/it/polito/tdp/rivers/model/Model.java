package it.polito.tdp.rivers.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.rivers.db.RiversDAO;

public class Model {
	private RiversDAO dao;
	private List<River> rivers;
	private Simulator sim;
	
	public Model() {
		dao = new RiversDAO();
		this.rivers = new ArrayList<>(dao.getAllRivers());
		this.sim = new Simulator();
	}
	
	public List<River> getAllRivers() {
		return this.rivers;
	}
	
	public void addFlows(River r) {
		rivers.get(rivers.indexOf(r)).setFlows(dao.fillRiver(r));
		dao.fillCompositeRiver(r);
	}
	
	public String getStats(River r, double cap) {
		sim.init(r, cap);
		sim.run();
		
		String result = String.format("I giorni in cui l'afflusso di acqua non è garantita sono %d, la capienza media invece è di %f.", sim.getGiorniNonGarantiti(), sim.getCapienzaMedia());
	
		return result;
	}
}