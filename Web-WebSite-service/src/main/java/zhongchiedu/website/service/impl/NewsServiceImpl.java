package zhongchiedu.website.service.impl;

import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.common.utils.Contents;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.pojo.User;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.News;
import zhongchiedu.website.service.NewsService;

@Service
public class NewsServiceImpl extends GeneralServiceImpl<News> implements NewsService {

	@Autowired
	private MultiMediaServiceImpl multiMediaSerice;

	/**
	 * 添加或修改关于我们
	 * 
	 * @param aboutUs
	 * @param session
	 */
	public void saveOrUpdateNews(News news, HttpSession session, MultipartFile[] file, String oldImg, String path,
			String dir, String editorValue) {
		News getnews = null;
		news.setNewsContent(editorValue);
		// 上传关于我们图片
		List<MultiMedia> newsImg = this.multiMediaSerice.uploadPictures(file, dir, path, "NEWS",225,145);
		if (Common.isNotEmpty(news.getId())) {
			getnews = this.findOneById(news.getId(), News.class);
			if (Common.isNotEmpty(getnews)) {
				news.setNewsImg(Common.isNotEmpty(oldImg) ? getnews.getNewsImg() : null);
			}
		}
		if (newsImg.size() > 0) {
			news.setNewsImg(newsImg.get(0));
		}

		if (Common.isNotEmpty(getnews)) {
			BeanUtils.copyProperties(news, getnews);
			this.save(getnews);
		} else {
			this.insert(news);
		}
	}

	public BasicDataResult newsDisable(String id) {

		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		News news = this.findOneById(id, News.class);
		if (news == null) {
			return BasicDataResult.build(400, "无法获取到新闻信息，该新闻可能已经被删除", null);
		}
		news.setIsDisable(news.getIsDisable().equals(true) ? false : true);
		this.save(news);

		return BasicDataResult.build(200, news.getIsDisable().equals(true) ? "新闻禁用成功" : "新闻恢复成功", news.getIsDisable());

	}

	@Override
	public List<News> findNewsByDate(int count) {

		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false)).with(new Sort(new Order(Direction.DESC, "createTime")))
				.limit(count);

		return this.find(query, News.class);

	}

	public List<News> findAllNews() {
		return this.find(new Query().addCriteria(Criteria.where("isDisable").is(false))
				.with(new Sort(new Order(Direction.DESC, "createTime"))), News.class);
	}

	@Override
	public void updateNewsVisit(String id) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateVisitByThread(id);
			}
		}).start();
	}

	public synchronized void updateVisitByThread(String id) {
		News n = this.findOneById(id, News.class);
		if (Common.isNotEmpty(n)) {
			n.setViews(n.getViews() + 1);
			this.save(n);
		}
	}

}
