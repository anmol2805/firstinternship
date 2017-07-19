package com.tophawks.vm.visualmerchandising;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Sanidhya on 11-Apr-17.
 */


public class ReadWriteExcelFile {

    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 123;
    private Context context;
    Uri fileUri=null;

    public ReadWriteExcelFile(Context context) {
        this.context = context;
        permissionRequest();
    }

    public Uri saveExcelFile(String fileName, ArrayList<ArrayList<String>> reportDataLOL, String moduleName)
    {

        Workbook wb=new HSSFWorkbook();

        Cell c;

        //Cell style for header row
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.AQUA.index);

        //New Sheet
        Sheet sheet1= wb.createSheet(fileName);
        int i=0;int j=0;
        for (ArrayList<String> rowList:reportDataLOL) {

            Row row=sheet1.createRow(i++);
            for(String item:rowList)
            {
                c=row.createCell(j++);
                c.setCellValue(item);
                c.setCellStyle(cs);
            }
            j=0;
        }
        sheet1.setDefaultColumnWidth(22);


        File root=new File(Environment.getExternalStorageDirectory()+
                File.separator+"Track'n'Train"+File.separator+moduleName+
                File.separator);
        root.mkdirs();
        File stockReportXls=new File(root,fileName);
        stockReportXls.delete();
        stockReportXls=new File(root,fileName);
        FileOutputStream os=null;

        try {
            os=new FileOutputStream(stockReportXls);
            wb.write(os);
            fileUri=Uri.fromFile(stockReportXls);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(os!=null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileUri;
    }

    private void permissionRequest() {

        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)context,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);
        }
    }

}
