package com.mballem.curso.jasper.spring.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class ConfiguracaoGenerica {

	@Bean
	public Connection connection(DataSource dataSource) throws SQLException {
		return dataSource.getConnection();
	}

	@Bean
	public ServletRegistrationBean<ImageServlet> imageServelet() {
		ServletRegistrationBean<ImageServlet> servlet = new ServletRegistrationBean<>(new ImageServlet(),
				"/image/servlet/*");
		servlet.setLoadOnStartup(1);
		return servlet;
	}

}