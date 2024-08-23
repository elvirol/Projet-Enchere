package fr.eni.enchere.bo;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Categorie {
	
	// ATTRIBUTS
	private Long noCategorie;
	@NotBlank
	@Size(max = 30)
	private String libelle;
	private List<ArticleVendu> listeArticle;
	
	@Override
	public String toString() {
		return "Categorie [noCategorie=" + noCategorie + ", libelle=" + libelle + ", listeArticle=" + listeArticle
				+ "]";
	}
	// GETTERS ET SETTERS
	public long getNoCategorie() {
		return noCategorie;
	}
	public void setNoCategorie(long noCategorie) {
		this.noCategorie = noCategorie;
	}
	public String getLibelle() {
		return libelle;
	}
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}
	public List<ArticleVendu> getListeArticle() {
		return listeArticle;
	}
	public void setListeArticle(List<ArticleVendu> listeArticle) {
		this.listeArticle = listeArticle;
	}
	
	

}
