package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.Bm;
import com.swpu.analyse.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface PositionMapper extends JpaRepository<Position, String> {

    void deleteAllByTime(String time);

    List<Position> findByTime(String time);
}
