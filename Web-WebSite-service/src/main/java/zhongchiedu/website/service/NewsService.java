package zhongchiedu.website.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.News;

public interface NewsService extends GeneralService<News> {

	public void saveOrUpdateNews(News news, HttpSession session, MultipartFile[] file, String oldImg, String path,
			String dir, String editorValue);

	public BasicDataResult newsDisable(String id);
	
	public List<News> findNewsByDate(int count);

	public List<News> findAllNews();
	
	public void updateNewsVisit(String id) ;
	
}
