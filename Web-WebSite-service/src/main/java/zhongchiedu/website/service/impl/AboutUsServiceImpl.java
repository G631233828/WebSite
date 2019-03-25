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
import zhongchiedu.website.service.AboutUsService;

@Service
public class AboutUsServiceImpl extends GeneralServiceImpl<AboutUs> implements AboutUsService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	/**
	 * 添加或修改关于我们
	 * @param aboutUs
	 * @param session
	 */
	public void saveOrUpdateAboutUs(AboutUs aboutUs, HttpSession session,MultipartFile[] filebanana,MultipartFile[] fileaboutUs,String oldbanana,String oldaboutUs,String path,String dir,String editorValue) {
			AboutUs getAboutUs = null;
			aboutUs.setAboutus(editorValue);
			//上传关于我们图片
			List<MultiMedia> aboutUsPic = this.multiMediaSerice.uploadPictures(fileaboutUs, dir, path, "ABOUTUS");
			List<MultiMedia> bananaPic = this.multiMediaSerice.uploadPictures(filebanana, dir, path, "ABOUTUS_BANANA");
			if(Common.isNotEmpty(aboutUs.getId())){
				 getAboutUs = this.findOneById(aboutUs.getId(), AboutUs.class);
				if(Common.isNotEmpty(getAboutUs)){
					aboutUs.setAboutusImg(Common.isNotEmpty(oldaboutUs)?getAboutUs.getAboutusImg():null);
					aboutUs.setBanana(Common.isNotEmpty(oldbanana)?getAboutUs.getBanana():null);
				}
			}
			if(aboutUsPic.size()>0){
				aboutUs.setAboutusImg(aboutUsPic.get(0));
			}
			if(bananaPic.size()>0){
				aboutUs.setBanana(bananaPic.get(0));
			}
			
			if(Common.isNotEmpty(getAboutUs)){
				BeanUtils.copyProperties(aboutUs, getAboutUs);
				this.save(getAboutUs);
			}else{
				this.insert(aboutUs);
			}
		}

}
