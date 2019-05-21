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
import com.swpu.analyse.vo.BasicDateAnalysisVo;
import com.swpu.analyse.vo.ResultVo;
import com.swpu.analyse.vo.SsYzyBkVo;
import com.swpu.analyse.vo.SsYzyLqVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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

    @Override
    public ResultVo upload(MultipartFile file, String fileName, Integer type) throws IOException {
        basicMapper.deleteAll();
        ssyzyBkMapper.deleteAll();
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
                excelUtil.bm(file.getInputStream(), fileName);
                break;
            //录取库信息
            case 5:
                excelUtil.lq(file.getInputStream(), fileName);
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
        scale.setTime((new Date().getYear() + 1900) + "");
        scale.setTotalBs(scaleDtoInsert.getTotalBs());
        BeanUtils.copyProperties(scaleDtoInsert, scale);
        scaleMapper.save(scale);
        return ResultVoUtil.success("新增成功");
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
        List<Lq> lqs = lqMapper.findAll();
        Set<String> hashSet = new HashSet<>();
        for (Lq lq : lqs) {
            hashSet.add(lq.getTime());
        }
        hashSet.add("2018");
        hashSet.add("2017");
        hashSet.add("2016");
        hashSet.add("2015");
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
                                if (bm.getByny().contains(new Date().getYear() + 1900 + "")) {
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
                                if (bm.getByny().contains(new Date().getYear() + 1900 + "")) {
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
        /* int total = 0;
        for (SsYzyBkVo ssYzyBkVo : ssYzyBkVos) {
            total += ssYzyBkVo.getTotal();
        }
        System.out.println(total);*/
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
        Sort.Order order = Sort.Order.asc("departmentId");
        Sort sort = Sort.by(order);
        List<Major> majors = majorMapper.findAll(sort);
        List<Lq> lqs = lqMapper.findAll();
        if (lqs.size() == 0) {
            return ResultVoUtil.error("没有数据");
        }
        for (Major major : majors) {
            SsYzyLqVo ssYzyLqVoQ = new SsYzyLqVo();
            SsYzyLqVo ssYzyLqVoF = new SsYzyLqVo();
            Department department = departmentMapper.getOne(major.getDepartmentId());
            ssYzyLqVoQ.setYxsdm(department.getId());
            ssYzyLqVoQ.setYxsmc(department.getName());
            ssYzyLqVoQ.setBkxxfs("全日制");
            ssYzyLqVoQ.setZzdm(major.getId());
            ssYzyLqVoQ.setZzmc(major.getName());
            ssYzyLqVoF.setYxsdm(department.getId());
            ssYzyLqVoF.setYxsmc(department.getName());
            ssYzyLqVoF.setBkxxfs("非全日制");
            ssYzyLqVoF.setZzdm(major.getId());
            ssYzyLqVoF.setZzmc(major.getName());
            int yzy985211Q = 0, yzySylQ = 0, yzyBxYjQ = 0, yzyBxWjQ = 0, yzyOtherQ = 0,
                    tj985211Q = 0, tjSylQ = 0, tjBxYjQ = 0, tjBxWjQ = 0, tjOtherQ = 0;
            int yzy985211F = 0, yzySylF = 0, yzyBxYjF = 0, yzyBxWjF = 0, yzyOtherF = 0,
                    tj985211F = 0, tjSylF = 0, tjBxYjF = 0, tjBxWjF = 0, tjOtherF = 0;
            for (Lq lq : lqs) {
                Bm bm = bmMapper.findByBmhAndBkdwmcAndBkzydmAndBkxxfs(lq.getBmh(),
                        "西南石油大学", lq.getZydm(), lq.getXxfsdm());
                //满足一志愿条件
                if (bm != null) {

                }
            }
        }
        return ResultVoUtil.success();
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
        int nowYear = new Date().getYear() + 1900;
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
}