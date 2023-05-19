package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.QuotationItem;
import entity.QuotationItemProcess;

@Repository(value = "quotationItemProcessDao")
public class QuotationItemProcessDao extends H2HBaseDao<QuotationItemProcess> {
	public void save(QuotationItemProcess qip) throws Exception {
		BigDecimal nextAmout = getNextAmount(qip.getQuotationItem(), qip.getType());
		BigDecimal remainAmount = qip.getQuotationItem().getQuality().subtract(nextAmout);
		if (remainAmount.compareTo(qip.getQuality()) < 0)
			throw new ResourceException("Mã quản lý %s, số lượng chuyển %s, vượt quá số lượng còn lại %s",
					new Object[] { qip.getQuotationItem().getManageCode(), FormatNumber.num2Str(qip.getQuality()),
							FormatNumber.num2Str(remainAmount) });
		getCurrentSession().save(qip);

	}

	@Override
	public void del(QuotationItemProcess qip) throws Exception {
		// Xoa so luong da chuyen giam di => Can dam bao so luong da chuyen lon hon so luong da thuc hien
		if ("CL".equals(qip.getType())) {
			// So luong da chuyen
			Long quatity = qip.getQuotationItem().getPolishingQuatity();
			// So luong da thuc hien
			Long done = qip.getQuotationItem().getPolishingDone();
			Long newQuatity = quatity - qip.getQuality().longValue();
			if (newQuatity < done) {
				throw new ResourceException(
						"Thực hiện không thành công: Sau khi xóa, số lượng chuyển nguội %s nhỏ hơn số lượng nguội đã thực hiện %s",
						new Object[] { FormatNumber.num2Str(newQuatity), FormatNumber.num2Str(done) });
			}

		} else if ("QC".equals(qip.getType())) {
			// So luong da chuyen
			Long quatity = qip.getQuotationItem().getQcQuatity();
			// So luong da thuc hien
			Long done = qip.getQuotationItem().getQcDone();
			Long newQuatity = quatity - qip.getQuality().longValue();
			if (newQuatity < done)
				throw new ResourceException(
						"Thực hiện không thành công: Sau khi xóa, số lượng chuyển QC %s nhỏ hơn số lượng QC đã thực hiện %s",
						new Object[] { FormatNumber.num2Str(newQuatity), FormatNumber.num2Str(done) });
			// Da chuyen gia cong ngoai
			if (qip.getQuotationItem().getTotalToOs() != null && qip.getQuotationItem().getTotalToOs() > 0)
				throw new ResourceException(String.format("Chi tiết đã được QC chuyển GCN %s, kg thể xóa",
						FormatNumber.num2Str(qip.getQuotationItem().getTotalToOs())));
		}
		super.del(qip);

	}

	@SuppressWarnings("unchecked")
	/**
	 * Lay so luong da chuyen
	 * 
	 * @param qie
	 * @param type
	 * @return
	 */
	public BigDecimal getNextAmount(QuotationItem qie, String type) {
		List<QuotationItemProcess> lstQiP = getCurrentSession().createCriteria(QuotationItemProcess.class)
				.add(Restrictions.eq("quotationItem.id", qie.getId())).add(Restrictions.eq("type", type)).list();
		BigDecimal nextAmount = null;
		for (QuotationItemProcess item : lstQiP) {
			if (nextAmount == null)
				nextAmount = item.getQuality();
			else
				nextAmount = nextAmount.add(item.getQuality());
		}
		if (nextAmount == null)
			return new BigDecimal(0);
		return nextAmount;

	}
}
