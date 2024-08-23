package fr.eni.enchere.dal;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Retrait;

@Repository
public class RetraitDAOImpl implements RetraitDAO {
	
	private static final String CREATE = "INSERT INTO RETRAITS (no_article, rue, code_postal, ville) VALUES (:no_article, :rue, :code_postal, :ville)";
	private static final String FIND_BY_NO_ARTICLE = "SELECT no_article, rue, code_postal, ville FROM RETRAITS WHERE no_article = :no_article";
	private static final String DELETE_BY_IDARTICLE ="DELETE FROM RETRAITS WHERE no_article = :no_article";
	
	private NamedParameterJdbcTemplate jdbcTemplate;

	public RetraitDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void creer(Retrait retrait) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", retrait.getArticleVendu().getNoArticle());
		mapSqlParameterSource.addValue("rue", retrait.getRue());
		mapSqlParameterSource.addValue("code_postal", retrait.getCode_postal());
		mapSqlParameterSource.addValue("ville", retrait.getVille());
		
		jdbcTemplate.update(CREATE, mapSqlParameterSource);
		
	}
	
	@Override
	public Retrait recupererParNoArticle(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		return jdbcTemplate.queryForObject(FIND_BY_NO_ARTICLE, mapSqlParameterSource, new BeanPropertyRowMapper<>(Retrait.class));
	}
	
	@Override
	public void supprimerParArticle(long no_article) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_article", no_article);
		jdbcTemplate.update(DELETE_BY_IDARTICLE, mapSqlParameterSource);
	}	
}
