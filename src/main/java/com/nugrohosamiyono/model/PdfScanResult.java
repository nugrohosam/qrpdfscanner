package com.nugrohosamiyono.model;

import com.nugrohosamiyono.core.PdfScanner;

import java.io.IOException;
import java.nio.file.Path;

/**
 * In a PdfScanResult, meta data about QR-PDFs is stored: i.e. the file name (and, after renaming the old and new file
 * name), number of pages in the PDF, if a QR code was found, what it was and on which page it was found.
 * <p>
 * Note: the PdfScanner itself is not stored in the result.
 *
 * @author Lars Steggink
 */
public class PdfScanResult {

    private ResultStatus resultStatus;
    private int qrCodePage;
    private String qrCode;

    private Path inputFilePath;
    private Path outputFilePath;
    private String creation;
    private Boolean isRenamed;
    private int pageCount;

    /**
     * @param pdf          the PDF file
     * @param resultStatus the ResultStatus after QR code scanning
     * @param qrCodePage   the page that was scanned
     * @param qrCode       the QR code, if found, otherwise ""
     */
    public PdfScanResult(PdfScanner pdf, ResultStatus resultStatus, int qrCodePage, String qrCode) {
        this.inputFilePath = pdf.getPath();
        this.outputFilePath = inputFilePath;
        this.isRenamed = false;
        try {
            this.creation = pdf.getCreationTime().toString();
        } catch (IOException e) {
            // If time could not be determined, keep it blank.
            this.creation = "";
        }
        try {
            this.pageCount = pdf.getNumberOfPages();
        } catch (IOException e) {
            this.pageCount = -9;
        }
        this.resultStatus = resultStatus;
        this.qrCodePage = qrCodePage;
        this.qrCode = new String(qrCode);
    }

    /**
     * Whether a QR code was found.
     *
     * @return whether a QR code was found
     */
    public boolean isQRCodeFound() {
        return resultStatus == ResultStatus.QR_CODE_FOUND;
    }

    /**
     * Gets the QR code that was found. Note: first check isQRCodeFound().
     *
     * @return QR code
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * Gets file path as it was during the QR scan.
     *
     * @return file path
     */
    public Path getInputFilePath() {
        return inputFilePath;
    }

    /**
     * Gets whether the file was renamed from its original input file path.
     *
     * @return whether the file was renamed
     */
    public boolean isFileRenamed() {
        return isRenamed;
    }

    /**
     * Gets the output file path. This is the renamed file path, or the original file path if not renamed. Note: it is
     * up to the user to update (set) the new file path in this result. It does not follow the file automatically.
     *
     * @return output file path
     */
    public Path getOutputFilePath() {
        return outputFilePath;
    }

    /**
     * Remembers the old and new file paths after renaming.
     *
     * @param outputFilePath the new file path
     */
    public void setOutputFilePath(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
        this.isRenamed = true;
    }

    /**
     * Gets the date and time this file was created, or an empty string if it could not be determined.
     *
     * @return creation time
     */
    public String getFileCreationTime() {
        return creation;
    }

    /**
     * Gets the number of pages.
     *
     * @return number of pages
     */
    public int getPageCount() {
        return pageCount;
    }

    /**
     * Gets the page number that was scanned for a QR code.
     *
     * @return page number
     */
    public int getQrCodePage() {
        return qrCodePage;
    }

    /**
     * Gets the status of the QR code scanning (i.e. failed or found).
     *
     * @return status
     */
    public ResultStatus getQrCodeScanStatus() {
        return resultStatus;
    }

    /**
     * Present a property object to use the input file path in JavaFX.
     *
     * @return input file path
     */
    public Path inputFilePathProperty() {
        return inputFilePath;
    }

    /**
     * Present a property object to use the renamed file path in JavaFX.
     *
     * @return renamed file path
     */
    public Path renamedFilePathProperty() {
        return outputFilePath;
    }

    /**
     * Present a property object to use the QR code in JavaFX.
     *
     * @return qr code
     */
    public String qrCodeProperty() {
        return qrCode;
    }

    /**
     * Present a property object to use the QR code status in JavaFX.
     *
     * @return qr code status report
     */
    public ResultStatus qrCodeStatusProperty() {
        return resultStatus;
    }

    /**
     * Possible status reports for QR code scanned PDF files.
     */
    public enum ResultStatus {
        QR_CODE_FOUND, NO_FILE_ACCESS, NO_QR_CODE,
    }
}
