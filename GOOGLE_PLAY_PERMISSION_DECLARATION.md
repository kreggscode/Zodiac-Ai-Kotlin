# Google Play Permission Declaration

## READ_MEDIA_IMAGES Permission Justification

**For Google Play Console Upload Form:**

### Description to provide (under 250 characters):

```
Our app's core palm reading feature requires users to upload or capture photos of their palms for AI-powered palmistry analysis. Images are processed locally and sent to our AI service for personalized readings.
```

**Character count: 220/250** ✓

---

## Detailed Explanation

### Why This Permission is Required:

1. **Core Feature**: Palm reading analysis is a primary feature of Zodiac AI Finder
2. **User-Initiated**: Permission is only requested when users actively choose to:
   - Take a photo of their palm using the camera
   - Select an existing palm photo from their gallery
3. **Purpose**: Images are used exclusively for AI-powered palmistry analysis
4. **Frequency**: Used whenever user wants a palm reading (on-demand, not background)

### How It's Used in the App:

- **Screen**: `AnalyzePalmScreen.kt`
- **Trigger**: User clicks "Take Photo" or "Gallery" button
- **Process**: 
  1. User grants permission
  2. User captures/selects palm image
  3. Image is analyzed by AI for palmistry reading
  4. Results displayed to user
- **Storage**: Images stored temporarily in cache, not permanently saved

### Alternative Considered:

We evaluated using the Android Photo Picker, but our palm reading feature requires:
- Immediate camera access for optimal lighting conditions
- Ability to guide users on proper palm positioning
- Real-time feedback for image quality

Therefore, traditional camera and gallery permissions provide the best user experience for accurate palm readings.

---

## Steps to Complete Google Play Upload:

1. **Navigate to**: Google Play Console → Your App → Release → Production
2. **Find**: "Photo and video permissions" section
3. **Select**: "My app needs to access photos and videos frequently"
4. **Reason**: Select "Core functionality" or "User-generated content"
5. **Paste the description** (220 characters) in the text field
6. **Save** and continue with upload

---

## Technical Implementation:

- ✅ Permission declared in AndroidManifest.xml
- ✅ Runtime permission requests implemented
- ✅ User-friendly permission dialogs
- ✅ Graceful handling of permission denial
- ✅ Settings redirect for denied permissions
- ✅ Native debug symbols enabled for crash reporting

---

## Version Information:

- **Version Code**: 5
- **Version Name**: 1.1
- **Build Type**: Release
- **Debug Symbols**: Enabled (FULL)
