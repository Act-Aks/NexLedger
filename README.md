<div align="center">

# 💎 NexLedger

### Offline‑first personal finance manager built with Kotlin & Jetpack Compose

Take control of your money without logins, accounts, or cloud sync.  
Everything stays on your device — private, fast, and beautiful.

<br>

<!-- CI / quality badges (jobs from nexledger-ci.yml) -->
[![Build](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml/badge.svg?job=build)](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml)
[![Unit Tests](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml/badge.svg?job=unit-tests)](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml)
[![Lint](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml/badge.svg?job=lint)](https://github.com/Act-Aks/NexLedger/actions/workflows/nexledger-ci.yml)

<!-- Project / repo badges -->
[![Release](https://img.shields.io/github/v/release/Act-Aks/NexLedger?style=flat-square)](https://github.com/Act-Aks/NexLedger/releases)
[![Downloads](https://img.shields.io/github/downloads/Act-Aks/NexLedger/total?style=flat-square)](https://github.com/Act-Aks/NexLedger/releases)
[![Last Commit](https://img.shields.io/github/last-commit/Act-Aks/NexLedger?style=flat-square)](https://github.com/Act-Aks/NexLedger/commits/main)
[![Issues](https://img.shields.io/github/issues/Act-Aks/NexLedger?style=flat-square)](https://github.com/Act-Aks/NexLedger/issues)
[![Stars](https://img.shields.io/github/stars/Act-Aks/NexLedger?style=social)](https://github.com/Act-Aks/NexLedger/stargazers)
[![License](https://img.shields.io/github/license/Act-Aks/NexLedger?style=flat-square)](LICENSE)

<br>

<!-- Tech badges -->
<img src="https://img.shields.io/badge/Kotlin-2.1-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
<img src="https://img.shields.io/badge/Compose-Material3-4285F4?style=for-the-badge&logo=jetpackcompose" />
<img src="https://img.shields.io/badge/Room-Database-FF6D00?style=for-the-badge&logo=sqlite" />
<img src="https://img.shields.io/badge/Koin-DI-FF4081?style=for-the-badge" />
<img src="https://img.shields.io/badge/Navigation3-Latest-34A853?style=for-the-badge" />

<br>

[▶️ Download latest APK](https://github.com/Act-Aks/NexLedger/releases) ·
[📱 Screenshots](#-screenshots) ·
[✨ Features](#-features) ·
[🧱 Architecture](#-architecture)

</div>

---

## 📱 Screenshots

> Replace these with your real screenshots (e.g. `docs/...`).

| Dashboard               | Transactions               | Reports               |
|-------------------------|----------------------------|-----------------------|
| ![](docs/dashboard.png) | ![](docs/transactions.png) | ![](docs/reports.png) |

Seeing the UI is the quickest way to understand what NexLedger offers.

---

## ❤️ Why NexLedger?

Most finance apps want your email, your account, and your data. NexLedger doesn’t.

- ✅ No account or signup
- ✅ No ads or trackers
- ✅ No analytics
- ✅ No cloud sync
- ✅ Works completely offline
- ✅ Fast, modern Material 3 interface

If you care about privacy and still want rich, actionable insights into your spending and savings,
NexLedger is for you.

---

## ✨ Features

High‑level overview:

- 💳 Multiple accounts (cash, bank, wallet, credit card, savings)
- 💸 Income & expense tracking
- 📊 Budget management with progress bars & alerts
- 🎯 Savings goals with deadlines and reminders
- 📈 Reports & analytics (monthly/yearly, categories, net savings)
- 🔎 Powerful search & filters (type, category, account, date range)
- 💾 JSON & CSV backup & restore
- 🔒 PIN & biometric authentication
- 🌙 Light, Dark & AMOLED themes + dynamic colors
- 🔔 Smart reminders (budgets, goals, recurring bills)
- 🚀 Offline‑first, Room‑backed storage

More detail, for readers who scroll:

### 💳 Accounts

Track Cash, Bank, Wallet, Credit Card, and Savings accounts with real‑time balances. Transfers keep
accounts in sync, and swipe‑to‑delete always confirms before removing anything important.

### 🔖 Transactions

Create, edit, and browse transactions with full‑text search and filters (type, category, account,
date range). Add merchants and notes, and pick categories/accounts with lightweight, Compose‑based
dropdowns.

### 🎯 Budgets & Goals

Set monthly spending limits per category and watch progress with clear, color‑coded bars (green →
yellow → red). Define savings goals with target amounts and deadlines, plus 7‑day reminder
notifications.

### 📈 Reports & Statistics

See monthly and yearly income–expense–savings reports at a glance. Drill down into category
breakdowns and net savings, so it’s easy to understand where your money is going.

### 🔍 Search & Filters

Use full‑text search across notes and merchants, plus type/category/account chips and date‑range
filtering. The transactions list remains smooth thanks to client‑side filtering on reactive data.

### 💾 Backup & Restore

Export JSON and CSV via the Storage Access Framework. Import JSON with validation to restore your
data safely. Everything is local — NexLedger doesn’t talk to any servers.

### 🔒 Security & Theming

Secure access with a 4‑digit PIN and biometric unlock using Android `BiometricPrompt`. Pick Light,
Dark, AMOLED, or system‑follow themes, and enjoy Material You dynamic colors on Android 12+.

---

## 🚀 Quick Start

Minimal steps to get going:

```bash
# Clone
git clone https://github.com/Act-Aks/NexLedger.git
cd NexLedger

# Build debug APK + run core checks
./gradlew assembleDebug test lint detekt
```

Open in **Android Studio Hedgehog+** → `File` → `Open` → select `NexLedger/` → Sync → Run.

Instrumented tests (API 34 emulator):

```bash
./gradlew connectedCheck
```

---

## 🛠 Tech Stack

|               |                                             |
|---------------|---------------------------------------------|
| Language      | Kotlin 2.1                                  |
| UI            | Jetpack Compose + Material 3                |
| Navigation    | Navigation 3 (`androidx.navigation3:*`)     |
| Database      | Room 2.6                                    |
| DI            | Koin 4.0                                    |
| Preferences   | DataStore                                   |
| Background    | WorkManager                                 |
| Serialization | kotlinx.serialization                       |
| Build         | Gradle KTS + Version Catalog                |
| Testing       | JUnit · MockK · Turbine · Compose UI Test   |
| CI            | GitHub Actions (lint, test, build, release) |
| Quality       | Detekt static analysis                      |

---

## 📦 Project Structure

Technical depth is available, but folded by default:

<details>
<summary>📦 Modules & folders</summary>

```text
app/

core/
├── common          # Date utils, extensions
├── database        # Room entities, DAOs, repositories
├── datastore       # DataStore (theme, security, currency)
├── design-system   # Material 3 theme — Light/Dark/AMOLED/Dynamic
├── model           # Domain models
├── navigation      # Navigation 3 routes, bottom bar
└── ui              # Reusable UI: cards, rows, empty/error states

features/
├── dashboard       # Summary, recent txs, budget bars
├── transactions    # List, form, detail
├── accounts        # List, form
├── categories      # List, form
├── budgets         # List, form
├── goals           # List, form
├── reports         # Income/expense summary
├── statistics      # Category breakdown charts
├── search          # Full-text + filters
├── backup          # JSON/CSV export + import
├── settings        # Theme, security, currency
├── security        # PIN setup numpad, biometric
└── notifications   # WorkManager daily checks
```

</details>

---

## 🧱 Architecture

```text
┌──────────────────────┐
│ Compose Screen       │ observes StateFlow<State>
└──────────┬───────────┘
           │ Action (sealed interface)
           ▼
┌──────────────────────┐
│ ViewModel            │ @Koin viewModel { }
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Repository           │ Interface → Impl (Koin singleton)
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│ Room / DataStore     │ 6 entities · 6 DAOs · indexed FKs
└──────────────────────┘
```

Each screen follows a clear **MVI** flow: `State.kt` → `Action.kt` → `ViewModel.kt` → `Screen.kt`.
This keeps state management explicit, makes side‑effects predictable, and keeps navigation and data
logic out of the UI layer.

---

## 🧪 Testing & CI

### Testing

| Layer      | Framework               | What’s tested                                |
|------------|-------------------------|----------------------------------------------|
| ViewModel  | JUnit + MockK + Turbine | State transitions, intents, error handling   |
| Repository | JUnit + MockK           | DAO delegation, null safety, CRUD operations |
| Compose UI | Compose UI Testing      | Screen rendering, titles, content assertions |

### CI (GitHub Actions)

- CI runs on `main` and `develop` pushes, and on pull requests to `main`.
- Jobs: `lint` (Android lint + Detekt), `unit-tests` (JUnit/Turbine), `build` (debug + release APK),
  and `release` (tagged `v*` GitHub Releases draft).
- Badges at the top reflect the latest job status for this workflow, via GitHub’s official
  workflow‑status endpoint, and update automatically on every run. [web:2][web:6]

---

## 🔐 Security

- Room database is on‑device only — no network permission required.
- 4‑digit PIN lock with confirmation, stored via DataStore.
- Biometric unlock using Android `BiometricPrompt`.
- No analytics, no ads, no cloud — zero data exfiltration.
- ProGuard/R8 minification for release builds.

---

## 📄 License

MIT © [Act-Aks](https://github.com/Act-Aks)

Built with ❤️ using Kotlin, Jetpack Compose, and modern Android tooling.