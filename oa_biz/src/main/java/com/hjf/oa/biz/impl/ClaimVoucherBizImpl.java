package com.hjf.oa.biz.impl;

import com.hjf.oa.biz.ClaimVoucherBiz;
import com.hjf.oa.dao.ClaimVoucherDao;
import com.hjf.oa.dao.ClaimVoucherItemDao;
import com.hjf.oa.dao.DealRecordDao;
import com.hjf.oa.dao.EmployeeDao;
import com.hjf.oa.entity.ClaimVoucher;
import com.hjf.oa.entity.ClaimVoucherItem;
import com.hjf.oa.entity.DealRecord;
import com.hjf.oa.entity.Employee;
import com.hjf.oa.global.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {

    @Autowired
    private ClaimVoucherDao claimVoucherDao;

    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;

    @Autowired
    private DealRecordDao dealRecordDao;

    @Autowired
    private EmployeeDao employeeDao;

    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {

        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Constant.CLAIM_STATUS_CREATED);
        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            claimVoucherItemDao.insert(item);
        }
    }

    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getItems(int cvId) {
        return claimVoucherItemDao.selectByClaimVoucher(cvId);
    }

    public List<DealRecord> getRecords(int cvId) {
        return dealRecordDao.selectByClaimVoucher(cvId);
    }

    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {

        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Constant.CLAIM_STATUS_CREATED);
        claimVoucherDao.update(claimVoucher);

        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());
        for (ClaimVoucherItem old : olds) {
            boolean isHave = false;
            for (ClaimVoucherItem item : items) {
                if (item.getId() == old.getId()) {
                    isHave = true;
                    break;
                }
            }
            if (!isHave) {
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {
            item.setClaimVoucherId(claimVoucher.getId());
            if (item.getId() > 0)
                claimVoucherItemDao.update(item);
            else
                claimVoucherItemDao.insert(item);
        }
    }

    public void submit(int id) {

        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getCreateSn());

        claimVoucher.setStatus(Constant.CLAIM_STATUS_SUBMIT);
        if (claimVoucher.getCreator().getPost().equals(Constant.POST_FM)) {
            claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Constant.POST_GM).get(0).getSn());
        } else
            claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Constant.POST_FM).get(0).getSn());
        claimVoucherDao.update(claimVoucher);

        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Constant.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Constant.CLAIM_STATUS_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);
    }

    public void deal(DealRecord dealRecord) {

        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());

        if (dealRecord.getDealWay().equals(Constant.DEAL_PASS)) {

            if (claimVoucher.getTotalAmount() <= Constant.LIMIT_CHECK || employee.getPost().equals(Constant.POST_GM)) {
                claimVoucher.setStatus(Constant.CLAIM_STATUS_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Constant.POST_CASHIER).get(0).getSn());

                dealRecord.setDealResult(Constant.CLAIM_STATUS_APPROVED);
            } else {
                claimVoucher.setStatus(Constant.CLAIM_STATUS_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null, Constant.POST_GM).get(0).getSn());

                dealRecord.setDealResult(Constant.CLAIM_STATUS_RECHECK);
            }
        } else if (dealRecord.getDealWay().equals(Constant.DEAL_BACK)) {
            claimVoucher.setStatus(Constant.CLAIM_STATUS_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());

            dealRecord.setDealResult(Constant.CLAIM_STATUS_BACK);
        } else if (dealRecord.getDealWay().equals(Constant.DEAL_REJECT)) {
            claimVoucher.setStatus(Constant.CLAIM_STATUS_TERMINATED);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Constant.CLAIM_STATUS_TERMINATED);
        } else if (dealRecord.getDealWay().equals(Constant.DEAL_PAID)) {
            claimVoucher.setStatus(Constant.CLAIM_STATUS_PAID);
            claimVoucher.setNextDealSn(null);

            dealRecord.setDealResult(Constant.CLAIM_STATUS_PAID);
        }

        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);
    }
}
