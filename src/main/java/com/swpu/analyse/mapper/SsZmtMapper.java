package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.BasicDateAnalysis;
import com.swpu.analyse.entity.SsZmt;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface SsZmtMapper extends JpaRepository<SsZmt, String> {
    void deleteAllByTime(String time);
    List<SsZmt> findAllByTime(String time, Sort sort);
}
