package com.nugrohosamiyono;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.nugrohosamiyono.core.PdfScanner;
import com.nugrohosamiyono.core.ScanPdfsTask;
import com.nugrohosamiyono.model.PdfScanResult;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        List<PdfScanner> pdfFiles = new ArrayList<PdfScanner>();

        String filePath = args[0];
        pdfFiles.add(new PdfScanner(Path.of(filePath)));

        String page = args[1];
        int atPage = !page.isEmpty() ? Integer.parseInt(page) : -1;
        
        ScanPdfsTask scanPdfsTask = new ScanPdfsTask();
        List<PdfScanResult> results = scanPdfsTask.scanPdfFiles(pdfFiles, atPage);
        
        results.forEach(
            result -> System.out.println(result.getInputFilePath() + " => " + result.getQrCode())
        );
    }
}
