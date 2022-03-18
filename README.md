[![](https://jitpack.io/v/hannesa2/document-scanner.svg)](https://jitpack.io/#hannesa2/document-scanner)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
# Android Document Scanner
Contains an activity that allows the user to scan a A4 paper with the smartphone camera.
It is based on CameraX and OpenCV

### Installation
Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```groovy
dependencies {
	implementation 'com.github.hannesa2:document-scanner:$version'
}
```

### Usage
Inherit from `BaseScannerActivity`

```kotlin
class ScannerActivity : BaseScannerActivity() {
    override fun onError(throwable: Throwable) {
        when (throwable) {
            is NullCorners -> Toast.makeText(this, R.string.null_corners, Toast.LENGTH_LONG).show()
            else -> Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDocumentAccepted(bitmap: Bitmap) = Unit

    override fun onClose() {
        finish()
    }
}
```

### Preview
Detect document

![detect](https://github.com/hannesa2/document-scanner/blob/master/images/detect.jpg)

Crop document

![crop](https://github.com/hannesa2/document-scanner/blob/master/images/crop.jpg)
