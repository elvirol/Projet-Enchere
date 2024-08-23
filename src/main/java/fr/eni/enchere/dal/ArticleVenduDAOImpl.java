package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.bo.tech.FilterEncheres;

@Repository
public class ArticleVenduDAOImpl implements ArticleVenduDAO {
	
	private static final String FIND_ALL = "SELECT no_article, nom_article, description, image_article, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_vendeur, no_acheteur, no_categorie FROM ARTICLES_VENDUS";
	private static final String FIND_BY_FILTER = "SELECT no_article, nom_article, description, image_article, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_vendeur, no_acheteur, no_categorie FROM ARTICLES_VENDUS ";
	private static final String FIND_BY_NO_ARTICLE = "SELECT no_article, nom_article, description, image_article, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_vendeur, no_acheteur, no_categorie FROM ARTICLES_VENDUS WHERE no_article = :no_article";
	private static final String CREATE = "INSERT INTO ARTICLES_VENDUS (nom_article, description, image_article, date_debut_encheres, date_fin_encheres, prix_initial, no_categorie, no_vendeur) VALUES (:nom_article, :description, :image_article, :date_debut_encheres, :date_fin_encheres, :prix_initial, :no_categorie, :no_vendeur)";
	private static final String UPDATE_PRIX_ACHETEUR = "UPDATE ARTICLES_VENDUS SET prix_vente = :prix_vente, no_acheteur = :no_acheteur WHERE no_article = :no_article";
	private static final String COUNT_BY_NO = "SELECT COUNT(*) FROM ARTICLES_VENDUS WHERE no_article = :no_article";
	private static final String DELETE_BY_ID ="DELETE FROM ARTICLES_VENDUS WHERE no_article = :no_article";

	private NamedParameterJdbcTemplate jdbcTemplate;

	public ArticleVenduDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<ArticleVendu> recupererTous() {
		return jdbcTemplate.query(FIND_ALL, new ArticleVenduRowMapper());
	}

	@Override
	public ArticleVendu recupererParNo(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		return jdbcTemplate.queryForObject(FIND_BY_NO_ARTICLE, mapSqlParameterSource, new ArticleVenduRowMapper());
	}
	
	// ID 2001 Vendre un article
	@Override
	public void creer(ArticleVendu article) {
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		
		mapSqlParameterSource.addValue("nom_article", article.getNomArticle());
		mapSqlParameterSource.addValue("description", article.getDescription());
		mapSqlParameterSource.addValue("image_article", article.getImg_article());
		mapSqlParameterSource.addValue("date_debut_encheres", article.getDateDebutEncheres());
		mapSqlParameterSource.addValue("date_fin_encheres", article.getDateFinEncheres());
		mapSqlParameterSource.addValue("prix_initial", article.getMiseAPrix());
		mapSqlParameterSource.addValue("no_categorie", article.getCategorie().getNoCategorie());
		mapSqlParameterSource.addValue("no_vendeur", article.getVendeur().getNoUtilisateur());
		
		jdbcTemplate.update(CREATE, mapSqlParameterSource, keyHolder);
		
		if(keyHolder != null && keyHolder.getKey() != null) {
			article.setNoArticle(keyHolder.getKey().longValue());
		}
	}

	@Override
	public List<ArticleVendu> recupererParFiltre(FilterEncheres filterEncheres) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		String filter = "";

		if (filterEncheres.getCategories() != 0 && filterEncheres.getMotCles().isBlank()) {
			filter = "WHERE no_categorie = :no_categorie";
			mapSqlParameterSource.addValue("no_categorie", filterEncheres.getCategories());
		} else if (filterEncheres.getCategories() == 0 && !filterEncheres.getMotCles().isBlank()) {
			filter = "WHERE nom_article LIKE :text_filter";
			mapSqlParameterSource.addValue("text_filter", "%" + filterEncheres.getMotCles() + "%");
		} else {
			filter = "WHERE nom_article LIKE :text_filter AND no_categorie = :no_categorie";
			mapSqlParameterSource.addValue("no_categorie", filterEncheres.getCategories());
			mapSqlParameterSource.addValue("text_filter", "%" + filterEncheres.getMotCles() + "%");
		}
		
		return jdbcTemplate.query(FIND_BY_FILTER + filter, mapSqlParameterSource, new ArticleVenduRowMapper());
	}
	
	@Override
	public void majApresEnchere(Enchere enchere) {
		MapSqlParameterSource namedPArameters = new MapSqlParameterSource();
		namedPArameters.addValue("prix_vente", enchere.getMontant_enchere());
		namedPArameters.addValue("no_article", enchere.getArticleVendu().getNoArticle());
		namedPArameters.addValue("no_acheteur", enchere.getUtilisateur().getNoUtilisateur());
		
		jdbcTemplate.update(UPDATE_PRIX_ACHETEUR, namedPArameters);
	}
	
	@Override
	public int compteNbArticleParNo(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		return jdbcTemplate.queryForObject(COUNT_BY_NO, mapSqlParameterSource, Integer.class);
	}
	
	@Override
	public void supprimer(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		jdbcTemplate.update(DELETE_BY_ID, mapSqlParameterSource);
	}	
}

class ArticleVenduRowMapper implements RowMapper<ArticleVendu>{
	
	@Override
	public ArticleVendu mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
		
		ArticleVendu article = new ArticleVendu();
		article.setNoArticle(rs.getLong("no_article"));
		article.setNomArticle(rs.getString("nom_article"));
		article.setDescription(rs.getString("description"));
		article.setImg_article(rs.getString("image_article"));
		article.setDateDebutEncheres(LocalDateTime.parse(rs.getString("date_debut_encheres"), formatter));
		article.setDateFinEncheres(LocalDateTime.parse(rs.getString("date_fin_encheres"), formatter));
		article.setMiseAPrix(rs.getLong("prix_initial"));
		article.setPrixDeVente(rs.getLong("prix_vente"));
		
		//etatVente
		if(article.getDateDebutEncheres()!=null && article.getDateFinEncheres()!=null) {
			LocalDateTime now = LocalDateTime.now();
			article.setEtatVente("Cree");
			if(now.isAfter(article.getDateDebutEncheres())) {
				article.setEtatVente("EnCours");
				if(now.isAfter(article.getDateFinEncheres())) {
					article.setEtatVente("Termine");
				}
			}
		}
		
		// no_vendeur
		Utilisateur vendeur = new Utilisateur();
		vendeur.setNoUtilisateur(rs.getLong("no_vendeur"));
		article.setVendeur(vendeur);
		
		// no_acheteur
		if(rs.getLong("no_acheteur")>0) {
			Utilisateur acheteur = new Utilisateur();
			acheteur.setNoUtilisateur(rs.getLong("no_acheteur"));
			article.setAcheteur(acheteur);
		}
		
		// no_categorie
		Categorie categorie = new Categorie();
		categorie.setNoCategorie(rs.getLong("no_categorie"));
		article.setCategorie(categorie);
		
		return article;
	}
	
}
