package com.davivienda.consola.web.documentos.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davivienda.consola.web.documentos.util.Constantes;

public class PreferencesHandler implements Serializable {

	/** Serial de la clase */
	private static final long serialVersionUID = -6171738265333676118L;
	private static PreferencesHandler instance;
	private final Properties properties = new Properties();
	private static final Logger logger = LoggerFactory.getLogger(PreferencesHandler.class.getName());


	/**
	 *
	 */
	private PreferencesHandler() {        
		loadPreferences();
	}

	/**
	 *
	 */
	private void loadPreferences() {

		FileInputStream streamPreference = null;

		try {

			String fileLoc = System.getProperty(Constantes.PREFS_SYS_ENV_VAR);
			logger.info("Cargando archivo de propiedades: " + fileLoc);
			File file = new File(fileLoc);

			if (null != file && file.exists() && file.isFile()) {
				streamPreference = new FileInputStream(file);            	
				properties.load(streamPreference);
			}

		} catch (Exception e) {
			logger.error("Error al cargar configuración de preferencias. Error: {}", e.getMessage(), e);

		} finally {
			try {
				if (null != streamPreference) {
					streamPreference.close();
				}				
			} catch (IOException e) {
				logger.error("Error al cargar configuración de preferencias. Error: {}", e.getMessage(), e);
			}
		}
	}
	
	public static PreferencesHandler getInstance() {
		return PreferencesHandler.getInstance(false);
	}

	/**
	 *
	 * @return
	 */
	public static PreferencesHandler getInstance(boolean reload) {
		if (reload || null == instance) {
			PreferencesHandler temp = new PreferencesHandler();
			instance = temp;
		}
		return instance;
	}

	/**
	 * 
	 * @return 
	 */
	public String getProperty(String propertyName) {    
		return properties.getProperty(propertyName);
	}
}   