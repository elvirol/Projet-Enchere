package fr.eni.enchere.bll;

import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.exception.BusinessException;

public interface UtilisateurService {

	Utilisateur recupererUtilisateurParPseudo(String pseudo);
	Utilisateur recupererUtilisateurParEmail(String email);
	
	void soumettreNouveau(Utilisateur utilisateur,String confirmation) throws BusinessException;
	
	void majCompte(Utilisateur utilisateur,String nouveauMDP,String confirmation) throws BusinessException;
	
	void desactiverUtilisateur(Utilisateur utilisateur);
	
}
