package fr.eni.enchere.bo.tech;

import java.util.Objects;

public class FilterEncheres {
	
	private String motCles;
	private long noUtilisateur;
	private long categories;
	private String typeRecherche = "";
	private String encheresOuvertes = "";
	private String encheresEnCours = "";
	private String encheresRemportees = "";
	private String ventesEnCours = "";
	private String ventesNonDebutees = "";
	private String ventesTerminees = "";

	public String getMotCles() {
		return motCles;
	}

	public void setMotCles(String motCles) {
		this.motCles = motCles;
	}

	public long getNoUtilisateur() {
		return noUtilisateur;
	}

	public void setNoUtilisateur(long noUtilisateur) {
		this.noUtilisateur = noUtilisateur;
	}

	public long getCategories() {
		return categories;
	}

	public void setCategories(long categories) {
		this.categories = categories;
	}

	public String getTypeRecherche() {
		return typeRecherche;
	}

	public void setTypeRecherche(String typeRecherche) {
		this.typeRecherche = typeRecherche;
	}

	public String getEncheresOuvertes() {
		return encheresOuvertes;
	}

	public void setEncheresOuvertes(String encheresOuvertes) {
		this.encheresOuvertes = encheresOuvertes;
	}

	public String getEncheresEnCours() {
		return encheresEnCours;
	}

	public void setEncheresEnCours(String encheresEnCours) {
		this.encheresEnCours = encheresEnCours;
	}

	public String getEncheresRemportees() {
		return encheresRemportees;
	}

	public void setEncheresRemportees(String encheresRemportees) {
		this.encheresRemportees = encheresRemportees;
	}

	public String getVentesEnCours() {
		return ventesEnCours;
	}

	public void setVentesEnCours(String ventesEnCours) {
		this.ventesEnCours = ventesEnCours;
	}

	public String getVentesNonDebutees() {
		return ventesNonDebutees;
	}

	public void setVentesNonDebutees(String ventesNonDebutees) {
		this.ventesNonDebutees = ventesNonDebutees;
	}

	public String getVentesTerminees() {
		return ventesTerminees;
	}

	public void setVentesTerminees(String ventesTerminees) {
		this.ventesTerminees = ventesTerminees;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categories, encheresEnCours, encheresOuvertes, encheresRemportees, motCles, noUtilisateur,
				typeRecherche, ventesEnCours, ventesNonDebutees, ventesTerminees);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilterEncheres other = (FilterEncheres) obj;
		return categories == other.categories && Objects.equals(encheresEnCours, other.encheresEnCours)
				&& Objects.equals(encheresOuvertes, other.encheresOuvertes)
				&& Objects.equals(encheresRemportees, other.encheresRemportees)
				&& Objects.equals(motCles, other.motCles) && noUtilisateur == other.noUtilisateur
				&& Objects.equals(typeRecherche, other.typeRecherche)
				&& Objects.equals(ventesEnCours, other.ventesEnCours)
				&& Objects.equals(ventesNonDebutees, other.ventesNonDebutees)
				&& Objects.equals(ventesTerminees, other.ventesTerminees);
	}

	@Override
	public String toString() {
		return "FilterEncheres [motCles=" + motCles + ", noUtilisateur=" + noUtilisateur + ", categories=" + categories
				+ ", typeRecherche=" + typeRecherche + ", encheresOuvertes=" + encheresOuvertes + ", encheresEnCours="
				+ encheresEnCours + ", encheresRemportees=" + encheresRemportees + ", ventesEnCours=" + ventesEnCours
				+ ", ventesNonDebutees=" + ventesNonDebutees + ", ventesTerminees=" + ventesTerminees + "]";
	}
}
