package com.swpu.analyse.mapper;

import com.swpu.analyse.entity.BasicDateAnalysis;
import com.swpu.analyse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cyg
 * @date 2019/5/15 下午1:33
 **/
public interface UserMapper extends JpaRepository<User, String> {
}
