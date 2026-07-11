# 📦 Stocklify

**Stocklify** is an offline-first inventory and point-of-sale (POS) system designed for small businesses, sari-sari stores, and retail shops. It enables business owners to manage products, sales, inventory, and multiple stores while seamlessly syncing data to the cloud when an internet connection is available.

The application is built with a mobile-first approach and prioritizes speed, reliability, and ease of use even in areas with unstable internet connectivity.

---

## ✨ Features

### 📊 Dashboard
- Business overview
- Daily, weekly, and monthly sales summary
- Inventory insights
- Low stock alerts
- Quick access to common actions

### 🛒 Sales (POS)
- Fast checkout experience
- Barcode scanning
- Product search
- Automatic stock deduction
- Sales history
- Receipt generation *(planned)*

### 📦 Inventory Management
- Create, edit, and delete products
- Product images
- Categories
- Suppliers
- Stock adjustments
- Cost and selling prices
- Barcode support

### 🏪 Store Management
- Multiple stores
- Store switching
- Store-specific inventory
- Organization management

### 👥 Team Management
- Invite members
- Organization roles
- Secure access control
- Multi-user collaboration

### 🔄 Offline First
- Local SQLite database
- Full offline functionality
- Automatic synchronization
- Conflict resolution
- Background sync

### 🔐 Authentication
- Secure login
- Organization-based permissions
- Row Level Security (RLS)
- Role-based access

### ☁️ Cloud Sync
- Automatic backup
- Multi-device synchronization
- Incremental sync
- Sync status monitoring

---

## 🚀 Tech Stack

### Mobile
- Kotlin
- Jetpack Compose
- Android SDK
- Material 3
- Room Database
- DataStore
- Kotlin Coroutines
- Kotlin Serialization

### Backend
- Supabase
- PostgreSQL
- PostgREST
- PostgreSQL Functions (RPC)
- Row Level Security (RLS)
- Supabase Auth
- Supabase Storage
- Supabase Realtime

### Notifications
- Firebase Cloud Messaging (FCM)

---

## 🏗️ Architecture

```
Presentation
    │
    ▼
ViewModel
    │
    ▼
Repository
    │
 ┌──┴─────────────┐
 ▼                ▼
Local DB      Remote API
(Room)        (Supabase)
```

The application follows **Clean Architecture** principles with separation of concerns between presentation, domain, and data layers.

---

## 📱 Offline-First Design

Stocklify is designed to work without internet connectivity.

### Local Database
- SQLite (Room)
- Primary data source
- Instant data access

### Cloud Sync
- Incremental synchronization
- Version tracking
- Automatic retries
- Conflict resolution

---

## 🔒 Security

- Supabase Authentication
- PostgreSQL Row Level Security (RLS)
- Organization-based permissions
- Secure RPC functions
- Role-based access control

---

## 📂 Project Structure

```
app/
├── core/
│   ├── database/
│   ├── network/
│   ├── datastore/
│   ├── sync/
│   ├── notification/
│   └── ui/
│
├── feature/
│   ├── dashboard/
│   ├── inventory/
│   ├── sales/
│   ├── organization/
│   ├── settings/
│   └── profile/
│
└── di/
```

---

## 📦 Planned Features

- AI-powered inventory assistant
- OCR product extraction
- Sales analytics
- PDF reports
- Receipt printing
- Customer management
- Supplier management
- Expense tracking
- Barcode generation
- Multi-language support
- Dark mode improvements
- Web dashboard
- iOS application

---

## 🎯 Target Users

- Sari-sari stores
- Small retail businesses
- Mini marts
- Grocery stores
- Convenience stores
- Wholesalers

---

## 📈 Roadmap

### Version 1.0
- Inventory Management
- POS
- Offline Support
- Cloud Sync
- Multi-store
- Organization Management

### Version 1.1
- Reports
- Receipt Printing
- Supplier Module
- Categories

### Version 1.2
- AI Assistant
- OCR Product Scanner
- Smart Insights

### Future
- Web Application
- iOS Application
- Advanced Analytics
- Integrations

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

Developed by **Julius Andrie Antonio**.
Designed by **Rose Ann Guarin**
Building modern solutions for small businesses through mobile technology and AI.

---

## ⭐ Vision

Our goal is to empower every small business with an affordable, modern, and reliable inventory management solution that works anytime, anywhere—even without an internet connection.
