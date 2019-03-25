package zhongchiedu.controller.backstage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.Common;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.AboutUs;
import zhongchiedu.website.service.impl.AboutUsServiceImpl;

/**
 * 关于我们
 * 
 * @author fliay
 *
 */
@Controller
public class AboutUsController {

	@Autowired
	private AboutUsServiceImpl aboutUsService;

	@Autowired
	private MultiMediaServiceImpl multiMediaService;

	@GetMapping("/findAboutUs")
	// @RequiresPermissions(value = "aboutUs:list")
	@SystemControllerLog(description = "查询关于我们")
	public String findAboutUs(HttpSession session, Model model) {
			AboutUs aboutUs = this.aboutUsService.findOneByQuery(new Query(), AboutUs.class);
			model.addAttribute("aboutUs", Common.isNotEmpty(aboutUs) ? aboutUs : null);
		return "admin/aboutUs/add";
	}

	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;

	/**
	 * 添加或修改关于我们
	 * 
	 * @return
	 */
	@RequestMapping("/aboutUs")
	// @RequiresPermissions(value = "aboutUs:edit")
	@SystemControllerLog(description = "编辑关于我们")
	public String addOrEditAboutUs(@ModelAttribute("aboutUs") AboutUs aboutUs, HttpSession session,
			@RequestParam("filebanana") MultipartFile[] filebanana,
			@RequestParam(defaultValue = "", value = "oldbanana") String oldbanana,
			@RequestParam(defaultValue = "", value = "oldaboutUs") String oldaboutUs,
			@RequestParam("fileaboutUs") MultipartFile[] fileaboutUs, HttpServletRequest request,String editorValue) {
		if (Common.isEmpty(aboutUs.getId())) {
			aboutUs.setId(null);
		}
		this.aboutUsService.saveOrUpdateAboutUs(aboutUs, session, filebanana, fileaboutUs, oldbanana, oldaboutUs, imgPath,dir,editorValue);
		return "redirect:/findAboutUs";
	}

//	@RequestMapping(value = "deleteImg")
//	@ResponseBody
//	public BasicDataResult deleteImg(String id, HttpSession session) {
//
//		User user = (User) session.getAttribute(Contents.USER_SESSION);
//
//		if (Common.isNotEmpty(id)) {
//			AboutUs aboutUs = null;
//			if (Common.isNotEmpty(user)) {
//				aboutUs = this.aboutUsService.findOneByQuery(new Query(), AboutUs.class);
//			}
//			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
//			if (Common.isNotEmpty(multiMedia)) {
//				this.multiMediaService.remove(multiMedia);
//				Query query = new Query();
//				query.addCriteria(Criteria.where("belong").is("ABOUTUS_BANANA"));
//				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
//				aboutUs.setListbanana(list != null ? list : null);
//				aboutUs.setAboutusImg(aboutUs.getAboutusImg());
//				this.aboutUsService.save(aboutUs);
//				return BasicDataResult.build(200, "删除成功", id);
//			}
//		}
//		return BasicDataResult.build(400, "删除失败", null);
//	}

}
