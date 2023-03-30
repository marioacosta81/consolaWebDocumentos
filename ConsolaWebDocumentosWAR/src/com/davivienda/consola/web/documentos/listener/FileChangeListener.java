//package com.davivienda.consola.web.documentos.listener;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import javax.ejb.LocalBean;
//import javax.ejb.Stateful;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//
//import org.slf4j.Logger;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import org.apache.commons.jci.monitor.FilesystemAlterationListener;
//import org.apache.commons.jci.monitor.FilesystemAlterationMonitor;
//import org.apache.commons.jci.monitor.FilesystemAlterationObserver;
//
///**
// * @author 079944661
// *
// */
//@Stateful
//@LocalBean
//@TransactionAttribute(TransactionAttributeType.SUPPORTS)
//public class FileChangeListener implements FilesystemAlterationListener {
//
//	private FilesystemAlterationMonitor fileAlterMonitorObj = new FilesystemAlterationMonitor();
//
//	private File file;
//
//	private Logger logger;
//
//	private List<String> listaPlantillas;
//
//	private List<String> listaPaquetes;
//	
//	public FileChangeListener(){
//		
//	}
//
//	public FileChangeListener(String ruta, Logger logger) {
//		this.file = new File(ruta);
//		this.logger = logger;
//	}
//
//	public void attachListenerToFile() {
//		fileAlterMonitorObj.addListener(file, this);
//	}
//
//	public void startListening() {
//		fileAlterMonitorObj.start();
//		fileAlterMonitorObj.setInterval(100);
//
//		logger.debug("onStart de ACTUALIZACION DEL ARCHIVO: " + file.getAbsolutePath());
//		try {
//			cargarArchivos(file);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//
//	@Override
//	public void onFileChange(File arg0) {
//		try {
//			cargarArchivos(arg0);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//
//	}
//
//	private void cargarArchivos(File arg0)
//			throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
//		if (arg0.getAbsolutePath().contains("SrvScnGeneracionPaqueteDocumentos_scn_config")) {
//			setListaPaquetes(cargarListaXML(arg0, "/configParticular/configPaquetesDoc/paqueteDocs/paqueteDoc", "id"));
//		} else {
//			setListaPlantillas(cargarListaXML(arg0, "/configParticular/listaPlantillas/plantilla", "nombre"));
//
//		}
//	}
//
//	private ArrayList<String> cargarListaXML(File inputFile, String tags, String tag)
//			throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
//		try {
////			File inputFile = new File(xml);
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
//		} catch (SAXException e) {
//			logger.error(e.getMessage());
//		} catch (IOException e) {
//			logger.error(e.getMessage());
//		} catch (XPathExpressionException e) {
//			logger.error(e.getMessage());
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return null;
//
//	}
//
//	@Override
//	public void onFileCreate(File arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onDirectoryChange(File arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onDirectoryCreate(File arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onDirectoryDelete(File arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onFileDelete(File arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStart(FilesystemAlterationObserver arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStop(FilesystemAlterationObserver arg0) {
//		// TODO Auto-generated method stub
//
//	}
//
//	public List<String> getListaPlantillas() {
//		return listaPlantillas;
//	}
//
//	public void setListaPlantillas(ArrayList<String> listaPlantillas) {
//		this.listaPlantillas = listaPlantillas;
//	}
//
//	public List<String> getListaPaquetes() {
//		return listaPaquetes;
//	}
//
//	public void setListaPlantillas(List<String> listaPlantillas) {
//		this.listaPlantillas = listaPlantillas;
//	}
//
//	public void setListaPaquetes(List<String> listaPaquetes) {
//		this.listaPaquetes = listaPaquetes;
//	}
//
//
//
//}
