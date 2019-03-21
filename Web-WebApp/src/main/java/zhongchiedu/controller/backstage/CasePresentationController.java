package zhongchiedu.controller.backstage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.service.CasePresentationService;
import zhongchiedu.website.service.CaseTypeService;

@Controller
public class CasePresentationController {

	private static final Logger log = LoggerFactory.getLogger(CasePresentationController.class);

	@Autowired
	private CasePresentationService casePresentationService;

	@Autowired
	private CaseTypeService caseTypeService;

	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;

	@RequestMapping("/findcasePresentation")
	@RequiresPermissions(value = "casePresentation:list")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

		Pagination<CasePresentation> pagination;
		try {
			pagination = this.casePresentationService.findPaginationByQuery(new Query(), pageNo, pageSize,
					CasePresentation.class);
			if (Common.isEmpty(pagination))
				pagination = new Pagination<>();
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询案例失败" + e.toString());
			e.printStackTrace();
		}
		return "admin/casePresentation/list";
	}

	@GetMapping("/casePresentation")
	public String topage(Model model) {
		// 查询所有的案例分类
		List<CaseType> list = this.caseTypeService.listCaseTypes();
		model.addAttribute("types", list);
		return "admin/casePresentation/add";
	}

	@PostMapping("/casePresentation")
	@RequiresPermissions(value = "casePresentation:add")
	@SystemControllerLog(description = "添加案例")
	public String add(@ModelAttribute("casePresentation") CasePresentation casePresentation, String editorValue,
			String types, MultipartFile[] filemedia, String oldMedia) {
		this.casePresentationService.saveOrUpdate(casePresentation, filemedia, oldMedia, imgPath, dir, editorValue,
				types);
		return "redirect:findcasePresentation";
	}

	@PutMapping("/casePresentation")
	@RequiresPermissions(value = "casePresentation:edit")
	@SystemControllerLog(description = "修改案例")
	public String edit(@ModelAttribute("casePresentation") CasePresentation casePresentation, String editorValue,
			String types, MultipartFile[] filemedia, String oldMedia) {
		if (Common.isEmpty(casePresentation.getId())) {
			casePresentation.setId(null);
		}
		this.casePresentationService.saveOrUpdate(casePresentation, filemedia, oldMedia, imgPath, dir, editorValue,
				types);
		return "redirect:findcasePresentation";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/casePresentation{id}")
	public String toeditPage(@PathVariable String id, Model model) {

		CasePresentation casePresentation = this.casePresentationService.findOneById(id, CasePresentation.class);
		Object[] caseTypes = {};
		List<CaseType> listc = casePresentation.getCaseTypes();
		if (Common.isNotEmpty(listc)) {
			List<String> lists = new ArrayList<>();
			for (int i = 0; i < listc.size(); i++) {
				lists.add(listc.get(i).getId());
			}
			caseTypes = lists.toArray();
			model.addAttribute("caseTypes", caseTypes);
		}
		model.addAttribute("casePresentation", casePresentation);
		// 查询所有的案例分类
		List<CaseType> list = this.caseTypeService.listCaseTypes();
		model.addAttribute("types", list);
		return "admin/casePresentation/add";

	}

	@RequestMapping(value = "/casePresentation/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult disable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.casePresentationService.disable(id);
	}
	
	
	
	@DeleteMapping("/casePresentation/{id}")
	@RequiresPermissions(value = "casePresentation:delete")
	@SystemControllerLog(description = "删除案例")
	public String todelete(@PathVariable String id) {
		List ids = Arrays.asList(id.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		this.casePresentationService.remove(CasePresentation.class, query);
		return "redirect:/findcasePresentation";
	}

	@RequestMapping(value = "/casePresentation/toTop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult toTop(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.casePresentationService.toTop(id);
	}

}
