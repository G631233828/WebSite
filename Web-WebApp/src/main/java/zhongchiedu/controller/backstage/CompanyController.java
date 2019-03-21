package zhongchiedu.controller.backstage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.Common;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.log.annotation.SystemControllerLog;
import zhongchiedu.website.pojo.Company;
import zhongchiedu.website.service.CompanyService;

@Controller
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	
	@Value("${upload-imgpath}")
	private String imgPath;
	@Value("${upload-dir}")
	private String dir;

	@RequestMapping("/findCompany")
	@SystemControllerLog(description="查询企业信息")
	public String findCompany(Model model) {
		Company company = this.companyService.findOneByQuery(new Query(), Company.class);
		model.addAttribute("company", company);
		return "admin/company/add";
	}

	@RequestMapping("editcompany")
	public String editCompany(Company company,MultipartFile[] file,String oldQrCode){
		if(Common.isEmpty(company.getId())){
			company.setId(null);
		}
		this.companyService.saveOrUpdate(company, file, oldQrCode, imgPath, dir);
		return "redirect:findCompany";
	}
	
	
	
	
	
	
}
