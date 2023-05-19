package cic.h2h.dao.hibernate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.FormatNumber;
import common.util.ResourceException;
import entity.QuotationRepaire;

@Repository(value = "quotationRepaireDao")
public class QuotationRepaireDao extends H2HBaseDao<QuotationRepaire> {
	private static final Logger logger = Logger.getLogger(QuotationRepaireDao.class);
	@Autowired
	private QuotationRepaireDao quotationRepaireDao;

	@Override
	public void save(QuotationRepaire qr) throws Exception {
		long qcRepaire = qr.getQuotationItem().getQcRepaire();
		long repairedAmount = qr.getQuotationItem().getRepairedAmount();
		if (qr.getId() != null) {
			QuotationRepaire qrIndb = quotationRepaireDao.get(qr.getId());
			repairedAmount -= qrIndb.getAmount().longValue();
		}
		repairedAmount += qr.getAmount().longValue();
		if (repairedAmount > qcRepaire)
			throw new ResourceException("Tổng số lượng chi tiết sửa %s lớn hơn số lượng chi tiết lỗi %s",
					new Object[] { FormatNumber.num2Str(repairedAmount), FormatNumber.num2Str(qcRepaire) });
		super.save(qr);
	}
}
