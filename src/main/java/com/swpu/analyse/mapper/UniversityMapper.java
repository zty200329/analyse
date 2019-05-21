package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cyg
 * @date 2019/5/14 下午12:33
 **/
public interface UniversityMapper extends JpaRepository<University, String> {

}
