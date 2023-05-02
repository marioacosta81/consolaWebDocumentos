package com.davivienda.consola.web.documentos.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.davivienda.consola.web.documentos.util.Constantes;
import com.davivienda.consola.web.documentos.util.ConsultaErroresDataRespType;
import com.davivienda.consola.web.documentos.util.SingletonCache;
import com.davivienda.consola.web.documentos.util.Util;
import com.ibm.exceptions.core.IBMException;

import co.com.ibm.components.pager.interfaces.IPageData;
import co.com.ibm.components.pager.util.PageDataFactory;

//@ViewScoped
@SessionScoped
@ManagedBean(value = "erroresBean")
public class ErroresBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SingletonCache.class.getName());

	@EJB
	private ConsolaWebDocumentosFacade facade;
	private LazyDataModel<ConsultaErroresDataRespType> result;
	private String error;
	private IPageData pageData;
	private int pageSize;
	private int maxRegistros;

	private Date dateInit;
	private Date dateFinal;
	private String fechaInicio;
	private String fechaFinal;
	private String idPlantilla;
	private String idPaquete;
	private String proceso;
	private String dateSesion;
	private boolean mostrarFiltros;
	private List<String> listaPlantillas;
	private List<String> listaPaquetes;

	@PostConstruct
	public void init() {
		try {
			this.maxRegistros = Integer.parseInt(Util.getProperty(Constantes.MAX_RIGISTROS));
			this.pageSize = Integer.parseInt(Util.getProperty(Constantes.PAGE_SIZE));
			this.pageData = PageDataFactory.getOracleStrategyPageData();
			this.listaPlantillas = Util.cargarListaXML(logger,
					Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_GENERACION_REPORTES),
					"/configParticular/listaPlantillas/plantilla", "nombre");
			this.listaPaquetes = Util.cargarListaXML(logger,
					Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_PAQUETE_DOCUMENTOS),
					"/configParticular/configPaquetesDoc/paqueteDocs/paqueteDoc", "id");
			this.pageData.setPageSize(this.pageSize);
			this.dateSesion = "";
			this.setMostrarFiltros(true);

			String key = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("key");
			if (key != null) {
				logger.debug("Acción ver Errores");
				consultarErroresId(key);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void consultarErrores() {
		logger.debug("Ingresa a consultarErrores");
		setFechaInicio(dateInit);
		setFechaFinal(dateFinal);
		try {
			pageData = facade.consultarErrores(fechaInicio, fechaFinal, proceso, idPlantilla, idPaquete, pageData);
			int tam = pageData.getTotalRecords();
			this.result = new LazyDataModel<ConsultaErroresDataRespType>() {
				private static final long serialVersionUID = 8885722005055879976L;

				@SuppressWarnings("unchecked")
				@Override
				public List<ConsultaErroresDataRespType> load(int first, int pageSize, Map<String, SortMeta> arg2,
						Map<String, FilterMeta> arg3) {
					logger.debug("Ingresa al load de consultarErrores");
					List<ConsultaErroresDataRespType> lista = new ArrayList<ConsultaErroresDataRespType>();
					try {
						pageData.setPageNumber((first / pageSize) + 1);
						pageData.setPageSize(pageSize);
						logger.debug("Page Number " + pageData.getPageNumber());
						pageData = facade.consultarErrores(fechaInicio, fechaFinal, proceso, idPlantilla, idPaquete,
								pageData);
						if (pageData != null) {
							if (pageData.getTotalRecords() < maxRegistros) {
								lista.addAll((Collection<ConsultaErroresDataRespType>) pageData.getPageItems());
							} else {
								logger.debug("se supera el maximo de registros, solo se muestran los primeros "
										+ maxRegistros + " resultados");
								lista = ((Collection<ConsultaErroresDataRespType>) pageData.getPageItems()).stream()
										.limit(maxRegistros).collect(Collectors.toList());
							}
							setRowCount(pageData.getTotalRecords());
						}

					} catch (Exception e) {
						logger.error(e.getMessage(), e);
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
					}
					logger.debug("Datos Totales " + lista.size());
					return lista;
				}
			};

			if (tam > maxRegistros) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Máximo " + maxRegistros + " registros",
						"Hay mas registros que cumplen la condición. Agregue mas filtros. Se muestra los primeros "
								+ maxRegistros + " registros"));
			} else if (tam == 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No se encontraron resultados"));
			} else {
				this.setMostrarFiltros(false);
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			this.error = ex.getMessage();
		}
	}

	public void consultarErroresId(final String key) throws Exception {
		logger.debug("Ingresa a consultarErroresId");

		try {
//			final String key = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
//					.get("key");
			logger.debug("id proceso " + key);

			pageData = facade.consultarErroresId(key, pageData);
			int tam = pageData.getTotalRecords();
			this.result = new LazyDataModel<ConsultaErroresDataRespType>() {

				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				public List<ConsultaErroresDataRespType> load(int first, int pageSize, Map<String, SortMeta> arg2,
						Map<String, FilterMeta> arg3) {
					logger.debug("Ingresa al load de consultarErroresId");

					List<ConsultaErroresDataRespType> lista = new ArrayList<ConsultaErroresDataRespType>();
					try {
						pageData.setPageNumber((first / pageSize) + 1);
						pageData.setPageSize(pageSize);
						logger.debug("Page Number " + pageData.getPageNumber());
						pageData = facade.consultarErroresId(key, pageData);
						if (pageData != null && pageData.getTotalRecords() < maxRegistros) {
							lista.addAll((Collection<ConsultaErroresDataRespType>) pageData.getPageItems());
							setRowCount(pageData.getTotalRecords());
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
						e.printStackTrace();
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
					}
					logger.debug("Datos Totales" + lista.size());
					return lista;
				}
			};

			if (tam == 0) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "No se encontraron errores"));
			}
//			else {
//				FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
//						.handleNavigation(FacesContext.getCurrentInstance(), null, "menu2.xhtml");
//			}
		} catch (IBMException ex) {
			logger.error(ex.getMessage());
			this.error = ex.getMessage();
		}

	}

	public String formatoNameService(String value) {
		if (value == null || value.isEmpty()) {
			return value;
		}
		return value.replaceAll("SrvScn", "");
	}

	public String getFechaHoraActual() {
		return Util.getFechaHoraActual(this.dateSesion);
	}

	public void showFilters() {
		this.setMostrarFiltros(true);
	}

	public void hiddeFilters() {
		this.setMostrarFiltros(false);
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		ErroresBean.logger = logger;
	}

	public ConsolaWebDocumentosFacade getFacade() {
		return facade;
	}

	public void setFacade(ConsolaWebDocumentosFacade facade) {
		this.facade = facade;
	}

	public LazyDataModel<ConsultaErroresDataRespType> getResult() {
		return result;
	}

	public void setResult(LazyDataModel<ConsultaErroresDataRespType> result) {
		this.result = result;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public IPageData getNewPageData() {
		return pageData;
	}

	public void setNewPageData(IPageData pageData) {
		this.pageData = pageData;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		this.fechaInicio = dateFormat.format(date);
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		this.fechaFinal = dateFormat.format(date);
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public Date getDateInit() {
		return dateInit;
	}

	public void setDateInit(Date dateInit) {
		this.dateInit = dateInit;
	}

	public Date getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Date dateFinal) {
		this.dateFinal = dateFinal;
	}

	public int getPageSize() {
		return pageSize;
	}

	public boolean isMostrarFiltros() {
		return mostrarFiltros;
	}

	public void setMostrarFiltros(boolean mostrarFiltros) {
		this.mostrarFiltros = mostrarFiltros;
	}

	public List<String> getListaPlantillas() {
		return listaPlantillas;
	}

	public void setListaPlantillas(List<String> listaPlantillas) {
		this.listaPlantillas = listaPlantillas;
	}

	public List<String> getListaPaquetes() {
		return listaPaquetes;
	}

	public void setListaPaquetes(List<String> listaPaquetes) {
		this.listaPaquetes = listaPaquetes;
	}

	public String getIdPlantilla() {
		return idPlantilla;
	}

	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	public String getIdPaquete() {
		return idPaquete;
	}

	public void setIdPaquete(String idProceso) {
		this.idPaquete = idProceso;
	}

}
