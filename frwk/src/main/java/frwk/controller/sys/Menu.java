package frwk.controller.sys;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.util.Formater;
import common.util.JsonUtils;
import entity.frwk.SysMenu;
import entity.frwk.SysObjects;
import frwk.dao.hibernate.sys.SysMenuDao;

@Service(value = "menuService")
public class Menu {
	private static Logger lg = Logger.getLogger(Menu.class);
	private List<SysObjects> userObjects;
	private List<SysMenu> menus;
	private List<SysObjects> sysRights;
	private SysMenu vitualRoot = new SysMenu();
	@Autowired
	private SysMenuDao sysMenuDao;

	public Menu(List<SysObjects> userObjects, List<SysMenu> menus, List<SysObjects> sysRights) {
		this.userObjects = userObjects;
		this.sysRights = sysRights;
		this.menus = menus;
	}

	public String make(List<SysObjects> userObjects, List<SysObjects> sysRights) throws Exception {
		List<SysMenu> menus = sysMenuDao.getAllLeaf();
		return new Menu(userObjects, menus, sysRights).make();
	}

	private String make() throws Exception {
		for (SysMenu leafMenu : menus)
			if (haveMenu(leafMenu))
				vitualRoot.add(leafMenu);
		// Order
		order(vitualRoot);
		return StringEscapeUtils.escapeEcmaScript(JsonUtils.writeToString(vitualRoot));

	}

	private void order(SysMenu menu) {
		if (Formater.isNull(menu.getChildren()))
			return;
		Collections.sort(menu.getChildren(), new Comparator<SysMenu>() {
			@Override
			public int compare(final SysMenu before, final SysMenu after) {
				if (Formater.isNull(before.getDisOrder())) {
					if (!Formater.isNull(after.getDisOrder()))
						return -1;
					else
						return 0;
				}
				if (Formater.isNull(after.getDisOrder()))
					return 1;
				return before.getDisOrder().compareTo(after.getDisOrder());
			}
		});
		for (SysMenu child : menu.getChildren())
			order(child);

	}

	private boolean haveMenu(SysMenu leafMenu) {
		// Kiem tra quyen NSD
		for (SysObjects so : userObjects) {
			if (yesItIs(leafMenu.getAction(), so.getAction()))
				return true;
		}
		// Kiem tra quyen co duoc dinh nghia trong SysRights khong
		if (Formater.isNull(sysRights))
			return true;
		for (SysObjects so : sysRights) {
			// lg.info(String.format("menu.getAction(): %s, so.getAction(): %s", new Object[] {menu.getAction(),
			// so.getAction()}));
			if (yesItIs(leafMenu.getAction(), so.getAction()))
				return false;
		}
		return true;
	}

	private static boolean yesItIs(String sourceAct, String desAction) {
		// Truong hop giong nhau toan bo
		if (sourceAct.equals(desAction))
			return true;
		int idxOfSrcParam = sourceAct.indexOf("?");
		int idxOfDesParam = desAction.indexOf("?");
		if (idxOfDesParam != idxOfSrcParam)
			return false;
		// Return false do 2 uri kg giong nhau, giong nhau thi da roi vao case dau tien roi
		if (idxOfSrcParam < 0)
			return false;
		// URI kg giong nhau
		String sourceUri = sourceAct.substring(0, idxOfSrcParam);
		String desUri = desAction.substring(0, idxOfDesParam);
		if (!desUri.equals(sourceUri))
			return false;

		// Tham so action
		String[] arrSourceActionParam = sourceAct.substring(idxOfSrcParam + 1).split("&");
		String[] arrDesActionParam = desAction.substring(idxOfDesParam + 1).split("&");
		// Khong cung so luong tham so
		if (arrDesActionParam.length != arrSourceActionParam.length)
			return false;

		// So sanh tham so va gia tri
		Map<String, String> mSourceParm = new HashMap<String, String>();
		for (int i = 0; i < arrSourceActionParam.length; i++) {
			String[] temp = arrSourceActionParam[i].split("=");
			mSourceParm.put(temp[0], temp[1]);
		}
		Map<String, String> mDesParm = new HashMap<String, String>();
		for (int i = 0; i < arrDesActionParam.length; i++) {
			String[] temp = arrDesActionParam[i].split("=");
			mDesParm.put(temp[0], temp[1]);
		}

		for (String sourceParam : mSourceParm.keySet()) {
			// Khong trung tham so
			if (!mDesParm.containsKey(sourceParam))
				return false;
			// Khong trung gia tri
			if (!mDesParm.get(sourceParam).equals(mSourceParm.get(sourceParam)))
				return false;
		}
		return true;

	}

}
