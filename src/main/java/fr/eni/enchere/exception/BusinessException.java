package fr.eni.enchere.exception;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception{
	
	private List<String> erreurs;
	
	public BusinessException() {
		this.erreurs=new ArrayList<String>();
	}
	
	public void ajouterMessage(String message) {
		erreurs.add(message);
	}

	public List<String> getErreurs() {
		return erreurs;
	}
	
}
