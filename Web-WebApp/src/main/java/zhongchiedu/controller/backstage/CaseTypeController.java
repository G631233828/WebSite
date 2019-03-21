package zhongchiedu.controller.backstage;

import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
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
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.service.CaseTypeService;

@Controller
public class CaseTypeController {

	private static final Logger log = LoggerFactory.getLogger(CaseTypeController.class);

	@Autowired
	private CaseTypeService caseTypeService;

	@RequestMapping("/findcaseType")
	@RequiresPermissions(value = "caseType:list")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

		Pagination<CaseType> pagination;
		try {
			pagination = this.caseTypeService.findPaginationByQuery(new Query(), pageNo, pageSize, CaseType.class);
			if (Common.isEmpty(pagination))
				pagination = new Pagination<>();
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询案例分类失败" + e.toString());
			e.printStackTrace();
		}
		return "admin/caseType/list";
	}

	@GetMapping("/caseType")
	public String topage() {

		return "admin/caseType/add";
	}

	@PostMapping("/caseType")
	@RequiresPermissions(value = "caseType:add")
	@SystemControllerLog(description = "添加分类")
	public String add(@ModelAttribute("caseType") CaseType caseType) {
		this.caseTypeService.saveOrUpdate(caseType);
		return "redirect:findcaseType";
	}
	
	
	@PutMapping("/caseType")
	@RequiresPermissions(value = "caseType:edit")
	@SystemControllerLog(description = "修改分类")
	public String edit(@ModelAttribute("caseType") CaseType caseType) {
		if(Common.isEmpty(caseType.getId())){
			caseType.setId(null);
		}
		this.caseTypeService.saveOrUpdate(caseType);
		return "redirect:findcaseType";
	}
	
	
	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/caseType{id}")
	public String toeditPage(@PathVariable String id, Model model) {
		CaseType caseType = this.caseTypeService.findOneById(id, CaseType.class);
		model.addAttribute("caseType", caseType);
		return "admin/caseType/add";

	}
	
	
	
	@RequestMapping(value = "/caseType/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult disable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.caseTypeService.disable(id);
	}

	
	@DeleteMapping("/caseType/{id}")
	@RequiresPermissions(value = "caseType:delete")
	@SystemControllerLog(description = "删除分类")
	public String todelete(@PathVariable String id) {
		
		List ids = Arrays.asList(id.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		this.caseTypeService.remove(CaseType.class, query);
		return "redirect:/findcaseType";
	}

	
	
	
	
	

}
