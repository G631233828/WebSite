package zhongchiedu.website.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.CasePresentation;

public interface CasePresentationService extends GeneralService<CasePresentation> {
	
	
	public void saveOrUpdate(CasePresentation casePresentation,MultipartFile[] file,String oldMedia,String path,String dir, String editorValue, String types);

	public BasicDataResult disable(String id);

	public BasicDataResult toTop(String id);
	
	public List<CasePresentation> findTopCasePresentation();

	public void updateNewsVisit(String id);
	
	public List<CasePresentation> findAllCasePresentation();
}
