package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.BasicDateAnalysis;
import com.swpu.analyse.entity.Bm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface BasicMapper extends JpaRepository<BasicDateAnalysis, String> {
    void deleteAllByTime(String time);
}
