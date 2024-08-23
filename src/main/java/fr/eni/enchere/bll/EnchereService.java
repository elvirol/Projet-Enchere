package fr.eni.enchere.bll;

import java.util.List;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.tech.FilterEncheres;
import fr.eni.enchere.exception.BusinessException;

public interface EnchereService {
	
	List<ArticleVendu> afficherEncheres();
	List<ArticleVendu> filtrerEncheres(FilterEncheres filterEncheres);
	
	List<Categorie> afficherCategories();
	Categorie afficherCategorieParID(long id);
	
	ArticleVendu consulterArticle(Long no_article);
	
	void creerArticle(ArticleVendu article)throws BusinessException;
	
	void soumettreEnchere(Enchere enchere) throws BusinessException;
	
	void supprimerArticle(long no_article) throws BusinessException;
	
}
