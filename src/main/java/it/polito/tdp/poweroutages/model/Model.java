package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.polito.tdp.poweroutages.DAO.PowerOutageDAO;

public class Model {
	
	PowerOutageDAO podao;
	List<PowerOutage> tutti;
	List<PowerOutage> migliore;
	int maxCustomers;
	
	
	public Model() {
		podao = new PowerOutageDAO();
		migliore = null;
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public List<PowerOutage> trovaSequenza(int anni, int ore, Nerc nerc) {
		List<PowerOutage> parziale = new ArrayList<PowerOutage>();
		tutti = podao.getPowerOutages(nerc);
		
		trovaSequenzaRicorsiva(anni, ore, parziale);
		maxCustomers = sommaUtentiCoinvolti(migliore);
		
		return migliore;
	}
	
	private void trovaSequenzaRicorsiva(int anni, int ore, List<PowerOutage> parziale) {
		if(!controlloOreEAnni(anni, ore, parziale)) {
			//Se fallisco questo controllo allora parziale non è una soluzione accettabile
			return;
		}
		
		if(migliore == null || sommaUtentiCoinvolti(migliore)<sommaUtentiCoinvolti(parziale)) {
			//Se ho piu utenti coinvolti nella sol parziale allora questa diventa la mia migliore
			migliore = new ArrayList<PowerOutage>(parziale);
			
		}
		
		
		for(int i=0; i<tutti.size(); i++) {
			if(!parziale.contains(tutti.get(i))) {
				parziale.add(tutti.get(i));
				trovaSequenzaRicorsiva(anni, ore, parziale);
				parziale.remove(tutti.get(i));
			}else {
				return;
			}
			
		}
		
		
	}
	
	private boolean controlloOreEAnni(int anni, int ore, List<PowerOutage> parziale) {
		if(parziale.size()==0) {
			return true;
		}
		
		long sommaOre = 0;
		List<PowerOutage> prova = new ArrayList<PowerOutage>(parziale);
		
		//Controllo sulle ore
		for(PowerOutage p : prova) {
			sommaOre += Duration.between(p.getDateBegan(), p.getDateFinished()).toHours();
			
			if(sommaOre>ore) {
				return false;
			}
		}
		
		//Ordino prova per data di inizio
		Collections.sort(prova, Comparator.comparing(PowerOutage::getDateBegan));
		
		//Controllo sugli anni
		if(Duration.between(prova.get(0).getDateBegan(), 
				prova.get(prova.size()-1).getDateBegan()).toDays()/365>anni) {
			return false;
		}
		
		return true;
	}
	
	private int sommaUtentiCoinvolti(List<PowerOutage> parziale) {
		int somma = 0;
		
		for(PowerOutage p : parziale) {
			somma += p.getCustomersAffected();
		}
		
		return somma;
	}

	public int getMaxCustomers() {
		return maxCustomers;
	}
	
	
	
}
