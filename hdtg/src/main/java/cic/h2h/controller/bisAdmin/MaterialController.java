package cic.h2h.controller.bisAdmin;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.MaterialDao;
import cic.h2h.dao.hibernate.MaterialTypeDao;
import cic.h2h.form.MaterialForm;
import cic.utils.JasperUtils;
import common.util.FormatNumber;
import common.util.Formater;
import entity.Material;
import entity.MaterialType;
import entity.frwk.SysDictParam;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Controller
@RequestMapping("/material")
public class MaterialController extends CatalogController<MaterialForm, Material> {

	private static Logger log = Logger.getLogger(MaterialController.class);

	@Autowired
	private MaterialDao materialDao;

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Autowired
	private JasperUtils jasperUtils;

	@Override
	public void createSearchDAO(HttpServletRequest request, MaterialForm form, BaseDao<Material> dao) throws Exception {

		if (!Formater.isNull(form.getCode())) {
			dao.addRestriction(Restrictions.like("code", form.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getName())) {
			dao.addRestriction(Restrictions.like("name", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		dao.addRestriction(Restrictions.isNull("previousVersion"));
		dao.addOrder(Order.asc("name"));
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MaterialForm form)
			throws Exception {
		
		if (form.getMaterial() != null && !Formater.isNull(form.getMaterial().getInitPriceStr()))
			form.getMaterial().setInitPrice(FormatNumber.str2num(form.getMaterial().getInitPriceStr()));
		super.save(model, rq, rs, form);
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MaterialForm form)
			throws Exception {
		String id = rq.getParameter("id");
		Material o = materialDao.getObject(Material.class, id);
		if (o.getInitPrice() != null)
			o.setInitPriceStr(FormatNumber.num2Str(o.getInitPrice()));
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(o));
		out.print(jsonObject);
		out.flush();
		out.close();
	}

	@Override
	protected void pushToJa(JSONArray ja, Material e, MaterialForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		if (e.getMaterialType() != null)
			ja.put(e.getMaterialType().getName());
		else
			ja.put("");
		ja.put(Formater.num2str(e.getDensity()));
		if (e.getInitPrice() != null)
			ja.put(FormatNumber.num2Str(e.getInitPrice()) + " (" + e.getCurrency().getCode() + ")");

		else
			ja.put("");
		if (e.getUnit() != null)
			ja.put(e.getUnit().getValue());
		else
			ja.put("");
		if (!Formater.isNull(e.getDescription()))
			ja.put(e.getDescription());
		else
			ja.put("");
	}

	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public String export(ModelMap model, HttpServletRequest request, HttpServletResponse response, MaterialForm form) {
//		Session session = materialDao.openNewSession();
//		SessionImpl sessionImpl = (SessionImpl) session;
//		Connection connection = sessionImpl.connection();
//		try {
//			List<Material> list = materialDao.getAll(Material.class);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("jrDataSource", list);
//			InputStream result = jasperUtils.createReport("quotaition_item", map, Constants.Excel, "quotaition_item", connection,"sub_exe");
//			response.setContentType("application/octet-stream");
//			response.addHeader("Content-Disposition", "attachment; filename=quotaition_item.xls");
//			OutputStream responseOutputStream = response.getOutputStream();
//			int bytes;
//			while ((bytes = result.read()) != -1) {
//				responseOutputStream.write(bytes);
//			}
//			result.close();
//			responseOutputStream.flush();
//			responseOutputStream.close();
//		} catch (Exception e) {
//			// TODO: handle exception
//			log.error("export",e);
//		} finally {
//			// TODO: handle finally clause
//			DataSourceConfiguration.releaseSqlResources(null, null, null, connection);
//			session.close();
//		}

		return "material/material";
	}

	@Override
	public BaseDao<Material> getDao() {
		
		return materialDao;
	}

	@Override
	public String getJsp() {
		
		return "quan_tri_nghiep_vu/vat_lieu";
	}

	@Autowired
	private MaterialTypeDao materialTypeDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MaterialForm form)
			throws Exception {
		List<SysDictParam> units = sysDictParamDao.getByType("MEASR");
		model.addAttribute("units", units);
		List<MaterialType> materialTypes = materialTypeDao.getAll();
		model.addAttribute("materialTypes", materialTypes);
		// Loai tien vat lieu USD
		SysDictParam usd = sysDictParamDao.getByTypeAndCode(Constants.CAT_TYPE_CURRENCY, "USD");
		List<SysDictParam> lstCurrency = new ArrayList<SysDictParam>();
		lstCurrency.add(usd);
		rq.setAttribute("lstCurrency", lstCurrency);
	}

}
