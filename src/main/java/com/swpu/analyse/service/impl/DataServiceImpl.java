package com.swpu.analyse.service.impl;

import com.swpu.analyse.dto.ScaleDtoUpadte;
import com.swpu.analyse.entity.*;
import com.swpu.analyse.entity.ScaleDtoInsert;
import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.exception.AnalyseException;
import com.swpu.analyse.mapper.*;
import com.swpu.analyse.service.DataService;
import com.swpu.analyse.util.ExcelUtil;
import com.swpu.analyse.util.RandomUtil;
import com.swpu.analyse.util.ResultVoUtil;
import com.swpu.analyse.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author cyg
 * @date 2019/5/14 上午10:55
 **/
@Slf4j
@Service
@Transactional(rollbackFor = AnalyseException.class)
public class DataServiceImpl implements DataService {

    @Autowired
    private UniversityMapper universityMapper;
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private ScaleMapper scaleMapper;
    @Autowired
    private LqMapper lqMapper;
    @Autowired
    private BmMapper bmMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    BasicMapper basicMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private SsyzyBkMapper ssyzyBkMapper;
    @Autowired
    private SsyzyLqMapper ssyzyLqMapper;
    @Autowired
    private ShRkMapper shRkMapper;
    @Autowired
    private SsTkMapper ssTkMapper;
    @Autowired
    private ProvinceMapper provinceMapper;
    @Autowired
    private PositionMapper positionMapper;

    @Override
    public ResultVo upload(MultipartFile file, String fileName,
                           Integer type, String time) throws IOException {
        basicMapper.deleteAllByTime(time);
        ssyzyBkMapper.deleteAllByTime(time);
        ssyzyLqMapper.deleteAllByTime(time);
        ssTkMapper.deleteAllByTime(time);
        shRkMapper.deleteAllByTime(time);
        positionMapper.deleteAllByTime(time);
        if (file == null) {
            throw new AnalyseException(ResultEnum.UPLOAD_FILE_FAILURE);
        }
        switch (type) {
            //中国985工程、211工程、双一流建设高校汇总表
            case 1:
                excelUtil.getDataUniversitys(file.getInputStream(), fileName);
                break;
            //软科排名
            case 2:
                excelUtil.rkpm(file.getInputStream(), fileName);
                break;
            //专业推免生数量信息
            case 3:
                excelUtil.tms(file.getInputStream(), fileName);
                break;
            //报名库信息
            case 4:
                excelUtil.bm(file.getInputStream(), fileName, time);
                break;
            //录取库信息
            case 5:
                excelUtil.lq(file.getInputStream(), fileName, time);
                break;
            default:
                return ResultVoUtil.error("请选择正确的文件类型");
        }

        return ResultVoUtil.success("数据处理成功");
    }

    @Override
    public ResultVo insertGmLq(ScaleDtoInsert scaleDtoInsert) {
        String id = RandomUtil.getRandomInteger(8);
        Scale scale = new Scale();
        scale.setId(id);
        scale.setTotalBs(scaleDtoInsert.getTotalBs());
        BeanUtils.copyProperties(scaleDtoInsert, scale);
        scaleMapper.save(scale);
        return ResultVoUtil.success("新增成功");
    }

    @Override
    public ResultVo selectGmLq(Integer type) {
        Sort.Order orderTime = Sort.Order.desc("time");
        Sort.Order orderType = Sort.Order.desc("type");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(orderTime);
        orders.add(orderType);
        Sort sort = Sort.by(orders);
        List<Scale> scales = new ArrayList<>();
        if (type == 1) {
            scales = scaleMapper.findByType(type, sort);
        } else {
            scales = scaleMapper.findAll(sort);
            scales.removeIf(scale -> scale.getType() == 1);
        }
        return ResultVoUtil.success(scales);
    }

    @Override
    public ResultVo deleteGmLq(String id) {
        scaleMapper.deleteById(id);
        return ResultVoUtil.success("删除成功");
    }

    @Override
    public ResultVo updateGmLq(ScaleDtoUpadte scaleDtoUpadte) {
        Optional<Scale> scale = scaleMapper.findById(scaleDtoUpadte.getId());
        BeanUtils.copyProperties(scaleDtoUpadte, scale.get());
        scaleMapper.save(scale.get());
        return ResultVoUtil.success("修改成功");
    }

    @Override
    public ResultVo basicDateAnalysis(String time) {
        Optional<BasicDateAnalysis> basicDateAnalysis = basicMapper.findById(time);
        BasicDateAnalysisVo basicDateAnalysisVo = new BasicDateAnalysisVo();
        try {
            BeanUtils.copyProperties(basicDateAnalysis.get(), basicDateAnalysisVo);
        } catch (Exception e) {
            List<Lq> lqs = lqMapper.findByTime(time);
            List<Bm> bms = bmMapper.findByTime(time);
            List<Bm> bmsGirl = bmMapper.findByTimeAndXbm(time, 2);
            basicDateAnalysisVo = getMaxGirlBkMajor(bmsGirl, basicDateAnalysisVo);
            basicDateAnalysisVo = getData(lqs, bms, basicDateAnalysisVo);
            BasicDateAnalysis basicDateAnalysis1 = new BasicDateAnalysis();
            basicDateAnalysis1.setTime(time);
            BeanUtils.copyProperties(basicDateAnalysisVo, basicDateAnalysis1);
            basicMapper.save(basicDateAnalysis1);
        }
        return ResultVoUtil.success(basicDateAnalysisVo);
    }

    @Override
    public ResultVo getChoiceTime() {
        Sort.Order order = Sort.Order.asc("time");
        Sort sort = Sort.by(order);
        List<Lq> lqs = lqMapper.findAll(sort);
        Set<String> hashSet = new HashSet<>();
        for (Lq lq : lqs) {
            hashSet.add(lq.getTime());
        }
        hashSet.add("2018");
        hashSet.add("2017");
        return ResultVoUtil.success(hashSet);
    }

    @Override
    public ResultVo ssYzyBk(String time) {
        List<SsYzyBkVo> ssYzyBkVos = new ArrayList<>();
        Sort.Order orderYxsdm = Sort.Order.asc("yxsdm");
        Sort.Order orderZzmc = Sort.Order.asc("zzmc");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(orderYxsdm);
        orders.add(orderZzmc);
        Sort sort = Sort.by(orders);
        List<SsYzyBk> ssYzyBks = ssyzyBkMapper.findAllByTime(time, sort);
        if (ssYzyBks.size() != 0) {
            for (SsYzyBk ssYzyBk : ssYzyBks) {
                SsYzyBkVo ssYzyBkVo = new SsYzyBkVo();
                BeanUtils.copyProperties(ssYzyBk, ssYzyBkVo);
                ssYzyBkVos.add(ssYzyBkVo);
            }
            return ResultVoUtil.success(ssYzyBkVos);
        }

        List<Bm> bms = bmMapper.findByTime(time);
        if (bms.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        Sort.Order orderDepartmentId = Sort.Order.asc("departmentId");
        Sort.Order orderName = Sort.Order.asc("name");
        List<Sort.Order> orders1 = new ArrayList<Sort.Order>();
        orders1.add(orderDepartmentId);
        orders1.add(orderName);
        Sort sort1 = Sort.by(orders1);
        List<Major> majors = majorMapper.findAll(sort1);
        for (Major major : majors) {
            Department department = departmentMapper.getOne(major.getDepartmentId());
            SsYzyBkVo ssYzyBkVoQ = new SsYzyBkVo();
            SsYzyBkVo ssYzyBkVoF = new SsYzyBkVo();
            ssYzyBkVoQ.setYxsdm(department.getId());
            ssYzyBkVoQ.setYxs(department.getName());
            ssYzyBkVoQ.setBkxxfs("全日制");
            ssYzyBkVoQ.setZzdm(major.getId());
            ssYzyBkVoQ.setZzmc(major.getName());
            ssYzyBkVoF.setYxsdm(department.getId());
            ssYzyBkVoF.setYxs(department.getName());
            ssYzyBkVoF.setBkxxfs("非全日制");
            ssYzyBkVoF.setZzdm(major.getId());
            ssYzyBkVoF.setZzmc(major.getName());
            int by985211Q = 0, bySylQ = 0, bxYjQ = 0, bxWjQ = 0,
                    otherQ = 0, tmsQ = 0, boyQ = 0, totalQ = 0;
            int by985211F = 0, bySylF = 0, bxYjF = 0, bxWjF = 0,
                    otherF = 0, tmsF = 0, boyF = 0, totalF = 0;
            for (Bm bm : bms) {
                //满足一志愿报考我校
                if (bm.getBkdwmc().equals("西南石油大学") && bm.getBkzydm().equals(major.getId())) {
                    SsYzyBkVo ssYzyBkVo = new SsYzyBkVo();
                    //全日制
                    if (bm.getBkxxfs() == 1) {
                        totalQ++;
                        try {
                            University university = excelUtil.getUniversity(bm.getBydw());
                            if (university.getB985() != 0 || university.getB211() != 0) {
                                by985211Q++;
                            }
                            if (university.getSyl() != 0) {
                                bySylQ++;
                            }
                            if (university.getName().equals("西南石油大学")) {
                                if (bm.getByny().contains(Calendar.getInstance().get(Calendar.YEAR) + "")) {
                                    bxYjQ++;
                                } else {
                                    bxWjQ++;
                                }
                            }
                            if (university.getB985() == 0 && university.getB211() == 0 &&
                                    university.getSyl() == 0 && !university.getName().equals("西南石油大学")) {
                                otherQ++;
                            }
                        } catch (EntityNotFoundException e) {
                            otherQ++;
                            //e.printStackTrace();
                        }
                        if (bm.getKsfsm().equals("22") || bm.getKsfsm().equals("2")) {
                            tmsQ++;
                        }
                        if (bm.getXbm() == 1) {
                            boyQ++;
                        }
                    } else {
                        totalF++;
                        try {
                            University university = excelUtil.getUniversity(bm.getBydw());
                            if (university.getB985() != 0 || university.getB211() != 0) {
                                by985211F++;
                            }
                            if (university.getSyl() != 0) {
                                bySylF++;
                            }
                            if (university.getName().equals("西南石油大学")) {
                                if (bm.getByny().contains(Calendar.getInstance().get(Calendar.YEAR) + "")) {
                                    bxYjF++;
                                } else {
                                    bxWjF++;
                                }
                            }
                            if (university.getB985() == 0 && university.getB211() == 0 &&
                                    university.getSyl() == 0 && !university.getName().equals("西南石油大学")) {
                                otherF++;
                            }
                        } catch (EntityNotFoundException e) {
                            otherF++;
                            //e.printStackTrace();
                        }
                        if (bm.getKsfsm().equals("22") || bm.getKsfsm().equals("2")) {
                            tmsF++;
                        }
                        if (bm.getXbm() == 1) {
                            boyF++;
                        }
                    }
                }
            }
            //全日制
            ssYzyBkVoQ.setBy985211(by985211Q);
            ssYzyBkVoQ.setBySyl(bySylQ);
            ssYzyBkVoQ.setBxYj(bxYjQ);
            ssYzyBkVoQ.setBxWj(bxWjQ);
            ssYzyBkVoQ.setOther(otherQ);
            ssYzyBkVoQ.setTms(tmsQ);
            ssYzyBkVoQ.setBoy(boyQ);
            ssYzyBkVoQ.setTotal(totalQ);
            //非全日制
            ssYzyBkVoF.setBy985211(by985211F);
            ssYzyBkVoF.setBySyl(bySylF);
            ssYzyBkVoF.setBxYj(bxYjF);
            ssYzyBkVoF.setBxWj(bxWjF);
            ssYzyBkVoF.setOther(otherF);
            ssYzyBkVoF.setTms(tmsF);
            ssYzyBkVoF.setBoy(boyF);
            ssYzyBkVoF.setTotal(totalF);
            ssYzyBkVos.add(ssYzyBkVoQ);
            ssYzyBkVos.add(ssYzyBkVoF);
        }
        for (SsYzyBkVo ssYzyBkVo : ssYzyBkVos) {
            SsYzyBk ssYzyBk = new SsYzyBk();
            ssYzyBk.setId(RandomUtil.getRandomInteger(10));
            ssYzyBk.setTime(time);
            BeanUtils.copyProperties(ssYzyBkVo, ssYzyBk);
            ssyzyBkMapper.save(ssYzyBk);
        }
        return ResultVoUtil.success(ssYzyBkVos);
    }

    @Override
    public ResultVo ssYzyLq(String time) {
        List<SsYzyLqVo> ssYzyLqVos = new ArrayList<>();
        Sort.Order orderYxsdm = Sort.Order.asc("yxsdm");
        Sort.Order orderZzmc = Sort.Order.asc("zzmc");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(orderYxsdm);
        orders.add(orderZzmc);
        Sort sort1 = Sort.by(orders);
        List<SsYzyLq> ssYzyLqs = ssyzyLqMapper.findAllByTime(time, sort1);
        //先查数据库,没有再重新计算
        if (ssYzyLqs.size() != 0) {
            for (SsYzyLq ssYzyLq : ssYzyLqs) {
                SsYzyLqVo ssYzyLqVo = new SsYzyLqVo();
                BeanUtils.copyProperties(ssYzyLq, ssYzyLqVo);
                ssYzyLqVos.add(ssYzyLqVo);
            }
            return ResultVoUtil.success(ssYzyLqVos);
        }
        Sort.Order order = Sort.Order.asc("departmentId");
        Sort sort2 = Sort.by(order);
        List<Major> majors = majorMapper.findAll(sort2);
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        for (Major major : majors) {
            SsYzyLqVo ssyzyLqVoQ = new SsYzyLqVo();
            SsYzyLqVo ssyzyLqVoF = new SsYzyLqVo();
            Department department = departmentMapper.getOne(major.getDepartmentId());
            ssyzyLqVoQ.setYxsdm(department.getId());
            ssyzyLqVoQ.setYxsmc(department.getName());
            ssyzyLqVoQ.setBkxxfs("全日制");
            ssyzyLqVoQ.setZzdm(major.getId());
            ssyzyLqVoQ.setZzmc(major.getName());
            ssyzyLqVoF.setYxsdm(department.getId());
            ssyzyLqVoF.setYxsmc(department.getName());
            ssyzyLqVoF.setBkxxfs("非全日制");
            ssyzyLqVoF.setZzdm(major.getId());
            ssyzyLqVoF.setZzmc(major.getName());
            int yzy985211Q = 0, yzySylQ = 0, yzyBxYjQ = 0, yzyBxWjQ = 0, yzyOtherQ = 0,
                    tj985211Q = 0, tjSylQ = 0, tjBxYjQ = 0, tjBxWjQ = 0, tjOtherQ = 0,
                    boyQ = 0, totalQ = 0;
            int yzy985211F = 0, yzySylF = 0, yzyBxYjF = 0, yzyBxWjF = 0, yzyOtherF = 0,
                    tj985211F = 0, tjSylF = 0, tjBxYjF = 0, tjBxWjF = 0, tjOtherF = 0,
                    boyF = 0, totalF = 0;
            for (Lq lq : lqs) {
                if (lq.getZydm().equals(major.getId())) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    if (bm != null) {
                        //全日制
                        if (bm.getBkxxfs() == 1) {
                            totalQ++;
                            if (lq.getXbdm() == 1) {
                                boyQ++;
                            }
                            //满足一志愿条件
                            if (bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                                    bm.getBkxxfs() == lq.getXxfsdm()) {
                                Integer[] values = judgeUniversity(yzy985211Q, yzySylQ, yzyBxYjQ, yzyBxWjQ, yzyOtherQ, lq);
                                yzy985211Q = values[0];
                                yzySylQ = values[1];
                                yzyBxYjQ = values[2];
                                yzyBxWjQ = values[3];
                                yzyOtherQ = values[4];
                            } else {
                                Integer[] values = judgeUniversity(tj985211Q, tjSylQ, tjBxYjQ, tjBxWjQ, tjOtherQ, lq);
                                tj985211Q = values[0];
                                tjSylQ = values[1];
                                tjBxYjQ = values[2];
                                tjBxWjQ = values[3];
                                tjOtherQ = values[4];
                            }
                        } else {
                            totalF++;
                            if (lq.getXbdm() == 1) {
                                boyF++;
                            }
                            //满足一志愿条件
                            if (bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) && bm.getBkxxfs() == lq.getXxfsdm()) {
                                Integer[] values = judgeUniversity(yzy985211F, yzySylF, yzyBxYjF, yzyBxWjF, yzyOtherF, lq);
                                yzy985211F = values[0];
                                yzySylF = values[1];
                                yzyBxYjF = values[2];
                                yzyBxWjF = values[3];
                                yzyOtherF = values[4];
                            } else {
                                Integer[] values = judgeUniversity(tj985211F, tjSylF, tjBxYjF, tjBxWjF, tjOtherF, lq);
                                tj985211F = values[0];
                                tjSylF = values[1];
                                tjBxYjF = values[2];
                                tjBxWjF = values[3];
                                tjOtherF = values[4];
                            }
                        }
                    } else {
                        if (lq.getXxfsdm() == 1) {
                            totalQ++;
                            if (lq.getXbdm() == 1) {
                                boyQ++;
                            }
                            Integer[] values = judgeUniversity(tj985211Q, tjSylQ, tjBxYjQ, tjBxWjQ, tjOtherQ, lq);
                            tj985211Q = values[0];
                            tjSylQ = values[1];
                            tjBxYjQ = values[2];
                            tjBxWjQ = values[3];
                            tjOtherQ = values[4];
                        } else {
                            totalF++;
                            if (lq.getXbdm() == 1) {
                                boyF++;
                            }
                            Integer[] values = judgeUniversity(tj985211F, tjSylF, tjBxYjF, tjBxWjF, tjOtherF, lq);
                            tj985211F = values[0];
                            tjSylF = values[1];
                            tjBxYjF = values[2];
                            tjBxWjF = values[3];
                            tjOtherF = values[4];
                        }
                    }
                }
            }
            //全日制
            ssyzyLqVoQ.setYzy985211(yzy985211Q);
            ssyzyLqVoQ.setYzySyl(yzySylQ);
            ssyzyLqVoQ.setYzyBxYj(yzyBxYjQ);
            ssyzyLqVoQ.setYzyBxWj(yzyBxWjQ);
            ssyzyLqVoQ.setYzyOther(yzyOtherQ);
            ssyzyLqVoQ.setTj985211(tj985211Q);
            ssyzyLqVoQ.setTjSyl(tjSylQ);
            ssyzyLqVoQ.setTjBxYj(tjBxYjQ);
            ssyzyLqVoQ.setTjBxWj(tjBxWjQ);
            ssyzyLqVoQ.setTjOther(tjOtherQ);
            ssyzyLqVoQ.setBoy(boyQ);
            ssyzyLqVoQ.setTotal(totalQ);
            ssYzyLqVos.add(ssyzyLqVoQ);
            //非全日制
            ssyzyLqVoF.setYzy985211(yzy985211F);
            ssyzyLqVoF.setYzySyl(yzySylF);
            ssyzyLqVoF.setYzyBxYj(yzyBxYjF);
            ssyzyLqVoF.setYzyBxWj(yzyBxWjF);
            ssyzyLqVoF.setYzyOther(yzyOtherF);
            ssyzyLqVoF.setTj985211(tj985211F);
            ssyzyLqVoF.setTjSyl(tjSylF);
            ssyzyLqVoF.setTjBxYj(tjBxYjF);
            ssyzyLqVoF.setTjBxWj(tjBxWjF);
            ssyzyLqVoF.setTjOther(tjOtherF);
            ssyzyLqVoF.setBoy(boyF);
            ssyzyLqVoF.setTotal(totalF);
            ssYzyLqVos.add(ssyzyLqVoF);
        }
        //保存计算数据
        for (SsYzyLqVo ssYzyLqVo : ssYzyLqVos) {
            SsYzyLq ssYzyLq = new SsYzyLq();
            ssYzyLq.setId(RandomUtil.getRandomInteger(10));
            ssYzyLq.setTime(time);
            BeanUtils.copyProperties(ssYzyLqVo, ssYzyLq);
            ssyzyLqMapper.save(ssYzyLq);
        }
        return ResultVoUtil.success(ssYzyLqVos);
    }

    @Override
    public ResultVo shRk(String time) {
        List<ShRkVo> shRkVos = new ArrayList<>();
        //查询结果排序规则
        Sort.Order orderYxsdm = Sort.Order.asc("yxsdm");
        Sort.Order orderZzmc = Sort.Order.asc("zzmc");
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(orderYxsdm);
        orders.add(orderZzmc);
        Sort sort1 = Sort.by(orders);
        //先查数据库,没有再重新计算
        List<ShRk> shRks = shRkMapper.findByTime(time, sort1);
        if (shRks.size() != 0) {
            for (ShRk shRk : shRks) {
                ShRkVo shRkVo = new ShRkVo();
                BeanUtils.copyProperties(shRk, shRkVo);
                shRkVos.add(shRkVo);
            }
            return ResultVoUtil.success(shRkVos);
        }
        //按照院系id排序查询出所有专业
        Sort.Order order = Sort.Order.asc("departmentId");
        Sort sort2 = Sort.by(order);
        List<Major> majors = majorMapper.findAll(sort2);
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        for (Major major : majors) {
            ShRkVo shRkVoQ = new ShRkVo();
            ShRkVo shRkVoF = new ShRkVo();
            Department department = departmentMapper.getOne(major.getDepartmentId());
            shRkVoQ.setYxsdm(department.getId());
            shRkVoQ.setYxsmc(department.getName());
            shRkVoQ.setBkxxfs("全日制");
            shRkVoQ.setZzdm(major.getId());
            shRkVoQ.setZzmc(major.getName());
            shRkVoF.setYxsdm(department.getId());
            shRkVoF.setYxsmc(department.getName());
            shRkVoF.setBkxxfs("非全日制");
            shRkVoF.setZzdm(major.getId());
            shRkVoF.setZzmc(major.getName());
            int yzyAQ = 0, yzyBQ = 0, yzyCQ = 0, yzyDQ = 0, yzyEQ = 0,
                    tjAQ = 0, tjBQ = 0, tjCQ = 0, tjDQ = 0, tjEQ = 0,
                    totalQ = 0;
            int yzyAF = 0, yzyBF = 0, yzyCF = 0, yzyDF = 0, yzyEF = 0,
                    tjAF = 0, tjBF = 0, tjCF = 0, tjDF = 0, tjEF = 0,
                    totalF = 0;
            for (Lq lq : lqs) {
                if (lq.getZydm().equals(major.getId())) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    if (bm != null) {
                        //全日制
                        if (bm.getBkxxfs() == 1) {
                            totalQ++;
                            //满足一志愿条件
                            if (bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                                    bm.getBkxxfs() == lq.getXxfsdm()) {
                                Integer[] values = shRkUniversity(yzyAQ, yzyBQ, yzyCQ, yzyDQ, yzyEQ, lq);
                                yzyAQ = values[0];
                                yzyBQ = values[1];
                                yzyCQ = values[2];
                                yzyDQ = values[3];
                                yzyEQ = values[4];
                            } else {
                                Integer[] values = shRkUniversity(tjAQ, tjBQ, tjCQ, tjDQ, tjEQ, lq);
                                tjAQ = values[0];
                                tjBQ = values[1];
                                tjCQ = values[2];
                                tjDQ = values[3];
                                tjEQ = values[4];
                            }
                        } else {
                            totalF++;
                            //满足一志愿条件
                            if (bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                                    bm.getBkxxfs() == lq.getXxfsdm()) {
                                Integer[] values = shRkUniversity(yzyAF, yzyBF, yzyCF, yzyDF, yzyEF, lq);
                                yzyAF = values[0];
                                yzyBF = values[1];
                                yzyCF = values[2];
                                yzyDF = values[3];
                                yzyEF = values[4];
                            } else {
                                Integer[] values = shRkUniversity(tjAF, tjBF, tjCF, tjDF, tjEF, lq);
                                tjAF = values[0];
                                tjBF = values[1];
                                tjCF = values[2];
                                tjDF = values[3];
                                tjEF = values[4];
                            }
                        }
                    } else {
                        if (lq.getXxfsdm() == 1) {
                            totalQ++;
                            Integer[] values = shRkUniversity(tjAQ, tjBQ, tjCQ, tjDQ, tjEQ, lq);
                            tjAQ = values[0];
                            tjBQ = values[1];
                            tjCQ = values[2];
                            tjDQ = values[3];
                            tjEQ = values[4];
                        } else {
                            totalF++;
                            Integer[] values = shRkUniversity(tjAF, tjBF, tjCF, tjDF, tjEF, lq);
                            tjAF = values[0];
                            tjBF = values[1];
                            tjCF = values[2];
                            tjDF = values[3];
                            tjEF = values[4];
                        }
                    }
                }
            }
            //全日制
            shRkVoQ.setYzyA(yzyAQ);
            shRkVoQ.setYzyB(yzyBQ);
            shRkVoQ.setYzyC(yzyCQ);
            shRkVoQ.setYzyD(yzyDQ);
            shRkVoQ.setYzyE(yzyEQ);
            shRkVoQ.setTjA(tjAQ);
            shRkVoQ.setTjB(tjBQ);
            shRkVoQ.setTjC(tjCQ);
            shRkVoQ.setTjD(tjDQ);
            shRkVoQ.setTjE(tjEQ);
            shRkVoQ.setTotal(totalQ);
            shRkVos.add(shRkVoQ);
            //非全日制
            shRkVoF.setYzyA(yzyAF);
            shRkVoF.setYzyB(yzyBF);
            shRkVoF.setYzyC(yzyCF);
            shRkVoF.setYzyD(yzyDF);
            shRkVoF.setYzyE(yzyEF);
            shRkVoF.setTjA(tjAF);
            shRkVoF.setTjB(tjBF);
            shRkVoF.setTjC(tjCF);
            shRkVoF.setTjD(tjDF);
            shRkVoF.setTjE(tjEF);
            shRkVoF.setTotal(totalF);
            shRkVos.add(shRkVoF);
        }
        //保存计算数据
        for (ShRkVo shRkVo : shRkVos) {
            ShRk shRk = new ShRk();
            shRk.setId(RandomUtil.getRandomInteger(10));
            shRk.setTime(time);
            BeanUtils.copyProperties(shRkVo, shRk);
            shRkMapper.save(shRk);
        }
        return ResultVoUtil.success(shRkVos);
    }

    @Override
    public ResultVo ssZmt(String time, Integer excellentScore, Integer passScore) {
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        //存储自命题的名称
        HashSet<String> hashSet = new HashSet<>();
        //记录平均分
        TreeSet<Double> treeSet = new TreeSet<>();
        for (Lq lq : lqs) {
            if (!lq.getYwk2mc().equals("无")) {
                hashSet.add(lq.getYwk2dm() + " " + lq.getYwk2mc());
            }
        }
        List<SsZmtVo> ssZmtVos = new ArrayList<>();
        for (String name : hashSet) {
            SsZmtVo ssZmtVo = new SsZmtVo();
            //有效成绩人数,优生人数,及格人数
            int yxcjrs = 0, excellentNum = 0, passNum = 0;
            double averageScore = 0d, maxScore = 0d, minScore = 0d,
                    excellentRate = 0d, passRate = 0d, heightDifference = 0d;
            double total = 0d;
            maxScore = lqs.get(0).getYwk2();
            minScore = lqs.get(0).getYwk2();
            for (Lq lq : lqs) {
                if (name.equals(lq.getYwk2dm() + " " + lq.getYwk2mc())) {
                    yxcjrs++;
                    total += lq.getYwk2();
                    if (lq.getYwk2() > excellentScore) {
                        excellentNum++;
                    }
                    if (lq.getYwk2() > passScore) {
                        passNum++;
                    }
                    if (lq.getYwk2() >= maxScore) {
                        maxScore = lq.getYwk2();
                    }
                    if (lq.getYwk2() <= minScore) {
                        minScore = lq.getYwk2();
                    }
                }
            }
            averageScore = total / yxcjrs;
            excellentRate = (excellentNum + 0.0) / yxcjrs;
            passRate = (passNum + 0.0) / yxcjrs;
            heightDifference = maxScore - minScore;
            ssZmtVo.setName(name);
            ssZmtVo.setYxcjrs(yxcjrs);
            ssZmtVo.setAverageScore((double) Math.round(averageScore * 100) / 100);
            treeSet.add((double) Math.round(averageScore * 100) / 100);
            ssZmtVo.setMaxScore(maxScore);
            ssZmtVo.setMinScore(minScore);
            ssZmtVo.setExcellentNum(excellentNum);
            ssZmtVo.setExcellentRate((double) Math.round(excellentRate * 100) / 100);
            ssZmtVo.setPassNum(passNum);
            ssZmtVo.setPassRate((double) Math.round(passRate * 100) / 100);
            ssZmtVo.setHeightDifference(heightDifference);
            ssZmtVos.add(ssZmtVo);
        }
        int i = 1;
        List<SsZmtVo> ssZmtVosResult = new ArrayList<>();
        while (treeSet.size() > 0) {
            double score = treeSet.pollLast();
            for (SsZmtVo ssZmtVo : ssZmtVos) {
                if (ssZmtVo.getAverageScore() == score) {
                    ssZmtVo.setPm(i);
                    ssZmtVosResult.add(ssZmtVo);
                    i++;
                }
            }
        }
        return ResultVoUtil.success(ssZmtVosResult);
    }

    @Override
    public ResultVo ssTk(String time) {
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        Sort.Order order = Sort.Order.asc("name");
        Sort sort = Sort.by(order);
        List<SsTk> ssTks = ssTkMapper.findByTime(time, sort);
        List<SsTkVo> ssTkVos = new ArrayList<>();
        if (ssTks.size() == 5) {
            for (SsTk ssTk : ssTks) {
                SsTkVo ssTkVo = new SsTkVo();
                BeanUtils.copyProperties(ssTk, ssTkVo);
                ssTkVos.add(ssTkVo);
            }
            return ResultVoUtil.success(ssTkVos);
        }
        ssTkVos.add(getSsTkVo(lqs, "统考英语一", time));
        ssTkVos.add(getSsTkVo(lqs, "统考英语二", time));
        ssTkVos.add(getSsTkVo(lqs, "统考数学一", time));
        ssTkVos.add(getSsTkVo(lqs, "统考数学二", time));
        ssTkVos.add(getSsTkVo(lqs, "统考思想政治理论", time));
        return ResultVoUtil.success(ssTkVos);
    }

    @Override
    public ResultVo tjBk(String time) {
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        List<TjBkVo> tjBkVos = new ArrayList<>();
        for (Lq lq : lqs) {
            Bm bm = bmMapper.findByBmh(lq.getBmh());
            //满足调剂条件
            if (bm == null || !bm.getBkdwdm().equals("10615") || !bm.getBkzydm().equals(lq.getZydm()) ||
                    bm.getBkxxfs() != lq.getXxfsdm()) {
                TjBkVo tjBkVo = new TjBkVo();
                if (lq.getXxfsdm() == 1) {
                    tjBkVo.setXxfsdm("全日制");
                } else {
                    tjBkVo.setXxfsdm("非全日制");
                }
                BeanUtils.copyProperties(lq, tjBkVo);
                try {
                    University university = excelUtil.getUniversity(lq.getBkdwmc());
                    if (university.getB985() != 0 || university.getB211() != 0) {
                        tjBkVo.setBkdw985211("1");
                    } else {
                        tjBkVo.setBkdw985211("");
                    }
                    if (university.getSyl() != 0) {
                        tjBkVo.setBkdwsyl("1");
                    } else {
                        tjBkVo.setBkdwsyl("");
                    }
                    if (university.getName().equals("西南石油大学")) {
                        tjBkVo.setBkdwbx("1");
                    } else {
                        tjBkVo.setBkdwbx("");
                    }
                    if (university.getB985() == 0 && university.getB211() == 0 &&
                            university.getSyl() == 0 && !university.getName().equals("西南石油大学")) {
                        tjBkVo.setBkdwother("1");
                    } else {
                        tjBkVo.setBkdwother("");
                    }
                    tjBkVo.setRkpm(university.getRkpm());
                } catch (EntityNotFoundException e) {
                    //e.printStackTrace();
                }
                tjBkVos.add(tjBkVo);
            }
        }
        return ResultVoUtil.success(tjBkVos);
    }

    @Override
    public ResultVo tjBy(String time) {
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        List<TjByVo> tjByVos = new ArrayList<>();
        for (Lq lq : lqs) {
            Bm bm = bmMapper.findByBmh(lq.getBmh());
            //满足调剂条件
            if (bm == null || !bm.getBkdwdm().equals("10615") || !bm.getBkzydm().equals(lq.getZydm()) ||
                    bm.getBkxxfs() != lq.getXxfsdm()) {
                TjByVo tjByVo = new TjByVo();
                if (lq.getXxfsdm() == 1) {
                    tjByVo.setXxfsdm("全日制");
                } else {
                    tjByVo.setXxfsdm("非全日制");
                }
                BeanUtils.copyProperties(lq, tjByVo);
                try {
                    University university = excelUtil.getUniversity(lq.getBydwmc());
                    if (university.getB985() != 0 || university.getB211() != 0) {
                        tjByVo.setBkdw985211("1");
                    } else {
                        tjByVo.setBkdw985211("");
                    }
                    if (university.getSyl() != 0) {
                        tjByVo.setBkdwsyl("1");
                    } else {
                        tjByVo.setBkdwsyl("");
                    }
                    if (university.getName().equals("西南石油大学")) {
                        tjByVo.setBkdwbx("1");
                    } else {
                        tjByVo.setBkdwbx("");
                    }
                    if (university.getB985() == 0 && university.getB211() == 0 &&
                            university.getSyl() == 0 && !university.getName().equals("西南石油大学")) {
                        tjByVo.setBkdwother("1");
                    } else {
                        tjByVo.setBkdwother("");
                    }
                    tjByVo.setRkpm(university.getRkpm());
                } catch (EntityNotFoundException e) {
                    tjByVo.setBkdw985211("");
                    tjByVo.setBkdwsyl("");
                    tjByVo.setBkdwbx("");
                    tjByVo.setBkdwother("1");
                    tjByVo.setRkpm(0);
                    //e.printStackTrace();
                }
                tjByVos.add(tjByVo);
            }
        }
        return ResultVoUtil.success(tjByVos);
    }

    @Override
    public ResultVo position(String time) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        List<Position> positions = positionMapper.findByTime(time);
        if (positions.size() != 0) {
            for (Position position : positions) {
                hashMap.put(position.getName(), position.getNum());
            }
            return ResultVoUtil.success(hashMap);
        }
        List<Lq> lqs = lqMapper.findByTime(time);
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        List<Province> provinces = provinceMapper.findAll();
        for (Province province : provinces) {
            hashMap.put(province.getName(), 0);
        }
        for (Lq lq : lqs) {
            try {
                University university = excelUtil.getUniversity(lq.getBydwmc());
                Province province = provinceMapper.getOne(university.getProvinceId());
                int value = hashMap.get(province.getName()) + 1;
                hashMap.put(province.getName(), value);
            } catch (EntityNotFoundException e) {
                //e.printStackTrace();
            }
        }
        for (String s : hashMap.keySet()) {
            Position position = new Position();
            position.setId(RandomUtil.getRandomInteger(10));
            position.setTime(time);
            position.setName(s);
            position.setNum(hashMap.get(s));
            positionMapper.save(position);
        }
        return ResultVoUtil.success(hashMap);
    }

    private BasicDateAnalysisVo getMaxGirlBkMajor(List<Bm> bmsGirl, BasicDateAnalysisVo basicDateAnalysisVo) {
        List<Major> majors = majorMapper.findAll();
        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Major major : majors) {
            hashMap.put(major.getId(), 0);
        }
        for (Bm bm : bmsGirl) {
            int value = hashMap.get(bm.getBkzydm()) + 1;
            hashMap.put(bm.getBkzydm(), value);
        }
        int maxValue = hashMap.get(majors.get(0).getId());
        String maxGirlBkMajor = "无";
        for (Major major : majors) {
            if (hashMap.get(major.getId()) > maxValue) {
                maxValue = hashMap.get(major.getId());
                maxGirlBkMajor = major.getName();
            }
        }
        basicDateAnalysisVo.setMaxGirlBkMajor(maxGirlBkMajor);
        return basicDateAnalysisVo;
    }

    private BasicDateAnalysisVo getData(List<Lq> lqs, List<Bm> bms, BasicDateAnalysisVo basicDateAnalysisVo) {
        if (lqs.size() == 0) {
            basicDateAnalysisVo.setMaxAge(0);
            basicDateAnalysisVo.setMinAge(0);
            basicDateAnalysisVo.setPartyMember(0);
            return basicDateAnalysisVo;
        }
        //党员人数
        int partyMember = 0;
        //未婚人数,已婚人数,丧偶人数,离婚人数,其他人数
        int wh = 0, yh = 0, so = 0, lh = 0, other = 0;
        //一志愿分数最高/最低
        Double maxYzy = 0d, minYzy = 0d;
        //调剂分数最高/最低
        Double maxTj = 0d, minTj = 0d;
        //女生录取最多专业,女生录取最少专业
        String maxGirlMajor = null, minGirlMajor = null;
        //结婚人数最多专业/结婚人数最少专业
        String maxHyMajor = null, minHyMajor = null;
        //一志愿报录比最高专业/最低专业
        String maxYzyLqMajor = null, minYzyLqMajor = null;
        //录取男生/女生
        Integer boy = 0, girl = 0;
        //汉族人数/非汉族人数
        Integer han = 0, hanNot = 0;
        //一志愿录取人数/未录取人数
        Integer yzyLq = 0, yiyNotLq = 0;
        int maxAge;
        int minAge;
        try {
            maxAge = Integer.parseInt(lqs.get(0).getCsrq());
            minAge = Integer.parseInt(lqs.get(0).getCsrq());
        } catch (Exception e) {
            maxAge = 20;
            minAge = 25;
        }
        List<Major> majors = majorMapper.findAll();
        //记录各个专业的女生录取情况
        HashMap<String, Integer> hashMap = new HashMap<>();
        //记录各个专业的录取学生婚姻情况
        HashMap<String, Integer> hashMapHy = new HashMap<>();
        //记录各个专业的一志愿录取人数
        HashMap<String, Integer> hashMapYzyLq = new HashMap<>();
        //记录各个专业的一志愿未录取人数
        HashMap<String, Integer> hashMapYzyNotLq = new HashMap<>();
        //记录毕业学校来的人数
        HashMap<String, Integer> hashMapByxx = new HashMap<>();
        //初始化
        for (Major major : majors) {
            hashMap.put(major.getId(), 0);
            hashMapHy.put(major.getId(), 0);
            hashMapYzyLq.put(major.getId(), 0);
            hashMapYzyNotLq.put(major.getId(), 0);
        }
        for (Lq lq : lqs) {
            //年龄最值
            try {
                if (Integer.parseInt(lq.getCsrq()) < maxAge) {
                    maxAge = Integer.parseInt(lq.getCsrq());
                }
                if (Integer.parseInt(lq.getCsrq()) > minAge) {
                    minAge = Integer.parseInt(lq.getCsrq());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //政治面貌(党员)
            if (lq.getZzmmdm() != null && lq.getZzmmdm().equals("01")) {
                partyMember++;
            }
            //婚姻状况(人数)
            switch (lq.getHfdm()) {
                case 1:
                    wh++;
                    break;
                case 2:
                    yh++;
                    break;
                case 3:
                    so++;
                    break;
                case 4:
                    lh++;
                    break;
                case 5:
                    other++;
                    break;
                default:
            }
            //一志愿分数/调剂分数 最值
            try {
                Bm bm = bmMapper.findByBmhAndBkdwmcAndBkzydmAndBkxxfs(lq.getBmh(),
                        "西南石油大学", lq.getZydm(), lq.getXxfsdm());
                if (bm != null) {
                    if (maxYzy == 0d) {
                        maxYzy = lq.getZf();
                    }
                    if (minYzy == 0d) {
                        minYzy = lq.getZf();
                    }
                    if (lq.getZf() > maxYzy) {
                        maxYzy = lq.getZf();
                    }
                    if (lq.getZf() < minYzy) {
                        minYzy = lq.getZf();
                    }
                } else {
                    if (maxTj == 0d) {
                        maxTj = lq.getZf();
                    }
                    if (minTj == 0d) {
                        minTj = lq.getZf();
                    }
                    if (lq.getZf() > maxTj) {
                        maxTj = lq.getZf();
                    }
                    if (lq.getZf() < minTj) {
                        minTj = lq.getZf();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (lq.getBmh().equals(lqs.get(0).getBmh())) {
                    maxTj = lq.getZf();
                    minTj = lq.getZf();
                }
                if (lq.getZf() > maxTj) {
                    maxTj = lq.getZf();
                }
                if (lq.getZf() < minTj) {
                    minTj = lq.getZf();
                }
                //e.printStackTrace();
            }
            //女生录取最值专业 录取女生数量
            if (lq.getXbdm() == 2) {
                int value = hashMap.get(lq.getZydm()) + 1;
                hashMap.put(lq.getBkzydm(), value);
                girl++;
            }
            /*int maxValue = hashMap.get(majors.get(0).getId());
            int minValue = hashMap.get(majors.get(0).getId());
            maxGirlMajor = majors.get(0).getName();
            minGirlMajor = majors.get(0).getName();
            for (Major major : majors) {
                if (hashMap.get(major.getId()) > maxValue) {
                    maxValue = hashMap.get(major.getId());
                    maxGirlMajor = major.getName();
                }
                if (hashMap.get(major.getId()) < minValue) {
                    minValue = hashMap.get(major.getId());
                    minGirlMajor = major.getName();
                }
            }*/
            //录取男生数量
            if (lq.getXbdm() == 1) {
                boy++;
            }
            //民族情况
            if (lq.getMzdm().equals("01")) {
                han++;
            } else {
                hanNot++;
            }
            //婚姻状况(专业)
            if (lq.getHfdm() == 2) {
                int value = hashMapHy.get(lq.getZydm()) + 1;
                hashMapHy.put(lq.getZydm(), value);
            }
            int maxValueHy = hashMapHy.get(majors.get(0).getId());
            int minValueHy = hashMapHy.get(majors.get(0).getId());
            maxHyMajor = majors.get(0).getName();
            minHyMajor = majors.get(0).getName();
            for (Major major : majors) {
                if (hashMapHy.get(major.getId()) > maxValueHy) {
                    maxValueHy = hashMap.get(major.getId());
                    maxHyMajor = major.getName();
                }
                if (hashMapHy.get(major.getId()) < minValueHy) {
                    minValueHy = hashMap.get(major.getId());
                    minHyMajor = major.getName();
                }
            }
            //毕业学校之最
            if (hashMapByxx.get(lq.getBydwmc()) == null) {
                hashMapByxx.put(lq.getBydwmc(), 1);
            } else {
                int value = hashMapByxx.get(lq.getBydwmc()) + 1;
                hashMapByxx.put(lq.getBydwmc(), value);
            }
        }
        //记录一志愿报录比之最(专业以及对应值)
        for (Bm bm : bms) {
            Lq lq = lqMapper.findFirstByBmhAndBkyxsdmAndBkzydmAndXxfsdm(
                    bm.getBmh(), bm.getBkyxsm(), bm.getBkzydm(), bm.getBkxxfs());
            if (lq != null) {
                int value = hashMapYzyLq.get(bm.getBkzydm()) + 1;
                hashMapYzyLq.put(bm.getBkzydm(), value);
            } else {
                int value = hashMapYzyNotLq.get(bm.getBkzydm()) + 1;
                hashMapYzyNotLq.put(bm.getBkzydm(), value);
            }
        }
        double maxYzyBlb = 0.0;
        double minYzyBlb = 1.0;
        maxYzyLqMajor = majors.get(0).getName();
        minYzyLqMajor = majors.get(0).getName();
        for (Major major : majors) {
            if ((hashMapYzyLq.get(major.getId()) + hashMapYzyNotLq.get(major.getId())) != 0) {
                double blb = (hashMapYzyLq.get(major.getId()) + 0d) /
                        (hashMapYzyLq.get(major.getId()) + hashMapYzyNotLq.get(major.getId()));
                if (blb > maxYzyBlb) {
                    maxYzyBlb = blb;
                    maxYzyLqMajor = major.getName();
                }
                if (blb < maxYzyBlb) {
                    minYzyBlb = blb;
                    minYzyLqMajor = major.getName();
                }
            }
        }
        int nowYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            maxAge = nowYear - Integer.parseInt((maxAge + "").substring(0, 4));
            minAge = nowYear - Integer.parseInt((minAge + "").substring(0, 4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //毕业学校之最
        int maxmaxByUniversityValue = 0;
        //毕业人数做多的学校
        String maxByUniversity = null;
        int maxValue = hashMap.get(majors.get(0).getId());
        int minValue = hashMap.get(majors.get(0).getId());
        maxGirlMajor = majors.get(0).getName();
        minGirlMajor = majors.get(0).getName();
        for (Major major : majors) {
            if (hashMap.get(major.getId()) > maxValue) {
                maxValue = hashMap.get(major.getId());
                maxGirlMajor = major.getName();
            }
            if (hashMap.get(major.getId()) < minValue) {
                minValue = hashMap.get(major.getId());
                minGirlMajor = major.getName();
            }
        }
        for (String key : hashMapByxx.keySet()) {
            if (hashMapByxx.get(key) > maxmaxByUniversityValue) {
                maxmaxByUniversityValue = hashMapByxx.get(key);
                maxByUniversity = key;
            }
        }
        basicDateAnalysisVo.setMaxAge(maxAge);
        basicDateAnalysisVo.setMinAge(minAge);
        basicDateAnalysisVo.setPartyMember(partyMember);
        basicDateAnalysisVo.setWh(wh);
        basicDateAnalysisVo.setYh(yh);
        basicDateAnalysisVo.setSo(so);
        basicDateAnalysisVo.setLh(lh);
        basicDateAnalysisVo.setOther(other);
        basicDateAnalysisVo.setMaxYzy(maxYzy);
        basicDateAnalysisVo.setMinYzy(minYzy);
        basicDateAnalysisVo.setMaxTj(maxTj);
        basicDateAnalysisVo.setMinTj(minTj);
        basicDateAnalysisVo.setMaxGirlMajor(maxGirlMajor);
        basicDateAnalysisVo.setMinGirlMajor(minGirlMajor);
        basicDateAnalysisVo.setBoy(boy);
        basicDateAnalysisVo.setGirl(girl);
        basicDateAnalysisVo.setHan(han);
        basicDateAnalysisVo.setHanNot(hanNot);
        basicDateAnalysisVo.setMaxHyMajor(maxHyMajor);
        basicDateAnalysisVo.setMinHyMajor(minHyMajor);
        basicDateAnalysisVo.setMaxByUniversity(maxByUniversity);
        if (maxYzyBlb != 0d) {
            basicDateAnalysisVo.setMaxBlYzyValue(maxYzyBlb);
            basicDateAnalysisVo.setMaxBlYzyMajor(maxYzyLqMajor);
        } else {
            basicDateAnalysisVo.setMaxBlYzyValue(0d);
            basicDateAnalysisVo.setMaxBlYzyMajor("无");
        }
        if (minYzyBlb != 1.0d) {
            basicDateAnalysisVo.setMinBlYzyValue(minYzyBlb);
            basicDateAnalysisVo.setMinBlYzyMajor(minYzyLqMajor);
        } else {
            basicDateAnalysisVo.setMinBlYzyValue(0d);
            basicDateAnalysisVo.setMinBlYzyMajor("无");
        }
        return basicDateAnalysisVo;
    }

    private Integer[] judgeUniversity(Integer yzy985211, Integer yzySyl, Integer yzyBxYj, Integer yzyBxWj, Integer yzyOther, Lq lq) {
        Integer[] values = new Integer[5];
        try {
            University university = excelUtil.getUniversity(lq.getBydwmc());
            if (university.getB985() != 0 || university.getB211() != 0) {
                yzy985211++;
            }
            if (university.getSyl() != 0) {
                yzySyl++;
            }
            if (university.getName().equals("西南石油大学")) {
                if (lq.getByny().contains(Calendar.getInstance().get(Calendar.YEAR) + "")) {
                    yzyBxYj++;
                } else {
                    yzyBxWj++;
                }
            }
            if (university.getB985() == 0 && university.getB211() == 0 &&
                    university.getSyl() == 0 && !university.getName().equals("西南石油大学")) {
                yzyOther++;
            }
        } catch (EntityNotFoundException e) {
            yzyOther++;
            //e.printStackTrace();
        }
        values[0] = yzy985211;
        values[1] = yzySyl;
        values[2] = yzyBxYj;
        values[3] = yzyBxWj;
        values[4] = yzyOther;
        return values;
    }

    private Integer[] shRkUniversity(Integer a, Integer b, Integer c, Integer d, Integer e, Lq lq) {
        Integer[] values = new Integer[5];
        try {
            University university = excelUtil.getUniversity(lq.getBydwmc());
            University swpu = excelUtil.getUniversity("西南石油大学");
            Integer swpuPm = swpu.getRkpm();
            if (university.getRkpm() == 0) {
                e++;
            } else if (university.getName().equals("西南石油大学")) {
                a++;
            } else if (university.getRkpm() < swpuPm && (swpuPm - university.getRkpm()) > 25) {
                b++;
            } else if (Math.abs(swpuPm - university.getRkpm()) <= 25) {
                c++;
            } else {
                d++;
            }
        } catch (EntityNotFoundException e1) {
            e++;
            //e.printStackTrace();
        }
        values[0] = a;
        values[1] = b;
        values[2] = c;
        values[3] = d;
        values[4] = e;
        return values;
    }

    private SsTkVo getSsTkVo(List<Lq> lqs, String name, String time) {
        SsTkVo ssTkVo = new SsTkVo();
        int numYzy = 0;
        double pjYzy = 0d, fcYzy = 0d, totalScoreYzy = 0d;
        double pjTj = 0d, fcTj = 0d, totalScoreTj = 0;
        int numTj = 0;
        if (name.equals("统考英语一") || name.equals("统考英语二")) {
            ssTkVo.setName(name + "(100)");
            for (Lq lq : lqs) {
                if (lq.getWgymc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        numYzy++;
                        totalScoreYzy += lq.getWgy();
                    } else {
                        numTj++;
                        totalScoreTj += lq.getWgy();
                    }
                }
            }
            pjYzy = totalScoreYzy / numYzy;
            pjTj = totalScoreTj / numTj;
            //记录方差乘n
            double countYzy = 0d;
            //记录方差乘n
            double countTj = 0d;
            for (Lq lq : lqs) {
                if (lq.getWgymc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        countYzy += Math.pow((lq.getWgy() - pjYzy), 2);
                    } else {
                        countTj += Math.pow((lq.getWgy() - pjTj), 2);
                    }
                }
            }
            fcYzy = countYzy / numYzy;
            fcTj = countTj / numTj;
        }
        if (name.equals("统考数学一") || name.equals("统考数学二")) {
            ssTkVo.setName(name + "(150)");
            for (Lq lq : lqs) {
                if (lq.getYwk1mc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        numYzy++;
                        totalScoreYzy += lq.getYwk1();
                    } else {
                        numTj++;
                        totalScoreTj += lq.getYwk1();
                    }
                }
            }
            pjYzy = totalScoreYzy / numYzy;
            pjTj = totalScoreTj / numTj;
            //记录方差乘n
            double countYzy = 0d;
            //记录方差乘n
            double countTj = 0d;
            for (Lq lq : lqs) {
                if (lq.getYwk1mc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        countYzy += Math.pow((lq.getYwk1() - pjYzy), 2);
                    } else {
                        countTj += Math.pow((lq.getYwk1() - pjTj), 2);
                    }
                }
            }
            fcYzy = countYzy / numYzy;
            fcTj = countTj / numTj;
        }
        if (name.equals("统考思想政治理论")) {
            ssTkVo.setName(name + "(100)");
            for (Lq lq : lqs) {
                if (lq.getZzllmc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        numYzy++;
                        totalScoreYzy += lq.getZzll();
                    } else {
                        numTj++;
                        totalScoreTj += lq.getZzll();
                    }
                }
            }
            pjYzy = totalScoreYzy / numYzy;
            pjTj = totalScoreTj / numTj;
            //记录方差乘n
            double countYzy = 0d;
            //记录方差乘n
            double countTj = 0d;
            for (Lq lq : lqs) {
                if (lq.getZzllmc().equals(name)) {
                    Bm bm = bmMapper.findByBmh(lq.getBmh());
                    //满足一志愿条件
                    if (bm != null && bm.getBkdwdm().equals("10615") && bm.getBkzydm().equals(lq.getZydm()) &&
                            bm.getBkxxfs() == lq.getXxfsdm()) {
                        countYzy += Math.pow((lq.getZzll() - pjYzy), 2);
                    } else {
                        countTj += Math.pow((lq.getZzll() - pjTj), 2);
                    }
                }
            }
            fcYzy = countYzy / numYzy;
            fcTj = countTj / numTj;
        }
        ssTkVo.setYzyPj((double) Math.round(pjYzy * 100) / 100);
        ssTkVo.setYzyFc((double) Math.round(fcYzy * 100) / 100);
        ssTkVo.setTjPj((double) Math.round(pjTj * 100) / 100);
        ssTkVo.setTjFc((double) Math.round(fcTj * 100) / 100);
        SsTk ssTk = new SsTk();
        BeanUtils.copyProperties(ssTkVo, ssTk);
        ssTk.setTime(time);
        ssTk.setId(RandomUtil.getRandomInteger(8));
        ssTkMapper.save(ssTk);
        return ssTkVo;
    }
}
