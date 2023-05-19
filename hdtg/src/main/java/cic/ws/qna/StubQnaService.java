package cic.ws.qna;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
@Service
public class StubQnaService implements QnaService {
	private static Logger logger = Logger.getLogger(StubQnaService.class);
	@Override
	public void bookHoliday(String userName, String passWord, String name, String fileContent, String fileName) {
		logger.info(String.format("userName:, passWord:, name:, fileContent:, fileName:",
				new Object[] { userName, passWord, name, fileContent, fileName }));
	}
 
}
