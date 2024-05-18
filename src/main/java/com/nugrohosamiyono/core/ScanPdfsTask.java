package com.nugrohosamiyono.core;

import com.google.zxing.NotFoundException;
import com.nugrohosamiyono.model.PdfScanResult;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * In this Task, PDF files in the input directory are scanned recursively (at a specified page), results are aggregated
 * and finally reported as an CSV file within the input directory.
 * <p>
 * The user should predefine the page where the QR code can be found, as this will make scanning for QR codes
 * tremendously more efficient.
 *
 * @author Lars Steggink
 */
public class ScanPdfsTask {

    public List<PdfScanResult> scanPdfFiles(List<PdfScanner> pdfFiles, int atPage) {

        int fileCount = pdfFiles.size();
        int success = 0;
        int failed = 0;

        // Start the loop through all files.
        List<PdfScanResult> results = new ArrayList<>();
        for (PdfScanner pdf : pdfFiles) {

            try {
                String qrCode = pdf.getQRCode(atPage, false, false);
                results.add(new PdfScanResult(pdf, PdfScanResult.ResultStatus.QR_CODE_FOUND, atPage, qrCode));
                success++;
            } catch (IOException e) {
                results.add(new PdfScanResult(pdf, PdfScanResult.ResultStatus.NO_FILE_ACCESS, atPage, ""));
                failed++;
            } catch (NotFoundException e) {
                results.add(new PdfScanResult(pdf, PdfScanResult.ResultStatus.NO_QR_CODE, atPage, ""));
                failed++;
            }
        }
        
        return results;
    }
}