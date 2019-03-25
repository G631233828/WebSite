package zhongchiedu.website.service;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.AboutUs;

public interface AboutUsService extends GeneralService<AboutUs> {

	public void saveOrUpdateAboutUs(AboutUs aboutUs, HttpSession session, MultipartFile[] filebanana,
			MultipartFile[] fileaboutUs, String oldbanana, String oldaboutUs, String path, String dir,String editorValue);

}
