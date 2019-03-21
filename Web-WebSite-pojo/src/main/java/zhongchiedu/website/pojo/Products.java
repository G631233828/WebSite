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
public class Products  extends GeneralBean<Products>{

	private static final long serialVersionUID = 9148938112584702882L;
	private String type;//产品类型
	private String name;//产品名字
	private String desp;//描述
	private String title;//标题
	private boolean hasvideo;//是否含有视频
	@DBRef
	private MultiMedia  showImg;//显示的banana图片
	@DBRef
	private MultiMedia  video;//产品介绍视频
	@DBRef
	private User user;

}
