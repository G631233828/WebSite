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
import zhongchiedu.website.pojo.CooperativeParner;

@Service
public class CooperativeParnerServiceImpl extends GeneralServiceImpl<CooperativeParner> {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	public void saveOrUpdateCooperativeParner(CooperativeParner cp, HttpSession session, 
			MultipartFile[] parners, String imgPath,String dir) {

		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if (Common.isNotEmpty(user)) {
			CooperativeParner getparners = null;
			// icon
			List<MultiMedia> listparner = this.multiMediaSerice.uploadPictures(parners, dir, imgPath,  "COOPERATIVEPARNER");
			if (Common.isNotEmpty(cp.getId())) {
				getparners = this.findOneById(cp.getId(), CooperativeParner.class);
				if (Common.isNotEmpty(getparners)) {
					if (Common.isNotEmpty(getparners.getListbanana())) {
						List<MultiMedia> lm = getparners.getListbanana();
						listparner.addAll(lm);
					}
				}
			}
			if(listparner.size()>0){
				cp.setListbanana(listparner);
			}
			if (Common.isNotEmpty(getparners)) {
				BeanUtils.copyProperties(cp, getparners);
				getparners.setListbanana(Common.isNotEmpty(parners) ? listparner : getparners.getListbanana());
				this.save(getparners);
			} else {
				this.insert(cp);
			}

		}

	}

}
