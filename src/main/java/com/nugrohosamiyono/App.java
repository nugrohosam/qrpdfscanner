package com.nugrohosamiyono;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
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
        System.setProperty("java.awt.headless", "true");
        List<PdfScanner> pdfFiles = new ArrayList<PdfScanner>();

        String filePath = args[0];
        pdfFiles.add(new PdfScanner(Path.of(filePath)));

        String page = args[1];
        int atPage = !page.isEmpty() ? Integer.parseInt(page) : -1;
        
        ScanPdfsTask scanPdfsTask = new ScanPdfsTask();
        List<PdfScanResult> results = scanPdfsTask.scanPdfFiles(pdfFiles, atPage);

        List<String> qrCodes = new ArrayList<>();
        results.forEach(
            result -> {
                if (result.getQrCodeScanStatus() == PdfScanResult.ResultStatus.QR_CODE_FOUND) {
                    qrCodes.add(result.getQrCode());
                }
            }
        );

        System.out.println(new Gson().toJson(qrCodes));
    }
}
