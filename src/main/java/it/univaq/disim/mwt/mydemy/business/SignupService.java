package it.univaq.disim.mwt.mydemy.business;


import it.univaq.disim.mwt.mydemy.domain.Utente;

public interface SignupService {
	
	void signUp(Utente utente) throws BusinessException;

    void changePassword(Utente utente, String password) throws BusinessException;
}
