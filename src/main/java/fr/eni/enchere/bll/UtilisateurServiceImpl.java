package fr.eni.enchere.bll;

import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.dal.UtilisateurDAO;
import fr.eni.enchere.exception.BusinessException;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

	private UtilisateurDAO utilisateurDAO;
	
	
	public UtilisateurServiceImpl(UtilisateurDAO utilisateurDAO) {
		this.utilisateurDAO = utilisateurDAO;
	}
	
	@Override
	public Utilisateur recupererUtilisateurParPseudo(String pseudo) {
		return utilisateurDAO.recupererParPseudo(pseudo);
	}
	
	@Override
	public Utilisateur recupererUtilisateurParEmail(String email) {
		return utilisateurDAO.recupererParEmail(email);
	}
	
	@Override
	public void soumettreNouveau(Utilisateur utilisateur,String confirmation) throws BusinessException {
		
		BusinessException be = new BusinessException();
		
		//verif unicité pseudo et email parmi tous les pseudos et emails confondus + verif mot de passe
		boolean isValid=estPseudoUnique(utilisateur.getPseudo(),be)
							& estEmailUnique(utilisateur.getEmail(),be)
							& estPseudoUnique(utilisateur.getEmail(),be)
							& estEmailUnique(utilisateur.getPseudo(),be)
							& confirmerMotDePasse(utilisateur.getMotDePasse(),confirmation,be);
		
		if(isValid) {
			utilisateur.setCredit(0);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			utilisateur.setMotDePasse("{bcrypt}"+encoder.encode(utilisateur.getMotDePasse()));
			try {
				utilisateurDAO.creer(utilisateur);
			} catch (DataAccessException de) {
				be.ajouterMessage("Erreur : un problème est survenu avec la base de données//"+de.getMessage());
				throw be;
			}
		} else {
			throw be;
		}
		
	}

	private boolean confirmerMotDePasse(String motDePasse, String confirmation, BusinessException be) {
		if(motDePasse.equals(confirmation)) {
			return true;
		} else {
			be.ajouterMessage("La confirmation du mot de passe est incorrecte");
			return false;
		}
	}

	private boolean estPseudoUnique(String pseudo,BusinessException be) {
		if(utilisateurDAO.nbParPseudo(pseudo)==0) {
			return true;
		} else {
			be.ajouterMessage(pseudo+" déjà utilisé !");
			return false;
		}
	}
	
	private boolean estEmailUnique(String email,BusinessException be) {
		if(utilisateurDAO.nbParEmail(email)==0) {
			return true;
		} else {
			be.ajouterMessage(email+" déjà utilisé !");
			return false;
		}
	}

	
	@Override
	public void desactiverUtilisateur(Utilisateur utilisateur) {
		utilisateurDAO.desactiverParNo(utilisateur.getNoUtilisateur());
	}

	@Override
	public void majCompte(Utilisateur utilisateurV2, String nouveauMDP, String confirmationMDP) throws BusinessException {
		Utilisateur utilisateurV1 = utilisateurDAO.recupererParNo(utilisateurV2.getNoUtilisateur());
		BusinessException be = new BusinessException();
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		boolean isValid=true;
		
		//si nouveau MDP complété alors vérifier que le mot de passe actuel est complété et que nouveau=confirmation
		if(!nouveauMDP.isEmpty()) {
			isValid &= confirmerMotDePasse(nouveauMDP,confirmationMDP,be)
						& verifierMotDePasseActuel(utilisateurV2.getMotDePasse(),utilisateurV2.getNoUtilisateur(),encoder,be);
		}
		//si nouveau pseudo alors verif unicité  parmi tous les pseudos et emails confondus
		String pseudoV2 = utilisateurV2.getPseudo();
		if(!pseudoV2.equals(utilisateurV1.getPseudo())) {
			isValid &= estPseudoUnique(pseudoV2,be)
						& estEmailUnique(pseudoV2,be);
		}
		//si nouveau email alors verif unicité  parmi tous les pseudos et emails confondus
		String emailV2 = utilisateurV2.getEmail();
		if(!emailV2.equals(utilisateurV1.getEmail())) {
			isValid &= estEmailUnique(emailV2,be)
						& estPseudoUnique(emailV2,be);
		}
		
		if(isValid) {
			if(!nouveauMDP.isEmpty()) {
				utilisateurV2.setMotDePasse("{bcrypt}"+encoder.encode(nouveauMDP));
			} else {
				utilisateurV2.setMotDePasse(utilisateurDAO.recupererCryptMDPParNo(utilisateurV2.getNoUtilisateur()));
			}
			try {
				utilisateurDAO.actualiser(utilisateurV2);
			} catch (DataAccessException de) {
				be.ajouterMessage("Erreur : un problème est survenu avec la base de données//"+de.getMessage());
				throw be;
			}
		} else {
			throw be;
		}
	}

	private boolean verifierMotDePasseActuel(String motDePasse,Long no_utilisateur,BCryptPasswordEncoder encoder,BusinessException be) {
		String originalCryptMDP=utilisateurDAO.recupererCryptMDPParNo(no_utilisateur);
		if(originalCryptMDP.startsWith("{bcrypt}") && originalCryptMDP.length()>"{bcrypt}".length()) {
			originalCryptMDP= originalCryptMDP.substring("{bcrypt}".length());
			if(encoder.matches(motDePasse, originalCryptMDP)) {
				return true;
			} else {
				be.ajouterMessage("Le mot de passe actuel ne correspond pas");
				return false;
			}
		} else {
			be.ajouterMessage("Problème technique avec le mot de passe actuel");
			return false;
		}
	}
	
}
