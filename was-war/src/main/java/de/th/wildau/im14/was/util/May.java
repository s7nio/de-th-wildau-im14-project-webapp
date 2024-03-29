package de.th.wildau.im14.was.util;

import java.io.Serializable;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

@Named
@RequestScoped
public class May implements Serializable {

	private static final long serialVersionUID = -8707045774926901160L;

	public boolean isAuthenticated() {
		return getRequest().getUserPrincipal() != null;
	}

	public boolean isUserInRole(String role) {
		return getRequest().isUserInRole(role);
	}
	
	protected HttpServletRequest getRequest() {
		Object request = FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		return request instanceof HttpServletRequest ? (HttpServletRequest) request
				: null;
	}
}
