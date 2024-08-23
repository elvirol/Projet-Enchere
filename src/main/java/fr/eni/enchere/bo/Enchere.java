package fr.eni.enchere.bo;

import java.time.LocalDateTime;

public class Enchere {
	
	// ATTRIBUTS
	private LocalDateTime dateEnchere;
	private long montant_enchere;
	private Utilisateur utilisateur;
	private ArticleVendu articleVendu;
	
	
	// CONSTRUCTEURS
	public Enchere() {
	}
	
	public Enchere(LocalDateTime dateEnchere, long montant_enchere, Utilisateur utilisateur, ArticleVendu articleVendu) {
	this.dateEnchere = dateEnchere;
	this.montant_enchere = montant_enchere;
	this.utilisateur = utilisateur;
	this.articleVendu = articleVendu;
	}
		
	
	// GETTERS ET SETTERS
	public LocalDateTime getDateEnchere() {
		return dateEnchere;
	}
	public void setDateEnchere(LocalDateTime dateEnchere) {
		this.dateEnchere = dateEnchere;
	}
	public long getMontant_enchere() {
		return montant_enchere;
	}
	public void setMontant_enchere(long montant_enchere) {
		this.montant_enchere = montant_enchere;
	}
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	public ArticleVendu getArticleVendu() {
		return articleVendu;
	}
	public void setArticleVendu(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}
	
}
