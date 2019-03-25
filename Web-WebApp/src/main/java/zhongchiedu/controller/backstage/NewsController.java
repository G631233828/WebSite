package zhongchiedu.controller.backstage;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

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

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.pagination.Pagination;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.News;
import zhongchiedu.website.service.NewsService;

/**
 * 新闻
 * 
 * @author fliay
 *
 */
@Controller
public class NewsController {

	private static final Logger log = LoggerFactory.getLogger(NewsController.class);

	@Autowired
	private NewsService newsService;

	@Autowired
	private MultiMediaServiceImpl multiMediaService;

	@Value("${upload-imgpath}")
	private String imgPath;

	@Value("${upload-dir}")
	private String dir;

	@GetMapping("/findnews")
	@RequiresPermissions(value = "news:list")
	@SystemControllerLog(description = "查询所有新闻资源")
	public String list(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, Model model,
			@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize, HttpSession session) {

		// 分页查询数据
		Pagination<News> pagination;
		try {
			pagination = newsService.findPaginationByQuery(new Query(), pageNo, pageSize, News.class);
			if (pagination == null)
				pagination = new Pagination<News>();

			model.addAttribute("pageList", pagination);

		} catch (Exception e) {
			log.info("查询所有新闻失败——————————》" + e.toString());
			e.printStackTrace();
		}

		return "admin/news/list";
	}

	/**
	 * 跳转到添加页面
	 */
	@GetMapping("/news")
	public String addNewsPage(Model model) {

		return "admin/news/add";
	}

	@PostMapping("/news")
//	@RequiresPermissions(value = "news:add")
	@SystemControllerLog(description = "添加新闻")
	public String addResource(@ModelAttribute("news") News news, HttpSession session,
			@RequestParam("filenews") MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue = "", value = "oldnewsImg") String oldnewsImg) {

		this.newsService.saveOrUpdateNews(news, session, filenews, oldnewsImg, imgPath, dir,editorValue);

		return "redirect:findnews";
	}

	@PutMapping("/news")
//	@RequiresPermissions(value = "news:edit")
	@SystemControllerLog(description = "修改新闻")
	public String editResource(@ModelAttribute("news") News news, @RequestParam("filenews") MultipartFile[] filenews,String editorValue,
			@RequestParam(defaultValue = "", value = "oldnewsImg") String oldnewsImg, HttpSession session) {

		if (Common.isEmpty(news.getId())) {
			news.setId(null);
		}
		this.newsService.saveOrUpdateNews(news, session, filenews, oldnewsImg, imgPath,dir,editorValue);

		return "redirect:findnews";
	}

	/**
	 * 跳转到编辑界面
	 * 
	 * @return
	 */
	@GetMapping("/news{id}")
	public String toeditPage(@PathVariable String id, Model model, HttpSession session) {

		News news = this.newsService.findOneById(id, News.class);

		model.addAttribute("news", news);

		return "admin/news/add";

	}

	@RequestMapping(value = "/news/disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public BasicDataResult newsDisable(@RequestParam(value = "id", defaultValue = "") String id) {

		return this.newsService.newsDisable(id);

	}

	@DeleteMapping("/news/{id}")
	public String todelete(@PathVariable String id) {
		
		List ids = Arrays.asList(id.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		this.newsService.remove(News.class, query);
	/*	
		if (Common.isNotEmpty(id)) {
			News news = this.newsService.findOneById(id, News.class);
			if (Common.isNotEmpty(news)) {
				this..remove(news);
			}
		}*/
		return "redirect:/findnews";
	}

}
