package fr.eni.enchere.bll;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.eni.enchere.bo.ArticleVendu;
import fr.eni.enchere.bo.Categorie;
import fr.eni.enchere.bo.Enchere;
import fr.eni.enchere.bo.tech.FilterEncheres;
import fr.eni.enchere.dal.ArticleVenduDAO;
import fr.eni.enchere.dal.CategorieDAO;
import fr.eni.enchere.dal.EnchereDAO;
import fr.eni.enchere.dal.RetraitDAO;
import fr.eni.enchere.dal.UtilisateurDAO;
import fr.eni.enchere.exception.BusinessException;

@Service
public class EnchereServiceImpl implements EnchereService {
	
	private ArticleVenduDAO articleVenduDAO;
	private UtilisateurDAO utilisateurDAO;
	private CategorieDAO categorieDAO;
	private RetraitDAO retraitDAO;
	private EnchereDAO enchereDAO;
	
	public EnchereServiceImpl(ArticleVenduDAO articleVenduDAO, UtilisateurDAO utilisateurDAO,
			CategorieDAO categorieDAO,RetraitDAO retraitDAO,EnchereDAO enchereDAO) {
		this.articleVenduDAO = articleVenduDAO;
		this.utilisateurDAO = utilisateurDAO;
		this.categorieDAO = categorieDAO;
		this.retraitDAO = retraitDAO;
		this.enchereDAO = enchereDAO;
	}

	@Override
	public List<ArticleVendu> afficherEncheres() {
		List<ArticleVendu> listeArticles = articleVenduDAO.recupererTous();
		
		listeArticles.forEach(a ->{
			a.setVendeur(utilisateurDAO.recupererParNo(a.getVendeur().getNoUtilisateur()));
			a.setCategorie(categorieDAO.recupererParNo(a.getCategorie().getNoCategorie()));
			a.setLieuRetrait(retraitDAO.recupererParNoArticle(a.getNoArticle()));
			if(a.getAcheteur()!=null) {
				a.setAcheteur(utilisateurDAO.recupererParNo(a.getAcheteur().getNoUtilisateur()));
			}
		});
		
		return listeArticles;
	}

	@Override
	public List<Categorie> afficherCategories() {
		List<Categorie> listeCategories = categorieDAO.recupererTous();
		return listeCategories;
	}

	// ID 2001 Vendre un article
	@Override
	@Transactional (rollbackFor = BusinessException.class)
	public void creerArticle(ArticleVendu article) throws BusinessException {
		
		BusinessException be = new BusinessException();
		
		boolean isValid = checkDate(article.getDateDebutEncheres(), article.getDateFinEncheres(), be);
		isValid &= checkCategorie(article.getCategorie().getNoCategorie(), be);
		
		if(isValid) {
			try {
				articleVenduDAO.creer(article);
				article.getLieuRetrait().setArticleVendu(article);
				retraitDAO.creer(article.getLieuRetrait());
			} catch (DataAccessException e) {
				be.ajouterMessage("Erreur lors de la création de l'article");
				throw be;
			}
		}else {
			throw be;
		}
	}
	
	private boolean checkDate(LocalDateTime dateDebutEnchere, LocalDateTime dateFinEnchere, BusinessException be) {
		
		boolean isValid = false;
		
		if(!dateDebutEnchere.isAfter(LocalDateTime.now())) {
			be.ajouterMessage("Création impossible. La date de début doit être supérieur à celle d'aujourd'hui");
		} else {
			if(!dateDebutEnchere.isAfter(dateFinEnchere)) {
				isValid = true;
			}else {
				be.ajouterMessage("Création impossible. La date de début d'enchère doit être antérieur à la date de fin d'enchère.");
			}
		}
		return isValid;
	}
	
	private boolean checkCategorie(Long no_categorie, BusinessException be) {
		
		boolean isValid = false;
		int nbCategorie = categorieDAO.countNbCategorieById(no_categorie);
		
		if(nbCategorie != 0) {
			isValid = true;
		} else {
			be.ajouterMessage("Création impossible, Cette catégorie n'existe pas.");
		}
		
		return isValid;
	}
	
	@Override
	public List<ArticleVendu> filtrerEncheres(FilterEncheres filterEncheres) {
		List<ArticleVendu> listeArticles = articleVenduDAO.recupererParFiltre(filterEncheres);

		listeArticles.forEach(a -> {
			a.setVendeur(utilisateurDAO.recupererParNo(a.getVendeur().getNoUtilisateur()));
			a.setCategorie(categorieDAO.recupererParNo(a.getCategorie().getNoCategorie()));
			a.setLieuRetrait(retraitDAO.recupererParNoArticle(a.getNoArticle()));
			if(a.getAcheteur()!=null) {
				a.setAcheteur(utilisateurDAO.recupererParNo(a.getAcheteur().getNoUtilisateur()));
			}
		});
		return listeArticles;
	}
	
	@Override
	public Categorie afficherCategorieParID(long id) {
		if(categorieDAO.countNbCategorieById(id)==1) {
			return categorieDAO.recupererParNo(id);
		} else {
			return  null;
		}
	}

	@Override
	public ArticleVendu consulterArticle(Long no_article) {
		if(articleVenduDAO.compteNbArticleParNo(no_article)==1) {
			ArticleVendu article = articleVenduDAO.recupererParNo(no_article);
			article.setCategorie(categorieDAO.recupererParNo(article.getCategorie().getNoCategorie()));
			article.setVendeur(utilisateurDAO.recupererParNo(article.getVendeur().getNoUtilisateur()));
			article.setLieuRetrait(retraitDAO.recupererParNoArticle(no_article));
			if(article.getAcheteur()!=null) {
				article.setAcheteur(utilisateurDAO.recupererParNo(article.getAcheteur().getNoUtilisateur()));
			}
			return article;
		} else {
			return null;
		}
	}
	
	public void soumettreEnchere(Enchere enchere) throws BusinessException {

		BusinessException be = new BusinessException();
		//savoir si l'utilisateur parie deux fois d'affilé sur la meme offre 
		boolean memeParieur = enchere.getArticleVendu().getAcheteur()==null ? false : enchere.getArticleVendu().getAcheteur().getNoUtilisateur()==enchere.getUtilisateur().getNoUtilisateur();
		if(memeParieur) {
			be.ajouterMessage("Vous avez déjà la meilleure offre");
			throw be;
		}
		//savoir si son offre bat l'offre en cours
		boolean meilleurProposition = enchere.getMontant_enchere()>Math.max(enchere.getArticleVendu().getMiseAPrix(),enchere.getArticleVendu().getPrixDeVente());
		if(!meilleurProposition) {
			be.ajouterMessage("Offre trop faible");
			throw be;
		}
		//savoir si l'utilisateur a les finances pour enchérir
		boolean creditSuffisant = enchere.getUtilisateur().getCredit() >= enchere.getMontant_enchere();
		if(!creditSuffisant) {
			be.ajouterMessage("Crédit insuffisant");
			throw be;
		}
		
		try {
			// récupérer le pseudo et montant de l'offre la plus élevé avant modification pour re-créditer si offre battue
			Enchere enchereAV = new Enchere();
			if(enchereDAO.compteNbEncheresParArticle(enchere.getArticleVendu().getNoArticle())>0) {
				enchereAV = enchereDAO.obtenirEnchereMax(enchere.getArticleVendu().getNoArticle());
			}
			//ajouter la nouvelle enchere en bdd
			enchereDAO.ajouter(enchere);
			// modifier le prix de vente et l'acheteur de l'article en bdd
			articleVenduDAO.majApresEnchere(enchere);
			//débiter l'acheteur
			utilisateurDAO.modifierCredit(enchere.getUtilisateur().getNoUtilisateur(), enchere.getUtilisateur().getCredit()-enchere.getMontant_enchere());
			// recréditer l'ancien acheteur si ce n'est pas la première offre
			if(enchereDAO.compteNbEncheresParArticle(enchere.getArticleVendu().getNoArticle())>0) {
				utilisateurDAO.modifierCredit(enchereAV.getUtilisateur().getNoUtilisateur(), enchereAV.getUtilisateur().getCredit()+enchereAV.getMontant_enchere());
			}
			
		} catch (DataAccessException e) {
			be.ajouterMessage("Un problème avec la base de données est survenu"+e.getMessage());
			throw be;
		}
	
	}
	
	@Override
	public void supprimerArticle(long no_article) throws BusinessException {
		
		BusinessException be = new BusinessException();
		
		ArticleVendu article = articleVenduDAO.recupererParNo(no_article);
		
		try {
			retraitDAO.supprimerParArticle(no_article);
			articleVenduDAO.supprimer(no_article);
			
			//TODO recrditer l'acheteur si il y a
			if(article.getAcheteur()!=null) {
				utilisateurDAO.modifierCredit(article.getAcheteur().getNoUtilisateur(), article.getAcheteur().getCredit()+article.getPrixDeVente());
			}
			
		} catch (DataAccessException e) {
			be.ajouterMessage("Un problème avec la base de données est survenu"+e.getMessage());
			throw be;
		}
		
	}
	
	
}
