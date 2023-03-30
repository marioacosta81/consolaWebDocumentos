package com.davivienda.consola.web.documentos.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.davivienda.consola.web.documentos.util.ConsolaWebDocumentos;
import com.davivienda.consola.web.documentos.util.ConsultaDocumentosDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaErroresDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaGeneracionDataRespType;
import com.davivienda.consola.web.documentos.util.ConsultaRequestDataRespType;
import com.davivienda.consola.web.documentos.util.Util;

import com.ibm.exceptions.core.IBMException;

import co.com.ibm.components.pager.dto.ParameterDTO;
import co.com.ibm.components.pager.interfaces.IPageData;
import co.com.ibm.components.pager.logic.PaginationLogicService;

/**
 * 
 * @author Yilber Salazar
 *
 */
public class ConsolaWebDocumentosDAO {

	private static Logger logger = Logger.getLogger(ConsolaWebDocumentosDAO.class);
	private static final String SCHEMA;

	static {
		String schemaDB = ConsolaWebDocumentos.DEFAULT_SCHEMA_DB;
		try {
			schemaDB = Util.getProperty(ConsolaWebDocumentos.PROPERTY_DB_SCHEMA_CONSOLA_WEB_DOCUMENTOS);
		}
		// no funciona IBMException
		catch (Exception e) {
			logger.error(e.getMessage());
			logger.warn(e.getMessage());
		}
		SCHEMA = schemaDB;
	}

	/** Objeto datasource a utilizar en la consulta. */
	private DataSource dataSource;
	/** Objeto connection a utilizar en la consulta. */
	private Connection connection;
	/** Objeto preparedStatement a utilizar en la consulta. */
	private PreparedStatement preparedStatement;
	/** Objeto ResultSet a utilizar en la consulta. */
	private ResultSet resultSet;

	private String consultarDocumentosTx = "SELECT cd.id_proceso, " + "cd.id_paquete, " + "cd.id_plantilla, "
			+ "cd.canal, " + "cd.fecha_ingreso, " + "cd.fecha_actualizacion, " + "cd.tipo_id_cliente, "
			+ "cd.num_id_cliente, " + "cd.almacenado_doc, " + "cd.intentos_gen_reporte, " + "cd.fecha_gen_reporte, "
			+ "cd.requiere_filenet, " + "cd.intentos_filenet, " + "cd.fecha_envio_filenet, " + "cd.requiere_correo, "
			+ "cd.envado_correo, " + "cd.intentos_correo, " + "cd.fecha_envio_correo, " + "cd.req_i_almacenado, "
			+ "cd.req_reporte_almacenado, " + "cd.req_filenet_almacenado, " + "cd.req_correo_almacenado, "
			+ "cd.generado_reporte " + "FROM " + SCHEMA + ".detalle_documentos cd "
			+ "WHERE cd.fecha_ingreso BETWEEN TO_DATE (?, 'yyyy-mm-dd hh24:mi:ss') "
			+ "AND TO_DATE (?,'yyyy-mm-dd hh24:mi:ss') ";

	private String consultaErroresTx = "SELECT ce.key_primary, " + "ce.codigo_error, " + "ce.descripcion, "
			+ "ce.fecha_creacion, " + "ce.componente, " + "ce.cod_plantilla, " + "ce.cod_paquete " + "FROM " + SCHEMA
			+ ".detalle_errores ce " + "WHERE trunc(ce.fecha_creacion) BETWEEN TO_DATE (?, 'yyyy-mm-dd') "
			+ "AND TO_DATE (?, 'yyyy-mm-dd')";

	private String consultaErroresIdTx = "SELECT ce.key_primary, " + "ce.codigo_error, " + "ce.descripcion, "
			+ "ce.fecha_creacion, " + "ce.componente, " + "ce.cod_plantilla, " + "ce.cod_paquete " + "FROM " + SCHEMA
			+ ".detalle_errores ce " + "WHERE ce.key_primary = ?";

	private String consultaRequestTx = "SELECT r.blob_requestinvocacion, " + "r.blob_requestgeneracion, "
			+ "r.blob_requestcorreo, " + "r.blob_requestfilenet " + "FROM " + SCHEMA + ".request r "
			+ "WHERE r.key_request = ?";

	private String consultaDocumentoAlmacenadoTx = "SELECT g.blob_documentogenerado " + "FROM " + SCHEMA
			+ ".generacion g " + "WHERE g.key_generacion = ?";

	private String actualizarIntentosCorreoTx = "UPDATE " + SCHEMA + ".correo c " + "SET c.val_intentoscorreo = '5' "
			+ "WHERE c.key_correo IN (?";

	private String actualizarIntentosFilenetTx = "UPDATE " + SCHEMA + ".filenet f " + "SET f.val_intentosfilenet = '5' "
			+ "WHERE f.key_filenet IN (?";

	private String actualizarIntentosGeneracionTx = "UPDATE " + SCHEMA + ".generacion g "
			+ "SET g.val_intentosgeneracion = '5' " + "WHERE g.key_generacion IN (?";

	/**
	 * Constructor del DAO que recibe como parámetro un datasource.
	 * 
	 * @param dataSource dataSource a utilizar en la consulta.
	 * @throws IBMException error en la inicialización del DAO.
	 */
	public ConsolaWebDocumentosDAO(DataSource dataSource) throws IBMException {
		if (dataSource == null) {
			throw new IBMException(ConsolaWebDocumentos.ERROR_GENERICO);
		}
		this.dataSource = dataSource;
	}

	/**
	 * Consulta web documentos en vista
	 * 
	 * @param fecha
	 * @param hora_inicial
	 * @param hora_final
	 * @param id_proceso
	 * @param identificacion
	 * @param id_plantilla
	 * @param id_paquete
	 * @param pendiente_correo
	 * @param pendiente_filenet
	 * @param pendiente_documento
	 * @return información de la consulta
	 * @throws Exception
	 */
	public IPageData consultarDocumentos(String fecha_hora_inicial, String fecha_hora_final, String id_proceso,
			String identificacion, String id_plantilla, String id_paquete, boolean pendiente_correo,
			boolean pendiente_filenet, boolean pendiente_documento, IPageData pageData) throws Exception {
		logger.debug("Consultando datos en BDs consultarDocumentos");

		int i = 1;
		Collection<ParameterDTO> params = new ArrayList<ParameterDTO>();
		params.add(new ParameterDTO(i, fecha_hora_inicial));
		params.add(new ParameterDTO(++i, fecha_hora_final));

		if (id_proceso != null && !id_proceso.isEmpty()) {
			this.consultarDocumentosTx += " AND cd.id_proceso = ?";
			params.add(new ParameterDTO(++i, id_proceso));
		}
		if (identificacion != null && !identificacion.isEmpty()) {
			this.consultarDocumentosTx += " AND cd.num_id_cliente = ?";
			params.add(new ParameterDTO(++i, identificacion));
		}
		if (id_plantilla != null && !id_plantilla.isEmpty()) {
			this.consultarDocumentosTx += " AND cd.id_plantilla = ?";
			params.add(new ParameterDTO(++i, id_plantilla));
		}
		if (id_paquete != null && !id_paquete.isEmpty()) {
			this.consultarDocumentosTx += " AND cd.id_paquete = ?";
			params.add(new ParameterDTO(++i, id_paquete));
		}
		if (pendiente_correo) {
			this.consultarDocumentosTx += " AND cd.requiere_correo = ?" + "AND cd.envado_correo = ?";
			params.add(new ParameterDTO(++i, "S"));
			params.add(new ParameterDTO(++i, "N"));

		}
		if (pendiente_filenet) {
			this.consultarDocumentosTx += " AND cd.requiere_filenet = ?" + "AND cd.enviado_filenet = ?";
			params.add(new ParameterDTO(++i, "S"));
			params.add(new ParameterDTO(++i, "N"));
		}
		if (pendiente_documento) {
			this.consultarDocumentosTx += " AND cd.almacenado_doc = ?";
			params.add(new ParameterDTO(++i, "N"));
		}
		return PaginationLogicService.executeQueryAddingPagination(this.consultarDocumentosTx, params, dataSource,
				new ConsultaDocumentosDataRespType(), pageData);

	}

	public IPageData consultarErrores(String fecha_inicial, String fecha_final, String id_proceso, String idPlantilla,
			String idPaquete, IPageData pageData) throws Exception {

		int i = 1;
		Collection<ParameterDTO> params = new ArrayList<ParameterDTO>();
		params.add(new ParameterDTO(i, fecha_inicial));
		params.add(new ParameterDTO(++i, fecha_final));

		if (id_proceso != null && !id_proceso.isEmpty()) {
			this.consultaErroresTx += " AND ce.key_primary = ?";
			params.add(new ParameterDTO(++i, id_proceso));
		}
		if (idPlantilla != null && !idPlantilla.isEmpty()) {
			this.consultaErroresTx += " AND ce.cod_plantilla = ?";
			params.add(new ParameterDTO(++i, idPlantilla));
		}
		if (idPaquete != null && !idPaquete.isEmpty()) {
			this.consultaErroresTx += " AND ce.cod_paquete = ?";
			params.add(new ParameterDTO(++i, idPaquete));
		}
		return PaginationLogicService.executeQueryAddingPagination(this.consultaErroresTx, params, dataSource,
				new ConsultaErroresDataRespType(), pageData);

	}

	public IPageData consultarErroresId(String key, IPageData pageData) throws Exception {

		Collection<ParameterDTO> params = new ArrayList<ParameterDTO>();
		params.add(new ParameterDTO(1, key));
		return PaginationLogicService.executeQueryAddingPagination(this.consultaErroresIdTx, params, dataSource,
				new ConsultaErroresDataRespType(), pageData);

	}

	public ConsultaRequestDataRespType consultarRequest(String key) throws IBMException {
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(consultaRequestTx);
			preparedStatement.setString(1, key);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ConsultaRequestDataRespType data = new ConsultaRequestDataRespType();
				data.setRequestInvocacion(resultSet.getBlob("blob_requestinvocacion"));
				data.setRequestGeneracion(resultSet.getBlob("blob_requestgeneracion"));
				data.setRequestCorreo(resultSet.getBlob("blob_requestcorreo"));
				data.setRequestFilenet(resultSet.getBlob("blob_requestfilenet"));
				return data;
			}
		} catch (SQLException ex) {
			logger.error("error al consultar rquest: " + ex.getMessage());
			String[] params = { ex.getMessage() };
			throw new IBMException(ConsolaWebDocumentos.ERROR_DB, params);
		} finally {
			closeResources();
		}
		return null;
	}

	public ConsultaGeneracionDataRespType consultarDocumentoAlmacenado(String key) throws IBMException {
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(consultaDocumentoAlmacenadoTx);
			preparedStatement.setString(1, key);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				ConsultaGeneracionDataRespType data = new ConsultaGeneracionDataRespType();
				data.setDocumentoAlmacenado(resultSet.getBlob("blob_documentogenerado"));
				return data;
			}
		} catch (SQLException ex) {
			logger.error("error al consultar documento: " + ex.getMessage());
			String[] params = { ex.getMessage() };
			throw new IBMException(ConsolaWebDocumentos.ERROR_DB, ex, params);
		} finally {
			closeResources();
		}
		return null;
	}

	public void actualizarIntentosCorreos(List<Long> idsCorreo) throws IBMException {
		try {
			connection = dataSource.getConnection();
			if (idsCorreo.size() > 1) {
				for (int i = 1; i < idsCorreo.size(); i++) {
					actualizarIntentosCorreoTx += ", ?";
				}
			}
			actualizarIntentosCorreoTx += ")";
			preparedStatement = connection.prepareStatement(actualizarIntentosCorreoTx);
			for (int i = 0; i < idsCorreo.size(); i++) {
				preparedStatement.setLong(i + 1, idsCorreo.get(i));
			}
			preparedStatement.execute();
		} catch (SQLException ex) {
			logger.error("error al actualizar intentos de correo: " + ex.getMessage());
			String[] params = { ex.getMessage() };
			throw new IBMException(ConsolaWebDocumentos.ERROR_DB, ex, params);
		} finally {
			closeResources();
		}
	}

	public void actualizarIntentosFilenet(List<Long> idsFilenet) throws IBMException {
		try {
			connection = dataSource.getConnection();
			if (idsFilenet.size() > 1) {
				for (int i = 1; i < idsFilenet.size(); i++) {
					actualizarIntentosFilenetTx += ", ?";
				}
			}
			actualizarIntentosFilenetTx += ")";
			preparedStatement = connection.prepareStatement(actualizarIntentosFilenetTx);
			for (int i = 0; i < idsFilenet.size(); i++) {
				preparedStatement.setLong(i + 1, idsFilenet.get(i));
			}
			preparedStatement.execute();
		} catch (SQLException ex) {
			logger.error("error al actualizar intentos de filenet: " + ex.getMessage());
			String[] params = { ex.getMessage() };
			throw new IBMException(ConsolaWebDocumentos.ERROR_DB, ex, params);
		} finally {
			closeResources();
		}

	}

	public void actualizarIntentosGeneracion(List<Long> idsGeneracion) throws IBMException {
		try {
			connection = dataSource.getConnection();
			if (idsGeneracion.size() > 1) {
				for (int i = 1; i < idsGeneracion.size(); i++) {
					actualizarIntentosGeneracionTx += ", ?";
				}
			}
			actualizarIntentosGeneracionTx += ")";
			preparedStatement = connection.prepareStatement(actualizarIntentosGeneracionTx);
			for (int i = 0; i < idsGeneracion.size(); i++) {
				preparedStatement.setLong(i + 1, idsGeneracion.get(i));
			}
			preparedStatement.execute();
		} catch (SQLException ex) {
			logger.error("error al actualizar intentos de generacion: " + ex.getMessage());
			String[] params = { ex.getMessage() };
			throw new IBMException(ConsolaWebDocumentos.ERROR_DB, ex, params);
		} finally {
			closeResources();
		}

	}

	/**
	 * Método utilitario para cerrar los recursos de base de datos.
	 */
	private void closeResources() {
		try {
			if (this.resultSet != null) {
				this.resultSet.close();
			}
			if (this.preparedStatement != null) {
				this.preparedStatement.close();
			}
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			logger.warn("No se pudo cerrar recurso de BD: " + e.getMessage());
		}
	}

}
