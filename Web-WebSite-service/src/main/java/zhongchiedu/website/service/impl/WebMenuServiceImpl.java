package zhongchiedu.website.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.website.pojo.WebMenu;

@Service
public class WebMenuServiceImpl extends GeneralServiceImpl<WebMenu> {

	/**
	 * 添加修改菜单
	 * 
	 * @param webmenu
	 */
	public void saveOrUpdateUser(WebMenu webmenu, HttpSession session) {

		if (Common.isNotEmpty(webmenu)) {
			if (Common.isNotEmpty(webmenu.getId())) {
				// 修改
				WebMenu ed = this.findOneById(webmenu.getId(), WebMenu.class);
				BeanUtils.copyProperties(webmenu, ed);
				this.save(ed);
			} else {
				WebMenu ad = new WebMenu();
				BeanUtils.copyProperties(webmenu, ad);
				this.insert(ad);
			}
		}
	}

	/**
	 * 根据学校ID与type 查询菜单
	 * 
	 * @param type
	 * @param session
	 * @return
	 */
	public List<WebMenu> findWebMenuByType(String type, HttpSession session) {

		Query query = new Query();
		query.addCriteria(Criteria.where("type").is(type));
		List<WebMenu> list = this.find(query, WebMenu.class);
		return list.size() > 0 ? list : null;

	}

	/**
	 * 禁用启用
	 * 
	 * @param id
	 * @return
	 */
	public BasicDataResult webMenuDisable(String id) {

		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		WebMenu resource = this.findOneById(id, WebMenu.class);
		if (resource == null) {
			return BasicDataResult.build(400, "无法获取到资源信息，该资源可能已经被删除", null);
		}

		resource.setIsDisable(resource.getIsDisable().equals(true) ? false : true);
		this.save(resource);

		return BasicDataResult.build(200, resource.getIsDisable().equals(true) ? "资源禁用成功" : "资源启用成功",
				resource.getIsDisable());

	}

}
