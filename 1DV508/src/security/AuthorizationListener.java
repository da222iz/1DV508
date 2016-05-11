package security;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Sources: https://aquaryus.wordpress.com/2012/03/29/jsf-login-logout-methods-session-timeouts-page-jumping-issues-facelets/
 * https://turbomanage.wordpress.com/2006/08/08/disable-browser-caching-in-jsf/
 */

public class AuthorizationListener implements PhaseListener {
	 
public void afterPhase(PhaseEvent event) {
 
FacesContext facesContext = event.getFacesContext();
String currentPage = facesContext.getViewRoot().getViewId();
 
boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
 
if(session==null){
NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
nh.handleNavigation(facesContext, null, "loginPage");
}
 
else{
Object currentUser = session.getAttribute("username");
 
if (!isLoginPage && (currentUser == null || currentUser == "")) {
NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
nh.handleNavigation(facesContext, null, "loginPage");
}
}
}
 
public void beforePhase(PhaseEvent event) {
	
	FacesContext facesContext = event.getFacesContext();
    HttpServletResponse response = (HttpServletResponse) facesContext
            .getExternalContext().getResponse();
    
    // Tell the browser not to cache so user can't navigate back after logout
    response.addHeader("Pragma", "no-cache");
    response.addHeader("Cache-Control", "no-cache");
    response.addHeader("Cache-Control", "no-store");
    response.addHeader("Cache-Control", "must-revalidate");

}
 
public PhaseId getPhaseId() {
return PhaseId.RESTORE_VIEW;
}
}