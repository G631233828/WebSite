package zhongchiedu.website.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
import zhongchiedu.website.pojo.Product;
import zhongchiedu.website.service.CasePresentationService;
import zhongchiedu.website.service.CaseTypeService;
import zhongchiedu.website.service.ProductService;

@Service
public class ProductServiceImpl extends GeneralServiceImpl<Product> implements ProductService {

	@Autowired
	private MultiMediaServiceImpl multiMediaService;
	@Autowired
	private CaseTypeService caseTypeService;

	private AtomicLong in = new AtomicLong();

	@Override
	public void saveOrUpdate(Product product, MultipartFile[] file, String oldMedia, String path, String dir,
			String editorValue, String types) {
		List ids = Arrays.asList(types.split(","));
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").in(ids));
		List<CaseType> list = this.caseTypeService.find(query, CaseType.class);
		Product getProduct = null;
		product.setContent(editorValue);
		product.setCaseTypes(list);
		// 上传图片
		List<MultiMedia> productPic = this.multiMediaService.uploadPictures(file, dir, path, "PRODUCT", 400, 251);
		if (Common.isNotEmpty(product.getId())) {
			// 执行修改
			getProduct = this.findOneById(product.getId(), Product.class);
			if (Common.isNotEmpty(getProduct)) {
				product.setMedia(Common.isNotEmpty(oldMedia) ? getProduct.getMedia() : null);
			}
		}
		if (productPic.size() > 0) {
			product.setMedia(productPic.get(0));
		}
		if (Common.isNotEmpty(getProduct)) {
			BeanUtils.copyProperties(product, getProduct);
			this.save(getProduct);
		} else {
			this.insert(product);
		}

	}

	@Override
	public BasicDataResult disable(String id) {
		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		Product dis = this.findOneById(id, Product.class);
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
		Product dis = this.findOneById(id, Product.class);
		if (Common.isEmpty(dis)) {
			return BasicDataResult.build(400, "显示到首页失败，该条信息可能已被删除", null);
		}
		// 查询目前显示到首页的数量，如果超过6条就无法继续添加
		Query query = new Query();
		query.addCriteria(Criteria.where("top").is(true));
		List<Product> list = this.find(query, Product.class);
		if (list.size() >= 6) {
			return BasicDataResult.build(400, "目前已经有6条显示在首页的数据了，请先取消再试", dis.getTop());
		} else {
			dis.setTop(dis.getTop().equals(true) ? false : true);
			this.save(dis);
			return BasicDataResult.build(200, dis.getTop().equals(true) ? "显示成功" : "取消成功", dis.getTop());
		}
	}

	@Override
	public List<Product> findTopProduct() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false)).addCriteria(Criteria.where("top").is(true));
		return this.find(query, Product.class);
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
		Product n = this.findOneById(id, Product.class);
		if (Common.isNotEmpty(n)) {
//			in.set(n.getBrowseVolume());
//			n.setBrowseVolume(in.getAndIncrement());
			n.setBrowseVolume(n.getBrowseVolume()+1);
			this.save(n);
		}
	}

	@Override
	public List<Product> findAllProduct() {
		return this.find(new Query().addCriteria(Criteria.where("isDisable").is(false))
				.with(new Sort(new Order(Direction.DESC, "createTime"))), Product.class);
	}


}
