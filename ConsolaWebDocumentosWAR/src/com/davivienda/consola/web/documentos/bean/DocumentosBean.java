package com.davivienda.consola.web.documentos.bean;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import javax.servlet.http.HttpServlet;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.davivienda.consola.web.documentos.util.Constantes;
import com.davivienda.consola.web.documentos.util.ConsultaDocumentosDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaErroresDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaRequestDataRespType;
import com.davivienda.consola.web.documentos.util.SingletonCache;
import com.davivienda.consola.web.documentos.util.Util;
import com.ibm.exceptions.core.IBMException;
import co.com.ibm.components.pager.interfaces.IPageData;
import co.com.ibm.components.pager.util.PageDataFactory;

@ViewScoped
@ManagedBean(value = "documentosBean")
public class DocumentosBean extends HttpServlet implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger logger = LoggerFactory.getLogger(SingletonCache.class.getName());
	private Map<Long, Boolean> checkedCorreo = new HashMap<Long, Boolean>();
	private Map<Long, Boolean> checkedFilenet = new HashMap<Long, Boolean>();
	private Map<Long, Boolean> checkedGeneracion = new HashMap<Long, Boolean>();

	@EJB
	private ConsolaWebDocumentosFacade facade;
	private LazyDataModel<ConsultaDocumentosDataRespType> result;
	private ConsultaRequestDataRespType request;
	@SuppressWarnings("unused")
	private String error;
	private IPageData pageData;
	private int pageSize;
	private int maxRegistros;
	private String dateSesion;

	private String fecha;
	private Date date;
	private Date hourInit;
	private Date hourFinal;
	private String horaInicio;
	private String horaFinal;
	private String identificacion;
	private String proceso;
	private String plantilla;
	private String paquete;
	private boolean correo;
	private boolean filenet;
	private boolean documento;
	private boolean reintentoRegistro;
	private boolean reintentoCorreo;
	private boolean reintentoFilenet;
	private List<String> listaPlantillas;
	private List<String> listaPaquetes;
	private boolean mostrarFiltros;
	private StreamedContent Archivo;

	@PostConstruct
	public void init() {
		try {
			this.maxRegistros = Integer.parseInt(Util.getProperty(Constantes.MAX_RIGISTROS));
			this.pageSize = Integer.parseInt(Util.getProperty(Constantes.PAGE_SIZE));
			this.dateSesion = "";
			this.listaPlantillas = Util.cargarListaXML(logger,
					Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_GENERACION_REPORTES),
					"/configParticular/listaPlantillas/plantilla", "nombre");
			this.listaPaquetes = Util.cargarListaXML(logger,
					Util.getProperty(Constantes.PARAM_ARCHIVO_CONFIG_PAQUETE_DOCUMENTOS),
					"/configParticular/configPaquetesDoc/paqueteDocs/paqueteDoc", "id");
			this.pageData = PageDataFactory.getOracleStrategyPageData();
			this.pageData.setPageSize(this.pageSize);
			this.mostrarFiltros = true;

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	public void consultarDocumentos() {
		logger.debug("Ingresa a consultarDocumentos");
		checkedCorreo = new HashMap<Long, Boolean>();
		checkedFilenet = new HashMap<Long, Boolean>();
		checkedGeneracion = new HashMap<Long, Boolean>();
		setFecha(date);
		setHoraInicio(hourInit);
		setHoraFinal(hourFinal);
		try {
			pageData = facade.consultarDocumentos(fecha + " " + horaInicio, fecha + " " + horaFinal, proceso,
					identificacion, plantilla, paquete, correo, filenet, documento, pageData);
			int tam = pageData.getTotalRecords();
			this.result = new LazyDataModel<ConsultaDocumentosDataRespType>() {
				private static final long serialVersionUID = 8885722005055879976L;

				@SuppressWarnings("unchecked")
				@Override
				public List<ConsultaDocumentosDataRespType> load(int first, int pageSize, Map<String, SortMeta> arg2,
						Map<String, FilterMeta> arg3) {
					logger.debug("Ingresa al load de documentos");
					List<ConsultaDocumentosDataRespType> lista = new ArrayList<ConsultaDocumentosDataRespType>();
					try {
						pageData.setPageNumber((first / pageSize) + 1);
						pageData.setPageSize(pageSize);
						logger.debug("Page Number " + pageData.getPageNumber());
						pageData = facade.consultarDocumentos(fecha + " " + horaInicio, fecha + " " + horaFinal,
								proceso, identificacion, plantilla, paquete, correo, filenet, documento, pageData);
						if (pageData != null) {
							if (pageData.getTotalRecords() < maxRegistros) {
								lista.addAll((Collection<ConsultaDocumentosDataRespType>) pageData.getPageItems());
							} else {
								logger.debug("se supera el maximo de registros, solo se muestran los primeros "
										+ maxRegistros + " resultados");
								lista = ((Collection<ConsultaDocumentosDataRespType>) pageData.getPageItems()).stream()
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
				this.mostrarFiltros = false;
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
		}
	}

	public void reintentarProcesosDocumento() throws IBMException {
		List<Long> idsCorreo = new ArrayList<Long>();
		List<Long> idsFilenet = new ArrayList<Long>();
		List<Long> idsGeneracion = new ArrayList<Long>();
		Iterator<Long> itCorreo = checkedCorreo.keySet().iterator();
		while (itCorreo.hasNext()) {
			Object key = itCorreo.next();
			if (checkedCorreo.get(key)) {
				idsCorreo.add((Long) key);
			}
		}
		Iterator<Long> itFilenet = checkedFilenet.keySet().iterator();
		while (itFilenet.hasNext()) {
			Object key = itFilenet.next();
			if (checkedFilenet.get(key)) {
				idsFilenet.add((Long) key);
			}
		}
		Iterator<Long> itGeneracion = checkedGeneracion.keySet().iterator();
		while (itGeneracion.hasNext()) {
			Object key = itGeneracion.next();
			if (checkedGeneracion.get(key)) {
				idsGeneracion.add((Long) key);
			}
		}
		try {
			if (idsCorreo.isEmpty() && idsFilenet.isEmpty() && idsGeneracion.isEmpty()) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No hay registros seleccionados"));
			} else {
				if (!idsCorreo.isEmpty()) {
					this.facade.actualizarIntentosCorreos(idsCorreo);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se actualizo reintentos de Correos"));
				}
				if (!idsFilenet.isEmpty()) {
					this.facade.actualizarIntentosFilenet(idsFilenet);
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se actualizo reintentos de Filenet"));
				}
				if (!idsGeneracion.isEmpty()) {
					this.facade.actualizarIntentosGeneracion(idsGeneracion);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Info", "Se actualizo reintentos de Generacion"));
				}
				checkedCorreo = new HashMap<Long, Boolean>();
				checkedFilenet = new HashMap<Long, Boolean>();
				checkedGeneracion = new HashMap<Long, Boolean>();
			}

		} catch (IBMException ex) {
			logger.error(ex.getMessage(), ex);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", ex.getMessage()));
		}
	}

	public void verErrores() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null,
				"menu2.xhtml");
	}

	public String validarProceso(String val) {
		if (val == null || val.isEmpty()) {
			return "";
		}
		if (val.equals("S")) {
			return "procesado";
		}
		return "noProcesado";
	}

	public void showFilters() {
		this.mostrarFiltros = true;
	}

	public void hiddeFilters() {
		this.mostrarFiltros = false;
	}

	public String getFechaHoraActual() {
		return Util.getFechaHoraActual(this.dateSesion);
	}

	public LazyDataModel<ConsultaDocumentosDataRespType> getResult() {
		return result;
	}

	public void setResult(LazyDataModel<ConsultaDocumentosDataRespType> result) {
		this.result = result;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		this.fecha = dateFormat.format(fecha);
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getHourInit() {
		return hourInit;
	}

	public void setHourInit(Date hourInit) {
		this.hourInit = hourInit;
	}

	public Date getHourFinal() {
		return hourFinal;
	}

	public void setHourFinal(Date hourFinal) {
		this.hourFinal = hourFinal;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Date horaInicio) {
		DateFormat hora = new SimpleDateFormat("HH:mm:ss");
		this.horaInicio = hora.format(horaInicio);
	}

	public String getHoraFinal() {
		return horaFinal;
	}

	public void setHoraFinal(Date horaFinal) {
		DateFormat hora = new SimpleDateFormat("HH:mm:ss");
		this.horaFinal = hora.format(horaFinal);
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPaquete() {
		return paquete;
	}

	public void setPaquete(String paquete) {
		this.paquete = paquete;
	}

	public boolean isCorreo() {
		return correo;
	}

	public void setCorreo(boolean correo) {
		this.correo = correo;
	}

	public boolean isFilenet() {
		return filenet;
	}

	public void setFilenet(boolean filenet) {
		this.filenet = filenet;
	}

	public boolean isDocumento() {
		return documento;
	}

	public void setDocumento(boolean documento) {
		this.documento = documento;
	}

	public boolean isReintentoRegistro() {
		return reintentoRegistro;
	}

	public void setReintentoRegistro(boolean reintentoRegistro) {
		this.reintentoRegistro = reintentoRegistro;
	}

	public boolean isReintentoCorreo() {
		return reintentoCorreo;
	}

	public void setReintentoCorreo(boolean reintentoCorreo) {
		this.reintentoCorreo = reintentoCorreo;
	}

	public boolean isReintentoFilenet() {
		return reintentoFilenet;
	}

	public void setReintentoFilenet(boolean reintentoFilenet) {
		this.reintentoFilenet = reintentoFilenet;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		DocumentosBean.logger = logger;
	}

	public ConsultaRequestDataRespType getRequest() {
		return request;
	}

	public void setRequest(ConsultaRequestDataRespType request) {
		this.request = request;
	}

	public void setListaDocumentos(ArrayList<String> listaPlantillas) {
		this.listaPlantillas = listaPlantillas;
	}

	public void setListaPaquetes(ArrayList<String> listaPaquetes) {
		this.listaPaquetes = listaPaquetes;
	}

	public static String getFacesParamValue(String name) {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
	}

	public boolean isFechaMayorHoy(Date fecha) {
		Date hoy = new Date();
		if (fecha != null && fecha.after(hoy)) {
			return true;
		}
		return false;
	}

	public IPageData getPageData() {
		return pageData;
	}

	public void setPageData(IPageData pageData) {
		this.pageData = pageData;
	}

//	public ArrayList<String> cargarListaXML(String xml, String tags, String tag)
//			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
//		try {
//			File inputFile = new File(xml);
//			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder dBuilder;
//			dBuilder = dbFactory.newDocumentBuilder();
//			Document doc = dBuilder.parse(inputFile);
//			doc.getDocumentElement().normalize();
//			XPath xPath = XPathFactory.newInstance().newXPath();
//			NodeList nodeList = (NodeList) xPath.compile(tags).evaluate(doc, XPathConstants.NODESET);
//			ArrayList<String> items = new ArrayList<String>();
//			items.add("");
//			for (int i = 0; i < nodeList.getLength(); i++) {
//				Node nNode = nodeList.item(i);
//				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element eElement = (Element) nNode;
//					if (eElement.getElementsByTagName(tag).item(0) != null) {
//						items.add(eElement.getElementsByTagName(tag).item(0).getTextContent());
//					}
//
//				}
//
//			}
//			Collections.sort(items);
//			return items;
//		} catch (ParserConfigurationException e) {
//			logger.error(e.getMessage());
//			this.error = e.getMessage();
//		} catch (SAXException e) {
//			logger.error(e.getMessage());
//			this.error = e.getMessage();
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//			this.error = e.getMessage();
//		} catch (XPathExpressionException e) {
//			logger.error(e.getMessage());
//			this.error = e.getMessage();
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			this.error = e.getMessage();
//		}
//		return null;
//
//	}

	public Map<Long, Boolean> getCheckedCorreo() {
		return checkedCorreo;
	}

	public void setCheckedCorreo(Map<Long, Boolean> checked) {
		this.checkedCorreo = checked;
	}

	public Map<Long, Boolean> getCheckedFilenet() {
		return checkedFilenet;
	}

	public void setCheckedFilenet(Map<Long, Boolean> checkedFilenet) {
		this.checkedFilenet = checkedFilenet;
	}

	public Map<Long, Boolean> getCheckedGeneracion() {
		return checkedGeneracion;
	}

	public void setCheckedGeneracion(Map<Long, Boolean> checkedGeneracion) {
		this.checkedGeneracion = checkedGeneracion;
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

	public StreamedContent getArchivo() {
		return Archivo;
	}

	public void setArchivo(StreamedContent archivo) {
		Archivo = archivo;
	}

}
