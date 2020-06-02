package it.polito.tdp.rivers.model;

import java.security.InvalidParameterException;
import java.util.PriorityQueue;

import javax.management.InvalidAttributeValueException;

public class Simulator {
	
	// Parametri di simulazione
	River r;
	private double capienzaMax;
	private int numeroGiorni;
	private double flussoUscitaMinimo;
	
	// Output da calcolare
	private int giorniNonGarantiti;
	private int giorniTotali;
	private double capienzaCumulata;
	private double quantitaTotaleNonGarantita;
	
	// Stato del sistema
	private double capienzaAttuale;
	// private double flussoUscitaAttuale;
	
	// Coda degli eventi
	PriorityQueue<Flow> coda = new PriorityQueue<>();
	
	// Inizializzazione
	public void init(River r, double cap) {
		if (cap <= 0.0)
			throw new InvalidParameterException("E' stato inserito un campo non valido. Il fattore k deve essere strettametne positivo.");
		
		this.giorniNonGarantiti = 0;
		this.numeroGiorni = 30;
		this.quantitaTotaleNonGarantita = 0.0;
		this.giorniTotali = 0;
		this.capienzaCumulata = 0.0;
		
		if (r==null)
			throw new NullPointerException("Non è stato inserito un fiume.");
		else
			this.r = r;
		
		if (r.getFlowAvg() == 0.0)
			throw new NullPointerException("Non è presente il flusso medio.");
		
		this.capienzaMax = cap * r.getFlowAvg() * numeroGiorni;
		
		this.capienzaAttuale = this.capienzaMax / 2;
		
		this.flussoUscitaMinimo = r.getFlowAvg() * 0.8;
		
		System.out.println(r.getFlowAvg() + ": FLUSSO MEDIO, " + this.flussoUscitaMinimo + ": FLUSSO USCITA MINIMO, " + this.capienzaMax + ": CAPIENZA MASSIMA.\n");
		for (Flow f: r.getFlows()) {
			System.out.println(f);
			coda.add(f);
		}
	}
	
	public void run() {
		if (coda.isEmpty())
			throw new NullPointerException("Nessun elemento presente nella coda. Sicuri di aver selezionato un fiume?");
		
		while(!coda.isEmpty()) {
			Flow f = coda.poll();

			processEvent(f);
		}
	}
	// Esecuzione
	
	// Altre funzioni
	
	private void processEvent(Flow f) {
		double flowIn = f.getFlow();
		double flowOut = this.calculateFlowOut();
				
		double flowDifference = flowIn - flowOut;
		
		if (flowIn <= 0.0 || flowOut <= 0.0)
			throw new InvalidParameterException("Ci sono stati problemi nella gestione del flusso. Ritenta la simulazione o controlla che il database sia funzionante.");
		
		if (flowDifference + capienzaAttuale <= 0.0) {
			this.giorniNonGarantiti ++;
			// non aumento la capienza in quanto esce tutta l'acqua che entra, il bilancio dunque è nullo
			quantitaTotaleNonGarantita += Math.abs(flowDifference);
		}
		else if (capienzaAttuale + flowDifference >= capienzaMax)
			capienzaAttuale = capienzaMax;
			// non posso far risalire l'acqua oltre la capienza massima, la faccio scendere
		else
			capienzaAttuale += flowDifference;
			// condizione normale, aumento la portata dell'acqua
		
		// Gestione variabili di sistema
		this.giorniTotali++;
		this.capienzaCumulata += capienzaAttuale;
		
		System.out.println("EROGAZIONE FLUSSO: " + f.getDay() + ", DIFFERENZA DI FLUSSO: " + flowDifference + " E CAPIENZA ATTUALE DI " + capienzaAttuale + ";");
	}

	private double calculateFlowOut() {
		double rand = Math.random();
		double flowOut;
		if (rand<=0.05)
			flowOut = 10 * flussoUscitaMinimo;
		else
			flowOut = flussoUscitaMinimo;
		
		return flowOut;
	}
	
	public double getCapienzaMedia() {
		return capienzaCumulata / giorniTotali;
	}

	public int getGiorniNonGarantiti() {
		return giorniNonGarantiti;
	}
	

}
