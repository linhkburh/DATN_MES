package cic.ws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import cic.ws.model.PHTepBaoCao;
import cic.ws.model.PHTepBaoCaoVanTin;
import cic.ws.model.TepBaoCao;
import cic.ws.model.TepBaoCaoVanTin;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;

public class WsClient extends WebServiceGatewaySupport {
	@Autowired
	private SysParamDao sysParamDao;
	@Value("${H2H_CORE_SVR_ADD}")
	//@Autowired
	//@Qualifier("H2H_CORE_SVR_ADD")
	private String h2hCoreSvrAdd;
	public PHTepBaoCao sendReport(TepBaoCao tepbaocao) throws ResourceException {
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = "http://" + H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/service/report";
		PHTepBaoCao rs = (PHTepBaoCao) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepbaocao);
		//throw new ResourceException(rs.getTTPhanHoi().getMa() + "::" + rs.getTTPhanHoi().getMoTa());
		return rs;

	}

	public PHTepBaoCaoVanTin chkReport(TepBaoCaoVanTin tepbaocao) throws ResourceException {
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = "http://" + H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/service/report";
		PHTepBaoCaoVanTin rs = (PHTepBaoCaoVanTin) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepbaocao);
		//rs.getTepTraLoi().getTTChung().setMoTa("xxxx");
		return rs;

	}

}
