package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.Bm;
import com.swpu.analyse.entity.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface BmMapper extends JpaRepository<Bm, String> {

    void deleteAllByTime(String time);

    Bm findByBmhAndBkdwmcAndBkzydmAndBkxxfs(String bmh, String bkdwmc,
                                            String bkzydm, Integer xxfs);

    Bm findByBmh(String bmh);

    List<Bm> findByTime(String time);

    List<Bm> findByTimeAndXbm(String time, Integer xbm);
}
