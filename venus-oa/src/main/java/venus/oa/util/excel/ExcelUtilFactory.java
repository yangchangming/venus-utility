package venus.oa.util.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import venus.frames.mainframe.util.Helper;

import java.io.*;

/**
 * @author changming.Y <changming.yang.ah@gmail.com>
 *
 */
public class ExcelUtilFactory {

    public interface ExcelUtil{
        public void writeExcel(String name, String sheet, String arr[][]);
        public String[][] readExcel(String name, String sheet);
    }

    private static ExcelUtil util = null;
    private static final String EXCELTOOLS = "ExcelUtil";
    static{
        try{

            util = (ExcelUtil) Helper.getBean(EXCELTOOLS);



        }catch(Exception e){
            util = null;
        }
    }

    public static ExcelUtil getUtil(){
        if(null!=util)
            return util;
        return new ExcelUtil(){

            public String[][]/*line,row*/ readExcel(String name, String sheet) {
                FileInputStream fis = null;
                String row[][] = null;
                try {
                    fis = new FileInputStream(name);
                    HSSFWorkbook workbook = new HSSFWorkbook(fis);
                    HSSFSheet esheet = workbook.getSheet(sheet);
                    int size = esheet.getPhysicalNumberOfRows();
                    int length = esheet.getRow(0).getPhysicalNumberOfCells();
                    row = new String[size][length];
                    for(int i=0;i<size;i++){
                        HSSFRow erow = (HSSFRow)esheet.getRow(i);
                        String cell[] = new String[length];
                        for(short j=0;j<length;j++){
                            HSSFCell ecell = erow.getCell( j);
                            cell[j] = ecell.getRichStringCellValue().getString();
                        }
                        row[i] = cell;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return row;
            }

            synchronized public void writeExcel(String name, String sheet, String[][]/*line,row*/ arr) {
                FileOutputStream fileOut = null;
                FileInputStream fileIn = null;
                File excel = new File(name);
                HSSFSheet worksheet = null;
                HSSFWorkbook workbook = null;
                int currentRowNum = -1;
                try {
                    if(excel.exists()){
                        fileIn = new FileInputStream(name); 
                        POIFSFileSystem ps=new POIFSFileSystem(fileIn);
                        workbook=new HSSFWorkbook(ps); 
                        worksheet = workbook.getSheet(sheet);
                        currentRowNum = worksheet.getLastRowNum();
                    }else{
                        workbook=new HSSFWorkbook(); 
                        worksheet = workbook.createSheet(sheet);
                    }
                    fileOut = new FileOutputStream(name);
                    for(int i=0;i<arr.length;i++){
                        ++currentRowNum;
                        HSSFRow row = worksheet.createRow(currentRowNum);
                        for(short j=0;j<arr[i].length;j++){
                            HSSFCell cell   = row.createCell((short)j); 
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
                            cell.setCellValue(new HSSFRichTextString(arr[i][j]));
                        }
                    }
                    workbook.write(fileOut);
                    fileOut.flush();                    
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(null!=fileOut)
                        try {
                            fileOut.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    if(null!=fileIn)
                    try{
                        fileIn.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }            
        };
    }
}