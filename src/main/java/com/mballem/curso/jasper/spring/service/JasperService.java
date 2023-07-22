package com.mballem.curso.jasper.spring.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.HtmlResourceHandler;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.j2ee.servlets.BaseHttpServlet;
import net.sf.jasperreports.web.util.WebHtmlResourceHandler;

@Service
public class JasperService {

	private static final String JASPER_DIRETORIO = "classpath:jasper/";
	private static final String JASPER_PREFIXO = "funcionarios-";
	private static final String JASPER_SUFIXO = ".jasper";

	@Autowired
	private Connection connection;

	private Map<String, Object> params = new HashMap<>();

	public JasperService() {
		this.addParams("IMAGEM_DIRETORIO", JASPER_DIRETORIO);
	}

	public void addParams(String key, Object value) {
		params.put(key, value);
	}

	public byte[] exportarPDF(String code) {
		byte[] bytes = null;
		try {
			File file = ResourceUtils
					.getFile(JASPER_DIRETORIO.concat(JASPER_PREFIXO).concat(code).concat(JASPER_SUFIXO));
			JasperPrint print = JasperFillManager.fillReport(file.getAbsolutePath(), params, connection);
			bytes = JasperExportManager.exportReportToPdf(print);
		} catch (FileNotFoundException | JRException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	public HtmlExporter exportarHtml(String code, HttpServletRequest request, HttpServletResponse response) {
		
		HtmlExporter htmlExporter = null;
	
		try {
			File file = ResourceUtils
					.getFile(JASPER_DIRETORIO.concat(JASPER_PREFIXO).concat(code).concat(JASPER_SUFIXO));
			JasperPrint print = JasperFillManager.fillReport(file.getAbsolutePath(), params, connection);
			htmlExporter = new HtmlExporter();
			htmlExporter.setExporterInput(new SimpleExporterInput(print));
			htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getWriter()));
			
			SimpleHtmlExporterOutput output = new SimpleHtmlExporterOutput(response.getWriter());
			
			HtmlResourceHandler resourceHandler = new WebHtmlResourceHandler(request.getContextPath() + 
					"/image/servlet?image={0}");
			output.setImageHandler(resourceHandler);
			htmlExporter.setExporterOutput(output);
			
			request.getSession().setAttribute(BaseHttpServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, print);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlExporter;
	}

}