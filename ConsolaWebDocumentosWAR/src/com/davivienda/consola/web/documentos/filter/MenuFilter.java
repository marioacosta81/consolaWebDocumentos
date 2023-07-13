/**
 * Copyright (c) 2018 DAVIVIENDA. All Rights Reserved.
 * 
 * This software is confidential and propietary information of DAVIVIENDA.
 * ("Confidential Information").
 * It may not be copied or reproduced in any manner without the express
 * written permission of DAVIVIENDA.
 */
package com.davivienda.consola.web.documentos.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>IBM. Global Bussiness Services AMS Colombia.</b>
 * <p>
 * Description:
 * </p>
 *
 * @author <A HREF="mailto:fmoncada@co.ibm.com">Freddy Moncada</A>
 *         <p>
 *         <b>Fecha de creación(dd/mmm/aaaa): </b> Jul 10, 2017
 *
 * @version [1.0, JUL 10, 2017]
 *
 */
public class MenuFilter implements Filter {

//	private static final Logger logger = LoggerFactory.getLogger(PreferencesHandler.class.getName());
	
	public MenuFilter() {
	}

	/**
	 *
	 * @param request
	 *            The servlet request we are processing
	 * @param response
	 *            The servlet response we are creating
	 * @param chain
	 *            The filter chain we are processing
	 *
	 * @exception IOException
	 *                if an input/output error occurs
	 * @exception ServletException
	 *                if a servlet error occurs
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		httpResponse.addHeader("X-Content-Type-Options", "nosniff");
		httpResponse.addHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
		httpResponse.addHeader("Pragma", "no-cache"); // HTTP 1.0.
		//httpResponse.addHeader("Expires", 0); // Proxies.

		
        chain.doFilter(httpRequest,httpResponse);
	}


	/**
	 *
	 * @param filterConfig
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	/**
	 *
	 */
	@Override
	public void destroy() {
		
	}

}
