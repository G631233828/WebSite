package zhongchiedu.website.pojo;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.MultiMedia;

/**
 * 案例展示
 * 
 * @author fliay
 *
 */
@Getter
@Setter
@ToString
public class CasePresentation extends GeneralBean<CasePresentation> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2102497161673538551L;

	private String name;// 案例名称
	
	private String charge;//负责人

	@DBRef
	private List<CaseType> caseTypes;// 所属分类 可以支持多个分类

	@DBRef
	private MultiMedia media;// 封面图片

	private String content; // 案例具体信息
	
	private long browseVolume;//浏览量
	
	private Boolean top = false;//首页显示  支持显示6条数据
	
	@Transient
	private String types;
	
	

}
