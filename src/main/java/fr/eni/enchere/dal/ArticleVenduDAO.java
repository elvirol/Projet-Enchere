package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.tech.FilterEncheres;

public interface ArticleVenduDAO {
	
	List<ArticleVendu> recupererTous();
	
	ArticleVendu recupererParNo(long no_article);
	List<ArticleVendu> recupererParFiltre(FilterEncheres filterEncheres);
	
	void creer(ArticleVendu article);

	void majApresEnchere(Enchere enchere);
	
	int compteNbArticleParNo(long no_article);
	
	void supprimer(long no_article);
	
}
