# Book Tracker App 📚

A modern Android application for personal library management and reading tracking, built with Kotlin and Jetpack Compose.

---


## ✨ Features

A comprehensive list of what the app can do:

* **📖 Track Reading Progress:** Easily manage a book's status (e.g., Will Read, In Progress, Completed, Abandoned).
* **⭐ Rate & Review:** Assign personal star ratings and write notes or reviews for completed books.
* **📚 Custom Bookshelves:** Organize your library by creating custom bookshelves (e.g., "Fantasy," "Favorites") and adding books to them.
* **🔍 Online Search:** Find and add new books to your library by searching the Open Library API by title, author, or ISBN.
* **📊 Reading Stats:** View a summary of your reading habits on the Account screen.
* **🎨 Dynamic Theming:** Switch between Light, Dark, and System Default themes. Your preference is saved and persists across app launches.
* **📱 Modern UI:** A clean, responsive, and intuitive user interface built with Jetpack Compose and Material 3.
* **👆 Gesture Controls:** Intuitive swipe-to-delete functionality for managing bookshelves.

---

## 🛠️ Tech Stack & Architecture

This project showcases a modern Android development setup, emphasizing best practices.

* **Language:** **Kotlin**
* **UI:** **Jetpack Compose** with **Material 3**
* **Architecture:** **MVVM (Model-View-ViewModel)** with a layered architecture (UI, Domain, Data). Follows Google's official architecture recommendations.
* **Asynchronous Programming:** **Kotlin Coroutines & Flow** for managing background tasks and reactive data streams.
* **Dependency Injection:** **Hilt** for managing dependencies throughout the app.
* **Navigation:** **Navigation Compose** for navigating between screens.
* **Local Database:** **Room** for persisting user data locally.
* **Networking:** **Retrofit & Moshi** for making API calls to the Open Library API.
* **Image Loading:** **Coil** for loading and displaying book cover images.
* **Data Persistence:** **Jetpack DataStore** for saving user preferences (like the app theme).
