package zhongchiedu.controller.backstage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import zhongchiedu.website.pojo.Settings;
import zhongchiedu.website.service.impl.SettingsServiceImpl;


/**
 * 关于我们
 * @author fliay
 *
 */
@Controller
public class SettingsController {
	
	@Autowired
	private SettingsServiceImpl settingsService;
	
	
	@Autowired
	private MultiMediaServiceImpl multiMediaService;
	
	
	@GetMapping("/findSettings")
//	@RequiresPermissions(value = "aboutUs:list")
	@SystemControllerLog(description = "查询网站设置我们")
	public String findAboutUs(HttpSession session,Model model){
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if(Common.isNotEmpty(user)){
			Settings settings = this.settingsService.findOneByQuery(new Query(), Settings.class);	
			model.addAttribute("settings", Common.isNotEmpty(settings)?settings:null);
		}
		return "admin/settings/add";
	}
	
	
	@Value("${upload-imgpath}")
	private String imgPath;
	
	@Value("${upload-dir}")
	private String dir;
	
	/**
	 * 添加或修改关于我们
	 * @return
	 */
	@RequestMapping("/settings")
	//@RequiresPermissions(value = "aboutUs:edit")
	@SystemControllerLog(description = "编辑网站设置")
	public String addOrEditAboutUs(@ModelAttribute("settings")Settings settings,
			@RequestParam(defaultValue="",value="oldIcon")String oldIcon,
			@RequestParam(defaultValue="",value="oldLogo")String oldLogo,
			@RequestParam(defaultValue="",value="oldqRcode")String oldqRcode,
			HttpSession session,@RequestParam("fileicon")MultipartFile[] fileicon,
			@RequestParam("filelogo")MultipartFile[] filelogo,
			@RequestParam("fileqRcode")MultipartFile[] fileqRcode,
			@RequestParam("filebanana")MultipartFile[] filebanana,
			HttpServletRequest request){
		if(Common.isEmpty(settings.getId())){
			settings.setId(null);
		}
		this.settingsService.saveOrUpdateSettings(settings,session,fileicon,filelogo,filebanana,fileqRcode,imgPath,oldIcon,oldLogo,oldqRcode,dir);
		
		return "redirect:/findSettings";
	}
	
	
	
	
	
	@RequestMapping(value="deleteIndexImg")
	@ResponseBody
	public BasicDataResult deleteImg(String id,HttpSession session){
		
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if(Common.isNotEmpty(id)){
			Settings settings = null;
			
			if(Common.isNotEmpty(user)){
					 settings = this.settingsService.findOneByQuery(new Query(), Settings.class);	
				}
			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
			if(Common.isNotEmpty(multiMedia)){
				this.multiMediaService.remove(multiMedia);
				
				Query query = new Query();
				query.addCriteria(Criteria.where("belong").is("SETTINGS_BANANA"));
				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
				settings.setListBanana(list!=null?list:null);
				this.settingsService.save(settings);
				return BasicDataResult.build(200, "删除成功", id);
			}
		}
		return BasicDataResult.build(400, "删除失败", null);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
