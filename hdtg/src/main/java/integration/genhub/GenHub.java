package integration.genhub;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;

public class GenHub {
	private static final Logger lg = LogManager.getLogger(GenHub.class);
	private GenFileEndpoint service;
	// Url wsdl genhub service
	private String url;

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Tham so dia chi GenHub
	 */
	private String genHubSvrAddParamKey = null;

	public void setGenHubSvrAddParamKey(String genHubSvrAddParamKey) {
		this.genHubSvrAddParamKey = genHubSvrAddParamKey;
	}

	/**
	 * Ma ung dung
	 */
	private String appCodeParamKey = null;

	public void setAppCodeParamKey(String appCodeParamKey) {
		this.appCodeParamKey = appCodeParamKey;
	}

	@Autowired
	private SysParamDao sysParamDao;
	private String genHubSvrAdd, appCode;

	@PostConstruct
	private void init() throws MalformedURLException {
		// appCode
		SysParam appCodeParam = sysParamDao.getSysParamByCode(appCodeParamKey);
		appCode = appCodeParam.getValue().trim();
		// Service Address
		SysParam genHubSvrAddParam = sysParamDao.getSysParamByCode(genHubSvrAddParamKey);
		genHubSvrAdd = genHubSvrAddParam.getValue().trim();
		// service
		URL url = GenFileService.class.getClassLoader().getResource("");
		URL wsdlLocation = new URL(url, this.url);
		service = new GenFileService(wsdlLocation).getGenFileEndpointPort();
		BindingProvider bp = (BindingProvider) service;
		// Service Address
		bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://localhost:8080/genhub/GenFileService");
		//bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, "http://mes.cnctech.vn/genhub/GenFileService");
		//bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, genHubSvrAdd);
		// Http header
		Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
		requestHeaders.put(appCodeParamKey, Arrays.asList(appCode));
		bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
	}

	public GenHubFile genFile(String template_code, String data, Integer format, Double incFontSize) throws Exception {		
		GenHubFile file = service.genFile(template_code, data, format, incFontSize == null ? new Integer(0) : incFontSize);
		if (!Formater.isNull(file.getErrorCode()))
			throw new ResourceException(file.getErrorCode() + "-" + file.getErrorDes());
		return file;
	}

}
