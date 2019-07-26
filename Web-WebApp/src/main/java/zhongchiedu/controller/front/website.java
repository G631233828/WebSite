package zhongchiedu.controller.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.website.pojo.AboutUs;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.pojo.Company;
import zhongchiedu.website.pojo.Honor;
import zhongchiedu.website.pojo.News;
import zhongchiedu.website.pojo.Product;
import zhongchiedu.website.service.CasePresentationService;
import zhongchiedu.website.service.CaseTypeService;
import zhongchiedu.website.service.CompanyService;
import zhongchiedu.website.service.NewsService;
import zhongchiedu.website.service.ProductService;
import zhongchiedu.website.service.impl.AboutUsServiceImpl;
import zhongchiedu.website.service.impl.CooperativeParnerServiceImpl;
import zhongchiedu.website.service.impl.HonorServiceImpl;

@Controller
@RequestMapping("website")
public class website {

	@Autowired
	private AboutUsServiceImpl aboutUsService;

	@Autowired
	private HonorServiceImpl honorServiceImpl;

	@Autowired
	private NewsService newsService;

	@Autowired
	private CompanyService companySercvice;

	@Autowired
	private ProductService productService;

	@Autowired
	private CooperativeParnerServiceImpl cooperativeParnerService;

	@Autowired
	private CasePresentationService casePresentationService;
	
	@Autowired
	private CaseTypeService caseTypeService;

	@RequestMapping("/index")
	public String webIndex(HttpSession session, Model model) {
		// 获取所有显示在首页的成功案例
		List<CasePresentation> listcase = this.casePresentationService.findTopCasePresentation();
		model.addAttribute("listcase", listcase);
		//获取4条最新的新闻数据
		List<News> listnews = this.newsService.findNewsByDate(4);
		model.addAttribute("listnews", listnews);
		model.addAttribute("active","index");
		
		return "front/index";
	}

	@RequestMapping("caseDetails/{id}")
	public String caseDetails(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable(value = "id") String id) {
		// 通过id查询案例
		CasePresentation casePresentation = this.casePresentationService.findOneById(id, CasePresentation.class);
		model.addAttribute("casePresentation", casePresentation);
		String ip = request.getRemoteAddr();
		String getIp = (String) session.getAttribute(ip + "_" + id);
		if (Common.isEmpty(getIp)) {
			this.casePresentationService.updateNewsVisit(id);
			session.setAttribute(ip + "_" + id, ip + "_" + id);
		}
		CasePresentation upcasePresentation = new CasePresentation();
		CasePresentation nextcasePresentation = new CasePresentation();

		// 获取所有非禁用状态的案例
		List<CasePresentation> listcase = this.casePresentationService.findAllCasePresentation();
		int up = 0;
		int next = 0;
		if (listcase.size() > 0) {
			for (int i = 0; i < listcase.size(); i++) {
				if (listcase.get(i).getId().equals(casePresentation.getId())) {
					next = i == listcase.size()-1?0:i+1;
					up = i==0?listcase.size()-1:i-1;
				}
			}
			nextcasePresentation = listcase.get(next);
			upcasePresentation = listcase.get(up);
			model.addAttribute("next", nextcasePresentation != null ? nextcasePresentation : null);
			model.addAttribute("up", upcasePresentation != null ? upcasePresentation : null);

		}
		model.addAttribute("active","caseDetails");

		return "front/case_details";
	}
	
	


	@RequestMapping("/casePresentation")
	public String casePresentation(Model model,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize, String caseTypeId){
		//获取所有的类别
		List<CaseType> caseTypes = this.caseTypeService.find(new Query().addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("type").is(CaseType.Type.CASE)), CaseType.class);
		model.addAttribute("caseTypes", caseTypes);
		// 分页查询数据
		Pagination<CasePresentation> pagination;
		try {
			Query query = new Query();
			if(Common.isNotEmpty(caseTypeId)){
				query.addCriteria(Criteria.where("caseTypes.$id").is(new ObjectId(caseTypeId)));
			}
			query.addCriteria(Criteria.where("isDisable").is(false));
			pagination = casePresentationService.findPaginationByQuery(query, pageNo, pageSize, CasePresentation.class);
			if (pagination == null)
				pagination = new Pagination<CasePresentation>();

			model.addAttribute("caseTypeId", caseTypeId);
			model.addAttribute("pageList", pagination);
			model.addAttribute("active","casePresentation");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "front/case_list";
	}
	
	
	
	
	@RequestMapping("newsDetails/{id}")
	public String newsDetails(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable(value = "id") String id) {
		// 通过id查询案例
		News news = this.newsService.findOneById(id, News.class);
		model.addAttribute("news", news);
		String ip = request.getRemoteAddr();
		String getIp = (String) session.getAttribute(ip + "_" + id);
		if (Common.isEmpty(getIp)) {
			this.newsService.updateNewsVisit(id);
			session.setAttribute(ip + "_" + id, ip + "_" + id);
		}
		News up = new News();
		News next = new News();
		
		// 获取所有非禁用状态的新闻
		List<News> listnews = this.newsService.findAllNews();
		int upNum = 0;
		int nextNum = 0;
		if (listnews.size() > 0) {
			for (int i = 0; i < listnews.size(); i++) {
				if (listnews.get(i).getId().equals(news.getId())) {
					nextNum = i == listnews.size()-1?0:i+1;
					upNum = i==0?listnews.size()-1:i-1;
				}
			}
			next = listnews.get(nextNum);
			up = listnews.get(upNum);
			model.addAttribute("next", next != null ? next : null);
			model.addAttribute("up", up != null ? up : null);
		}
		model.addAttribute("active","newsDetails");
		return "front/news_details";
	}

	@RequestMapping("/aboutUs")
	public String webaboutUs(HttpSession session, Model model) {
		AboutUs aboutUs = this.aboutUsService.findOneByQuery(new Query(), AboutUs.class);
		model.addAttribute("aboutUs", aboutUs);
		model.addAttribute("active","aboutUs");
		return "front/about";
	}
	
	@RequestMapping("/contactUs")
	public String contactUs(Model model){
		
		Company cp = this.companySercvice.findOneByQuery(new Query(), Company.class);
		model.addAttribute("company", cp);
		model.addAttribute("active","contactUs");
		return "front/contact_us";
		
	}
	
	
	

	@RequestMapping("/honor")
	public String webhonor(HttpSession session, Model model) {
		Honor honor = this.honorServiceImpl.findOneByQuery(new Query(), Honor.class);
		model.addAttribute("honors", honor);
		return "front/honor";
	}

	@RequestMapping("/news")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize, HttpSession session) {
		// 分页查询数据
		Pagination<News> pagination;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("isDisable").is(false));
			pagination = newsService.findPaginationByQuery(query, pageNo, pageSize, News.class);
			if (pagination == null)
				pagination = new Pagination<News>();

			model.addAttribute("pageList", pagination);

		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("active","news");
		return "front/news";
	}

	@RequestMapping("/news/{id}")
	public String findNews(HttpServletRequest request, HttpSession session, @PathVariable String id, Model model) {

		if (Common.isNotEmpty(id)) {
			News news = this.newsService.findOneById(id, News.class);
			model.addAttribute("news", news);
		}

		// String ip = request.getRemoteAddr();
		// String getIp = (String) session.getAttribute(ip+"_"+id);
		// if(Common.isEmpty(getIp)){
		// this.newsService.updateNewsVisit(id);
		// session.setAttribute(ip+"_"+id, ip+"_"+id);
		// }
		model.addAttribute("active","news");
		return "front/news_detail";
	}


	@RequestMapping("productDetails/{id}")
	public String productDetails(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable(value = "id") String id) {
		// 通过id查询案例
		Product product = this.productService.findOneById(id, Product.class);
		model.addAttribute("product", product);
		String ip = request.getRemoteAddr();
		String getIp = (String) session.getAttribute(ip + "_" + id);
		if (Common.isEmpty(getIp)) {
			this.productService.updateNewsVisit(id);
			session.setAttribute(ip + "_" + id, ip + "_" + id);
		}
		Product upproduct = new Product();
		Product nextproduct = new Product();

		// 获取所有非禁用状态的案例
		List<Product> listcase = this.productService.findAllProduct();
		int up = 0;
		int next = 0;
		if (listcase.size() > 0) {
			for (int i = 0; i < listcase.size(); i++) {
				if (listcase.get(i).getId().equals(product.getId())) {
					next = i == listcase.size()-1?0:i+1;
					up = i==0?listcase.size()-1:i-1;
				}
			}
			nextproduct = listcase.get(next);
			upproduct = listcase.get(up);
			model.addAttribute("next", nextproduct != null ? nextproduct : null);
			model.addAttribute("up", upproduct != null ? upproduct : null);

		}
		model.addAttribute("active","products");

		return "front/product_details";
	}
	
	


	@RequestMapping("/products")
	public String products(Model model,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize, String caseTypeId){
		//获取所有的类别
		List<CaseType> caseTypes = this.caseTypeService.find(new Query().addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("type").is(CaseType.Type.PRODUCT)), CaseType.class);
		model.addAttribute("caseTypes", caseTypes);
		// 分页查询数据
		Pagination<Product> pagination;
		try {
			Query query = new Query();
			if(Common.isNotEmpty(caseTypeId)){
				query.addCriteria(Criteria.where("caseTypes.$id").is(new ObjectId(caseTypeId)));
			}
			query.addCriteria(Criteria.where("isDisable").is(false));
			pagination = productService.findPaginationByQuery(query, pageNo, pageSize, Product.class);
			if (pagination == null)
				pagination = new Pagination<Product>();

			model.addAttribute("caseTypeId", caseTypeId);
			model.addAttribute("pageList", pagination);
			model.addAttribute("active","products");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "front/product_list";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
