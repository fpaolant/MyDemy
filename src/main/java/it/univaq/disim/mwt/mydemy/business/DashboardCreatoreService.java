package it.univaq.disim.mwt.mydemy.business;

import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface DashboardCreatoreService {

	ResponsePieData getVendite(Utente creatore);
}
