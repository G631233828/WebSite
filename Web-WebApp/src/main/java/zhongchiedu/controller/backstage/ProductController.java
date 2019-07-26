package zhongchiedu.controller.backstage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import lombok.extern.slf4j.Slf4j;
import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.pojo.Product;
import zhongchiedu.website.service.CaseTypeService;
import zhongchiedu.website.service.ProductService;

@Controller
@Slf4j
public class ProductController {
	@Autowired
	private ProductService productService;

	@Autowired
	private CaseTypeService caseTypeService;

	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;

	@RequestMapping("/findproduct")
	@RequiresPermissions(value = "product:list")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) {

		Pagination<Product> pagination;
		try {
			pagination = this.productService.findPaginationByQuery(new Query(), pageNo, pageSize, Product.class);
			if (Common.isEmpty(pagination))
				pagination = new Pagination<>();
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询产品失败" + e.toString());
			e.printStackTrace();
		}
		return "admin/product/list";
	}

	@GetMapping("/product")
	public String topage(Model model) {
		// 查询所有的案例分类
		List<CaseType> list = this.caseTypeService.listCaseTypesByProduct();
		model.addAttribute("types", list);
		return "admin/product/add";
	}

	@PostMapping("/product")
	@RequiresPermissions(value = "product:add")
	@SystemControllerLog(description = "添加产品")
	public String add(@ModelAttribute("product") Product product, String editorValue, String types,
			MultipartFile[] filemedia, String oldMedia) {
		this.productService.saveOrUpdate(product, filemedia, oldMedia, imgPath, dir, editorValue, types);
		return "redirect:findproduct";
	}

	@PutMapping("/product")
	@RequiresPermissions(value = "product:edit")
	@SystemControllerLog(description = "修改产品")
	public String edit(@ModelAttribute("product") Product product, String editorValue, String types,
			MultipartFile[] filemedia, String oldMedia) {
		if (Common.isEmpty(product.getId())) {
			product.setId(null);
		}
		this.productService.saveOrUpdate(product, filemedia, oldMedia, imgPath, dir, editorValue, types);
		return "redirect:findproduct";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/product{id}")
	public String toeditPage(@PathVariable String id, Model model) {

		Product product = this.productService.findOneById(id, Product.class);
		Object[] caseTypes = {};
		List<CaseType> listc = product.getCaseTypes();
		if (Common.isNotEmpty(listc)) {
			List<String> lists = new ArrayList<>();
			for (int i = 0; i < listc.size(); i++) {
				lists.add(listc.get(i).getId());
			}
			caseTypes = lists.toArray();
			model.addAttribute("caseTypes", caseTypes);
		}
		model.addAttribute("product", product);
		// 查询所有的案例分类
		List<CaseType> list = this.caseTypeService.listCaseTypesByProduct();
		model.addAttribute("types", list);
		return "admin/product/add";

	}

	@RequestMapping(value = "/product/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult disable(@RequestParam(value = "id", defaultValue = "") String id) {
		return this.productService.disable(id);
	}

	@DeleteMapping("/product/{id}")
	@RequiresPermissions(value = "product:delete")
	@SystemControllerLog(description = "删除产品")
	public String todelete(@PathVariable String id) {
		List ids = Arrays.asList(id.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		this.productService.remove(Product.class, query);
		return "redirect:/findproduct";
	}

}
