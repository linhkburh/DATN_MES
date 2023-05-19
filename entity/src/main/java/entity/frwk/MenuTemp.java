package entity.frwk;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import common.util.Formater;
@Entity
@Table(name = "MENU_TEMP")
public class MenuTemp {
		private String id;
		private MenuTemp parent;
		private String code;
		private String name;
		private String nameEn;
		private String action;
		private Short disOrder;
		private Boolean deActive;
		private List<MenuTemp> children;

		public MenuTemp() {
		}

		public MenuTemp(String id, String code, String name) {
			this.id = id;
			this.code = code;
			this.name = name;
		}

		public MenuTemp(String id, MenuTemp parent, String code, String name, String action, Short disOrder,
				List<MenuTemp> children) {
			this.id = id;
			this.parent = parent;
			this.code = code;
			this.name = name;
			this.action = action;
			this.disOrder = disOrder;
			this.children = children;
		}

		@Id
		@Column(name = "ID", unique = true, nullable = false, length = 40)
		public String getId() {
			return this.id;
		}

		public void setId(String id) {
			this.id = id;
		}

		@ManyToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "PARENT")
		@JsonIgnore
		public MenuTemp getParent() {
			return parent;
		}

		public void setParent(MenuTemp parent) {
			this.parent = parent;
		}

		@Column(name = "CODE", nullable = false, length = 40)
		public String getCode() {
			return this.code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Column(name = "NAME", nullable = false, length = 600)
		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Column(name = "ACTION", length = 300)
		public String getAction() {
			return this.action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		@Column(name = "NAME_EN", nullable = false, length = 300)
		public String getNameEn() {
			return nameEn;
		}

		public void setNameEn(String nameEn) {
			this.nameEn = nameEn;
		}

		@Column(name = "DIS_ORDER", precision = 3, scale = 0)
		public Short getDisOrder() {
			return this.disOrder;
		}

		public void setDisOrder(Short disOrder) {
			this.disOrder = disOrder;
		}
		@Column(name = "DE_ACTIVE")
		public Boolean getDeActive() {
			return deActive;
		}

		public void setDeActive(Boolean deActive) {
			this.deActive = deActive;
		}

		@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
		public List<MenuTemp> getChildren() {
			return children;
		}

		public void setChildren(List<MenuTemp> children) {
			this.children = children;
		}

		private List<MenuTemp> tree = null;

		@Transient
		@JsonIgnore
		public List<MenuTemp> getTree() {
			if (tree != null)
				return tree;
			if (this.getParent() == null)
				return null;
			else {
				tree = new ArrayList<MenuTemp>();
				List<MenuTemp> parentTree = this.getParent().getTree();
				if (parentTree != null)
					tree.addAll(parentTree);
				tree.add(parent);
				return tree;
			}
		}

		@JsonIgnore
		@Transient
		public MenuTemp getRoot() {
			return Formater.isNull(tree) ? this : tree.get(0);
		}

		/**
		 * Add menu item vao menu tree (this-root of tree) <br>
		 * Add ancestor menu vao root, neu ancestor chua ton tai trong tree
		 * 
		 * @param leafMenu leaf menu
		 */
		public void add(MenuTemp leafMenu) {
			// Xac dinh addToItem menu
			MenuTemp addToItem = this;
			if (!Formater.isNull(leafMenu.getTree())) {
				int treeIdx = 0;
				while (true) {
					MenuTemp ancestorMenu = leafMenu.getTree().get(treeIdx);
					Boolean existAncestorMenu = Boolean.FALSE;
					if (!Formater.isNull(addToItem.children)) {
						for (MenuTemp c : addToItem.children) {
							if (c.getId().equals(ancestorMenu.getId())) {
								addToItem = c;
								existAncestorMenu = Boolean.TRUE;
								break;
							}
						}
					}
					if (!existAncestorMenu)
						addToItem = addToItem.addItem(ancestorMenu);
					treeIdx++;
					if (treeIdx == leafMenu.getTree().size())
						break;
				}
			}
			// Add menu vao dung vi tri
			addToItem.addItem(leafMenu);
		}

		private MenuTemp addItem(MenuTemp menu) {
			if (this.children == null)
				this.children = new ArrayList<MenuTemp>();
			MenuTemp nonProxy = nonProxy(menu);
			this.children.add(nonProxy);
			nonProxy.setParent(this);
			return nonProxy;
		}

		private MenuTemp nonProxy(MenuTemp menu) {
			MenuTemp clone = new MenuTemp();
			clone.setId(menu.getId());
			clone.setAction(menu.getAction());
			clone.setCode(menu.getCode());
			clone.setName(menu.getName());
			clone.setDisOrder(menu.getDisOrder());
			clone.setNameEn(menu.getNameEn());
			return clone;
		}
}
