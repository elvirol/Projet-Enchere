package fr.eni.enchere.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.enchere.bll.UtilisateurService;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"utilisateurSession"})
public class UtilisateurController {

	private UtilisateurService utilisateurService;
	
	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}
	
	@GetMapping("/monCompte")
	public String afficherMonCompte(@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession,Model model) {
		model.addAttribute("estNouveau", false);
		model.addAttribute("utilisateur", utilisateurSession);
		return "view-account";
	}
	
	@PostMapping("/monCompte")
	public String majMonCompte(@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession,
			@ModelAttribute("actuelMotDePasse") String actuelMDP,
			@ModelAttribute("nouveauMotDePasse") String nouveauMDP,
			@ModelAttribute("confirmationMotDePasse") String confirmationMDP,
			@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			model.addAttribute("estNouveau", false);
			return "view-account";
		} else {
			utilisateur.setMotDePasse(actuelMDP);
			utilisateur.setNoUtilisateur(utilisateurSession.getNoUtilisateur()); //maj num utilisateur car n'est pas dans le modèle
			utilisateur.setCredit(utilisateurSession.getCredit());//maj du crédit non autorisé par l'utilisteur mais dans le modèle pour l'affichage
			try {
				utilisateurService.majCompte(utilisateur,nouveauMDP,confirmationMDP);
				majUtilisateur(utilisateurSession,utilisateur);
				return "redirect:/";
			} catch (BusinessException be) {
				be.getErreurs().forEach(err->{
					ObjectError error = new ObjectError("globalError", err);
					bindingResult.addError(error);
					});
				model.addAttribute("estNouveau", false);
				return "view-account";
			}
		}
	}
	
	private void majUtilisateur(Utilisateur utilisateurCible, Utilisateur utilisateurSource) {
		if(utilisateurCible != null) {
			utilisateurCible.setNoUtilisateur(utilisateurSource.getNoUtilisateur());
			utilisateurCible.setPseudo(utilisateurSource.getPseudo());
			utilisateurCible.setNom(utilisateurSource.getNom());
			utilisateurCible.setPrenom(utilisateurSource.getPrenom());
			utilisateurCible.setEmail(utilisateurSource.getEmail());
			utilisateurCible.setTelephone(utilisateurSource.getTelephone());
			utilisateurCible.setRue(utilisateurSource.getRue());
			utilisateurCible.setCodePostal(utilisateurSource.getCodePostal());
			utilisateurCible.setVille(utilisateurSource.getVille());
			utilisateurCible.setEmail(utilisateurSource.getEmail());
			utilisateurCible.setEstAdmin(utilisateurSource.getEstAdmin());
			utilisateurCible.setEstActif(utilisateurSource.getEstActif());
			//attribut mot de passe volontairement ignoré
			//attributs listes volontairement ignorés pour le moment
			
		}
	}
	
	@GetMapping("/monCompte/creer")
	public String afficherNouveauCompte(Model model) {
		model.addAttribute("utilisateur", new Utilisateur());
		model.addAttribute("estNouveau", true);
		return "view-account";
	}
	
	@PostMapping("/monCompte/creer")
	public String creerNouveauCompte(@ModelAttribute("confirmationMotDePasse") String confirmation,
				@Valid @ModelAttribute("utilisateur") Utilisateur utilisateur, BindingResult bindingResult, Model model) {
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("estNouveau", true);
			return "view-account";
		} else {
			try {
				utilisateurService.soumettreNouveau(utilisateur,confirmation);
				return "redirect:/login";
			} catch (BusinessException be) {
				be.getErreurs().forEach(err->{
					ObjectError error = new ObjectError("globalError", err);
					bindingResult.addError(error);
					});
				model.addAttribute("estNouveau", true);
				return "view-account";
			}
			
		}
	}
	
	@GetMapping("/profil/{cible}")
	public String consulterProfil(@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession,
									@PathVariable("cible") String pseudo, Model model) {
		
		Utilisateur u = this.utilisateurService.recupererUtilisateurParPseudo(pseudo);
		model.addAttribute("utilisateur", u);
		model.addAttribute("estMonCompte", utilisateurSession.getPseudo().equals(pseudo));
		return "view-profile";
	}
	
	@GetMapping("/monCompte/supprimer")
	public String desactiverMonCompte (@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession) {
		utilisateurService.desactiverUtilisateur(utilisateurSession);
		return "redirect:/logout";
	}
}
