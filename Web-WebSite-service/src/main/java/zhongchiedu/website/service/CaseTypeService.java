package zhongchiedu.website.service;

import java.util.List;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.CaseType;

public interface CaseTypeService extends GeneralService<CaseType> {

	public void saveOrUpdate(CaseType caseType);
	
	public BasicDataResult disable(String id);
	
	public List<CaseType> listCaseTypes();
	
	public List<CaseType> listCaseTypesByProduct();
	
	
}
