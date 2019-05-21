package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.Bm;
import com.swpu.analyse.entity.Lq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface LqMapper extends JpaRepository<Lq, String> {

    void deleteAllByTime(String time);

    List<Lq> findByTime(String time);

    List<Lq> findByBmh(String bmh);

    Lq findFirstByBmhAndBkyxsdmAndBkzydmAndXxfsdm(String bmh, String bkyxsdm,
                                                  String bkzydm,Integer xxfsdm);
}
