package fr.eni.enchere.dal;

import fr.eni.enchere.bo.Utilisateur;

public interface UtilisateurDAO {
	
	Utilisateur recupererParPseudo(String pseudo);
	Utilisateur recupererParEmail(String email);
	Utilisateur recupererParNo(long no_utilisateur);
	String recupererCryptMDPParNo(long no_utilisateur);
	
	void creer(Utilisateur utilisateur);
	
	void actualiser(Utilisateur utilisateur);
	void modifierCredit(long no_utilisateur, long credit);
	
	void desactiverParNo(Long no_utilisateur);
	
	int nbParPseudo(String pseudo);
	int nbParEmail(String email);

}
