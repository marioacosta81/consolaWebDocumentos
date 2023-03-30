package com.davivienda.consola.web.documentos.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.sql.DataSource;

import com.davivienda.consola.web.documentos.dao.ConsolaWebDocumentosDAO;
import com.davivienda.consola.web.documentos.util.ConsolaWebDocumentos;
import com.davivienda.consola.web.documentos.util.ConsultaGeneracionDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaRequestDataRespType;
import com.ibm.exceptions.core.IBMException;

import co.com.ibm.components.pager.interfaces.IPageData;

/**
 * 
 * @author Yilber Salazar Session Bean implementation class
 *         ConsolaWebDocumentosFacade
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class ConsolaWebDocumentosFacade implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(lookup = ConsolaWebDocumentos.JNDI_DATASOURCE)
	private DataSource datasource;

	public IPageData consultarDocumentos(String fecha_hora_inicial, String fecha_hora_final, String id_proceso,
			String identificacion, String id_plantilla, String id_paquete, boolean pendiente_correo,
			boolean pendiente_filenet, boolean pendiente_documento, IPageData pageData) throws Exception {

		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		return dao.consultarDocumentos(fecha_hora_inicial, fecha_hora_final, id_proceso, identificacion, id_plantilla,
				id_paquete, pendiente_correo, pendiente_filenet, pendiente_documento, pageData);
	}

	public IPageData consultarErrores(String fecha_inicial, String fecha_final, String id_proceso, String idPlantilla,
			String idPaquete, IPageData pageData) throws Exception {

		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		return dao.consultarErrores(fecha_inicial, fecha_final, id_proceso, idPlantilla, idPaquete, pageData);
	}

	public IPageData consultarErroresId(String key, IPageData pageData) throws Exception {

		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		return dao.consultarErroresId(key, pageData);
	}

	public ConsultaRequestDataRespType consultaRequestDAO(String key) throws IBMException {

		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		return dao.consultarRequest(key);
	}

	public ConsultaGeneracionDataRespType consultaDocumentoAlmacenado(String key) throws IBMException {

		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		return dao.consultarDocumentoAlmacenado(key);
	}

	public void actualizarIntentosCorreos(List<Long> idsCorreo) throws IBMException {
		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		dao.actualizarIntentosCorreos(idsCorreo);

	}

	public void actualizarIntentosFilenet(List<Long> idsFilenet) throws IBMException {
		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		dao.actualizarIntentosFilenet(idsFilenet);

	}

	public void actualizarIntentosGeneracion(List<Long> idsGeneracion) throws IBMException {
		ConsolaWebDocumentosDAO dao = new ConsolaWebDocumentosDAO(this.datasource);
		dao.actualizarIntentosGeneracion(idsGeneracion);

	}

}
