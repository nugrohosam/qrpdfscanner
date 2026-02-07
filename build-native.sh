#!/bin/bash
# Universal build script for QR PDF Scanner native apps
# Supports macOS, Linux, and Windows (via WSL)

set -e

# Detect platform
OS="$(uname -s)"
case "${OS}" in
    Linux*)     PLATFORM=linux;;
    Darwin*)    PLATFORM=macos;;
    MINGW*|MSYS*|CYGWIN*)  PLATFORM=windows;;
    *)          PLATFORM="unknown:${OS}"
esac

echo "Building QRScan native app for: $PLATFORM"

# Check if jpackage is available
if ! command -v jpackage &> /dev/null; then
    echo "Error: jpackage not found. Please install JDK 14 or higher."
    echo "  macOS: brew install openjdk"
    echo "  Linux: sudo apt install openjdk-17-jdk"
    exit 1
fi

# Build JAR with Maven
echo "Step 1: Building JAR with Maven..."
mvn clean package -q

if [ $? -ne 0 ]; then
    echo "Maven build failed!"
    exit 1
fi

# Create input directory for jpackage
echo "Step 2: Preparing jpackage input..."
rm -rf jpackage-input
mkdir -p jpackage-input
cp target/qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar jpackage-input/

# Build native app with platform-specific options
echo "Step 3: Creating native app..."

if [ "$PLATFORM" = "macos" ]; then
    jpackage --input jpackage-input \
      --name QRScan \
      --main-jar qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar \
      --main-class com.nugrohosamiyono.App \
      --type app-image \
      --dest . \
      --vendor "QR PDF Scanner" \
      --app-version 1.0

    # Create wrapper
    cat > qrscan-native << 'EOF'
#!/bin/bash
./QRScan.app/Contents/MacOS/QRScan "$@"
EOF
    APP_DIR="QRScan.app"
    APP_BIN="QRScan.app/Contents/MacOS/QRScan"

elif [ "$PLATFORM" = "linux" ]; then
    jpackage --input jpackage-input \
      --name QRScan \
      --main-jar qrpdfscanner-1.0-SNAPSHOT-jar-with-dependencies.jar \
      --main-class com.nugrohosamiyono.App \
      --type app-image \
      --dest . \
      --vendor "QR PDF Scanner" \
      --app-version 1.0 \
      --linux-package-name qrscan \
      --linux-menu-group "Utilities" \
      --linux-shortcut \
      --linux-app-category "Utility" \
      -q

    # Create wrapper
    cat > qrscan-native << 'EOF'
#!/bin/bash
./QRScan/bin/qrscan "$@"
EOF
    APP_DIR="QRScan"
    APP_BIN="QRScan/bin/qrscan"

else
    echo "Error: Unsupported platform: $PLATFORM"
    rm -rf jpackage-input
    exit 1
fi

if [ $? -ne 0 ]; then
    echo "jpackage build failed!"
    rm -rf jpackage-input
    exit 1
fi

# Cleanup
rm -rf jpackage-input

# Make wrapper executable
chmod +x qrscan-native

# Create symlink for easy access
ln -sf "$(echo $APP_BIN | cut -d/ -f1)" qrscan-link 2>/dev/null || true

# Done
echo ""
echo "✓ Build complete!"
echo "  Platform: $PLATFORM"
echo "  App: $APP_DIR ($(du -sh $APP_DIR | cut -f1))"
echo ""
echo "Usage:"
echo "  $APP_BIN [file] [page]"
echo "  ./qrscan-native [file] [page]"
