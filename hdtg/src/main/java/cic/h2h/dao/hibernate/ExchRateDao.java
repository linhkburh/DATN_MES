package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.ExchRate;
import entity.frwk.SysDictParam;
import frwk.constants.Constants;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Repository("exchRateDao")
public class ExchRateDao extends H2HBaseDao<ExchRate> {
	public BigDecimal getRate(SysDictParam currency, Date exchangeDate) {
		ExchRate exchRate = getByCurrencyAndDate(currency, exchangeDate);
		if (exchRate == null)
			return null;
		return exchRate.getExchRate();
	}

	@SuppressWarnings("unchecked")
	public ExchRate getByCurrencyAndDate(SysDictParam currency, Date exchangeDate) {
		if (currency == null || Formater.isNull(currency.getValue()))
			return null;
		List<ExchRate> lstTemp = getCurrentSession().createCriteria(ExchRate.class).add(Restrictions.eq("currency", currency))
				.add(Restrictions.le("exchDate", exchangeDate)).addOrder(Order.desc("exchDate")).setMaxResults(1)
				.list();
		if (lstTemp.size() == 0)
			return null;
		return (ExchRate) lstTemp.get(0);
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;
	public ExchRate getByCurrencyAndDate(String currency_code, Date exchangeDate) {
		if (Formater.isNull(currency_code))
			return null;
		SysDictParam currency = sysDictParamDao.getByTypeAndCode(Constants.CAT_TYPE_CURRENCY, currency_code);
		return getByCurrencyAndDate(currency, exchangeDate);
	}

}
