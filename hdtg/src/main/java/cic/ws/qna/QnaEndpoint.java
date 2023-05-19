package cic.ws.qna;

import org.jdom2.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

@Endpoint
public class QnaEndpoint {
	private static final String NAMESPACE_URI = "http://cic/qna/schemas";
	private QnaService qnaService;

	@Autowired
	public QnaEndpoint(QnaService qnaService) {
		this.qnaService = qnaService;

	}
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "CicTemplate") 
	public void handleQnaRequest(@RequestPayload Element holidayRequest) {
		qnaService.bookHoliday("userName", "passWord", "name", "fileContent", "fileName");
		
	}
}
