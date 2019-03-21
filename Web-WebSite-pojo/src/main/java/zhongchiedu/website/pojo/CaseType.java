package zhongchiedu.website.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;

/**
 * 案例类型
 * @author fliay
 *
 */
@Getter
@Setter
@ToString
@Document
public class CaseType extends GeneralBean<CaseType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -979497505541989639L;
	
	
	private String name;//类型名称
	
	
}
