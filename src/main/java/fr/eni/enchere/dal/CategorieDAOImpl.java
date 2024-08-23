package fr.eni.enchere.dal;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Categorie;

@Repository
public class CategorieDAOImpl implements CategorieDAO {
	
	private static final String READ_BY_NO = "SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie = :no_categorie;";
	private static final String FIND_ALL = "SELECT no_categorie, libelle FROM CATEGORIES";
	private static final String COUNT_CATEGORY = "SELECT COUNT(*) no_categorie FROM CATEGORIES WHERE no_categorie = :id";
	
	private NamedParameterJdbcTemplate jdbcTemplate;

	public CategorieDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Categorie recupererParNo(long no_categorie) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_categorie", no_categorie);
		return jdbcTemplate.queryForObject(READ_BY_NO, mapSqlParameterSource, new BeanPropertyRowMapper<>(Categorie.class));
	}

	@Override
	public List<Categorie> recupererTous() {
		return jdbcTemplate.query(FIND_ALL, new BeanPropertyRowMapper<>(Categorie.class));
	}

	@Override
	public int countNbCategorieById(long id) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("id", id);
		return jdbcTemplate.queryForObject(COUNT_CATEGORY, mapSqlParameterSource, Integer.class);
	}
}
