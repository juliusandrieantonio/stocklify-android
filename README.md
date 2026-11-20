# Stocklify

**Smart Inventory Management for Small Businesses.**

Stocklify is an offline-first inventory management application designed for sari-sari stores and small business owners. It helps business owners manage products, monitor stock levels, track sales, and understand their business operations — without depending on a constant internet connection.

Built with native Android technologies, Stocklify prioritizes reliability, simplicity, and data ownership, making inventory management accessible even in areas with limited connectivity.

---

## ✨ Features

### Core Features

- **Inventory Management** – Add, update, organize, and monitor products.
- **Sales Management** – Record sales and manage daily transactions.
- **Stock Monitoring** – Keep track of available quantities and inventory movements.
- **Dashboard** – Get an overview of business activity and inventory.
- **Offline-First Experience** – Access essential features without an internet connection.
- **Local Data Persistence** – Store business data securely on the device.
- **Receipt Scanning** – Explore automated receipt data extraction through OCR.

### Premium Features

Stocklify follows a free-first business model, with additional capabilities available through premium features.

- Cloud synchronization
- Multi-device access
- Team and employee management
- Business reports and analytics
- AI-powered business assistance

---

## 🛠️ Tech Stack

Stocklify is built using modern Android development technologies.

| Technology | Purpose |
|---|---|
| Kotlin | Primary programming language |
| Jetpack Compose | Declarative UI development |
| Material 3 | UI components and design system |
| Room | Local database and persistence |
| SQLCipher | Encrypted local database |
| Android Keystore | Secure database key management |
| DataStore | Local preferences and settings |
| Hilt | Dependency injection |
| Supabase | Authentication, cloud database, and synchronization |
| PaddleOCR | Offline receipt text recognition |
| Coroutines | Asynchronous programming |
| Flow | Reactive data streams |

---

## 🏗️ Architecture

Stocklify follows an offline-first architecture where the local database serves as the primary source of truth.

```text
              Android Application
                      |
              Presentation Layer
           (Jetpack Compose / ViewModel)
                      |
                Domain Layer
               (Use Cases)
                      |
                Data Layer
                (Repository)
                      |
             Local Data Source
                (Room DB)
                      |
              SQLite + SQLCipher
                      |
             Synchronization Layer
                      |
                  Supabase
             (Cloud PostgreSQL)
```

### Architecture Principles

- **Offline-first:** Core application functionality remains accessible without internet connectivity.
- **Local-first data:** Operations are persisted locally before cloud synchronization.
- **Secure storage:** SQLCipher encrypts the local database, with encryption keys protected through Android Keystore.
- **Separation of concerns:** UI, business logic, and data management are separated.
- **Scalable synchronization:** Cloud synchronization supports the transition between single-device and multi-device usage.

---

## 🔐 Data Security

Stocklify prioritizes the security of locally stored business information.

- SQLCipher for encrypted SQLite database storage.
- Android Keystore for protecting encryption keys.
- AES-GCM encryption for database passphrase protection.
- Local-first data management to minimize unnecessary network dependency.

---

## 📱 Application Information

| Property | Value |
|---|---|
| Application Name | Stocklify |
| Package Name | `com.scalo.stocklify` |
| Platform | Android |
| Minimum SDK | 29 (Android 10) |
| Compile SDK | 36 |
| Target SDK | 36 |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | Offline-First |

---

## 🚀 Getting Started

### Prerequisites

Before running Stocklify, make sure you have:

- Android Studio
- JDK compatible with the project's Gradle configuration
- Android SDK 36
- An Android device or emulator running Android 10 or later

### Installation

1. Clone the repository.

```bash
git clone https://github.com/YOUR_USERNAME/stocklify.git
```

2. Open the project in Android Studio.

3. Allow Gradle to synchronize dependencies.

4. Configure the required Supabase credentials if cloud functionality is enabled.

5. Build and run the application.

---

## 🗺️ Roadmap

- [ ] Inventory management
- [ ] Product categorization
- [ ] Sales and transaction tracking
- [ ] Dashboard and business insights
- [ ] Offline-first data management
- [ ] Secure local database
- [ ] Receipt scanning and OCR
- [ ] Supabase authentication
- [ ] Cloud synchronization
- [ ] Multi-device support
- [ ] Team management
- [ ] AI-powered business assistant
- [ ] Advanced business analytics

---

## 🎯 Project Goals

Stocklify aims to simplify inventory management for small businesses by providing an affordable, reliable, and accessible digital solution.

Rather than requiring business owners to adapt to complicated enterprise software, Stocklify focuses on the everyday operations that matter most: knowing what is in stock, understanding what sells, and keeping business records organized.

---

## 👨‍💻 Development

Stocklify is an independent software project developed by **Julius Andrie Antonio** and designed by **Rose Ann Guarin**.

Built with ❤️ using Kotlin and Jetpack Compose.

---

*Stocklify — Smarter inventory. Simpler business.*
