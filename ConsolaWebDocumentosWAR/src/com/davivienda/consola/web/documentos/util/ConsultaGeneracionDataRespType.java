package com.davivienda.consola.web.documentos.util;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.SQLException;

public class ConsultaGeneracionDataRespType implements Serializable {

	private static final long serialVersionUID = 3570748150032324219L;

	private byte[] documentoAlmacenado;

	public ConsultaGeneracionDataRespType() {
		this.documentoAlmacenado = null;
	}

	public byte[] getDocumentoAlmacenado() {
		return documentoAlmacenado;
	}

	public void setDocumentoAlmacenado(Blob documentoAlmacenado) throws SQLException {
		if (documentoAlmacenado != null && documentoAlmacenado.length() > 0) {
			this.documentoAlmacenado = documentoAlmacenado.getBytes(1, (int) documentoAlmacenado.length());
		}
	}

}
