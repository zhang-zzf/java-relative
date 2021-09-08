package com.feng.learn.apache.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/21
 */
public interface WorkbookFactory {

    Workbook create();

    /**
     * @return the suffix of the Workbook
     */
    String suffix();

    /**
     * add a row of data to the sheet
     *
     * @param sheet Sheet
     * @param rowNumber number of the row in the sheet
     * @param columns data of the row
     */
    default void addRow(Sheet sheet, int rowNumber, String... columns) {
        Row row = sheet.createRow(rowNumber);
        for (int i = 0; i < columns.length; i++) {
            row.createCell(i, CellType.STRING).setCellValue(columns[i]);
        }
    }

    /**
     * create Workbook for .xlsx
     */
    class XSSFWorkbookFactory implements WorkbookFactory {

        @Override
        public Workbook create() {
            return new XSSFWorkbook();
        }

        @Override
        public String suffix() {
            return ".xlsx";
        }
    }

    class HSSFWorkbookFactory implements WorkbookFactory {

        @Override
        public Workbook create() {
            return new HSSFWorkbook();
        }

        @Override
        public String suffix() {
            return ".xls";
        }
    }

}
