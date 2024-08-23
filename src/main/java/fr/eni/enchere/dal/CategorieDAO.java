package fr.eni.enchere.dal;

import java.util.List;

import fr.eni.enchere.bo.Categorie;

public interface CategorieDAO {
	
	Categorie recupererParNo(long no_categorie);

	List<Categorie> recupererTous();
	
	int countNbCategorieById(long id);
	
}
