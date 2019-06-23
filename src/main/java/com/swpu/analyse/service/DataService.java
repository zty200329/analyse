package com.swpu.analyse.service;

import com.swpu.analyse.dto.ScaleDtoUpadte;
import com.swpu.analyse.entity.ScaleDtoInsert;
import com.swpu.analyse.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 数据处理
 *
 * @author cyg
 * @date 2019/5/14 上午10:24
 **/
public interface DataService {

    /**
     * 功能描述: <br>
     * 〈上传文件,数据处理入库〉
     *
     * @param file     文件
     * @param fileName 文件名
     * @param type     文件类型
     * @return ResultVo
     * @author cyg
     * @date 19-05-18 下午12:14
     */
    ResultVo upload(MultipartFile file, String fileName, Integer type, String time) throws IOException;

    /**
     * 功能描述: <br>
     * 〈下载文件〉
     *
     * @param type 文件类型
     * @return
     * @author cyg
     * @date 19-6-6 下午10:49
     */
    ResultVo download(Integer type, HttpServletRequest request, HttpServletResponse response)throws IOException;

    /**
     * 功能描述: <br>
     * 〈新增招生规模/招生情况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/17 下午12:08
     */
    ResultVo insertGmLq(ScaleDtoInsert scaleDtoInsert);

    /**
     * 功能描述: <br>
     * 〈查看招生规模/招生情况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/17 下午12:08
     */
    ResultVo selectGmLq(Integer type);

    /**
     * 功能描述: <br>
     * 〈删除招生规模/招生情况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/17 下午12:09
     */
    ResultVo deleteGmLq(String id);

    /**
     * 功能描述: <br>
     * 〈修改招生规模/招生情况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/17 下午12:09
     */
    ResultVo updateGmLq(ScaleDtoUpadte scaleDtoUpadte);

    /**
     * 功能描述: <br>
     * 〈基本数据分析查询〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/17 下午11:37
     */
    ResultVo basicDateAnalysis(String time);

    /**
     * 功能描述: <br>
     * 〈返回时间选择列表〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/18 下午7:53
     */
    ResultVo getChoiceTime();

    /**
     * 功能描述: <br>
     * 〈硕士各专业一志愿报考概况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/20 下午12:14
     */
    ResultVo ssYzyBk(String time);

    /**
     * 功能描述: <br>
     * 〈硕士各专业一志愿录取概况〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/20 下午12:14
     */
    ResultVo ssYzyLq(String time);

    /**
     * 功能描述: <br>
     * 〈上海软科排名〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/20 下午12:14
     */
    ResultVo shRk(String time);

    /**
     * 功能描述: <br>
     * 〈自命题成绩分析〉
     *
     * @param time              年份(2019)
     * @param excellentScore100 优秀分数(满分100)
     * @param passScore100      及格分数(满分100)
     * @param excellentScore150 优秀分数(满分150)
     * @param passScore150      及格分数(满分150)
     * @return
     * @author cyg
     * @date 2019/5/26 下午11:03
     */
    ResultVo ssZmt(String time, Integer excellentScore100, Integer passScore100,
                   Integer excellentScore150, Integer passScore150);

    /**
     * 功能描述: <br>
     * 〈统考题成绩分析〉
     *
     * @param time 年份(2019)
     * @return
     * @author cyg
     * @date 2019/5/27 下午4:01
     */
    ResultVo ssTk(String time);

    /**
     * 功能描述: <br>
     * 〈调剂生报考学校〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/28 上午11:39
     */
    ResultVo tjBk(String time);

    /**
     * 功能描述: <br>
     * 〈调剂生毕业学校〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/28 上午11:39
     */
    ResultVo tjBy(String time);

    /**
     * 功能描述: <br>
     * 〈学生的毕业省份信息〉
     *
     * @param
     * @return
     * @author cyg
     * @date 2019/5/28 下午9:08
     */
    ResultVo position(String time);
}
