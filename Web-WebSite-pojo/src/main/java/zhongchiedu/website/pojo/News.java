package zhongchiedu.website.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.pojo.User;

@Document
@Getter
@Setter
public class News  extends GeneralBean<News>{

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9148938112584702882L;
	
	
	private String newsContent;//关于我们的文字
	
	private String author;//作者
	
	private String title;//标题
	
	
	@DBRef
	private User user;
	
	@DBRef
	private MultiMedia newsImg;//显示的banana图片
	
	
	private int  views;//浏览量
	
	private boolean top;//置顶
	

}
