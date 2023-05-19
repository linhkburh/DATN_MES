package cic.h2h.controller.bisAdmin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.UserDocDao;
import cic.h2h.form.UserDocForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.UserDocumentation;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
@Controller
@RequestMapping(value = "/userDoc")
public class UserDocController extends CatalogController<UserDocForm, UserDocumentation>{
	private static Logger lg = LogManager.getLogger(UserDocController.class);
	@Autowired
	private UserDocDao userDocDao;
	@Override
	public void createSearchDAO(HttpServletRequest request, UserDocForm form, BaseDao<UserDocumentation> dao)
			throws Exception {
		if (!Formater.isNull(form.getKeyWord())) {
			dao.addRestriction(Restrictions.or(Restrictions.like("code", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase(),Restrictions.or(
					Restrictions.like("name", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("description", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase())));
		}
	}

	@Override
	protected void pushToJa(JSONArray ja, UserDocumentation e, UserDocForm modelForm) throws Exception {
		if (e.getId() != null || !Formater.isNull(e.getCode()))
			ja.put("<a href = '#' onclick = 'edit(\"" + e.getId() + "\")'>"
					+ e.getCode() + "</a>");
		else
			ja.put("");
		if (!Formater.isNull(e.getName()))
			ja.put(e.getName());
		else
			ja.put("");
		if (!Formater.isNull(e.getDescription()))
			ja.put(e.getDescription());
		else
			ja.put("");
	}

	@Override
	public BaseDao<UserDocumentation> getDao() {
		return userDocDao;
	}

	@Override
	public String getJsp() {
		return "qtht/tai_lieu_hdsd";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, UserDocForm form)
			throws Exception {
		
	}
	public void loadHelp(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, UserDocForm form) throws Exception {
		List<UserDocumentation> lst = userDocDao.getAll();
		returnJson(rs, lst);
	}
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, UserDocForm form) throws Exception {
		if (Formater.isNull(form.getUserDocumentation().getId())) {
			super.save(model, rq, rs, form);
		} else {
			UserDocumentation oldIndB = userDocDao.get(form.getUserDocumentation().getId()); 
			oldIndB.setCode(form.getUserDocumentation().getCode());
			oldIndB.setName(form.getUserDocumentation().getName());
			oldIndB.setDescription(form.getUserDocumentation().getDescription());
			userDocDao.save(oldIndB);
		}
		
	}
	private RightUtils rightUtils;
	@RequestMapping(value = "upload", method = { RequestMethod.POST })
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("id") String id) throws Exception {
//			if (!rightUtils.haveAction(rq, "method=save&saveType=update")
//					&& !rightUtils.haveAction(rq, "method=save&saveType=createNew"))
//				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
			lg.info("start upload fileXmlCfg");
			UserDocumentation obj = userDocDao.get(id);
			obj.setXmlCfgFileName(fileName);
			InputStream fileDocx = inputFile.getInputStream();
			byte[] targetArray = new byte[fileDocx.available()];
			fileDocx.read(targetArray);
			fileDocx.close();
			obj.setXmlModel(targetArray);
	}
	
	public String downloadDoc(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, UserDocForm form) throws FileNotFoundException, IOException, Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		UserDocumentation obj = userDocDao.get(id); 
		if(Formater.isNull(obj.getXmlCfgFileName())) 
			throw new ResourceException("File kh\u00F4ng t\u1ED3n t\u1EA1i!");
		
		rs.addHeader("Content-Disposition", "attachment; filename*=utf-8''" + encodeFileName(obj.getXmlCfgFileName())); 
		rs.getOutputStream().write(obj.getXmlModel());
		return null;
	}
	
	private static String encodeFileName(String fileName) throws Exception {
		  return URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
	}
	@RequestMapping(value = "sendPhotoCap", method = { RequestMethod.POST })
	public void sendPhotoCap(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,@RequestParam("photoData") String photoData,
			@RequestParam("id") String id) throws Exception  {
	      String filename = "captured-photo.png";
	      String directory = "C:\\Users\\Admin\\Desktop\\Document\\";
	      String filepath = directory + filename;
	      byte[] decodedBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(photoData.substring(photoData.indexOf(",")+1));
	      FileOutputStream fos = new FileOutputStream(filepath);
	      UserDocumentation obj = userDocDao.get(id);
	      obj.setXmlCfgFileName(filename);
	      obj.setXmlModel(decodedBytes);
	      fos.write(decodedBytes);
	      fos.flush();
	      fos.close();
	}
}
