package zhongchiedu.website.pojo;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.MultiMedia;

@Document
@Getter
@Setter
/**
 * 关于我们
 * @author fliay
 *
 */	
public class AboutUs extends GeneralBean<AboutUs> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4195730115753692384L;
	private String aboutus;//关于我们的文字介绍
	@DBRef
	private List<MultiMedia> listbanana;//显示的banana图片
	private boolean showInView;//是否在界面显示
	@DBRef
	private MultiMedia aboutusImg;
	
	
	
}
