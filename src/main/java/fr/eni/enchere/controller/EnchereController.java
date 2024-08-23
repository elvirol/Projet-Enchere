package fr.eni.enchere.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import fr.eni.enchere.bll.EnchereService;
import fr.eni.enchere.bll.ImageService;
import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.Retrait;
import fr.eni.enchere.bo.Utilisateur;
import fr.eni.enchere.bo.tech.FilterEncheres;
import fr.eni.enchere.exception.BusinessException;
import jakarta.validation.Valid;

@Controller
@SessionAttributes({"utilisateurSession"})
public class EnchereController {
	
	private EnchereService enchereService;
	private ImageService imageService;

	private static final String IMAGE_ARTICLE_DEFAULT = "images/articles/article-defaut.png";
	private static final String IMAGE_ARTICLE_NEW_UPLOAD = "src/main/resources/static/images/articles";
	
	public EnchereController(EnchereService enchereService,ImageService imageService) {
		this.enchereService = enchereService;
		this.imageService = imageService;
	}

	@GetMapping
	public String redirectionDefaut() {
		return "redirect:/encheres";
	}
	
	@GetMapping("/encheres")
	public String afficherEnchere(Model model) {
		FilterEncheres filter = new FilterEncheres();
		List<ArticleVendu> listeArticles = enchereService.afficherEncheres();
		List<Categorie> listeCategories = enchereService.afficherCategories();
		model.addAttribute("filterEncheres", filter);
		model.addAttribute("listeArticles", listeArticles);
		model.addAttribute("listeCategories", listeCategories);
		return "view-encheres";
	}
	
	@PostMapping("/encheres")
	public String searchFilter(@ModelAttribute("Filter") FilterEncheres filter,
			@ModelAttribute("utilisateurSession") Utilisateur utilisateur, Model model) {
		int count = 10;
		List<ArticleVendu> listeFinal = new ArrayList<ArticleVendu>();
		List<ArticleVendu> listePreFiltre;
		List<ArticleVendu> listePreFinal = new ArrayList<ArticleVendu>();
		// si auccun critère de filtre categorie et mot cles pres filtrage
		if (filter.getCategories() == 0 && filter.getMotCles().isBlank()) {
			System.out.println(filter.toString());
			System.out.println(utilisateur.getNoUtilisateur());
			listePreFiltre = enchereService.afficherEncheres();

			// si au moins un critère de filtre entre catégorie et mot cles
		} else {

			listePreFiltre = enchereService.filtrerEncheres(filter);

		}
		// ACHATS
		if (filter.getEncheresOuvertes() != null && filter.getEncheresOuvertes().equals("1")) {
			List<ArticleVendu> listEncheresOuvertes = listePreFiltre.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					.collect(Collectors.toList());
			listePreFinal.addAll(listEncheresOuvertes);
			count--;
		}

		if (filter.getEncheresEnCours() != null && filter.getEncheresEnCours().equals("1")) {

			List<ArticleVendu> listeFiltreAcheteur = listePreFiltre.stream()
					.filter(article -> article.getAcheteur() != null).collect(Collectors.toList());
			List<ArticleVendu> listEncheresEnCours = listeFiltreAcheteur.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now())
							&& articleVendu.getAcheteur().getNoUtilisateur() == utilisateur.getNoUtilisateur())
					.collect(Collectors.toList());
			listePreFinal.addAll(listEncheresEnCours);
			count--;
		}

		if (filter.getEncheresRemportees() != null && filter.getEncheresRemportees().equals("1")) {

			List<ArticleVendu> listeFiltreAcheteur = listePreFiltre.stream()
					.filter(article -> article.getAcheteur() != null).collect(Collectors.toList());
			List<ArticleVendu> listEncheresRemportees = listeFiltreAcheteur.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isBefore(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now())
							&& articleVendu.getAcheteur().getNoUtilisateur() == utilisateur.getNoUtilisateur())
					.collect(Collectors.toList());
			listePreFinal.addAll(listEncheresRemportees);
			count--;
		}

		// VENTES

		if (filter.getVentesEnCours() != null && filter.getVentesEnCours().equals("1")) {
			List<ArticleVendu> listVentesEnCours = listePreFiltre.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now())
							&& articleVendu.getVendeur().getNoUtilisateur() == utilisateur.getNoUtilisateur())
					.collect(Collectors.toList());
			listePreFinal.addAll(listVentesEnCours);
			System.out.println("listVentesEnCours : " + listVentesEnCours);
			count--;
		}

		if (filter.getVentesNonDebutees() != null && filter.getVentesNonDebutees().equals("1")) {
			List<ArticleVendu> listVentesNonDebutees = listePreFiltre.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isAfter(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isAfter(LocalDateTime.now()))
					.collect(Collectors.toList());
			listePreFinal.addAll(listVentesNonDebutees);
			System.out.println("listVentesNonDebutees : " + listVentesNonDebutees);
			count--;
		}

		if (filter.getVentesTerminees() != null && filter.getVentesTerminees().equals("1")) {
			List<ArticleVendu> listVentesTerminees = listePreFiltre.stream()
					.filter(articleVendu -> articleVendu.getDateFinEncheres().isBefore(LocalDateTime.now())
							&& articleVendu.getDateDebutEncheres().isBefore(LocalDateTime.now()))
					.collect(Collectors.toList());
			listePreFinal.addAll(listVentesTerminees);
			System.out.println("listVentesTerminees : " + listVentesTerminees);
			count--;
		}

		if (!listePreFinal.isEmpty()) {

			// supression doublon
			Set<ArticleVendu> setArticles = new HashSet<>(listePreFinal);

			// Conversion du Set en Liste
			listeFinal = new ArrayList<>(setArticles);
		} else if (listePreFinal.isEmpty() && count < 10) {
			listeFinal.addAll(listePreFinal);
		} else {
			listeFinal.addAll(listePreFiltre);
		}

		List<Categorie> listeCategories = enchereService.afficherCategories();
		model.addAttribute("listeArticles", listeFinal);
		model.addAttribute("listeCategories", listeCategories);
		model.addAttribute("filterEncheres", filter);
		return "view-encheres";

	}
	
	// ID 2001 Vendre un article
	@GetMapping("/article/creer")
	public String afficherCreationArticle(@ModelAttribute("utilisateurSession") Utilisateur utilisateur, Model model) {
		
		List<Categorie> listeCategories = enchereService.afficherCategories();
		ArticleVendu articleVendu = new ArticleVendu();
		
		Retrait retrait = new Retrait();
		retrait.setArticleVendu(articleVendu);
		retrait.setCode_postal(utilisateur.getCodePostal());
		retrait.setRue(utilisateur.getRue());
		retrait.setVille(utilisateur.getVille());
		articleVendu.setLieuRetrait(retrait);
		
		model.addAttribute("articleVendu", articleVendu);
		model.addAttribute("listeCategories",listeCategories);
		
		model.addAttribute("estNouveau", true);// estNouveau pour gérer le h1 + bouton creer 
		model.addAttribute("estVendeur",true); // estMonArticle pour gérer disabled + bouton enregistrer/annuler la vente + maProposition/bouton encherir
		model.addAttribute("estAcheteur",false);
		model.addAttribute("estEnCours", false);//estEnCours pour affichage bouton enchérir
		model.addAttribute("estTermine",false);//estTermine pour gérer le titre et l'éventuel bouton retrait effectué
		
		return "view-article";
	}
	
	// ID 2001 Vendre un article
	@PostMapping("/article/creer")
	public String creationArticle(@Valid @ModelAttribute("articleVendu") ArticleVendu articleVendu, BindingResult bindingResult,
			@RequestParam(name="imgArticle", required=false) MultipartFile img_article,
			@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession, 
			Model model){
		
		if(utilisateurSession != null && utilisateurSession.getNoUtilisateur() > 0 ) {
			if(bindingResult.hasErrors()) {
				List<Categorie> listeCategories = enchereService.afficherCategories();
				
				model.addAttribute("articleVendu", articleVendu);
				model.addAttribute("listeCategories",listeCategories);
				
				model.addAttribute("estNouveau", true);
				model.addAttribute("estVendeur",true);
				model.addAttribute("estAcheteur",false);
				model.addAttribute("estEnCours", false);
				model.addAttribute("estTermine",false);
				
				return "view-article";
			}else { 
				try {
					if(img_article.isEmpty()) {
						articleVendu.setImg_article(IMAGE_ARTICLE_DEFAULT);
					}else {
						String image_articleString ="";
						try {
							image_articleString += imageService.saveImageToStorage(IMAGE_ARTICLE_NEW_UPLOAD, img_article);
						} catch (Exception e) {
							ObjectError error = new ObjectError("globalError", "Un problème est survenu dans le téléchargement de l'image sur le serveur");
							bindingResult.addError(error);
						}
						articleVendu.setImg_article(image_articleString);
					}
					articleVendu.setVendeur(utilisateurSession);
					enchereService.creerArticle(articleVendu);
					return "redirect:/encheres";
					
				}catch (BusinessException e) {
					e.getErreurs().forEach(err->{
						ObjectError error = new ObjectError("globalError", err);
						bindingResult.addError(error);
					});
					
					List<Categorie> listeCategories = enchereService.afficherCategories();
					
					model.addAttribute("articleVendu", articleVendu);
					model.addAttribute("listeCategories",listeCategories);
					
					model.addAttribute("estNouveau", true);
					model.addAttribute("estVendeur",true);
					model.addAttribute("estAcheteur",false); 
					model.addAttribute("estEnCours", false);
					model.addAttribute("estTermine",false);
					
					return "view-article";
				} 
			}
		} else {
			return "redirect:/";
		}
	}
	
	@GetMapping("/article")
	public String afficherArticle(@RequestParam("no") Long no_article,
									@ModelAttribute("utilisateurSession") Utilisateur utilisateurSession, 
									Model model) {
		
		ArticleVendu article = enchereService.consulterArticle(no_article);
		
		if(article != null) {
			
			model.addAttribute("articleVendu", article);
			
			model.addAttribute("estNouveau", false);
			model.addAttribute("estVendeur",article.getVendeur().getPseudo().equals(utilisateurSession.getPseudo()));
			if(article.getAcheteur()!=null) {
				model.addAttribute("estAcheteur",article.getAcheteur().getPseudo().equals(utilisateurSession.getPseudo()));
			} else {
				model.addAttribute("estAcheteur",false);
			}
			model.addAttribute("estEnCours", article.getEtatVente().equals("EnCours"));
			model.addAttribute("estTermine",article.getEtatVente().equals("Termine"));
			
			return "view-article";
		} else {
			return "redirect:/";
		}
	}
	
	
	@PostMapping("/article/encherir")
	public String soumettreOffre(@ModelAttribute ("utilisateurSession") Utilisateur utilisateurSession, 
									@Valid @ModelAttribute("articleVendu") ArticleVendu article, BindingResult bindingResult,
									@RequestParam("proposition") long proposition,
									Model model) {
		
		ArticleVendu articleSource = enchereService.consulterArticle(article.getNoArticle()); //pour garder le lien entre article du modèle et le bindingResult
		article.setEtatVente(articleSource.getEtatVente());
		article.setVendeur(articleSource.getVendeur());
		article.setAcheteur(articleSource.getAcheteur());
		
		Enchere enchere = new Enchere(LocalDateTime.now(),proposition,utilisateurSession,article);
		
		 //appeler la méthode d'enchereService pour soumettre l'offre et savoir si celle-ci est possible
		try {
			enchereService.soumettreEnchere(enchere);
			utilisateurSession.setCredit(enchere.getUtilisateur().getCredit()); //pour maj les info de la session
			return "redirect:/article?no="+enchere.getArticleVendu().getNoArticle();
		} catch (BusinessException e) {
			e.getErreurs().forEach(err -> {
				ObjectError error = new ObjectError("globalError", err);
				bindingResult.addError(error);
				}
			);
			model.addAttribute("articleVendu", article);
			model.addAttribute("estNouveau", false);
			model.addAttribute("estVendeur",article.getVendeur().getPseudo().equals(utilisateurSession.getPseudo())); //maj article avec le vendeur pour utiliser
			if(article.getAcheteur()!=null) {
				model.addAttribute("estAcheteur",article.getAcheteur().getPseudo().equals(utilisateurSession.getPseudo()));
			} else {
				model.addAttribute("estAcheteur",false);
			}
			model.addAttribute("estEnCours", article.getEtatVente().equals("EnCours"));
			model.addAttribute("estTermine",article.getEtatVente().equals("Termine"));
			
			return "view-article";			
			
		}
		
	}
	
	@PostMapping("/article/modifier")
	public String modifierOffre(@ModelAttribute ("utilisateurSession") Utilisateur utilisateurSession, 
									@Valid @ModelAttribute("articleVendu") ArticleVendu article, BindingResult bindingResult,
									Model model) {

		if(bindingResult.hasErrors()) {
			List<Categorie> listeCategories = enchereService.afficherCategories();
			
			model.addAttribute("articleVendu", article);
			model.addAttribute("listeCategories",listeCategories);
			
			model.addAttribute("estNouveau", false);
			model.addAttribute("estVendeur",article.getVendeur().getPseudo().equals(utilisateurSession.getPseudo()));
			if(article.getAcheteur()!=null) {
				model.addAttribute("estAcheteur",article.getAcheteur().getPseudo().equals(utilisateurSession.getPseudo()));
			} else {
				model.addAttribute("estAcheteur",false);
			}
			model.addAttribute("estEnCours", false);//TODO en theorie prendre le vrai etat de vente
			model.addAttribute("estTermine",false);//TODO en theorie prendre le vrai etat de vente
			
			return "view-article";
		}else { 
			//TODO maj proprement article
			/*try {
				if(img_article.isEmpty()) {
					article.setImg_article(IMAGE_ARTICLE_DEFAULT);
				}else {
					String image_articleString ="";
					try {
						image_articleString += imageService.saveImageToStorage(IMAGE_ARTICLE_NEW_UPLOAD, img_article);
					} catch (Exception e) {
						ObjectError error = new ObjectError("globalError", "Un problème est survenu dans le téléchargement de l'image sur le serveur");
						bindingResult.addError(error);
					}
					article.setImg_article(image_articleString);
				}
				article.setVendeur(utilisateurSession);
				enchereService.creerArticle(article);
				return "redirect:/encheres";
				
			}catch (BusinessException e) {
				e.getErreurs().forEach(err->{
					ObjectError error = new ObjectError("globalError", err);
					bindingResult.addError(error);
				});
				
				List<Categorie> listeCategories = enchereService.afficherCategories();
				
				model.addAttribute("articleVendu", article);
				model.addAttribute("listeCategories",listeCategories);
				
				model.addAttribute("estNouveau", true);
				model.addAttribute("estVendeur",true);
				model.addAttribute("estAcheteur",false); 
				model.addAttribute("estEnCours", false);
				model.addAttribute("estTermine",false);
				
				return "view-article";
			} */
			
			//For testing only
			return "redirect:/";
		}
	}
	
	
	@GetMapping("/article/supprimer")
	public String annulerVente(@ModelAttribute ("utilisateurSession") Utilisateur utilisateurSession,
													@RequestParam("no") long no_article) {
		
		ArticleVendu article = enchereService.consulterArticle(no_article);
		
		//check vendeur = utilisateurSession
		if(article.getVendeur().getNoUtilisateur()!=utilisateurSession.getNoUtilisateur()) {
			return "redirect:/article?no="+no_article;
		}
		
		if(!article.getEtatVente().equals("Cree") && !article.getEtatVente().equals("EnCours")) {
			return "redirect:/article?no="+no_article;
		}
		
		try {
			//supprimer l'article en bdd
			enchereService.supprimerArticle(no_article);
			//TODO supprimer l'image de la mémoire si ce n'est pas l'image par défaut
			/*
			int lastSeparatorIndex = article.getImg_article().lastIndexOf("\\");
	        String fileName = article.getImg_article().substring(lastSeparatorIndex + 1);
	        String uploadDirectory = IMAGE_ARTICLE_NEW_UPLOAD+fileName;
			imageService.deleteImage(uploadDirectory);
			*/
			return "redirect:/";
		} catch (Exception e) {
			return "redirect:/article?no="+no_article;
		}
		
	}
	
}
