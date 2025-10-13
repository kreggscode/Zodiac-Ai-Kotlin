# ZodiacAI Finder - Setup Guide

## 🚀 Quick Start Guide

### Step 1: Open in Android Studio

1. Launch **Android Studio**
2. Click **File → Open**
3. Navigate to: `c:/Users/kreg9/Downloads/kreggscode/windsurf/Android Studio projects/zodiacai finder`
4. Click **OK**

### Step 2: Wait for Gradle Sync

Android Studio will automatically:
- Download required dependencies
- Configure the project
- Index files

This may take 2-5 minutes on first run.

### Step 3: Run the App

1. **Connect a device** or **start an emulator**
   - Physical Device: Enable USB debugging in Developer Options
   - Emulator: Tools → Device Manager → Create/Start device

2. **Click the Run button** (▶️) or press `Shift + F10`

3. Select your target device

4. Wait for the app to build and install

## 📋 Requirements

### Minimum System Requirements
- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: 8GB (16GB recommended)
- **Disk Space**: 4GB for Android Studio + 2GB for SDK
- **Internet**: Required for initial setup and AI features

### Android Requirements
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)
- **Compile SDK**: 34

## 🔧 Troubleshooting

### Issue: Gradle Sync Failed

**Solution 1**: Check Internet Connection
```
Gradle needs to download dependencies from Maven repositories
```

**Solution 2**: Invalidate Caches
```
File → Invalidate Caches → Invalidate and Restart
```

**Solution 3**: Update Gradle Wrapper
```bash
./gradlew wrapper --gradle-version 8.2
```

### Issue: Build Errors

**Solution**: Clean and Rebuild
```
Build → Clean Project
Build → Rebuild Project
```

### Issue: App Crashes on Launch

**Check**:
1. Minimum Android version (API 24+)
2. Internet permission in AndroidManifest.xml
3. LogCat for error messages

### Issue: AI Features Not Working

**Possible Causes**:
1. No internet connection
2. Pollination AI service temporarily unavailable
3. Network timeout (default: 60 seconds)

**Solution**: Check network and retry

## 🎨 Customization Guide

### Change App Colors

Edit `app/src/main/java/com/zodiacai/finder/ui/theme/Color.kt`:

```kotlin
val PrimaryLight = Color(0xFF6366F1) // Change this hex value
val SecondaryLight = Color(0xFFA855F7)
val TertiaryLight = Color(0xFFEC4899)
```

### Modify AI Temperature

Edit `app/src/main/java/com/zodiacai/finder/data/repository/ZodiacRepository.kt`:

```kotlin
val request = PollinationRequest(
    model = "openai",
    messages = messages,
    temperature = 1.0f // Change this value (0.0 - 2.0)
)
```

### Add New Zodiac Sign Details

Edit `app/src/main/java/com/zodiacai/finder/ui/screens/EncyclopediaScreen.kt`:

Update the helper functions:
- `getTraitsForSign()`
- `getStrengthsForSign()`
- `getWeaknessesForSign()`

## 📱 Testing the App

### Test Scenarios

1. **Home Screen**
   - Toggle dark/light mode
   - Click quick action cards
   - Verify navigation

2. **Zodiac Finder**
   - Select different birth dates
   - Verify correct sign calculation
   - Check element display

3. **Daily Horoscope**
   - Select different zodiac signs
   - Wait for AI generation
   - Verify lucky number/color display

4. **Compatibility**
   - Select two different signs
   - Wait for AI analysis
   - Check score displays

5. **AI Chat**
   - Send test messages
   - Verify response generation
   - Check message history

6. **Encyclopedia**
   - Search for signs
   - View sign details
   - Navigate back

7. **Reading Hub**
   - Test each tab (Tarot, Moon, Palm, Lucky)
   - Verify AI responses
   - Check UI rendering

## 🔐 Permissions

The app requires:
- **INTERNET**: For Pollination AI API calls
- **ACCESS_NETWORK_STATE**: To check connectivity
- **POST_NOTIFICATIONS**: For future notification features (optional)

All permissions are declared in `AndroidManifest.xml`

## 📊 Performance Tips

### Optimize Build Time
1. Enable Gradle daemon
2. Increase heap size in `gradle.properties`:
   ```
   org.gradle.jvmargs=-Xmx4096m
   ```

### Reduce APK Size
1. Enable ProGuard in release builds
2. Use vector drawables instead of PNGs
3. Enable resource shrinking

### Improve Runtime Performance
1. Use LazyColumn for long lists
2. Implement proper state hoisting
3. Avoid unnecessary recompositions

## 🚢 Building Release APK

### Step 1: Generate Signing Key

```bash
keytool -genkey -v -keystore zodiacai.keystore -alias zodiacai -keyalg RSA -keysize 2048 -validity 10000
```

### Step 2: Configure Signing in build.gradle.kts

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("zodiacai.keystore")
            storePassword = "your_password"
            keyAlias = "zodiacai"
            keyPassword = "your_password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

### Step 3: Build Release APK

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## 📚 Additional Resources

### Official Documentation
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Pollination AI API](https://pollinations.ai/)

### Learning Resources
- [Compose Pathway](https://developer.android.com/courses/pathways/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [MVVM Architecture](https://developer.android.com/topic/architecture)

## 💡 Tips for Development

1. **Use Compose Preview**: Add `@Preview` annotations to see UI changes instantly
2. **Enable Live Edit**: Tools → Live Edit for hot reload
3. **Use LogCat**: Monitor app logs for debugging
4. **Test on Real Device**: Better performance than emulator
5. **Use Version Control**: Commit changes regularly

## 🐛 Known Issues

1. **First API Call Slow**: Pollination AI cold start may take 10-15 seconds
2. **Large APK Size**: Due to Compose and Material3 libraries (~15MB)
3. **Memory Usage**: AI responses can be memory-intensive

## 🎯 Next Steps

After successful setup:

1. ✅ Explore all app features
2. ✅ Customize colors and themes
3. ✅ Test AI responses
4. ✅ Add your own features
5. ✅ Share with friends!

## 📞 Support

If you encounter issues:

1. Check this guide first
2. Review error messages in LogCat
3. Verify internet connectivity
4. Try clean and rebuild
5. Check Pollination AI status

---

**Happy Coding! 🚀✨**
