package zhongchiedu.website.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import zhongchiedu.common.utils.BasicDataResult;
import zhongchiedu.common.utils.Common;
import zhongchiedu.framework.service.GeneralServiceImpl;
import zhongchiedu.website.pojo.CaseType;
import zhongchiedu.website.service.CaseTypeService;

@Service
public class CaseTypeServiceImpl extends GeneralServiceImpl<CaseType> implements CaseTypeService {

	@Override
	public void saveOrUpdate(CaseType caseType) {
		if (Common.isNotEmpty(caseType)) {
			if (Common.isNotEmpty(caseType.getId())) {
				CaseType ed = this.findOneById(caseType.getId(), CaseType.class);
				BeanUtils.copyProperties(caseType, ed);
				this.save(caseType);
			} else {
				this.insert(caseType);
			}
		}
	}

	@Override
	public BasicDataResult disable(String id) {

		if (Common.isEmpty(id)) {
			return BasicDataResult.build(400, "无法禁用，请求出现问题，请刷新界面!", null);
		}
		CaseType caseType = this.findOneById(id, CaseType.class);
		if (Common.isEmpty(caseType)) {
			return BasicDataResult.build(400, "禁用失败，该条信息可能已被删除", null);
		}
		caseType.setIsDisable(caseType.getIsDisable().equals(true) ? false : true);
		this.save(caseType);
		return BasicDataResult.build(200, caseType.getIsDisable().equals(true) ? "禁用成功" : "恢复成功",
				caseType.getIsDisable());

	}

	/**
	 * 查询所有非禁用的分类
	 */
	@Override
	public List<CaseType> listCaseTypes() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("type").is(CaseType.Type.CASE));//获取枚举类型为CASE的类型
		List<CaseType> list = this.find(query, CaseType.class);
		return list;
	}

	@Override
	public List<CaseType> listCaseTypesByProduct() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isDisable").is(false));
		query.addCriteria(Criteria.where("type").is(CaseType.Type.PRODUCT));//获取枚举类型为Product的类型
		List<CaseType> list = this.find(query, CaseType.class);
		return list;
	}

}
