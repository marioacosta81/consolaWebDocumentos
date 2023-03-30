package com.davivienda.consola.web.documentos.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <b>IBM. Global Bussiness Services AMS Colombia.</b>
 *
 * <p>Clase cache para de la aplicación</p>
 *
 * @author <A HREF="mailto:ocaldero@co.ibm.com">Oscar Calderon</A> <p>
 * <b>Fecha de creación(dd/mmm/aaaa): </b> [23/11/2017]
 *
 * @version [1.0, 23/11/2017]
 *
 */
public class SingletonCache {

    private static SingletonCache instance;
    private Map<String, Map<String, Object>> mapaCaches = new HashMap<String, Map<String, Object>>();
    private long tiempoRecargaCache;
    private String lastTimeOutCache;
    private static Logger logger = LoggerFactory.getLogger(SingletonCache.class.getName());

    /**
     * Constructor de la clase
     */
    private SingletonCache() {
    }
    
    /**
     * Retorna la instancia del cache
     * @return the instance
     * @throws PSEException 
     */
    public static synchronized SingletonCache getInstance() throws Exception {

        String timeOutCache = Util.getProperty(Constantes.PARAM_TIMEOUT_CACHE_SISTEMA);
        
        if (instance == null) {
            instance = new SingletonCache();
            instance.setTiempoRecargaCache(-1);
            instance.setLastTimeOutCache(timeOutCache);
        } else {
            if ( ! instance.getLastTimeOutCache().equals(timeOutCache)){
                instance.setTiempoRecargaCache(Long.valueOf(timeOutCache));
                instance.setLastTimeOutCache(timeOutCache);
                logger.info("Se carga cache, timeout en minutos= " + ((Long.valueOf(timeOutCache)) / Constantes.UN_MINUTO_MILIS));
            }
        }
        
        if(instance.recargarCache()){
            
            try {
                logger.info("Cargando cache...");

                //cargar cache
                
            } catch(Exception e){
                instance.setTiempoRecargaCache(-1);
                logger.error("Error al cargar cache: " + e.getMessage(), e);
                throw new Exception("Error al cargar cache: " + e.getMessage());
            }
            instance.setTiempoRecargaCache(Long.valueOf(timeOutCache));
            logger.info("Se carga cache, timeout en minutos= " + ((Long.valueOf(timeOutCache)) / Constantes.UN_MINUTO_MILIS));
        }
        
        return instance;
    }

    /**
     * Metodo que coloca el objeto deseado en cache
     * @param nombreServicio
     * @param key
     * @param object
     */
    public void ponerElemento(String nombreServicio, String key, Object object) {
        if (object != null) {
            Map<String, Object> cache = mapaCaches.get(nombreServicio);
            if (cache == null) {
                cache = new HashMap<String, Object>();
                mapaCaches.put(nombreServicio, cache);
            }
            cache.put(key, object);
        }
    }

    /**
     * Metodo que obtiene el elemento de cache
     * @param nombreServicio
     * @param key
     * @return the key
     */
    public Object obtenerElemento(String nombreServicio, String key) {

        Map<String, Object> cache = mapaCaches.get(nombreServicio);
        if (cache != null) {
            return cache.get(key);
        } else {
            return null;
        }
    }
    
    /**
     * Metodo que obtiene el cache del servicio indicado
     * @param nombreServicio
     * @return the key
     */
    public Map<String, Object> obtenerCacheByName(String nombreServicio) {
        return mapaCaches.get(nombreServicio);
    }

    /**
     * Metodo que recarga el tiempo de activacion del cache
     * @return boolean
     */
    private boolean recargarCache(){
        if(System.currentTimeMillis() > tiempoRecargaCache){
            return true;
        }
        return false;
    }

    /**
     * Metodo para quitar el elemento de cache
     * @param nombreServicio
     * @param key
     */
    public void quitarElemento(String nombreServicio, String key) {
        Map<String, Object> cache = mapaCaches.get(nombreServicio);
        if (cache != null) {
            cache.remove(key);
        }
    }

    /**
     * Metodo de revisa si existe elemento en cache
     * @param nombreServicio
     * @param key
     * @return the cache.containsKey(key)
     */
    public boolean existeElemento(String nombreServicio, String key) {
        Map<String, Object> cache = mapaCaches.get(nombreServicio);
        if (cache != null) {
            return cache.containsKey(key);
        } else {
            return false;
        }
    }

    /**
     * Metodo para revisar la existencia de cache
     * @param nombreServicio
     * @return boolean
     */
    public boolean existeCache(String nombreServicio) {
        return mapaCaches.containsKey(nombreServicio);
    }

    /**
     * Metodo para eliminar cache
     * @param nombreServicio
     */
    public void eliminarCache(String nombreServicio) {
        mapaCaches.remove(nombreServicio);
    }

    /**
     * Metodo para obtener el tiempo del cache
     * @return the tiempoRecargaCache
     */
    public long getTiempoRecargaCache() {
        return tiempoRecargaCache;
    }

    /**
     * Metodo para setear el tiempo del cache
     * @param tiempoRecargaCache
     */
    private void setTiempoRecargaCache(long tiempoRecargaCache) {
        this.tiempoRecargaCache = System.currentTimeMillis() + tiempoRecargaCache;
    }

    /**
     * @return the lastTimeOutCache
     */
    public String getLastTimeOutCache() {
        return lastTimeOutCache;
    }

    /**
     * @param lastTimeOutCache the lastTimeOutCache to set
     */
    public void setLastTimeOutCache(String lastTimeOutCache) {
        this.lastTimeOutCache = lastTimeOutCache;
    }
}