package zhongchiedu.controller.backstage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.Honor;
import zhongchiedu.website.service.impl.HonorServiceImpl;

/**
 * 荣誉资质
 * 
 * @author fliay
 *
 */
@Controller
public class HonorController {

	@Autowired
	private HonorServiceImpl honorService;

	@Autowired
	private MultiMediaServiceImpl multiMediaService;

	@GetMapping("/findHonors")
	// @RequiresPermissions(value = "aboutUs:list")
	@SystemControllerLog(description = "查询荣誉资质")
	public String findAboutUs(HttpSession session, Model model) {
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if (Common.isNotEmpty(user)) {
			Honor honor = this.honorService.findOneByQuery(new Query(), Honor.class);
			model.addAttribute("honor", Common.isNotEmpty(honor) ? honor : null);
		}
		return "admin/honor/add";
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
	@RequestMapping("/honor")
	// @RequiresPermissions(value = "aboutUs:edit")
	@SystemControllerLog(description = "编辑荣誉资质")
	public String addOrEditHonor(@ModelAttribute("honor") Honor honor, HttpSession session,
			@RequestParam("file") MultipartFile[] honors, HttpServletRequest request) {
		if (Common.isEmpty(honor.getId())) {
			honor.setId(null);
		}
		this.honorService.saveOrUpdateHonor(honor, session, honors, imgPath,dir);

		return "redirect:/findHonors";
	}

	@RequestMapping(value = "deleteHonor")
	@ResponseBody
	public BasicDataResult deleteHonor(String id, HttpSession session) {

		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if (Common.isNotEmpty(id)) {
			Honor honor = null;
			if (Common.isNotEmpty(user)) {
				honor = this.honorService.findOneByQuery(new Query(), Honor.class);
			}
			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
			if (Common.isNotEmpty(multiMedia)) {
				this.multiMediaService.remove(multiMedia);

				Query query = new Query();
				query.addCriteria(Criteria.where("belong").is("HONOR"));
				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
				honor.setListhonors(list != null ? list : null);
				this.honorService.save(honor);
				return BasicDataResult.build(200, "删除成功", id);
			}
		}
		return BasicDataResult.build(400, "删除失败", null);
	}

}
