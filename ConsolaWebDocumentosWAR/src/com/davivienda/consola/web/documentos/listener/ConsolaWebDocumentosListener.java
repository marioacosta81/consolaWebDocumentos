package com.davivienda.consola.web.documentos.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davivienda.consola.web.documentos.util.Constantes;
import com.davivienda.consola.web.documentos.util.SingletonCache;
import com.davivienda.consola.web.documentos.util.Util;
import com.ibm.exceptions.core.IBMException;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Application Lifecycle Listener implementation class HomebrokerListener
 *
 */
@WebListener
public class ConsolaWebDocumentosListener implements ServletContextListener {

	private static Logger logger = LoggerFactory.getLogger(ConsolaWebDocumentosListener.class.getName());
	//private static Logger log = (Logger) LogManager.getLogger(Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_LOG));
	
//	private static FileChangeListener FPlantillas;
//	
//	private static FileChangeListener FPaquetes;

	/**
	 * Default constructor. 
	 */
	public ConsolaWebDocumentosListener() {
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		try{
			logger.info("Inicializando aplicacion Consola Web Documentos {}", Constantes.VERSION);
			String path = Util.getProperty(Constantes.PARAM_RUTA_CONFIG);
			String fileBaseName = Util.getProperty(Constantes.PARAM_MENSAJES_FILENAME);
			IBMException.init(path, fileBaseName);
			String pathLogfile = Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_LOG);
			initLog(pathLogfile);
			SingletonCache.getInstance();
			
//			FPlantillas = new FileChangeListener(Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_GENERACION_REPORTES), logger);
//			FPaquetes = new FileChangeListener(Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_PAQUETE_DOCUMENTOS), logger);
//			FPlantillas.attachListenerToFile();	
//			FPaquetes.attachListenerToFile();
//			FPlantillas.startListening();
//			FPaquetes.startListening();
		}  catch (Exception e) {
			logger.error("Error al inicializar aplicación: " + e.getMessage(), e);
		}
	}

	/**
	 * <p> Metodo que realiza la configuracion inicial del proyecto
	 * este es invocado cuando se despliega la aplicacion </p>
	 * 
	 * 
	 * @param pathLogfile
	 * @param pref 
	 * @throws DashboardBaseException 
	 */
	public void initLog(String pathLogfile) throws Exception {
		if (pathLogfile != null) {
			File f = new File(pathLogfile);
			if (f.exists()) {
				LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(context);
				context.reset();
				configurator.doConfigure(pathLogfile);
				Class<?> thisClass = this.getClass();
				logger = LoggerFactory.getLogger(thisClass.getName());
				logger.info("Configuracion de Log cargada desde archivo {}", pathLogfile);
				StatusPrinter.printInCaseOfErrorsOrWarnings(context);
			}
		}
	}

}
