package zhongchiedu.website.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.website.pojo.Company;
import zhongchiedu.website.service.CompanyService;
@Service
public class CompanyServiceImpl extends GeneralServiceImpl<Company> implements CompanyService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;
	
	@Override
	public void saveOrUpdate(Company company,MultipartFile[] file,String oldQrCode,String path,String dir) {
		Company getCompany = null;
		//上传图片
		List<MultiMedia> companyPic = this.multiMediaSerice.uploadPictures(file, dir, path, "COMPANY");
		if(Common.isNotEmpty(company.getId())){
			//执行修改
			getCompany = this.findOneById(company.getId(), Company.class);
			if(Common.isNotEmpty(getCompany)){
				company.setQrCode(Common.isNotEmpty(oldQrCode)?getCompany.getQrCode():null);
			}
		}
		if(companyPic.size()>0){
			company.setQrCode(companyPic.get(0));
		}
		
		if(Common.isNotEmpty(getCompany)){
			BeanUtils.copyProperties(company, getCompany);
			this.save(getCompany);
		}else{
			this.insert(company);
		}
		
		
		
		
	}
	
	
	

	

}
