package com.davivienda.consola.web.documentos.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import co.com.ibm.components.pager.interfaces.IResultsetToDTO;

public class ConsultaErroresDataRespType implements Serializable, IResultsetToDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long idProceso;
	private String codigoError;
	private String descripcionError;
	private Timestamp fechaCreacion;
	private String componente;
	private String plantilla;
	private String paquete;
	
	public ConsultaErroresDataRespType() {
		
	}
	
	@Override
	public Object convertToDTO(ResultSet resultSet) throws SQLException{
		int i=1;
		ConsultaErroresDataRespType item = new ConsultaErroresDataRespType();
		item.setIdProceso(resultSet.getLong(i));
		item.setCodigoError(resultSet.getString(++i));
		item.setDescripcionError(resultSet.getString(++i));
		item.setFechaCreacion(resultSet.getTimestamp(++i));
		item.setComponente(resultSet.getString(++i));
		item.setPlantilla(resultSet.getString(++i));
		item.setPaquete(resultSet.getString(++i));
		return item;
	}

	public long getIdProceso() {
		return idProceso;
	}

	public void setIdProceso(long idProceso) {
		this.idProceso = idProceso;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public String getDescripcionError() {
		return descripcionError;
	}

	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}

	public Timestamp getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
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
	

}
