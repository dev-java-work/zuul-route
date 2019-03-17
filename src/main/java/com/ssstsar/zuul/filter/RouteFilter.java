package com.ssstsar.zuul.filter;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
 
@Component
public class RouteFilter extends ZuulFilter {
	private static Logger log = LoggerFactory.getLogger(RouteFilter.class);
 
	@Override
	public String filterType() {
		return "route";
	}
 
	@Override
	public int filterOrder() {
		return 1;
	}
 
	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String requestURL = ctx.getRequest().getRequestURL().toString();
		 String method = ctx.getRequest().getMethod();
		if("GET".equalsIgnoreCase(method))
			return false;
		return !(requestURL.contains("proxyurl") || requestURL.contains("/admin/"));
	}
 
	@Override
	public Object run() {
		try {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();	
		InputStream body = request.getInputStream();
		String theString = IOUtils.toString(body);
		if(theString.contains("")) {
		    Map<String, List<String>> newParameterMap = new HashMap<>();
		    Map<String, String[]> parameterMap = context.getRequest().getParameterMap();
		    //getting the current parameter
		    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
		      String key = entry.getKey();
		      String[] values = entry.getValue();
		      newParameterMap.put(key, Arrays.asList(values));
		    }
		    //add a new parameter
		    String authenticatedKey = "method";
		    String authenticatedValue = "true";
		    newParameterMap.put(authenticatedKey,Arrays.asList(authenticatedValue));
		    context.setRequestQueryParams(newParameterMap);
		}
		log.error(theString);
		log.error("RouteFilter: " + String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		
		return null;
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
