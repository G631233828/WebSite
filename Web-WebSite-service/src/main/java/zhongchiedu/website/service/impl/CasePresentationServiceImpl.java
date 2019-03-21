package zhongchiedu.website.service.impl;

import java.util.Arrays;
import java.util.List;

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
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.general.pojo.MultiMedia;
import zhongchiedu.general.service.Impl.MultiMediaServiceImpl;
import zhongchiedu.website.pojo.CasePresentation;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.service.CasePresentationService;
import zhongchiedu.website.service.CaseTypeService;

@Service
public class CasePresentationServiceImpl extends GeneralServiceImpl<CasePresentation>
		implements CasePresentationService {

	@Autowired
	private MultiMediaServiceImpl multiMediaService;
	@Autowired
	private CaseTypeService caseTypeService;

	@Override
	public void saveOrUpdate(CasePresentation casePresentation, MultipartFile[] file, String oldMedia, String path,
			String dir, String editorValue, String types) {
		List ids = Arrays.asList(types.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		List<CaseType> list = this.caseTypeService.find(query, CaseType.class);
		CasePresentation getcasePresentation = null;
		casePresentation.setContent(editorValue);
		casePresentation.setCaseTypes(list);
		// 上传图片
		List<MultiMedia> casePresentationPic = this.multiMediaService.uploadPictures(file, dir, path,
				"CASEPRESENTATION");
		if (Common.isNotEmpty(casePresentation.getId())) {
			// 执行修改
			getcasePresentation = this.findOneById(casePresentation.getId(), CasePresentation.class);
			if (Common.isNotEmpty(getcasePresentation)) {
				casePresentation.setMedia(Common.isNotEmpty(oldMedia) ? getcasePresentation.getMedia() : null);
			}
		}
		if (casePresentationPic.size() > 0) {
			casePresentation.setMedia(casePresentationPic.get(0));
		}
		if (Common.isNotEmpty(getcasePresentation)) {
			BeanUtils.copyProperties(casePresentation, getcasePresentation);
			this.save(getcasePresentation);
		} else {
			this.insert(casePresentation);
		}

	}

	@Override
	public BasicDataResult disable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		CasePresentation dis = this.findOneById(id, CasePresentation.class);
		if (Common.isEmpty(dis)) {
			return BasicDataResult.build(400, "禁用失败，该条信息可能已被删除", null);
		}
		dis.setIsDisable(dis.getIsDisable().equals(true) ? false : true);
		this.save(dis);
		return BasicDataResult.build(200, dis.getIsDisable().equals(true) ? "禁用成功" : "恢复成功", dis.getIsDisable());
	}

	@Override
	public BasicDataResult toTop(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法显示到首页，请求出现问题，请刷新界面!", null);
		}
		CasePresentation dis = this.findOneById(id, CasePresentation.class);
		if (Common.isEmpty(dis)) {
			return BasicDataResult.build(400, "显示到首页失败，该条信息可能已被删除", null);
		}
		// 查询目前显示到首页的数量，如果超过6条就无法继续添加
		Query query = new Query();
		query.addCriteria(Criteria.where("top").is(true));
		List<CasePresentation> list = this.find(query, CasePresentation.class);
		if (list.size() >= 6) {
			return BasicDataResult.build(400, "目前已经有6条显示在首页的数据了，请先取消再试", dis.getTop());
		} else {
			dis.setTop(dis.getTop().equals(true) ? false : true);
			this.save(dis);
			return BasicDataResult.build(200, dis.getTop().equals(true) ? "显示成功" : "取消成功", dis.getTop());
		}
	}

	@Override
	public List<CasePresentation> findTopCasePresentation() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("top").is(true));
		return this.find(query, CasePresentation.class);
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
		CasePresentation n = this.findOneById(id, CasePresentation.class);
		if (Common.isNotEmpty(n)) {
			n.setBrowseVolume(n.getBrowseVolume() + 1);
			this.save(n);
		}
	}

	@Override
	public List<CasePresentation> findAllCasePresentation() {
		return this.find(new Query().addCriteria(Criteria.where("isDisable").is(false))
				.with(new Sort(new Order(Direction.DESC, "createTime"))), CasePresentation.class);
	}

}
