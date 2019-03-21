package zhongchiedu.website.service.impl;



import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.website.pojo.Products;

@Service
public class ProductsServiceImpl extends GeneralServiceImpl<Products> {

	
	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	public void saveOrUpdateProducts(Products products, HttpSession session, MultipartFile[] file,
			String oldImg, String path,String videoId,MultipartFile video,String videopath,String dir) {
		 User user = (User) session.getAttribute(Contents.USER_SESSION);
		  MultiMedia  mult=null;
		   if(videoId!=null&&!"".equals(videoId)&& video == null) { 
		     mult=multiMediaSerice.findOneById(videoId, MultiMedia.class);
		   }
		 if(Common.isNotEmpty(user)){
			Products getproducts = null;
			//上传关于产品图片
			List<MultiMedia> productsImg = this.multiMediaSerice.uploadPictures(file,dir, path, "Products");
			if(video!=null) {
				mult=this.multiMediaSerice.uploadVideo(video,dir,videopath, "Products");
			}
			
			if(Common.isNotEmpty(products.getId())){
				 getproducts = this.findOneById(products.getId(), Products.class);
				if(Common.isNotEmpty(getproducts)){
					products.setShowImg(Common.isNotEmpty(oldImg)?getproducts.getShowImg():null);
				}
			}
			if(productsImg.size()>0){
				products.setShowImg(productsImg.get(0));
			}
			if(Common.isNotEmpty(getproducts)){
				BeanUtils.copyProperties(products, getproducts);
				getproducts.setVideo(mult);
				this.save(getproducts);
			}else{
				products.setVideo(mult);
				this.insert(products);
			}

			
		}
	}

	public BasicDataResult productsDisable(String id) {
		if(Common.isEmpty(id)){
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Products products = this.findOneById(id, Products.class);
		if(products == null){
			return BasicDataResult.build(400, "无法获取到新闻信息，该新闻可能已经被删除", null);
		}
		products.setIsDisable(products.getIsDisable().equals(true)?false:true);
		this.save(products);
		
		return BasicDataResult.build(200, products.getIsDisable().equals(true)?"产品禁用成功":"产品恢复成功",products.getIsDisable());
	}
	
    



}
