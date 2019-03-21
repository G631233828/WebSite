package zhongchiedu.website.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

@Getter
@Setter
@ToString
@Document
public class WebMenu extends GeneralBean<WebMenu> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8838900980938868643L;
	private String name;//菜单名称
	private String parentId;//父菜单ID 没有父菜单则为0
	private String resUrl;//资源路径
	private String type;  //1 1级菜单 2 2级菜单 3 3级菜单
}

