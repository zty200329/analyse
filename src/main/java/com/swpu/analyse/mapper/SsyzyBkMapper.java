package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.BasicDateAnalysis;
import com.swpu.analyse.entity.SsYzyBk;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface SsyzyBkMapper extends JpaRepository<SsYzyBk, String> {

    List<SsYzyBk> findAllByTime(String time, Sort sort);

    void deleteAllByTime(String time);
}
