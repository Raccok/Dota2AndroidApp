# DotaScoop

A community project aiming to provide convenient features and content to the Dota 2 community.

The first states of the app were developed using [this](https://www.raywenderlich.com/132381/kotlin-for-android-an-introduction) tutorial and modifying the example app provided by the tutorial.

## Downloading and running the finished app (for any user)

The app is not yet available on the Play Store. However, you can still easily install the finished app (APK) on pretty much any Android device by following these steps:

On your device, go to `Settings > Security > Unknown sources` and enable "Allow installation of apps from unknown sources". On Android 8 or higher, go to `Settings > Apps & notifications > Advanced > Special app access > Install unknown apps` and allow your device's browser (most likely Chrome) to install unknown apps. Then, in your device's browser, click the following link to download the latest version:

https://sourceforge.net/projects/dotascoop/files/DotaScoop.apk/download

Open the downloaded file and follow the installation instructions, then you should be able to use this app like any other. For security reasons, disable "Allow installation of apps from unknown sources" again.

## Manually building and running the app (for developers)

Tested on Windows 10 and Windows 8.1.

### Recommended setup and building the app

Clone this repository with the Git tool of your choice. On (most) \*nix systems, `git` is preinstalled. To get a simple terminal with basic Unix commands and the `git` command for Windows, use [this](https://git-for-windows.github.io/).

Download Android Studio from [here](https://developer.android.com/studio/index.html). Click on `Download Options` or scroll all the way down to download it for Mac or Linux.

Start the installer and install Android Studio with the Android SDK and the recommended preconfigured virtual Android device. When this installation is finished and you start Android Studio for the first time, strangely enough another installation starts. Click `Next`, select "Custom" installation instead of "Standard" and click `Next`. Select your UI theme, click `Next`, select "Android Virtual Device", click `Next`, click `Next` and click `Finish`.

In Android Studio, choose `Open an existing Android Studio project` and open the `DotaScoop` folder which you received by cloning this repository earlier. When presented with a window titled "Gradle Sync", click `OK`. When presented with a Windows Firewall window, click `Allow access`. When presented with the "Tip of the Day" window, click `Close`.

Now you are probably presented with an error "Failed to find target [...]". Click `Install missing platform(s) and sync project`. Accept the agreement and click `Next`, when it's done click `Finish`. If you are presented with other errors like this, proceed the same way to install all missing components.

Wait for Android Studio to build the project. When it's done, you will see "Gradle build finished [...]" in a status bar at the bottom of Android Studio. This completes building the app.

### Running the manually built app on a virtual Android device

In Android Studio, click the green play button in the middle of the toolbar (or press Shift+F10) to run the app. When presented with "Select Deployment Target", you should see an entry under "Available Virtual Devices". If yes, click `OK`. If no, click `Create New Virtual Device`, install missing components, click `Next`, click `Download` to install a recommended system image, click `Next` and click `Finish`. Now you should definitely see an entry under "Available Virtual Devices", then click `OK`.

Watch Android Studio start the virtual Android device to run the app. Follow the status bar at the bottom of Android Studio, it gives you some nice information about what's currently happening. When presented with a window titled "GPU Driver Issue", select `Never show this again` and click `OK`.

The app should now be started on the virtual Android device and ready to use.

### Running the manually built app on a physical Android device

Of course you can also install and run the app on a physical Android device. Connect the device to your computer via USB. On the Android device, enable "Developer options" and "Android debugging" (or "USB debugging"), Google will tell you how to do that. Press the green play button in the middle of the toolbar (or press Shift+F10) and you should be able to select your physical Android device.
