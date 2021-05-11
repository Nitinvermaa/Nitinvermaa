package com.swg.framework.datadriven.reader;

import com.swg.framework.datadriven.model.DataContainer;
import com.swg.framework.datadriven.model.TestDataRecord;
import com.swg.framework.datadriven.model.Value;
import com.swg.framework.execution.HeaderData;
import com.swg.framework.logging.FrameworkLogger;
import com.swg.framework.setup.InitializerScript;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Specify a way to read test data saved in Excel documents
 */
public class ExcelDataReader extends TestDataReader {

    public static final String EXCEL_RESOLUTION = ".xlsx";

    @Override
    protected String getResolution() {
        return EXCEL_RESOLUTION;
    }

    @Override
    public <T extends InitializerScript> DataContainer readTestData(URL testDataUrl) {

        DataContainer result = new DataContainer();

        FileInputStream fileInputStream = null;
        try {
            URI uri = testDataUrl.toURI();
            File file = new File(uri);
            fileInputStream = new FileInputStream(file);
            logTestDataInfo(HeaderData.DataType.EXCEL.name(), file.getAbsolutePath());
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<String> headers = null;
            int rowsNum = sheet.getPhysicalNumberOfRows();
            for (int i = 0; i < rowsNum; i++) {
                XSSFRow row = sheet.getRow(i);
                int cellsNum = row.getLastCellNum();
                if (i == 0) {
                    headers = processHeaderRow(row, cellsNum);
                } else {
                    result.addTestData(processDataRow(row, headers));
                }

            }
        } catch (FileNotFoundException e) {
            FrameworkLogger.logError(e.toString());
        } catch (IOException e) {
            FrameworkLogger.logError(e.toString());
        } catch (URISyntaxException e) {
            FrameworkLogger.logError(e.toString());
        }


        return result;
    }

    private List<String> processHeaderRow(XSSFRow row, int cellsNum) {
        List<String> result = new ArrayList<String>(cellsNum);

        for (int i = 0; i < cellsNum; i++) {
            XSSFCell cell = row.getCell(i);
            result.add(cell != null ? cell.getStringCellValue() : "");
        }
        return result;
    }

    private TestDataRecord processDataRow(XSSFRow row, List<String> headers) {
        TestDataRecord result = new TestDataRecord();
        for (int i = 0; i < headers.size(); i++) {
            XSSFCell cell = row.getCell(i);

            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cell.getStringCellValue();
                        result.addValue(new Value(headers.get(i), cell.getStringCellValue()));
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cell.getNumericCellValue();
                        result.addValue(new Value(headers.get(i), cell.getNumericCellValue()));
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        cell.getBooleanCellValue();
                        result.addValue(new Value(headers.get(i), cell.getBooleanCellValue()));
                        break;
                    default:
                }
            } else {
                result.addValue(new Value(headers.get(i), null));
            }
        }

        return result;
    }
}
