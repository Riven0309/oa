package com.hjf.oa.dao;

import com.hjf.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("dealRecordDao")
public interface DealRecordDao {

    void insert(DealRecord dealRecord);
    List<DealRecord> selectByClaimVoucher(int cvId);
}
