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
import zhongchiedu.website.pojo.Honor;

@Service
public class HonorServiceImpl extends GeneralServiceImpl<Honor> {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	public void saveOrUpdateHonor(Honor honor, HttpSession session, 
			MultipartFile[] honors, String imgPath,String dir) {

		User user = (User) session.getAttribute(Contents.USER_SESSION);
		if (Common.isNotEmpty(user)) {
			Honor getHonor = null;
			// icon
			List<MultiMedia> listhonors = this.multiMediaSerice.uploadPictures(honors,dir, imgPath, "HONOR");
			if (Common.isNotEmpty(honor.getId())) {
				getHonor = this.findOneById(honor.getId(), Honor.class);
				if (Common.isNotEmpty(getHonor)) {
					if (Common.isNotEmpty(getHonor.getListhonors())) {
						List<MultiMedia> lm = getHonor.getListhonors();
						listhonors.addAll(lm);
					}
				}
			}
			if(listhonors.size()>0){
				honor.setListhonors(listhonors);
			}
			if (Common.isNotEmpty(getHonor)) {
				BeanUtils.copyProperties(honor, getHonor);
				getHonor.setListhonors(Common.isNotEmpty(honors) ? listhonors : getHonor.getListhonors());
				this.save(getHonor);
			} else {
				this.insert(honor);
			}

		}

	}

}
