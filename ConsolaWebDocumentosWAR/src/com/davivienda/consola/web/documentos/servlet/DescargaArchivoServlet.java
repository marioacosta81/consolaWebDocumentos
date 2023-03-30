package com.davivienda.consola.web.documentos.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davivienda.consola.web.documentos.bean.ConsolaWebDocumentosFacade;
import com.davivienda.consola.web.documentos.util.ConsultaGeneracionDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaRequestDataRespType;


@WebServlet("/DescargarArchivo")
public class DescargaArchivoServlet extends HttpServlet {
	
	@EJB
	private ConsolaWebDocumentosFacade facade;
	private byte[] file;

	/** Serial de la clase */
	private static final long serialVersionUID = -3823431035694288668L;
	private static Logger logger = LoggerFactory.getLogger(DescargaArchivoServlet.class.getName());
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DescargaArchivoServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.debug("Servlet descargar archivo...");
		HttpSession session = request.getSession(false);
        if( session == null ){
            return;
        }
        
        String idProceso = request.getParameter("key");
        if( idProceso == null ){
            return;
        }
        logger.debug(idProceso);
        
        String archivo = request.getParameter("archivo");
        if( archivo == null ){
            return;
        }
        logger.debug(archivo);
        
		OutputStream outputStream = null;
        try {
        	
			// Manejo de cache en el response
	        response.setHeader("Pragma", "no-cache" );
	        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate" );
	        response.setDateHeader("Expires", 0 );
            
            String nombreArchivo = idProceso;
            if("DA".equals(archivo)) {
    	        ConsultaGeneracionDataRespType datos = facade.consultaDocumentoAlmacenado(idProceso);
    	        ByteArrayInputStream bais = new ByteArrayInputStream(datos.getDocumentoAlmacenado());
    	        ObjectInputStream ois = new ObjectInputStream(bais);
    	        Object obj = ois.readObject();     
            	this.file = DatatypeConverter.parseBase64Binary(obj.toString());
            	response.setContentType("application/pdf");
    	        nombreArchivo += "_documento.pdf";
            }else {
            	ConsultaRequestDataRespType datos = facade.consultaRequestDAO(idProceso);
            	if("RI".equals(archivo)) {
            		this.file = datos.getRequestInvocacion();
             	}
            	if("RR".equals(archivo)) {
            		this.file = datos.getRequestGeneracion();
             	}
            	if("RF".equals(archivo)) {
            		this.file = datos.getRequestFilenet();
             	}
            	if("RC".equals(archivo)) {
            		this.file = datos.getRequestCorreo();
             	}
            	response.setContentType("text/plain");
            	nombreArchivo += "_"+archivo+".txt";
            	
            }
            
	        response.setHeader("Content-Disposition", "attachment; filename=\""+ nombreArchivo +"\"");
	        response.setHeader("Content-length", String.valueOf(file.length));
	        outputStream = response.getOutputStream();
	        if ( outputStream != null ) {
	            outputStream.write(file, 0, file.length);
	            outputStream.flush();
	        }

		} catch (Exception e) {
		    logger.error("[{}] Error al descargar archivo: {}", idProceso, e.getMessage(), e);
		} finally {
		    if ( outputStream != null ){
		        try {
		            outputStream.close();
		        } catch(Exception e) {
		            logger.warn("[{}] No se pudo cerrar OutputStream de descargar archivo: {}", idProceso, e.getMessage(), e);
		        }
		    }
		}
	}
}
