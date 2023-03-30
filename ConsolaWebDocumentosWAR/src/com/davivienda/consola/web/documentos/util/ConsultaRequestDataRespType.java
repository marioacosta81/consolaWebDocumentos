package com.davivienda.consola.web.documentos.util;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

public class ConsultaRequestDataRespType implements Serializable{
	
	private static final long serialVersionUID = 2899220625314875518L;
	
	private byte[] requestInvocacion;
	private byte[] requestGeneracion;
	private byte[] requestCorreo;
	private byte[] requestFilenet;
	
	public ConsultaRequestDataRespType() {
		this.requestInvocacion = null;
		this.requestGeneracion = null;
		this.requestCorreo = null;
		this.requestFilenet = null;
	}

	public byte[] getRequestInvocacion() {
		return requestInvocacion;
	}

	public void setRequestInvocacion(Blob requestInvocacion) throws SQLException {
		if(requestInvocacion != null &&  requestInvocacion.length() > 0) {
			this.requestInvocacion = requestInvocacion.getBytes(1, (int) requestInvocacion.length());
		}
	}

	public byte[] getRequestGeneracion() {
		return requestGeneracion;
	}

	public void setRequestGeneracion(Blob requestGeneracion) throws SQLException {
		if(requestGeneracion != null &&  requestGeneracion.length() > 0) {
			this.requestGeneracion = requestGeneracion.getBytes(1, (int) requestGeneracion.length());
		}
	}

	public byte[] getRequestCorreo() {
		return requestCorreo;
	}

	public void setRequestCorreo(Blob requestCorreo) throws SQLException {
		if(requestCorreo != null &&  requestCorreo.length() > 0) {
			this.requestCorreo = requestCorreo.getBytes(1, (int) requestCorreo.length());
			
		}
	}

	public byte[] getRequestFilenet() {
		return requestFilenet;
	}

	public void setRequestFilenet(Blob requestFilenet) throws SQLException {
		if(requestFilenet != null &&  requestFilenet.length() > 0) {
			this.requestFilenet = requestFilenet.getBytes(1, (int) requestFilenet.length());
			
		}
	}
	
	
	

}
