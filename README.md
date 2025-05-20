# DicodingStory

DicodingStory is an Android application developed as part of the Dicoding Academy curriculum. This project showcases modern Android development best practices including MVVM architecture, Jetpack components (LiveData, ViewModel, Room, DataStore), and the use of the Paging 3 library. The app allows users to register, log in, view, and share stories with images and location data, leveraging a remote API.

## View APK

The documentation of PlantRO application in Bangkit Capstone Project 2024

![View 1](https://github.com/nrhdyt3012/DicodingStory/blob/master/Screenshot_20250520_132120_Screenshot_20250520_132055_Screenshot_20250520_131714_Screenshot_20250520_131334_Screenshot_20250520_132009.jpeg)

## Features

- **User Authentication**: Register and login with secure session management.
- **Story Feed with Paging**: Browse a list of stories with infinite scrolling using Paging 3.
- **Add Stories**: Upload stories with images and optional location.
- **Story Maps**: View all stories with location data on a map.
- **Local Caching**: Stories are cached locally using Room for offline access and better performance.
- **Modern Android Practices**: Uses ViewModel, LiveData, Kotlin Coroutines, and DataStore.

## Tech Stack

- **Kotlin**
- **MVVM Architecture**
- **Android Jetpack**: ViewModel, LiveData, Room, DataStore
- **Paging 3** for efficient list pagination
- **Retrofit2** for network requests
- **Google Maps** integration
- **Glide** for image loading

## Getting Started

### Prerequisites

- Android Studio Hedgehog or newer
- An API key for Google Maps (add to your `local.properties` or manifest if needed)
- Internet connection

### Installation

1. **Clone this repository:**
   ```
   git clone https://github.com/nrhdyt3012/DicodingStory.git
   ```
2. **Open the project in Android Studio.**
3. **Sync Gradle:** The required dependencies will be downloaded automatically.

### Running the App

1. **Configure API Endpoint:**  
   Make sure the API base URL in `ApiConfig.kt` matches your backend endpoint.
2. **Google Maps API Key:**  
   Add your Maps API key to the `AndroidManifest.xml` if required.
3. **Build and Run:**  
   Run the app on an emulator or physical Android device.

## Project Structure

```
DicodingStory/
  └── app/
      ├── src/
      │   ├── main/
      │   │   ├── java/com/dicoding/picodiploma/loginwithanimation/
      │   │   │   ├── data/          # Data Layer: Repository, Network, Preferences, Room
      │   │   │   ├── view/          # UI Layer: Activities, Fragments, ViewModels
      │   │   │   ├── di/            # Dependency Injection
      │   │   │   └── ...            # Other modules
      │   │   └── res/               # Resources (layouts, drawables)
      └── build.gradle.kts
```

## Notable Libraries Used

- [Retrofit2](https://square.github.io/retrofit/) - Networking
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - Efficient data paging
- [Room](https://developer.android.com/training/data-storage/room) - Local database
- [DataStore](https://developer.android.com/topic/libraries/architecture/datastore) - Preferences and session management
- [Glide](https://bumptech.github.io/glide/) - Image loading
- [Google Maps](https://developers.google.com/maps/documentation/android-sdk/overview) - Maps integration

## Contributing

Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License

Distributed for educational purposes as part of Dicoding Academy coursework. Please check with the repository owner for further usage rights.

---

**Developed by [nrhdyt3012](https://github.com/nrhdyt3012)**
