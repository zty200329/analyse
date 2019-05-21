package com.swpu.analyse.service;

import com.swpu.analyse.dto.ScaleDtoUpadte;
import com.swpu.analyse.entity.ScaleDtoInsert;
import com.swpu.analyse.vo.ResultVo;
import org.springframework.web.multipart.MultipartFile;

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
    ResultVo upload(MultipartFile file, String fileName, Integer type) throws IOException;

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
    ResultVo selectGmLq(ScaleDtoInsert scaleDtoInsert);

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
}
