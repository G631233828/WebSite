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
import zhongchiedu.website.pojo.CooperativeParner;
import zhongchiedu.website.service.impl.CooperativeParnerServiceImpl;


/**
 * 合作伙伴与客户
 * @author fliay
 *
 */
@Controller
public class CooperativeParnerController {
	
	@Autowired
	private  CooperativeParnerServiceImpl  cooperativeParnerService;
	
	@Autowired
	private MultiMediaServiceImpl multiMediaService;
	

	
	@GetMapping("/findCooperativeParner")
//	@RequiresPermissions(value = "aboutUs:list")
	@SystemControllerLog(description = "查询合作伙伴与客户")
	public String findAboutUs(HttpSession session,Model model){
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if(Common.isNotEmpty(user)){
			CooperativeParner cooperativeParner = this.cooperativeParnerService.findOneByQuery(new Query(), CooperativeParner.class);	
			model.addAttribute("cooperativeParner", Common.isNotEmpty(cooperativeParner)?cooperativeParner:null);
		}
		return "admin/cooperativeParner/add";
	}
	
	
	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;
	
	/**
	 * 添加或修改关于我们
	 * @return
	 */
	@RequestMapping("/cooperativeParner")
	//@RequiresPermissions(value = "aboutUs:edit")
	@SystemControllerLog(description = "合作伙伴与客户")
	public String addOrEditAboutUs(@ModelAttribute("cooperativeParner")CooperativeParner cooperativeParner,
			HttpSession session,@RequestParam("parners")MultipartFile[] parners,
			HttpServletRequest request){
		if(Common.isEmpty(cooperativeParner.getId())){
			cooperativeParner.setId(null);
		}
		this.cooperativeParnerService.saveOrUpdateCooperativeParner(cooperativeParner,session,parners,imgPath,dir);
		return "redirect:/findCooperativeParner";
	}
	
	
	
	
	@RequestMapping(value="deleteCooperativeParner")
	@ResponseBody
	public BasicDataResult deleteCooperativeParner(String id,HttpSession session){
		
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if(Common.isNotEmpty(id)){
			CooperativeParner cooperativeParner = null;
			if(Common.isNotEmpty(user)){
					cooperativeParner = this.cooperativeParnerService.findOneByQuery(new Query(), CooperativeParner.class);	
				}
			MultiMedia multiMedia = this.multiMediaService.findOneById(id, MultiMedia.class);
			if(Common.isNotEmpty(multiMedia)){
				this.multiMediaService.remove(multiMedia);
				Query query = new Query();
				query.addCriteria(Criteria.where("belong").is("COOPERATIVEPARNER"));
				List<MultiMedia> list = this.multiMediaService.find(query, MultiMedia.class);
				cooperativeParner.setListbanana(list!=null?list:null);
				this.cooperativeParnerService.save(cooperativeParner);
				return BasicDataResult.build(200, "删除成功", id);
			}
		}
		return BasicDataResult.build(400, "删除失败", null);
	}
	
	
	
	
	
	
}
