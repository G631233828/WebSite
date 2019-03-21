package zhongchiedu.compent;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import zhongchiedu.website.pojo.Company;
import zhongchiedu.website.pojo.Settings;
import zhongchiedu.website.pojo.WebMenu;
import zhongchiedu.website.service.CompanyService;
import zhongchiedu.website.service.impl.SettingsServiceImpl;
import zhongchiedu.website.service.impl.WebMenuServiceImpl;
@Component
public class WebSiteInterceptor implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(WebSiteInterceptor.class);

	@Autowired
	private WebMenuServiceImpl webMenuService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private SettingsServiceImpl settingsService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		HttpSession session = request.getSession();
		//获取企业配置
		Company company = this.companyService.findOneByQuery(new Query(), Company.class);
		session.setAttribute("company", company);
		//查询网页菜单
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("type").is("1"));
		List<WebMenu> webMenulist = this.webMenuService.find(query, WebMenu.class);
		session.setAttribute("webMenus", webMenulist);
		//获取配置
		Settings settings = this.settingsService.findOneByQuery(new Query(), Settings.class);
		session.setAttribute("settings", settings);
		
		
		
		
		
		return true;
	}

	/**
	 * 请求controller之后，渲染视图之前
	 */
	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
	}

	/**
	 * 请求controller之后，视图渲染之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
	}
}
