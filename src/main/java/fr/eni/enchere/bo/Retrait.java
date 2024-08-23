package fr.eni.enchere.bo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Retrait {
	
	// ATTRIBUTS
	@NotBlank
	@Size(min = 1,max = 30)
	private String rue;
	@NotBlank
	@Size(max = 5, min = 5)
	private String code_postal;
	@NotBlank
	@Size(max = 30)
	private String ville;
	private ArticleVendu articleVendu;
	
	
	// CONSTRUCTEUR
	public Retrait() {
	}
	
	public Retrait(String rue, String code_postal, String ville) {
		this.rue = rue;
		this.code_postal = code_postal;
		this.ville = ville;
	}
	
	@Override
	public String toString() {
		return "Retrait [rue=" + rue + ", code_postal=" + code_postal + ", ville=" + ville + ", articleVendu="
				+ articleVendu + "]";
	}

	// GETTERS ET SETTERS
	public String getRue() {
		return rue;
	}
	
	public void setRue(String rue) {
		this.rue = rue;
	}
	public String getCode_postal() {
		return code_postal;
	}
	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}
	public ArticleVendu getArticleVendu() {
		return articleVendu;
	}
	public void setArticleVendu(ArticleVendu articleVendu) {
		this.articleVendu = articleVendu;
	}

}
