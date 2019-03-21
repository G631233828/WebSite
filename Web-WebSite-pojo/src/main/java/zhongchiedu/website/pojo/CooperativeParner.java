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
 * 合作伙伴与客户
 * @author fliay
 *
 */
public class CooperativeParner extends GeneralBean<CooperativeParner> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7007563812935608807L;

	
	
		@DBRef
		private List<MultiMedia> listbanana;//上传合作伙伴的图片
		
	
	
}
