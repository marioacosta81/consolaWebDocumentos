package com.davivienda.consola.web.documentos.util;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.davivienda.consola.web.documentos.handler.PreferencesHandler;

public class Util {

//	private static final Logger logger = LoggerFactory.getLogger(Util.class.getName());

	public static String getHttpParameterValue(String parameter, HttpServletRequest request) {
		String value = request.getParameter(parameter);
		return stripXSS(value);
	}

	private static String stripXSS(String value) {
		if (value != null) {
			// NOTE: It's highly recommended to use the ESAPI library and uncomment the
			// following line to
			// avoid encoded attacks.
			// value = ESAPI.encoder().canonicalize(value);

			value = value.replaceAll("[^\\dA-Za-z\\.\\s ]", ""); // .replaceAll("\\s+", "");

			// Avoid null characters
			value = value.replaceAll("", "");

			// Avoid anything between script tags
			Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid anything in a src='...' type of expression
			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome </script> tag
			scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Remove any lonesome <script ...> tag
			scriptPattern = Pattern.compile("<script(.*?)>",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid eval(...) expressions
			scriptPattern = Pattern.compile("eval\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid expression(...) expressions
			scriptPattern = Pattern.compile("expression\\((.*?)\\)",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid javascript:... expressions
			scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid vbscript:... expressions
			scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
			value = scriptPattern.matcher(value).replaceAll("");

			// Avoid onload= expressions
			scriptPattern = Pattern.compile("onload(.*?)=",
					Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
			value = scriptPattern.matcher(value).replaceAll("");
		}
		return value;
	}

	/**
	 * Metodo para obtener la ip desde el request
	 * 
	 * @param request
	 * @return the ip
	 */
	public static String getIP(HttpServletRequest request, String ipHeaderNames) {

		String ip = null;
		if (ipHeaderNames != null && !ipHeaderNames.isEmpty()) {
			StringTokenizer tokenizerHeaders = new StringTokenizer(ipHeaderNames, Constantes.CARACTER_COMA);
			while (tokenizerHeaders.hasMoreElements()) {
				String ipHeaderName = tokenizerHeaders.nextToken().trim();
				if ((ip = request.getHeader(ipHeaderName)) != null) {
					StringTokenizer tokenizerIps = new StringTokenizer(ip, Constantes.CARACTER_COMA);
					while (tokenizerIps.hasMoreElements()) {
						ip = tokenizerIps.nextToken().trim();
						return ip;
					}
				}
			}
		}

		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * Utilidad para obtener las propiedades.
	 * 
	 * @param propertyName Nombre de la propiedad (llave)
	 * @param parametros   Arreglo de objetos con los parametros que se reemplazarán
	 *                     en el mensaje. Se usará lo que devuelva el método
	 *                     toString() de cada objeto recibido
	 * @return Devuelve el valor de la propiedad de acuerdo a la llave recibida
	 */
	public static String getProperty(String propertyName, Object... parametros) {

		PreferencesHandler pref = PreferencesHandler.getInstance();
		String valuePropertie = pref.getProperty(propertyName);

		if (null != parametros && parametros.length > 0) {
			valuePropertie = MessageFormat.format(valuePropertie, parametros);
		}

		return valuePropertie;
	}

	/**
	 * Formatea el valor de las horas para mostrar en la presentación
	 * 
	 * @param dateSesion Hora guardada en el componente correspondiente a cada
	 *                   pantalla
	 */
	public static String getFechaHoraActual(String dateSesion) {

		if (dateSesion == "") {

			String[] meses = new String[] { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
					"Septiembre", "Octubre", "Noviembre", "Diciembre" };
			String[] diasSemana = new String[] { "", "Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes",
					"Sábado" };
			String[] meridiano = new String[] { "AM", "PM" };
			Calendar fecha = Calendar.getInstance(new Locale("CO"));
			String minuto = fecha.get(Calendar.MINUTE) < 10 ? "0" + fecha.get(Calendar.MINUTE)
					: "" + fecha.get(Calendar.MINUTE);
			int hrs = fecha.get(Calendar.HOUR);
			String hora = hrs < 10 ? (hrs == 0) ? "12" : "0" + hrs : "" + hrs;

			dateSesion = new StringBuilder().append(diasSemana[fecha.get(Calendar.DAY_OF_WEEK)]).append(" ")
					.append(fecha.get(Calendar.DAY_OF_MONTH)).append(" de ").append(meses[fecha.get(Calendar.MONTH)])
					.append(" de ").append(fecha.get(Calendar.YEAR)).append(", ").append(hora).append(":")
					.append(minuto).append(" ").append(meridiano[fecha.get(Calendar.AM_PM)]).toString();

			return dateSesion;
		} else {
			return dateSesion;
		}

	}
	
	public static ArrayList<String> cargarListaXML(Logger logger, String xml, String tags, String tag)
			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		try {
			File inputFile = new File(xml);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.compile(tags).evaluate(doc, XPathConstants.NODESET);
			ArrayList<String> items = new ArrayList<String>();
			items.add("");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement.getElementsByTagName(tag).item(0) != null) {
						items.add(eElement.getElementsByTagName(tag).item(0).getTextContent());
					}

				}

			}
			Collections.sort(items);
			return items;
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (XPathExpressionException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}
}
