package fr.eni.enchere.bo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ArticleVendu {
	
	// ATTRIBUTS
	private long noArticle;
	@NotBlank
	@Size(max = 30)
	private String nomArticle;
	@NotBlank
	@Size(max = 300)
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
	@NotNull
	private LocalDateTime dateDebutEncheres;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
	@NotNull
	private LocalDateTime dateFinEncheres;
	@Min(value = 1)
	private long miseAPrix;
	private long prixDeVente;
	private String etatVente;
	private Utilisateur acheteur;
	private Utilisateur vendeur;
	private List<Enchere> listeEncheres;
	@Valid
	private Categorie categorie;
	@Valid
	private Retrait lieuRetrait;
	private String img_article;
	
	@Override
	public String toString() {
		return "ArticleVendu [noArticle=" + noArticle + ", nomArticle=" + nomArticle + ", description=" + description
				+ ", dateDebutEncheres=" + dateDebutEncheres + ", dateFinEncheres=" + dateFinEncheres + ", miseAPrix="
				+ miseAPrix + ", prixDeVente=" + prixDeVente + ", etatVente=" + etatVente + ", acheteur=" + acheteur
				+ ", vendeur=" + vendeur + ", listeEncheres=" + listeEncheres + ", categorie=" + categorie
				+ ", lieuRetrait=" + lieuRetrait + "]";
	}
	
	// GETTERS ET SETTERS
	public long getNoArticle() {
		return noArticle;
	}
	public void setNoArticle(long noArticle) {
		this.noArticle = noArticle;
	}
	public String getNomArticle() {
		return nomArticle;
	}
	public void setNomArticle(String nomArticle) {
		this.nomArticle = nomArticle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getDateDebutEncheres() {
		return dateDebutEncheres;
	}
	public void setDateDebutEncheres(LocalDateTime dateDebutEncheres) {
		this.dateDebutEncheres = dateDebutEncheres;
	}
	public LocalDateTime getDateFinEncheres() {
		return dateFinEncheres;
	}
	public void setDateFinEncheres(LocalDateTime dateFinEncheres) {
		this.dateFinEncheres = dateFinEncheres;
	}
	public long getMiseAPrix() {
		return miseAPrix;
	}
	public void setMiseAPrix(long miseAPrix) {
		this.miseAPrix = miseAPrix;
	}
	public long getPrixDeVente() {
		return prixDeVente;
	}
	public void setPrixDeVente(long prixDeVente) {
		this.prixDeVente = prixDeVente;
	}
	public String getEtatVente() {
		return etatVente;
	}
	public void setEtatVente(String etatVente) {
		this.etatVente = etatVente;
	}
	public Utilisateur getAcheteur() {
		return acheteur;
	}
	public void setAcheteur(Utilisateur acheteur) {
		this.acheteur = acheteur;
	}
	public Utilisateur getVendeur() {
		return vendeur;
	}
	public void setVendeur(Utilisateur vendeur) {
		this.vendeur = vendeur;
	}
	public List<Enchere> getListeEncheres() {
		return listeEncheres;
	}
	public void setListeEncheres(List<Enchere> listeEncheres) {
		this.listeEncheres = listeEncheres;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	public Retrait getLieuRetrait() {
		return lieuRetrait;
	}
	public void setLieuRetrait(Retrait lieuRetrait) {
		this.lieuRetrait = lieuRetrait;
	}

	public String getImg_article() {
		return img_article;
	}

	public void setImg_article(String img_article) {
		this.img_article = img_article;
	}
	
	

}
