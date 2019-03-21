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
public class Honor  extends GeneralBean<Honor>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6157312654360810402L;

	/**
	 * 荣誉资质图片
	 */
	@DBRef
	private List<MultiMedia> listhonors;
	
}
