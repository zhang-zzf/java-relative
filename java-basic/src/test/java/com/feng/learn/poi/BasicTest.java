package com.feng.learn.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author 张占峰 (Email: zhang.zzf@alibaba-inc.com / ID: 235668)
 * @date 2021/9/8
 */
public class BasicTest {

    @Test
    public void getNumberToString() throws IOException {
        final InputStream file = ClassLoader.getSystemResourceAsStream("poi/test.xlsx");
        final XSSFWorkbook workbook = new XSSFWorkbook(file);
        final Sheet sheet = workbook.getSheetAt(0);
        final int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            final Cell cell = sheet.getRow(i).getCell(0);
            cell.setCellType(CellType.STRING);
            final String cellValue = cell.getStringCellValue();
            then(cellValue).isNotNull();
        }
    }

}
