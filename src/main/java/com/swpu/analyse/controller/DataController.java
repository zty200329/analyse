package com.swpu.analyse.controller;

import com.swpu.analyse.dto.ScaleDtoUpadte;
import com.swpu.analyse.entity.ScaleDtoInsert;
import com.swpu.analyse.service.DataService;
import com.swpu.analyse.service.UserService;
import com.swpu.analyse.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author cyg
 * @date 2019/5/14 上午9:47
 **/
@RestController
@CrossOrigin
@RequestMapping("data")
@Api(tags = "数据")
public class DataController {

    @Autowired
    private DataService dataService;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件 " +
            "1-(985、211、双一流建设高校汇总表)" +
            "2-(软科排名)" +
            "3-(专业推免生数量信息)" +
            "4-(报名库信息)" +
            "5-(录取库信息)")
    public ResultVo upload(MultipartFile file, String fileName, Integer type, String time) throws IOException {

        return dataService.upload(file, fileName, type, time);
    }

    @PostMapping("/insertGmLq")
    @ApiOperation(value = "新增招生规模/招生情况")
    public ResultVo insertGmLq(ScaleDtoInsert scaleDtoInsert) {

        return dataService.insertGmLq(scaleDtoInsert);
    }

    @PostMapping("/selectGmLq")
    @ApiOperation(value = "查询招生规模/招生情况 1-(全国) 2-(本校)")
    public ResultVo selectGmLq(Integer type) {
        return dataService.selectGmLq(type);
    }

    @PostMapping("/updateGmLq")
    @ApiOperation(value = "修改招生规模/招生情况")
    public ResultVo updateGmLq(ScaleDtoUpadte scaleDtoUpadte) {

        return dataService.updateGmLq(scaleDtoUpadte);
    }

    @PostMapping("/basicDateAnalysis")
    @ApiOperation(value = "基本数据分析查询")
    public ResultVo basicDateAnalysis(String time) {

        return dataService.basicDateAnalysis(time);
    }

    @PostMapping("/getChoiceTime")
    @ApiOperation(value = "返回时间选择列表")
    public ResultVo getChoiceTime() {

        return dataService.getChoiceTime();
    }

    @PostMapping("/ssYzyBk")
    @ApiOperation(value = "表三-(硕士各专业一志愿报考概况)")
    public ResultVo ssYzyBk(String time) {

        return dataService.ssYzyBk(time);
    }

    @PostMapping("/ssYzyLq")
    @ApiOperation(value = "表四-(硕士各专业一志愿录取概况)")
    public ResultVo ssYzyLq(String time) {

        return dataService.ssYzyLq(time);
    }

    @PostMapping("/shRk")
    @ApiOperation(value = "表五-(上海软科排名)")
    public ResultVo shRk(String time) {

        return dataService.shRk(time);
    }

    @PostMapping("/ssZmt")
    @ApiOperation(value = "表六-(自命题成绩分析)")
    public ResultVo ssZmt(String time, Integer excellentScore, Integer passScore) {

        return dataService.ssZmt(time, excellentScore, passScore);
    }

    @PostMapping("/ssTk")
    @ApiOperation(value = "表七-(统考题成绩分析)")
    public ResultVo ssTk(String time) {

        return dataService.ssTk(time);
    }

    @PostMapping("/tjBk")
    @ApiOperation(value = "表八-(调剂生报考学校)")
    public ResultVo tjBk(String time) {

        return dataService.tjBk(time);
    }

}
