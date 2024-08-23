package fr.eni.enchere.dal;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Utilisateur;

@Repository
public class EnchereDAOImpl implements EnchereDAO{
	
	private static final String AJOUTER_LIGNE_ENCHERE = "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (:no_utilisateur, :no_article, :date_enchere, :montant_enchere)";
	private static final String OBTENIR_ENCHERE = "SELECT * FROM ENCHERES WHERE no_utilisateur = :no_utilisateur AND no_article = :no_article";
	private static final String OBTENIR_ENCHERE_MAX = "SELECT no_utilisateur, no_article, date_enchere, montant_enchere FROM ENCHERES WHERE no_article = :no_article AND montant_enchere = (SELECT MAX(montant_enchere) FROM ENCHERES WHERE no_article = :no_article)";
	private static final String COUNT_BY_ARTICLE = "SELECT COUNT(*) FROM ENCHERES WHERE no_article = :no_article";
	
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public EnchereDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public Enchere recupererEnchere(Enchere enchere) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", enchere.getUtilisateur().getNoUtilisateur());
		namedParameters.addValue("no_article", enchere.getArticleVendu().getNoArticle());
		
		return jdbcTemplate.queryForObject(OBTENIR_ENCHERE, namedParameters, new EnchereRowMapper());
		
	}
	
	@Override
	public Enchere obtenirEnchereMax(long numeroArticle) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_article", numeroArticle);
		
		return jdbcTemplate.queryForObject(OBTENIR_ENCHERE_MAX, namedParameters, new EnchereRowMapper());
	}
	
	@Override
	public void ajouter(Enchere enchere) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", enchere.getUtilisateur().getNoUtilisateur());
		namedParameters.addValue("no_article", enchere.getArticleVendu().getNoArticle());
		namedParameters.addValue("date_enchere", enchere.getDateEnchere());
		namedParameters.addValue("montant_enchere", enchere.getMontant_enchere());
		
		jdbcTemplate.update(AJOUTER_LIGNE_ENCHERE, namedParameters);
	}

	@Override
	public int compteNbEncheresParArticle(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		return jdbcTemplate.queryForObject(COUNT_BY_ARTICLE, mapSqlParameterSource, Integer.class);
	}

}

class EnchereRowMapper implements RowMapper<Enchere>{
	
	@Override
	public Enchere mapRow(ResultSet rs, int rowNum) throws SQLException {
				
		Enchere enchere = new Enchere();
		enchere.setMontant_enchere(rs.getLong("montant_enchere"));
		
		ArticleVendu article = new ArticleVendu();
		article.setNoArticle(rs.getLong("no_article"));
		enchere.setArticleVendu(article);
		
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setNoUtilisateur(rs.getLong("no_utilisateur"));
		enchere.setUtilisateur(utilisateur);
			
		return enchere;
	}
	
}


