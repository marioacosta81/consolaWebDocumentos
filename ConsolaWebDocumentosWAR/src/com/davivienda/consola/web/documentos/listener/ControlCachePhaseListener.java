package com.davivienda.consola.web.documentos.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;

public class ControlCachePhaseListener  implements PhaseListener {
	
	private static final long serialVersionUID = 2759127646789250121L;
    public ControlCachePhaseListener() {
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void afterPhase(PhaseEvent arg0) {
        // TODO Auto-generated method stub
    }
   
    @Override
    public void beforePhase(PhaseEvent arg0) {
        // TODO Auto-generated method stub
        FacesContext facesContext = arg0.getFacesContext();
        HttpServletResponse response = (HttpServletResponse) facesContext
                .getExternalContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        // Stronger according to blog comment below that references HTTP spec
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "must-revalidate");
        // some date in the past
        response.addHeader("Expires", "Mon, 8 Aug 2006 10:00:00 GMT");
    }
    
    @Override
    public PhaseId getPhaseId() {
        // TODO Auto-generated method stub
        return PhaseId.RENDER_RESPONSE;
    }
	
	
	
}
