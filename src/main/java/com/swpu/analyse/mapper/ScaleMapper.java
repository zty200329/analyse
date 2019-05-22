package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.Bm;
import com.swpu.analyse.entity.Scale;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface ScaleMapper extends JpaRepository<Scale, String> {

    List<Scale> findByType(Integer type, Sort sort);
}
