package zhongchiedu.website.service;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.Company;

public interface CompanyService extends GeneralService<Company> {
	
	public void saveOrUpdate(Company company,MultipartFile[] file,String oldQrCode,String path,String dir);

}
