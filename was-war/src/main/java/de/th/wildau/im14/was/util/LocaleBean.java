package de.th.wildau.im14.was.util;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class LocaleBean implements Serializable {

	private static final long serialVersionUID = -6547913945910172375L;

	@Produces
	private Locale locale = FacesContext.getCurrentInstance().getViewRoot()
			.getLocale();

	public Locale getLocale() {
		return this.locale;
	}

	public String getLanguage() {
		return this.locale.getLanguage();
	}

	public void setLanguage(String language) {
		this.locale = new Locale(language);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(this.locale);
	}
}