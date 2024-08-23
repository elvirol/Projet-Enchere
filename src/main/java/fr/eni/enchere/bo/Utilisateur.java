package fr.eni.enchere.bo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Utilisateur {
	
	// ATTRIBUTS
	private long noUtilisateur;
	
	@NotBlank
	@Size(max=30)
	@Pattern(regexp ="^[A-Za-z0-9]*$")
	private String pseudo;
	
	@NotBlank
	@Size(max=30)
	private String nom;
	
	@NotBlank
	@Size(max=30)
	private String prenom;
	
	@NotBlank
	@Size(max=30)
	@Email
	private String email;
	
	@Size(max=15)
	private String telephone;
	
	@NotBlank
	@Size(max=30)
	private String rue;
	
	@NotBlank
	@Size(max=10)
	private String codePostal;
	
	@NotBlank
	@Size(max=30)
	private String ville;
	
	
	//@Size(max = 70) pas obligatoire car le bcrypt fait 60 caractères dans tous les cas
	@NotBlank
	private String motDePasse;
	
	private long credit;
	private boolean est_admin;
	private boolean est_actif;
	private List<ArticleVendu> listeArticlesVendus;
	private List<ArticleVendu> listeArticlesAchetes;
	private List<Enchere> listeEncheres;
	
	
	// CONSTRUCTEURS
	public Utilisateur() {
		this.est_admin = false;
		this.est_actif = true;
		listeArticlesVendus = new ArrayList<>();
		listeArticlesAchetes = new ArrayList<>();
	}
	
	public Utilisateur(String pseudo, String nom, String prenom, String email, String telephone, String rue, String codePostal,
			String ville, String motDePasse) {
		this();
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.rue = rue;
		this.codePostal = codePostal;
		this.ville = ville;
		this.motDePasse = motDePasse;
	}
	
	
	// GETTERS ET SETTERS
	public long getNoUtilisateur() {
		return noUtilisateur;
	}
	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getRue() {
		return rue;
	}
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCodePostal() {
		return codePostal;
	}
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public String getMotDePasse() {
		return motDePasse;
	}
	public void setMotDePasse(String motDePasse) {
		this.motDePasse = motDePasse;
	}
	public long getCredit() {
		return credit;
	}
	public void setCredit(long credit) {
		this.credit = credit;
	}
	public boolean getEstAdmin() {
		return est_admin;
	}
	public void setEstAdmin(boolean est_administrateur) {
		this.est_admin = est_administrateur;
	}
	public boolean getEstActif() {
		return est_actif;
	}
	public void setEstActif(boolean est_actif) {
		this.est_actif=est_actif;
	}
	public List<ArticleVendu> getArticlesVendus() {
		return listeArticlesVendus;
	}
	public void setArticleVendu(List<ArticleVendu> articlesVendus) {
		this.listeArticlesVendus = articlesVendus;
	}
	public List<ArticleVendu> getArticleAcheter() {
		return listeArticlesAchetes;
	}
	public void setArticleAcheter(List<ArticleVendu> articlesAchetes) {
		this.listeArticlesAchetes = articlesAchetes;
	}
	public List<Enchere> getEnchere() {
		return listeEncheres;
	}
	public void setEnchere(List<Enchere> encheres) {
		this.listeEncheres = encheres;
	}
	public List<ArticleVendu> getListeArticleVendu() {
		return listeArticlesVendus;
	}
	public void setListeArticleVendu(List<ArticleVendu> listeArticlesVendus) {
		this.listeArticlesVendus = listeArticlesVendus;
	}
	public List<ArticleVendu> getListeArticleAcheter() {
		return listeArticlesAchetes;
	}
	public void setListeArticleAcheter(List<ArticleVendu> listeArticlesAchetes) {
		this.listeArticlesAchetes = listeArticlesAchetes;
	}
	
	
	// TOSTRING & HASHCODE & EQUALS
	@Override
	public String toString() {
		return String.format(
				"Utilisateur [noUtilisateur=%s, pseudo=%s, nom=%s, prenom=%s, email=%s, telephone=%s, rue=%s, codePostal=%s, ville=%s]",
				noUtilisateur, pseudo, nom, prenom, email, telephone, rue, codePostal, ville);
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(noUtilisateur);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Utilisateur other = (Utilisateur) obj;
		return noUtilisateur == other.noUtilisateur;
	}



	
	
	
	
	
}
