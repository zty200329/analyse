package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.ShRk;
import com.swpu.analyse.entity.SsTk;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:50
 **/
public interface SsTkMapper extends JpaRepository<SsTk, String> {
    void deleteAllByTime(String time);

    List<SsTk> findByTime(String time, Sort sort);
}
