package com.davivienda.consola.web.documentos.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import co.com.ibm.components.pager.interfaces.IResultsetToDTO;

public class ConsultaDocumentosDataRespType implements Serializable, IResultsetToDTO {
	
	private static final long serialVersionUID = -5207298254037866206L;
	private long idProceso;
	private String idPaquete;
	private String idPlantilla;
	private String canal;
	private Timestamp fechaIngreso;
	private Timestamp fechaActualizacion;
	private String tipoCliente;
	private String numeroIdCliente;
	private String documentoGenerado;
	private int intentosGeneracionDocumento;
	private Timestamp fechaGeneracionDocumento;
	private String requiereEnvioFilenet;
	private String documentoAlmacenado;
	private int intentosEnvioFilenet;
	private Timestamp fechaEnvioFilenet;
	private String requiereEnvioCorreo;
	private String correoEnviado;
	private int intentosEnvioCorreo;
	private Timestamp fechaEnvioCorreo;
	private String requestInicialAlmacenado;
	private String requestReporteAlmacenado;
	private String requestFilenetAlmacenado;
	private String requestCorreoAlmacenado;
	
	
	public ConsultaDocumentosDataRespType() {
		super();
	}
	
	@Override
	public Object convertToDTO(ResultSet resultSet) throws SQLException {
		int i=1;
		ConsultaDocumentosDataRespType item = new ConsultaDocumentosDataRespType();
		item.setIdProceso(resultSet.getLong(i));
		item.setIdPaquete(resultSet.getString(++i));
		item.setIdPlantilla(resultSet.getString(++i));
		item.setCanal(resultSet.getString(++i));
		item.setFechaIngreso(resultSet.getTimestamp(++i));
		item.setFechaActualizacion(resultSet.getTimestamp(++i));
		item.setTipoCliente(resultSet.getString(++i));
		item.setNumeroIdCliente(resultSet.getString(++i));
		item.setDocumentoAlmacenado(resultSet.getString(++i));
		item.setIntentosGeneracionDocumento(resultSet.getInt(++i));
		item.setFechaGeneracionDocumento(resultSet.getTimestamp(++i));
		item.setRequiereEnvioFilenet(resultSet.getString(++i));
		item.setIntentosEnvioFilenet(resultSet.getInt(++i));
		item.setFechaEnvioFilenet(resultSet.getTimestamp(++i));
		item.setRequiereEnvioCorreo(resultSet.getString(++i));
		item.setCorreoEnviado(resultSet.getString(++i));
		item.setIntentosEnvioCorreo(resultSet.getInt(++i));
		item.setFechaEnvioCorreo(resultSet.getTimestamp(++i));
		item.setRequestInicialAlmacenado(resultSet.getString(++i));
		item.setRequestReporteAlmacenado(resultSet.getString(++i));
		item.setRequestFilenetAlmacenado(resultSet.getString(++i));
		item.setRequestCorreoAlmacenado(resultSet.getString(++i));
		item.setDocumentoGenerado(resultSet.getString(++i));
		return item;
	}
	
	
	public long getIdProceso() {
		return idProceso;
	}
	public void setIdProceso(long idProceso) {
		this.idProceso = idProceso;
	}
	public String getIdPaquete() {
		return idPaquete;
	}
	public void setIdPaquete(String idPaquete) {
		this.idPaquete = idPaquete;
	}
	public String getIdPlantilla() {
		return idPlantilla;
	}
	public void setIdPlantilla(String idPlantilla) {
		this.idPlantilla = idPlantilla;
	}
	public String getCanal() {
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	public Timestamp getFechaIngreso() {
		return fechaIngreso;
	}
	public void setFechaIngreso(Timestamp fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	public Timestamp getFechaActualizacion() {
		return fechaActualizacion;
	}
	public void setFechaActualizacion(Timestamp fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}
	public String getTipoCliente() {
		return tipoCliente;
	}
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
	public String getNumeroIdCliente() {
		return numeroIdCliente;
	}
	public void setNumeroIdCliente(String numeroIdCliente) {
		this.numeroIdCliente = numeroIdCliente;
	}
	public String getDocumentoGenerado() {
		return documentoGenerado;
	}
	public void setDocumentoGenerado(String documentoGenerado) {
		this.documentoGenerado = documentoGenerado;
	}
	public int getIntentosGeneracionDocumento() {
		return intentosGeneracionDocumento;
	}
	public void setIntentosGeneracionDocumento(int intentosGeneracionDocumento) {
		this.intentosGeneracionDocumento = intentosGeneracionDocumento;
	}
	public Timestamp getFechaGeneracionDocumento() {
		return fechaGeneracionDocumento;
	}
	public void setFechaGeneracionDocumento(Timestamp fechaGeneracionDocumento) {
		this.fechaGeneracionDocumento = fechaGeneracionDocumento;
	}
	public String getRequiereEnvioFilenet() {
		return requiereEnvioFilenet;
	}
	public void setRequiereEnvioFilenet(String requiereEnvioFilenet) {
		this.requiereEnvioFilenet = requiereEnvioFilenet;
	}
	public String getDocumentoAlmacenado() {
		return documentoAlmacenado;
	}
	public void setDocumentoAlmacenado(String documentoAlmacenado) {
		this.documentoAlmacenado = documentoAlmacenado;
	}
	public int getIntentosEnvioFilenet() {
		return intentosEnvioFilenet;
	}
	public void setIntentosEnvioFilenet(int intentosEnvioFilenet) {
		this.intentosEnvioFilenet = intentosEnvioFilenet;
	}
	public Timestamp getFechaEnvioFilenet() {
		return fechaEnvioFilenet;
	}
	public void setFechaEnvioFilenet(Timestamp fechaEnvioFilenet) {
		this.fechaEnvioFilenet = fechaEnvioFilenet;
	}
	public String getRequiereEnvioCorreo() {
		return requiereEnvioCorreo;
	}
	public void setRequiereEnvioCorreo(String requiereEnvioCorreo) {
		this.requiereEnvioCorreo = requiereEnvioCorreo;
	}
	public String getCorreoEnviado() {
		return correoEnviado;
	}
	public void setCorreoEnviado(String correoEnviado) {
		this.correoEnviado = correoEnviado;
	}
	public int getIntentosEnvioCorreo() {
		return intentosEnvioCorreo;
	}
	public void setIntentosEnvioCorreo(int intentosEnvioCorreo) {
		this.intentosEnvioCorreo = intentosEnvioCorreo;
	}
	public Timestamp getFechaEnvioCorreo() {
		return fechaEnvioCorreo;
	}
	public void setFechaEnvioCorreo(Timestamp fechaEnvioCorreo) {
		this.fechaEnvioCorreo = fechaEnvioCorreo;
	}
	public String getRequestInicialAlmacenado() {
		return requestInicialAlmacenado;
	}
	public void setRequestInicialAlmacenado(String requestInicialAlmacenado) {
		this.requestInicialAlmacenado = requestInicialAlmacenado;
	}
	public String getRequestReporteAlmacenado() {
		return requestReporteAlmacenado;
	}
	public void setRequestReporteAlmacenado(String requestReporteAlmacenado) {
		this.requestReporteAlmacenado = requestReporteAlmacenado;
	}
	public String getRequestFilenetAlmacenado() {
		return requestFilenetAlmacenado;
	}
	public void setRequestFilenetAlmacenado(String requestFilenetAlmacenado) {
		this.requestFilenetAlmacenado = requestFilenetAlmacenado;
	}
	public String getRequestCorreoAlmacenado() {
		return requestCorreoAlmacenado;
	}
	public void setRequestCorreoAlmacenado(String requestCorreoAlmacenado) {
		this.requestCorreoAlmacenado = requestCorreoAlmacenado;
	}
	
	

}
