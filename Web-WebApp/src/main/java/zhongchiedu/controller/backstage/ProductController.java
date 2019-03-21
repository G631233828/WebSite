package zhongchiedu.controller.backstage;




import javax.servlet.http.HttpSession;

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
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.Products;
import zhongchiedu.website.service.impl.ProductsServiceImpl;



@Controller
public class ProductController {
	
	private static final Logger log = LoggerFactory.getLogger(ProductController.class);

	
	@Value("${upload-imgpath}")
	private String imgPath;
	
	@Value("${upload-dir}")
	private String dir;
	
	@Value("${video.savePath}")
	private String videoPath;
	
	@Autowired
	private ProductsServiceImpl productsService;
    
	@Autowired
	private MultiMediaServiceImpl multiMediaService;
	
	@GetMapping("/findproducts")
//	@RequiresPermissions(value = "findproducts:list")
	@SystemControllerLog(description = "查询所有产品信息")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {

		User user = (User) session.getAttribute(Contents.USER_SESSION);
		// 分页查询数据
		Pagination<Products> pagination;
		try {
			Query query = new Query();
			pagination = productsService.findPaginationByQuery(new Query(), pageNo, pageSize, Products.class);
			if (pagination == null)
				pagination = new Pagination<Products>();
			model.addAttribute("pageList", pagination);
		} catch (Exception e) {
			log.info("查询所有产品失败——————————》" + e.toString());
			e.printStackTrace();
		}
		return "admin/products/list";
	}

	@PostMapping("/products")
//	@RequiresPermissions(value = "webmenu:add")
	@SystemControllerLog(description = "添加产品")
	public String addResource(@ModelAttribute("products") Products products,HttpSession session,
			@RequestParam("fileproduct")MultipartFile[] fileproduct,
			@RequestParam("videoproduct")MultipartFile videoproduct,
			@RequestParam(defaultValue="",value="oldproductsImg")String oldproductsImg,
			@RequestParam(defaultValue="",value="oldproductsvideo")String videoId) {
		System.out.println("------"+videoId);
		this.productsService.saveOrUpdateProducts(products, session, fileproduct, oldproductsImg, imgPath,videoId,videoproduct,videoPath,dir);
		return "redirect:findproducts";
	}
	
	
	
	@PutMapping("/products")
//	@RequiresPermissions(value = "webmenu:edit")
	@SystemControllerLog(description = "修改产品信息")
	public String editResource(@ModelAttribute("products") Products products,
			@RequestParam("fileproduct")MultipartFile[] fileproduct,
			@RequestParam("videoproduct")MultipartFile videoproduct,
			@RequestParam(defaultValue="",value="oldproductsImg")String oldproductsImg,
			@RequestParam(defaultValue="",value="oldproductsvideo")String videoId,
			HttpSession session,Model model) {
		this.productsService.saveOrUpdateProducts(products, session, fileproduct, oldproductsImg, imgPath,videoId,videoproduct,videoPath,dir);
		return "redirect:findproducts";
	}
	
	
	
	@DeleteMapping("/products/{id}")
	@SystemControllerLog(description = "删除产品")
	public String deleteProducts(@PathVariable String id) {
		this.productsService.remove(Products.class, new Query().addCriteria(Criteria.where("id").is(id)));
		log.info(id);
		return "redirect:/findproducts";
	}
	
	@RequestMapping(value = "/products/disable", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult productsDisable(@RequestParam(value = "id", defaultValue = "") String id) {
		
	return this.productsService.productsDisable(id);
		
	}
	
}
