package cic.h2h.controller.mes;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import common.util.Formater;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.frwk.SysDictParam;
import entity.frwk.SysFile;
import frwk.controller.sys.SysFileController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.form.SysFileForm;

@Controller
@RequestMapping(value = "/qiFile")
public class QuotationItemFileController extends SysFileController {
	@Autowired
	private QuotationItemDao quotationItemDao;
	@Autowired
	private QuotationItemExeDao quotationItemExeDao;

	@Override
	protected void pushToJa(JSONArray ja, SysFile e, SysFileForm modelForm) throws Exception {
		ja.put(e.getSysDictParam().getValue());
		ja.put(Formater.date2strDateTime(e.getUploadDate()));
		if (Boolean.TRUE.equals(modelForm.getReadOnly()))
			ja.put(e.getName());
		else
			ja.put("<a href = '#' title='Cập nhật' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getName() + "</a>");
		if(e.getFileSize() != null)
			ja.put(Formater.num2str(e.getFileSize()));
		else
			ja.put("");
		if ("pro".equals(modelForm.getTo())) {
			QuotationItemExe qie = quotationItemExeDao.get(e.getRecordId());
			if (qie != null) {
				if (qie.getExeStepId().getStepType().getLstExeStep().size() == 1)
					ja.put(qie.getExeStepId().getStepType().getDescription());
				else
					ja.put(qie.getExeStepId().getStepType().getDescription() + "-" + qie.getExeStepId().getStepName());
				ja.put(qie.getQuotationItemId().getManageCode());
			} else {
				ja.put("");
				ja.put("");
			}
		} else {
			QuotationItem qi = quotationItemDao.get(e.getRecordId());
			if (qi != null) {
				if (!Formater.isNull(qi.getManageCode()))
					ja.put(qi.getManageCode());
				else
					ja.put("");
			}
		}
		ja.put(e.getSysUsers().getName());
		ja.put("<a href='javascript:;' onclick='downloadFile(\"" + e.getId()
				+ "\")' style='float: left; margin-left: 10;'><i class='fa fa-download'></i></a>");
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, SysFileForm form, BaseDao<SysFile> dao) throws Exception {
		QuotationItem qi = quotationItemDao.get(form.getParentId());
		// File ban ve
		if (qi != null) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1 from QUOTATION_ITEM qi where qi.id = {alias}.RECORD_ID and qi.code = ?)",
					qi.getCode(), new StringType()));
		} else {
			// File chuong trinh
			QuotationItemExe qie = quotationItemExeDao.get(form.getParentId());
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1\r\n" + "          from quotation_item qi, quotation_item_exe qie\r\n"
							+ "         where qie.quotation_item_id = qi.id\r\n"
							+ "           and {alias}.record_id = qie.id\r\n" + "           and qie.exe_step_id = ?\r\n"
							+ "           and qi.code = ?)",
					new Object[] { qie.getExeStepId().getId(), qie.getQuotationItemId().getCode() },
					new Type[] { new StringType(), new StringType() }));
		}
		if (!Formater.isNull(form.getType()))
			dao.addRestriction(Restrictions.eq("sysDictParam.id", form.getType()));
		dao.addOrder(Order.asc("sysDictParam"));
		dao.addOrder(Order.desc("uploadDate"));
	}

	@Override
	public List<SysDictParam> getListFile(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			SysFileForm form) throws Exception {
		if ("drw".equals(form.getTo()))
			return sysDictParamDao.getByType("DOC");
		else
			return sysDictParamDao.getByType("PRO");
	}
}
