package fr.eni.enchere.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import fr.eni.enchere.bll.UtilisateurService;
import fr.eni.enchere.bo.Utilisateur;

@Controller
@SessionAttributes({"utilisateurSession"})
public class LoginController {
	
	UtilisateurService utilisateurService;

	public LoginController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}


	// permet d'afficher la page de login
	@GetMapping("/login")
	public String login() {
		return "view-login";
	}


	@GetMapping("/session")
	public String miseEnSession(@ModelAttribute ("utilisateurSession") Utilisateur utilisateurSession, Principal principal){
		String identifiant = principal.getName();
		Utilisateur utilisateur;
		if(identifiant.contains("@")) {
			utilisateur = utilisateurService.recupererUtilisateurParEmail(identifiant);
		} else {
			utilisateur = utilisateurService.recupererUtilisateurParPseudo(identifiant);
		}
		
		if(utilisateurSession != null) {
			utilisateurSession.setNoUtilisateur(utilisateur.getNoUtilisateur());
			utilisateurSession.setPseudo(utilisateur.getPseudo());
			utilisateurSession.setNom(utilisateur.getNom());
			utilisateurSession.setPrenom(utilisateur.getPrenom());
			utilisateurSession.setEmail(utilisateur.getEmail());
			utilisateurSession.setTelephone(utilisateur.getTelephone());
			utilisateurSession.setRue(utilisateur.getRue());
			utilisateurSession.setCodePostal(utilisateur.getCodePostal());
			utilisateurSession.setVille(utilisateur.getVille());
			utilisateurSession.setEmail(utilisateur.getEmail());
			utilisateurSession.setEstAdmin(utilisateur.getEstAdmin());
			utilisateurSession.setEstActif(utilisateur.getEstActif());
			utilisateurSession.setCredit(utilisateur.getCredit());
			utilisateurSession.setMotDePasse("fake"); //nécessaire de remplir un mot de passe pour Spring Validation
			//ne pas mettre les listes car le contenu peut varier par la suite
		}
		
		return "redirect:/";
	}
	
	
	@ModelAttribute("utilisateurSession")
	public Utilisateur chargerMembreSession() {
		return new Utilisateur();
	}
	
}