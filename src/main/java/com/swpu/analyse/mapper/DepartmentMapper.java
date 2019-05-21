package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.BasicDateAnalysis;
import com.swpu.analyse.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface DepartmentMapper extends JpaRepository<Department, String> {

}
