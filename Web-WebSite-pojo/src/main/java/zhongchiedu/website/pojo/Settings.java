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
 * 网站设置
 * @author fliay
 *
 */
public class Settings extends GeneralBean<Settings> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4662268148039841576L;
	@DBRef
	private MultiMedia icon;//网站的icon
	@DBRef
	private MultiMedia logo;//网站显示logo
	private String title;
	
	private String path;//网站访问地址
	@DBRef
	private MultiMedia qRcode;//网站二维码
	
	
	@DBRef
	private List<MultiMedia> listBanana;
	
	
	

}
