package fr.eni.enchere.dal;

import fr.eni.enchere.bo.Retrait;

public interface RetraitDAO {
	
	void creer(Retrait retrait);
	Retrait recupererParNoArticle(long no_article);
	
	void supprimerParArticle(long no_article);
	
}
