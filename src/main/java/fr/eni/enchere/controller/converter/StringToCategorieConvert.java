package fr.eni.enchere.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import fr.eni.enchere.bll.EnchereService;
import fr.eni.enchere.bo.Categorie;

@Component
public class StringToCategorieConvert implements Converter<String, Categorie> {
	
	private EnchereService enchereService;

	public StringToCategorieConvert(EnchereService enchereService) {
		this.enchereService = enchereService;
	}

	@Override
	public Categorie convert(String noCategorie) {
		return this.enchereService.afficherCategorieParID(Long.parseLong(noCategorie));
	}

}
