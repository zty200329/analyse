package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.Lq;
import com.swpu.analyse.entity.ShRk;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface ShRkMapper extends JpaRepository<ShRk, String> {

    List<ShRk> findByTime(String time, Sort sort);
}
