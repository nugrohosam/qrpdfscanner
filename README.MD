# QR PDF Scanner

A Java command-line tool that scans PDF files for QR codes and extracts their content. This tool is particularly useful for extracting URLs or other data embedded as QR codes in PDF documents.

## Features

- Scan specific pages of PDF files for QR codes
- Extract QR code content (URLs, text, etc.)
- Multi-resolution scanning (150-300 DPI) for reliable QR code detection
- File attribute caching support for faster repeated scans
- Detailed scan results with status reporting
- Support for various PDF formats (JPEG, JPEG2000, JBIG2)

## Project Structure

```
qrpdfscanner/
├── pom.xml                                          # Maven build configuration
├── README.MD                                        # This file
├── qr.pdf                                           # Sample PDF file
└── src/main/java/com/nugrohosamiyono/
    ├── App.java                                     # Main entry point
    ├── core/
    │   ├── PdfScanner.java                          # PDF scanning and QR code extraction
    │   └── ScanPdfsTask.java                        # Task handler for batch scanning
    └── model/
        └── PdfScanResult.java                       # Result model with scan metadata
```

## Key Components

### `App.java`
Entry point that parses command-line arguments and initiates the scanning process.

### `PdfScanner.java`
Core class that handles:
- Loading and rendering PDF pages as images
- QR code detection using ZXing library
- Multi-DPI scanning strategy for reliability
- Optional file attribute caching for performance

### `ScanPdfsTask.java`
Task handler that processes multiple PDF files and aggregates scan results.

### `PdfScanResult.java`
Model class containing scan metadata:
- Input/output file paths
- QR code content and page number
- Scan status (FOUND, NO_FILE_ACCESS, NO_QR_CODE)
- Page count and file creation time

## Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| Apache PDFBox | 2.0.24 | PDF processing and rendering |
| Google ZXing | 3.4.1 | QR code detection and decoding |
| TwelveMonkeys ImageIO | 3.7.0 | Additional image format support |
| Tinylog | 2.3.2 | Logging |

**Requirements:** Java 14 or higher

## Build

### Standard JAR

```bash
mvn clean package
```

This creates an executable JAR with all dependencies at:
```
target/qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Native App Bundle (No Java Required)

#### Universal Build Script (Recommended)

The `build-native.sh` script automatically detects your platform and builds the appropriate native app:

```bash
./build-native.sh
```

This creates a self-contained application with an embedded JRE (≈155MB) that runs without requiring Java installation.

#### Platform-Specific Builds

**macOS:**
```bash
./build-native.sh
# Creates: QRScan.app
```

**Linux:**
```bash
./build-native.sh
# Creates: QRScan/ directory with binary
```

**Windows:**
```bash
# Run via WSL or Git Bash
./build-native.sh
# Creates: QRScan.exe
```

## Usage

### macOS

**Native App:**
```bash
./QRScan.app/Contents/MacOS/QRScan [file] [page]
# Or use the convenience wrapper:
./qrscan-native [file] [page]
```

**With Alias:**
```bash
alias qrscan='./QRScan.app/Contents/MacOS/QRScan'
qrscan /path/to/file.pdf 1
```

### Linux

**Native App:**
```bash
./QRScan/bin/qrscan [file] [page]
# Or use the convenience wrapper:
./qrscan-native [file] [page]
```

**Symlink:**
```bash
ln -s QRScan/bin/qrscan qrscan
./qrscan /path/to/file.pdf 1
```

### Windows

**Batch Script:**
```cmd
qrscan.bat C:\path\to\file.pdf 1
```

### JAR with Java (Any Platform)

```bash
java -jar target/qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar [file] [page]
```

### Arguments

| Argument | Description |
|----------|-------------|
| `file` | Path to the PDF file to scan |
| `page` | Page number to scan for QR code (1-indexed) |

### Examples

**Scan page 1 of a PDF (macOS):**
```bash
./QRScan.app/Contents/MacOS/QRScan /path/to/file.pdf 1
```

**Scan page 3 (Linux):**
```bash
./QRScan/bin/qrscan ./documents/invoice.pdf 3
```

**Scan with JAR (Any Platform):**
```bash
java -jar target/qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar /path/to/file.pdf 1
```

## Output Format

The tool outputs results in the format:
```
[input_file_path] => [qr_code_content]
```

### Example Output

```
/Users/dot-mac-pro-m2-2022/Work/Project/qrpdfscanner/qr.pdf => https://326f3332-fa35-48bb-96f9-4354f29289de.mock.pstmn.io/efaktur/global/001
```

## How It Works

1. **Load PDF**: The PDF file is loaded using Apache PDFBox
2. **Render Page**: The specified page is rendered as a binary image at multiple DPI settings (150, 200, 250, 300)
3. **Detect QR Code**: ZXing library scans the rendered image for QR codes
4. **Extract Content**: If found, the QR code content is decoded and returned
5. **Output Result**: Results are printed to stdout in `file => qr_code` format

## Scan Status Results

| Status | Description |
|--------|-------------|
| `QR_CODE_FOUND` | QR code successfully detected and decoded |
| `NO_FILE_ACCESS` | Unable to read the PDF file (IO error) |
| `NO_QR_CODE` | No QR code found on the specified page |

## License

This project uses the following open-source libraries:
- Apache PDFBox (Apache License 2.0)
- ZXing (Apache License 2.0)
- TwelveMonkeys ImageIO (BSD 3-Clause)
- Tinylog (Apache License 2.0)