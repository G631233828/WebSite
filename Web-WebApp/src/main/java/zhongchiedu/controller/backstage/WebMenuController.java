package zhongchiedu.controller.backstage;


import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.WebMenu;
import zhongchiedu.website.service.impl.WebMenuServiceImpl;

@Controller
public class WebMenuController {

	private static final Logger log = LoggerFactory.getLogger(WebMenu.class);

	@Autowired
	private WebMenuServiceImpl webMenuService;

	@GetMapping("webmenus")
	@RequiresPermissions(value = "webmenu:list")
	@SystemControllerLog(description = "查询前台资源")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "9999") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<WebMenu> pagination;
		try {
			pagination = webMenuService.findPaginationByQuery(new Query(), pageNo, pageSize, WebMenu.class);
			if (pagination == null)
				pagination = new Pagination<WebMenu>();

			model.addAttribute("pageList", pagination);

		} catch (Exception e) {
			log.info("查询所有菜单失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "admin/webmenu/list";
	}

	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/webmenu")
	public String addWebMenuPage(Model model) {

		return "admin/webmenu/add";
	}

	@PostMapping("/webmenu")
	@RequiresPermissions(value = "webmenu:add")
	@SystemControllerLog(description = "添加资源")
	public String addResource(@ModelAttribute("webmenu") WebMenu webmenu,HttpSession session) {

		if (webmenu.getType().equals("1")) {
			webmenu.setParentId("0");
		}
		this.webMenuService.saveOrUpdateUser(webmenu,session);
		return "redirect:webmenus";
	}

	
	@PutMapping("/webmenu")
	@RequiresPermissions(value = "webmenu:edit")
	@SystemControllerLog(description = "修改资源")
	public String editResource(@ModelAttribute("webmenu") WebMenu webmenu,HttpSession session) {

		if (webmenu.getType().equals("1")) {
			webmenu.setParentId("0");
		}
		this.webMenuService.saveOrUpdateUser(webmenu,session);
		return "redirect:webmenus";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/webmenu{id}")
	public String toeditPage(@PathVariable String id, Model model, HttpSession session) {

		WebMenu webmenu = this.webMenuService.findOneById(id, WebMenu.class);

		model.addAttribute("webmenu", webmenu);
		if (webmenu.getType()!="1") {
			// 获取资源的type 查询该type的所有菜单
			String type = webmenu.getType().equals("2")?"1":"2";
			List<WebMenu> list = this.webMenuService.findWebMenuByType(type, session);

			model.addAttribute("listmenus", list);
		}

		return "admin/webmenu/add";

	}

	/**
	 * 根据选择的目录获取菜单
	 * 
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/getwebmenu", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult getparentmenu(@RequestParam(value = "type", defaultValue = "") String type,
			HttpSession session) {

		List<WebMenu> list = this.webMenuService.findWebMenuByType(type, session);

		return list != null ? BasicDataResult.build(200, "success", list) : BasicDataResult.build(400, "error", null);
	}

	@DeleteMapping("/webmenu/{id}")
	@RequiresPermissions(value = "webmenu:delete")
	@SystemControllerLog(description = "删除菜单")
	public String delete(@PathVariable String id) {

		String[] strids = id.split(",");
		for (String delids : strids) {
			log.info("删除菜单---》" + delids);
			WebMenu rm = this.webMenuService.findOneById(delids, WebMenu.class);
			this.webMenuService.remove(rm);// 删除某个id
		}

		return "redirect:/webmenus";
	}

	@RequestMapping(value = "/webmenu/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult webMenuDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.webMenuService.webMenuDisable(id);

	}

}
