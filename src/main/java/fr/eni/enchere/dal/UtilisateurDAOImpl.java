package fr.eni.enchere.dal;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import fr.eni.enchere.bo.Utilisateur;

@Repository
public class UtilisateurDAOImpl implements UtilisateurDAO{

	private static final String READ_BY_PSEUDO = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit from UTILISATEURS where pseudo= :pseudo";
	private static final String READ_BY_EMAIL = "select no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit from UTILISATEURS where email= :email";
	public static final String READ_BY_NO = "SELECT no_utilisateur, pseudo, nom, prenom, email, telephone, rue, code_postal, ville, credit, est_admin, est_actif FROM UTILISATEURS WHERE no_utilisateur = :no_utilisateur";
	public static final String READ_MDP_BY_NO = "SELECT mot_de_passe FROM UTILISATEURS WHERE no_utilisateur = :no_utilisateur";
	public static final String CREATE_MANDATORY = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, rue, code_postal, ville, mot_de_passe, credit, est_admin, est_actif) VALUES (:pseudo, :nom, :prenom, :email, :rue, :code_postal, :ville, :mot_de_passe, :credit, :est_admin, :est_actif)";
	public static final String UPDATE_MANDATORY_BY_NO = "UPDATE UTILISATEURS SET pseudo=:pseudo, nom=:nom, prenom=:prenom, email=:email, rue=:rue, code_postal=:code_postal, ville=:ville, mot_de_passe=:mot_de_passe, credit=:credit, est_admin=:est_admin, est_actif=:est_actif WHERE no_utilisateur=:no_utilisateur";
	public static final String UPDATE_OPTIONNAL_BY_NO = "UPDATE UTILISATEURS SET telephone=:tel WHERE no_utilisateur=:no_utilisateur";
	private static final String UPDATE_CREDIT = "update UTILISATEURS set credit = :credit where no_utilisateur = :no_utilisateur";
	private static final String DEACTIVATE_BY_NO = "UPDATE UTILISATEURS SET est_actif = 0 where no_utilisateur = :no_utilisateur";
	private static final String COUNT_BY_PSEUDO = "SELECT count(*) FROM UTILISATEURS WHERE email=:pseudo";
	private static final String COUNT_BY_EMAIL = "SELECT count(*) FROM UTILISATEURS WHERE email=:email";

	private NamedParameterJdbcTemplate jdbcTemplate;
	
	public UtilisateurDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Utilisateur recupererParPseudo(String pseudo) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(READ_BY_PSEUDO, namedParameter, new BeanPropertyRowMapper<>(Utilisateur.class));
	}
	
	@Override
	public Utilisateur recupererParEmail(String email) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("email", email);
		return jdbcTemplate.queryForObject(READ_BY_EMAIL, namedParameter, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public void creer(Utilisateur utilisateur) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("pseudo", utilisateur.getPseudo());
		namedParameter.addValue("nom", utilisateur.getNom());
		namedParameter.addValue("prenom", utilisateur.getPrenom());
		namedParameter.addValue("email", utilisateur.getEmail());
		namedParameter.addValue("rue", utilisateur.getRue());
		namedParameter.addValue("code_postal", utilisateur.getCodePostal());
		namedParameter.addValue("ville", utilisateur.getVille());
		namedParameter.addValue("mot_de_passe", utilisateur.getMotDePasse());
		namedParameter.addValue("credit", utilisateur.getCredit());
		namedParameter.addValue("est_admin", utilisateur.getEstAdmin());
		namedParameter.addValue("est_actif", utilisateur.getEstActif());
		jdbcTemplate.update(CREATE_MANDATORY, namedParameter,keyHolder);

		//maj des param optionnels
		if(!utilisateur.getTelephone().isEmpty()) {
			namedParameter.addValue("tel", utilisateur.getTelephone());
			namedParameter.addValue("no_utilisateur", keyHolder.getKey().longValue());
			jdbcTemplate.update(UPDATE_OPTIONNAL_BY_NO, namedParameter);
		}
		
	}

	@Override
	public int nbParPseudo(String pseudo) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("pseudo", pseudo);
		return jdbcTemplate.queryForObject(COUNT_BY_PSEUDO, namedParameter, Integer.class);
	}

	@Override
	public int nbParEmail(String email) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("email", email);
		return jdbcTemplate.queryForObject(COUNT_BY_EMAIL, namedParameter, Integer.class);
	}
	
	@Override
	public Utilisateur recupererParNo(long no_utilisateur) {
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("no_utilisateur", no_utilisateur);
		return jdbcTemplate.queryForObject(READ_BY_NO, mapSqlParameterSource, new BeanPropertyRowMapper<>(Utilisateur.class));
	}

	@Override
	public void desactiverParNo(Long no_utilisateur) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("no_utilisateur", no_utilisateur);
		jdbcTemplate.update(DEACTIVATE_BY_NO, namedParameter);
	}
	
	@Override
	public void actualiser(Utilisateur utilisateur) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("no_utilisateur", utilisateur.getNoUtilisateur());
		namedParameter.addValue("pseudo", utilisateur.getPseudo());
		namedParameter.addValue("nom", utilisateur.getNom());
		namedParameter.addValue("prenom", utilisateur.getPrenom());
		namedParameter.addValue("email", utilisateur.getEmail());
		namedParameter.addValue("rue", utilisateur.getRue());
		namedParameter.addValue("code_postal", utilisateur.getCodePostal());
		namedParameter.addValue("ville", utilisateur.getVille());
		namedParameter.addValue("mot_de_passe", utilisateur.getMotDePasse());
		namedParameter.addValue("credit", utilisateur.getCredit());
		namedParameter.addValue("est_admin", utilisateur.getEstAdmin());
		namedParameter.addValue("est_actif", utilisateur.getEstActif());
		jdbcTemplate.update(UPDATE_MANDATORY_BY_NO, namedParameter);
		
		if(!utilisateur.getTelephone().isEmpty()) {
			namedParameter.addValue("tel", utilisateur.getTelephone());
			jdbcTemplate.update(UPDATE_OPTIONNAL_BY_NO, namedParameter);
		}
	}
	
	@Override
	public String recupererCryptMDPParNo(long no_utilisateur) {
		MapSqlParameterSource namedParameter = new MapSqlParameterSource();
		namedParameter.addValue("no_utilisateur", no_utilisateur);
		return jdbcTemplate.queryForObject(READ_MDP_BY_NO, namedParameter, String.class);
	}
	
	@Override
	public void modifierCredit(long no_utilisateur, long credit) {
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("no_utilisateur", no_utilisateur);
		namedParameters.addValue("credit", credit);
		
		jdbcTemplate.update(UPDATE_CREDIT, namedParameters);
	}
}
