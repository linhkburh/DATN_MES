package frwk.controller.sys;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.frwk.SysDictParam;
import entity.frwk.SysFile;
import frwk.bean.FTPUtils;
import frwk.bean.FtpInf;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysFileDao;
import frwk.form.SysFileForm;

public abstract class SysFileController extends CatalogController<SysFileForm, SysFile> {

	static Logger lg = Logger.getLogger(SysFileController.class);

	@Autowired
	private SysFileDao sysFileDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, SysFileForm form, BaseDao<SysFile> dao) throws Exception {

		if (!Formater.isNull(form.getParentId())) {
			dao.addRestriction(Restrictions.eq("recordId", form.getParentId()));
//			QuotationItem item = quotationItemDao.get(form.getParentId());
//			if(item != null) {
//				List<String> ids = quotationItemDao.getLstIdByManagerCode(item.getCode());
//				if (!Formater.isNull(ids))
//					dao.addRestriction(Restrictions.in("recordId", ids));
//			}else {
//				dao.addRestriction(Restrictions.eq("recordId", form.getParentId()));
//			}

		}
//		if(!Formater.isNull(form.getLstType()))
//			dao.addRestriction(Restrictions.in("sysDictParam", form.getLstType()));
		dao.addOrder(Order.asc("uploadDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, SysFile e, SysFileForm modelForm) throws Exception {

		ja.put("<a href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getName() + "</a>");
		ja.put("item.getManageCode()");
		ja.put(e.getSysUsers().getName());
		ja.put(Formater.date2strDateTime(e.getUploadDate()));
		ja.put(e.getSysDictParam().getCode() + "-" + e.getSysDictParam().getValue());
		ja.put("<a href='javascript:;' onclick='downloadFile(\"" + e.getId()
				+ "\")' style='float: left; margin-left: 10;'><i class='fa fa-download'></i></a>");
	}

	@Override
	public BaseDao<SysFile> getDao() {

		return sysFileDao;
	}

	@Override
	public String getJsp() {

		return "qtht/danh_muc_danh_sach_file";
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysFileForm form) throws Exception {

		super.save(model, rq, rs, form);
	}

	@Autowired
	private FTPUtils ftpUtils;

	@RequestMapping(value = "upload", method = { RequestMethod.POST })
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("type") String type, @RequestParam("parentId") String parentId,
			@RequestParam("descripton") String descripton) throws Exception {
		SysFile sysFile = null;
		if (Formater.isNull(parentId))
			throw new ResourceException("Dữ liệu nghiệp vụ chưa tồn tại!");
		String id = rq.getParameter("id");
		if (!Formater.isNull(id)) {
			sysFile = sysFileDao.get(id);
			sysFile.setSysDictParam(new SysDictParam(type));
			sysFile.setModifiedBy(getSessionUser());
			sysFile.setDescripton(descripton);
			sysFile.setModifiedDate(Calendar.getInstance().getTime());
			sysFile.setName(fileName);
			Double fileSize = (double) inputFile.getSize()/ (1024 * 1024);
			sysFile.setFileSize(fileSize);
		} else {
			sysFile = new SysFile();
			sysFile.setSysDictParam(new SysDictParam(type));
			sysFile.setDescripton(descripton);
			sysFile.setName(fileName);
			sysFile.setRecordId(parentId);
			sysFile.setSysUsers(getSessionUser());
			sysFile.setUploadDate(Calendar.getInstance().getTime());
			Double fileSize = (double) inputFile.getSize()/ (1024 * 1024);
			sysFile.setFileSize(fileSize);
		}
		sysFileDao.save(sysFile);
		ftpUtils.storeFile(ftpInf, inputFile.getInputStream(), sysFile.getId());
	}

	@Autowired
	private FtpInf ftpInf;

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysFileForm form)
			throws Exception {
		List<SysDictParam> lstFile = getListFile(model, rq, rs, form);
		model.addAttribute("lstFile", lstFile);
		model.addAttribute("lstFileJson",
				StringEscapeUtils.escapeEcmaScript(JsonUtils.writeToString(Arrays.asList(lstFile))));
	}

	public void download(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysFileForm form)
			throws FileNotFoundException, IOException, Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");

		OutputStream responseOutputStream = rs.getOutputStream();
		if (Formater.isNull(id))
			throw new ResourceException("Cần lưu thông tin chương trình trước khi upload file");
		SysFile sysFile = sysFileDao.get(id);
		if (sysFile == null)
			throw new ResourceException("Không tồn tại file trên hệ thống");
		if (sysFile.getName().endsWith(".pdf"))
			rs.addHeader("Content-Disposition",
					"inline; filename*=utf-8''" + Formater.encodeFileName(sysFile.getName()));
		else
			rs.addHeader("Content-Disposition",
					"attachment; filename*=utf-8''" + Formater.encodeFileName(sysFile.getName()));
		ftpUtils.downloadFile(ftpInf, responseOutputStream, sysFile.getId());
	}

	public void loadFiles(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysFileForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
//		QuotationItemExe itemExe = quotationItemExeDao.get(id);
//		List<SysFile> files = new ArrayList<SysFile>();
//		if (itemExe != null) {
//			QuotationItem item = quotationItemDao.get(itemExe.getQuotationItemId().getId());
//			List<String> quotationItemIds = quotationItemDao.getLstIdByManagerCode(item.getCode());
//			List<String> ids = quotationItemExeDao.getItemExeIds(quotationItemIds, itemExe.getExeStepId().getId());
//			if (!Formater.isNull(ids)) {
//				files = sysFileDao.getFileByRecordIds(ids);
//			}
//		}
//		if (!Formater.isNull(files)) {
//			for (SysFile file : files) {
//				QuotationItemExe item = quotationItemExeDao.get(file.getRecordId());
//				if (item == null)
//					continue;
//				file.setOwner(item.getQuotationItemId().getManageCode());
//			}
//		}
		List<SysFile> files = sysFileDao.getAll();
		returnJson(rs, files);
		sysFileDao.clear();
	}

	public abstract List<SysDictParam> getListFile(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			SysFileForm form) throws Exception;

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysFileForm form) throws Exception {
		super.del(model, rq, rs, form);
		ftpUtils.removeFile(ftpInf, form.getModel().getId());

	}
}
