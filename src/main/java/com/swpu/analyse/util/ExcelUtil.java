package com.swpu.analyse.util;

import com.swpu.analyse.entity.Bm;
import com.swpu.analyse.entity.Lq;
import com.swpu.analyse.entity.Major;
import com.swpu.analyse.entity.University;
import com.swpu.analyse.enums.ResultEnum;
import com.swpu.analyse.exception.AnalyseException;
import com.swpu.analyse.mapper.BmMapper;
import com.swpu.analyse.mapper.LqMapper;
import com.swpu.analyse.mapper.MajorMapper;
import com.swpu.analyse.mapper.UniversityMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 解析Excel
 *
 * @author cyg
 * @date 2019/5/14 上午10:59
 **/
@Component
@Slf4j
public class ExcelUtil {

    @Autowired
    private UniversityMapper universityMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private BmMapper bmMapper;
    @Autowired
    private LqMapper lqMapper;

    /**
     * 985/211/双一流信息
     **/
    public void getDataUniversitys(InputStream inputStream, String fileName) throws IOException {

        List<University> universities = universityMapper.findAll();
        for (University university : universities) {
            university.setB211(0);
            university.setB985(0);
            university.setSyl(0);
        }
        universityMapper.saveAll(universities);
        Workbook wb = getWorkbook(inputStream, fileName);
        Sheet sheet = wb.getSheetAt(0);
        universities = new ArrayList<>();
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            try {
                String name = row.getCell(0).getStringCellValue();
                University university = getUniversity(name);
                university.setSubjection(row.getCell(2).getStringCellValue());
                if (!row.getCell(3).getStringCellValue().equals("")) {
                    university.setB985(1);
                } else {
                    university.setB985(0);
                }
                if (!row.getCell(4).getStringCellValue().equals("")) {
                    university.setB211(1);
                } else {
                    university.setB211(0);
                }
                if (!row.getCell(5).getStringCellValue().equals("")) {
                    university.setSyl(1);
                } else {
                    university.setSyl(0);
                }
                row.getCell(6).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                university.setNumberOfYlXk((int) row.getCell(6).getNumericCellValue());
                universities.add(university);
            } catch (EntityNotFoundException e) {
                if (!row.getCell(0).getStringCellValue().equals("高校名称")) {
                    log.info(row.getCell(0).getStringCellValue());
                }
                e.getMessage();
            }
        }
        universityMapper.saveAll(universities);
    }

    /**
     * 软科排名
     **/
    public void rkpm(InputStream inputStream, String fileName) throws IOException {
        List<University> universities = universityMapper.findAll();
        for (University university : universities) {
            university.setRkpm(0);
        }
        universityMapper.saveAll(universities);
        Workbook wb = getWorkbook(inputStream, fileName);
        Sheet sheet = wb.getSheetAt(0);
        universities = new ArrayList<>();
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String name = row.getCell(1).getStringCellValue();
            try {
                University university = getUniversity(name);
                row.getCell(0).setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                university.setRkpm((int) row.getCell(0).getNumericCellValue());
                universities.add(university);
            } catch (EntityNotFoundException e) {
                e.getMessage();
            }
            universityMapper.saveAll(universities);
        }
    }

    /**
     * 推免权
     **/
    public void tms(InputStream inputStream, String fileName) throws IOException {
        List<Major> majors = majorMapper.findAll();
        for (Major major : majors) {
            major.setTms(0);
        }
        majorMapper.saveAll(majors);
        Workbook wb = getWorkbook(inputStream, fileName);
        Sheet sheet = wb.getSheetAt(0);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            String majorId = row.getCell(0).getStringCellValue();
            Major major = majorMapper.getOne(majorId);
            try {
                major.setTms((int) row.getCell(1).getNumericCellValue());
                majorMapper.save(major);
            } catch (EntityNotFoundException e) {
                if (!majorId.equals("总计")) {
                    System.out.println(majorId);
                }
                e.getMessage();
            }
        }
    }

    public void bm(InputStream inputStream, String fileName, String time) throws IOException {
        bmMapper.deleteAllByTime(time);
        Workbook wb = getWorkbook(inputStream, fileName);
        Sheet sheet = wb.getSheetAt(0);
        Row rowFirst = sheet.getRow(0);
        String[] bxs = {"bmh", "xm", "ksfsm", "csrq", "mzm", "xbm", "hfm", "zzmmm", "bydw", "bydwm", "byzymc",
                "byny", "bkdwdm", "bkdwmc", "bkyxsm", "bkyxsmc", "bkzydm", "bkzymc", "bkxxfs",
                "zzllmc", "wgymc", "ywk1mc", "ywk2mc"};
        HashMap<String, Integer> hashMap = getMapping(bxs, rowFirst);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            Bm bm = new Bm();
            try {
                bm.setBmId(time + "-" + RandomUtil.getRandomInteger(8));
                row.getCell(hashMap.get("bmh")).setCellType(HSSFCell.CELL_TYPE_STRING);
                bm.setBmh(row.getCell(hashMap.get("bmh")).getStringCellValue());
                bm.setXm(row.getCell(hashMap.get("xm")).getStringCellValue());
                bm.setKsfsm(row.getCell(hashMap.get("ksfsm")).getStringCellValue());
                bm.setCsrq(row.getCell(hashMap.get("csrq")).getStringCellValue());
                bm.setMzm(row.getCell(hashMap.get("mzm")).getStringCellValue());
                bm.setXbm(Integer.parseInt(row.getCell(hashMap.get("xbm")).getStringCellValue()));
                bm.setHfm(Integer.parseInt(row.getCell(hashMap.get("hfm")).getStringCellValue()));
                bm.setZzmmm(row.getCell(hashMap.get("zzmmm")).getStringCellValue());
                bm.setBydw(row.getCell(hashMap.get("bydw")).getStringCellValue());
                bm.setBydwm(row.getCell(hashMap.get("bydwm")).getStringCellValue());
                bm.setByzymc(row.getCell(hashMap.get("byzymc")).getStringCellValue());
                bm.setByny(row.getCell(hashMap.get("byny")).getStringCellValue());
                bm.setBkdwdm(row.getCell(hashMap.get("bkdwdm")).getStringCellValue());
                bm.setBkdwmc(row.getCell(hashMap.get("bkdwmc")).getStringCellValue());
                bm.setBkyxsm(row.getCell(hashMap.get("bkyxsm")).getStringCellValue());
                bm.setBkyxsmc(row.getCell(hashMap.get("bkyxsmc")).getStringCellValue());
                bm.setBkzydm(row.getCell(hashMap.get("bkzydm")).getStringCellValue());
                bm.setBkzymc(row.getCell(hashMap.get("bkzymc")).getStringCellValue());
                bm.setBkxxfs(Integer.parseInt(row.getCell(hashMap.get("bkxxfs")).getStringCellValue()));
                bm.setZzllmc(row.getCell(hashMap.get("zzllmc")).getStringCellValue());
                bm.setWgymc(row.getCell(hashMap.get("wgymc")).getStringCellValue());
                bm.setYwk1mc(row.getCell(hashMap.get("ywk1mc")).getStringCellValue());
                bm.setYwk2mc(row.getCell(hashMap.get("ywk2mc")).getStringCellValue());
                bm.setTime(time);
                bmMapper.save(bm);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("第{}行数据有误", (r + 1));
            }
        }
    }

    public void lq(InputStream inputStream, String fileName, String time) throws IOException {
        lqMapper.deleteAllByTime(time);
        Workbook wb = getWorkbook(inputStream, fileName);
        Sheet sheet = wb.getSheetAt(0);
        Row rowFirst = sheet.getRow(0);
        String[] lqs = {"bmh", "ksbh", "xm", "yxsdm", "yxsmc", "zydm", "zymc", "xxfsdm", "bkdwmc", "bkyxsdm", "bkyxsmc",
                "bkzydm", "csrq", "mzdm", "xbdm", "hfdm", "zzmmdm", "kslymc", "bydwmc",
                "byzydm", "byzymc", "byny", "zzllmc", "wgymc", "ywk1mc", "ywk2dm", "ywk2mc", "zzll", "wgy",
                "ywk1", "ywk2", "zf"};
        HashMap<String, Integer> hashMap = getMapping(lqs, rowFirst);
        int sum = 0;
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            Lq lq = new Lq();
            try {
                lq.setLqId(time + "-" + RandomUtil.getRandomInteger(8));
                row.getCell(hashMap.get("bmh")).setCellType(HSSFCell.CELL_TYPE_STRING);
                lq.setBmh(row.getCell(hashMap.get("bmh")).getStringCellValue());
                row.getCell(hashMap.get("ksbh")).setCellType(HSSFCell.CELL_TYPE_STRING);
                lq.setKsbh(row.getCell(hashMap.get("ksbh")).getStringCellValue());
                lq.setXm(row.getCell(hashMap.get("xm")).getStringCellValue());
                lq.setYxsdm(row.getCell(hashMap.get("yxsdm")).getStringCellValue());
                lq.setYxsmc(row.getCell(hashMap.get("yxsmc")).getStringCellValue());
                lq.setZydm(row.getCell(hashMap.get("zydm")).getStringCellValue());
                lq.setZymc(row.getCell(hashMap.get("zymc")).getStringCellValue());
                lq.setXxfsdm(Integer.parseInt(row.getCell(hashMap.get("xxfsdm")).getStringCellValue()));
                lq.setBkdwmc(row.getCell(hashMap.get("bkdwmc")).getStringCellValue());
                lq.setBkyxsdm(row.getCell(hashMap.get("bkyxsdm")).getStringCellValue());
                lq.setBkyxsmc(row.getCell(hashMap.get("bkyxsmc")).getStringCellValue());
                lq.setBkzydm(row.getCell(hashMap.get("bkzydm")).getStringCellValue());
                lq.setCsrq(row.getCell(hashMap.get("csrq")).getStringCellValue());
                lq.setMzdm(row.getCell(hashMap.get("mzdm")).getStringCellValue());
                lq.setXbdm(Integer.parseInt(row.getCell(hashMap.get("xbdm")).getStringCellValue()));
                lq.setHfdm(Integer.parseInt(row.getCell(hashMap.get("hfdm")).getStringCellValue()));
                lq.setZzmmdm(row.getCell(hashMap.get("zzmmdm")).getStringCellValue());
                lq.setKslymc(row.getCell(hashMap.get("kslymc")).getStringCellValue());
                lq.setBydwmc(row.getCell(hashMap.get("bydwmc")).getStringCellValue());
                lq.setByzydm(row.getCell(hashMap.get("byzydm")).getStringCellValue());
                lq.setByzymc(row.getCell(hashMap.get("byzymc")).getStringCellValue());
                lq.setByny(row.getCell(hashMap.get("byny")).getStringCellValue());
                lq.setZzllmc(row.getCell(hashMap.get("zzllmc")).getStringCellValue());
                lq.setWgymc(row.getCell(hashMap.get("wgymc")).getStringCellValue());
                lq.setYwk1mc(row.getCell(hashMap.get("ywk1mc")).getStringCellValue());
                lq.setYwk2dm(row.getCell(hashMap.get("ywk2dm")).getStringCellValue());
                lq.setYwk2mc(row.getCell(hashMap.get("ywk2mc")).getStringCellValue());
                lq.setZzll(row.getCell(hashMap.get("zzll")).getNumericCellValue());
                lq.setWgy(row.getCell(hashMap.get("wgy")).getNumericCellValue());
                lq.setYwk1(row.getCell(hashMap.get("ywk1")).getNumericCellValue());
                lq.setYwk2(row.getCell(hashMap.get("ywk2")).getNumericCellValue());
                lq.setZf(row.getCell(hashMap.get("zf")).getNumericCellValue());
                lq.setTime(time);
                lqMapper.save(lq);
            } catch (Exception e) {
                if (!row.getCell(hashMap.get("xxfsdm")).getStringCellValue().equals("录取学习方式")) {
                    e.printStackTrace();
                    log.info("第{}行数据有误", (r + 1));
                }
            }
            //System.out.println(lqs1.size());
        }
    }

    private Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new AnalyseException(ResultEnum.UPLOAD_FILE_FAILURE);
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(inputStream);
        } else {
            wb = new XSSFWorkbook(inputStream);
        }
        return wb;
    }

    /**
     * 处理学校特殊名称
     **/
    public University getUniversity(String name) {
        University university = new University();
        if (name.contains("中国石油大学") && name.contains("华东")) {
            university = universityMapper.getOne("中国石油大学（华东）");
        } else if (name.contains("中国石油大学") && name.contains("北京")) {
            university = universityMapper.getOne("中国石油大学（北京）");
        } else if (name.contains("中国矿业大学") && name.contains("北京")) {
            university = universityMapper.getOne("中国矿业大学（北京）");
        } else if (name.contains("中国地质大学") && name.contains("武汉")) {
            university = universityMapper.getOne("中国地质大学（武汉）");
        } else if (name.contains("中国地质大学") && name.contains("北京")) {
            university = universityMapper.getOne("中国地质大学（北京）");
        } else if (name.contains("国防科学技术大学")) {
            university = universityMapper.getOne("解放军国防科学技术大学");
        } else if (name.contains("第二军医大学")) {
            university = universityMapper.getOne("解放军第二军医大学");
        } else if (name.contains("第四军医大学")) {
            university = universityMapper.getOne("解放军第四军医大学");
        } else if (name.contains("华北电力大学") && name.contains("保定")) {
            university = universityMapper.getOne("华北电力大学");
        } else {
            university = universityMapper.getOne(name);
        }
        return university;
    }

    /**
     * 得到值和列数的映射关系
     **/
    public HashMap getMapping(String[] strings, Row row) {
        HashMap<String, Integer> hashMap = new HashMap();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            for (String s : strings) {
                if (row.getCell(i).getStringCellValue().equals(s)) {
                    hashMap.put(s, i);
                }
            }
        }
        return hashMap;
    }
}

