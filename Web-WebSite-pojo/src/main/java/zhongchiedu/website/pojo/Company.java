package zhongchiedu.website.pojo;

import org.springframework.data.mongodb.core.mapping.DBRef;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import zhongchiedu.framework.pojo.GeneralBean;
import zhongchiedu.general.pojo.MultiMedia;

@Getter
@Setter
@ToString
public class Company  extends GeneralBean<Company>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3949011882858633920L;
	private String companyName;//公司名称
	private String email;      //邮箱
	private String address;	   //地址	
	private String phone;	   //联系电话
	private String zipCode;	   //邮编
	private String recordNumber;//京备案号
	private String shanghaiRecoredNumber;//沪备案号
	private String copyright;//版权
	@DBRef
	private MultiMedia qrCode;//二维码
	
	
}
