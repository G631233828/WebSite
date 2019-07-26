package zhongchiedu.website.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.framework.service.GeneralService;
import zhongchiedu.website.pojo.Product;

public interface ProductService extends GeneralService<Product> {
	
	
	public void saveOrUpdate(Product product,MultipartFile[] file,String oldMedia,String path,String dir, String editorValue, String types);

	public BasicDataResult disable(String id);

	public BasicDataResult toTop(String id);
	
	public List<Product> findTopProduct();

	public void updateNewsVisit(String id);
	
	public List<Product> findAllProduct();
}
