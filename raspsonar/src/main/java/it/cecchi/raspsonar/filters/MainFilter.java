package it.cecchi.raspsonar.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class MainFilter implements Filter {
	
	public void destroy() {	

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		System.out.println("test filter");
		doFilter(arg0, arg1, arg2);
	}

	public void init(FilterConfig arg0) throws ServletException {

	}
}
