package zhongchiedu.website.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.website.pojo.AboutUs;

@Service
public class AboutUsServiceImpl extends GeneralServiceImpl<AboutUs> {

	
	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;
	
	/**
	 * 添加或修改关于我们
	 * @param aboutUs
	 * @param session
	 */
	public void saveOrUpdateAboutUs(AboutUs aboutUs, HttpSession session,MultipartFile[] file,MultipartFile[] fileaboutUs,String oldaboutUs,String path,String dir) {
		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if(Common.isNotEmpty(user)){
			AboutUs getAboutUs = null;
			//上传关于我们图片
			List<MultiMedia> aboutUsPic = this.multiMediaSerice.uploadPictures(fileaboutUs, dir, path, "ABOUTUS");
			List<MultiMedia> list = this.multiMediaSerice.uploadPictures(file, dir, path, "ABOUTUS_BANANA");
			if(Common.isNotEmpty(aboutUs.getId())){
				 getAboutUs = this.findOneById(aboutUs.getId(), AboutUs.class);
				if(Common.isNotEmpty(getAboutUs)){
					
					aboutUs.setAboutusImg(Common.isNotEmpty(oldaboutUs)?getAboutUs.getAboutusImg():null);
					if(Common.isNotEmpty(getAboutUs.getListbanana())){
						List<MultiMedia> lm = getAboutUs.getListbanana();
						list.addAll(lm);
						}
				}
			}
			
			
			if(aboutUsPic.size()>0){
				aboutUs.setAboutusImg(aboutUsPic.get(0));
			}
			if(list.size()>0){
				aboutUs.setListbanana(list);
			}
			
			if(Common.isNotEmpty(getAboutUs)){
				BeanUtils.copyProperties(aboutUs, getAboutUs);
				getAboutUs.setListbanana(Common.isNotEmpty(file)?list:getAboutUs.getListbanana());
				this.save(getAboutUs);
			}else{
				this.insert(aboutUs);
			}
			
			
			
			
/*			if(Common.isNotEmpty(aboutUs.getId())){
				AboutUs getAboutUs = this.findOneById(aboutUs.getId(), AboutUs.class);
				if(Common.isEmpty(getAboutUs))
					getAboutUs = new AboutUs();
				
				
				//修改
				BeanUtils.copyProperties(aboutUs, getAboutUs);
				getAboutUs.setListbanana(Common.isNotEmpty(file)?list:getAboutUs.getListbanana());
				getAboutUs.setSchool(user.getSchool());
			
				getAboutUs.setAboutusImg(Common.isNotEmpty(oldaboutUs)?aboutUs.getAboutusImg():null);
				if(Common.isNotEmpty(getAboutUs.getListbanana())){
				List<MultiMedia> lm = getAboutUs.getListbanana();
				list.addAll(lm);
				}
				
				this.save(getAboutUs);
			}else{
				//添加
				aboutUs.setListbanana(Common.isNotEmpty(file)?list:null);
				aboutUs.setSchool(user.getSchool());
				this.insert(aboutUs);
			}*/
		}
	}

}
