package fr.eni.enchere.dal;

import fr.eni.enchere.bo.Enchere;

public interface EnchereDAO {
	
	Enchere obtenirEnchereMax(long numeroArticle);
	
	void ajouter(Enchere enchere);
	
	Enchere recupererEnchere(Enchere enchere);

	int compteNbEncheresParArticle(long no_article);
}
