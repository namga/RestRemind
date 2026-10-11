# RestRemind Android build

This repository contains the original RestRemind PWA and an Android WebView wrapper under `android/`.

## Build APK on GitHub
1. Upload the full contents of this ZIP to the root of your GitHub repository, preserving `.github/` and `android/` folders.
2. Open **Actions → Build RestRemind APK**. Use **Run workflow** or push a change under `android/`.
3. Download the `restremind-debug-apk` artifact from the completed run. It contains `app-debug.apk`.

## Build locally
Open the `android/` folder in Android Studio and build the `app` debug variant, or run `gradle assembleDebug` from that folder with JDK 17 and Android SDK Platform 35 installed.

## Important behavior notes
- The app bundles the HTML, CSS, JavaScript, manifest, and icons locally. WebView local storage preserves preferences on the device.
- The original PWA uses browser Service Worker and Notification APIs. These APIs are not fully available from `file:///android_asset/` WebView pages, so background/system notifications and reminders may not work reliably when the app is closed or in the background. The current wrapper supports foreground in-app reminders and vibration; production-grade background reminders need native Android scheduling and notification integration.
- This is a debug APK workflow, not a Play Store signed release.

## Selecting music and background files
- The Android wrapper now implements WebView's file chooser callback for HTML file inputs.
- Tap **📁 Chọn file** in the music player and select an audio file from Android's system document picker (for example, Music, Downloads, or a supported cloud provider). The same picker support is used for the custom background image input.
- This uses Android's Storage Access Framework and does not request broad access to all device files. Only files the user selects are shared with the app.
- After choosing a music file, the PWA stores a copy in its IndexedDB so it can restore the last selected track later. Very large audio files may take time or exceed available WebView storage.
