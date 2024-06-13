# Fridge Management App

## Overview
The Fridge Management App is an Android application designed to help users efficiently manage their food inventory. The app allows users to add, edit, and track food items with purchase and expiry dates, generate recipe suggestions based on expiring items, and maintain a shopping list. The app leverages Firebase for real-time data storage and synchronization across multiple devices.

## Features
- **Food Inventory Management:** Add, edit, and track food items with purchase and expiry dates.
- **Recipe Suggestions:** Generate recipes based on items that are about to expire.
- **Real-time Database:** Use Firebase to store items in the cloud, ensuring data consistency and real-time synchronization across devices.
- **Comprehensive Food Database:** Fetch food items, recipes, and nutritional content in real-time from an extensive database.
- **Shopping List Integration:** Maintain a shopping list with automatic addition of expired items and user-driven item management.
- **User-friendly Interface:** Intuitive and visually appealing UI designed for seamless user interaction.

## Technologies Used
- **Java:** Core programming language used for app development.
- **Firebase:** Cloud storage and real-time database for data synchronization and consistency.
- **Android Studio:** Development environment used for building the app.
- **HTML5, CSS, and JavaScript:** Technologies used for frontend design and user interface.

## Installation

### Prerequisites
- Android Studio installed on your computer.
- Firebase account set up with a real-time database and Firestore enabled.

### Steps
1. **Clone the Repository:**
   
   ```bash
   git clone https://github.com/yourusername/fridge-management-app.git

3. **Open in Android Studio:**

- Open Android Studio.
- Select "Open an existing Android Studio project."
- Navigate to the cloned repository folder and select it.

3. **Configure Firebase:**

- Go to the Firebase console.
- Create a new project or use an existing project.
- Add an Android app to your Firebase project.
- Download the google-services.json file and place it in the app directory of your Android project.
- Add the necessary Firebase dependencies to your build.gradle files.

4. **Build and Run the App:**

- Sync your project with Gradle files.
- Click the "Run" button in Android Studio to build and run the app on an emulator or a connected device.
