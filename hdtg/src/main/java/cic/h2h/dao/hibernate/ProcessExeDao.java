package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.ProcessExe;
import entity.frwk.UserLog;

@Repository(value = "processExeDao")
public class ProcessExeDao extends H2HBaseDao<ProcessExe> {

	@SuppressWarnings("unchecked")
	public BigDecimal getAllAmount(String quotationItemProcessId, String processExeId) {
		List<ProcessExe> lstQiP = getCurrentSession().createCriteria(ProcessExe.class)
				.add(Restrictions.eq("quotationItem.id", quotationItemProcessId)).list();
		BigDecimal nextAmount = null;
		for (ProcessExe item : lstQiP) {
			if (processExeId.equals(item.getId()))
				continue;
			if (nextAmount == null)
				nextAmount = item.getTotalAmount();
			else
				nextAmount = nextAmount.add(item.getTotalAmount());
		}
		if (nextAmount == null)
			nextAmount = new BigDecimal("0");
		return nextAmount;
	}

	@Override
	public void save(ProcessExe processExe) throws Exception {
		ProcessExe oldDb = null;
		if (!Formater.isNull(processExe.getId()))
			oldDb = getPoJo(processExe.getId());
		// Nguoi sua NG, QC kg co case nay
		if (Boolean.TRUE.equals(processExe.getNgRepaire())) {
			processExe.setNgAmount(null);
			Long totalNgAmount = processExe.getQuotationItem().getPolishingRepaire();
			Long totalrepairedAmount = processExe.getQuotationItem().getPolishingRepaireDone();
			if (!Formater.isNull(processExe.getId()))
				totalrepairedAmount -= oldDb.getAmount().longValue();
			totalNgAmount = totalNgAmount - totalrepairedAmount;
			if (totalNgAmount < processExe.getTotalAmount().longValue()) {
				throw new ResourceException("Số lượng thực hiện %s, vượt quá số lượng sửa còn lại %s",
						new Object[] { processExe.getTotalAmountStr(), FormatNumber.num2Str(totalNgAmount) });
			}
		} else {
			Long todoAmount = "QC".equals(processExe.getType()) ? processExe.getQuotationItem().getQcQuatity()
					: processExe.getQuotationItem().getPolishingQuatity();
			Long doneAmount = "QC".equals(processExe.getType()) ? processExe.getQuotationItem().getQcDone()
					: processExe.getQuotationItem().getPolishingDone();
			if (!Formater.isNull(processExe.getId()))
				doneAmount -= oldDb.getTotalAmount().longValue();
			todoAmount = todoAmount - doneAmount;
			if (todoAmount < processExe.getTotalAmount().longValue())
				throw new ResourceException("Số lượng thực hiện %s, vượt quá số lượng còn lại %s", new Object[] {
						FormatNumber.num2Str(processExe.getTotalAmount()), FormatNumber.num2Str(todoAmount) });

		}
		super.save(processExe);
	}

	@Override
	public void del(ProcessExe pexe) throws Exception {
		// Validate so luong repare voi so luong da repaired
		if ("QC".equals(pexe.getType())) {
			BigDecimal rpAmout = pexe.getNgAmount();
			if (rpAmout != null) {
				Long lRpAmount = rpAmout.longValue();
				if (lRpAmount > 0) {
					// So luong repaire giam
					lRpAmount = pexe.getQuotationItem().getQcRepaire() - lRpAmount;
					Long repairedAmount = pexe.getQuotationItem().getRepairedAmount();
					if (repairedAmount != null && lRpAmount < repairedAmount)
						throw new ResourceException(
								"Sau khi xóa, số lượng chi tiết cần sửa %s, nhỏ hơn số lượng sản xuất đã sửa %s",
								new Object[] { FormatNumber.num2Str(lRpAmount), FormatNumber.num2Str(repairedAmount) });
				}
			}
		}
		super.del(pexe);

	}

	private static final Logger logger = Logger.getLogger(ProcessExeDao.class);
}
