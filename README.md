<div align="center">

<img src="https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
<img src="https://img.shields.io/badge/Compose-Material%203-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose" />
<img src="https://img.shields.io/badge/Navigation-3-34A853?style=for-the-badge&logo=android&logoColor=white" alt="Navigation 3" />
<img src="https://img.shields.io/badge/DI-Koin-FF4081?style=for-the-badge&logo=insert-koin&logoColor=white" alt="Koin" />
<img src="https://img.shields.io/badge/DB-Room-FF6D00?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room" />

<br /><br />

<img src="https://raw.githubusercontent.com/tandpfun/skill-icons/main/icons/Kotlin-Dark.svg" width="48" />
<h1>NexLedger</h1>

<h3>💎 Premium Offline-First Personal Finance Manager</h3>

<p>
  <b>Private.</b> No cloud. No accounts. No tracking.<br />
  <b>Fast.</b> Reactive Room database. Compose UI.<br />
  <b>Beautiful.</b> Material 3. Light, Dark, AMOLED themes.<br />
  <b>Complete.</b> Expenses, Income, Budgets, Goals, Reports, Charts.
</p>

</div>

---

## ✨ Features

<table>
<tr>
  <td width="50%">

### 📊 Dashboard
At-a-glance financial health — total balance, monthly income/expenses, recent transactions, budget progress bars. Pull-to-refresh, FAB for quick-add.

### 💳 Accounts
Track Cash, Bank, Wallet, Credit Card & Savings accounts. Real-time balance tracking with transfer support. Swipe to delete with confirmation.

### 🔖 Transactions
Full CRUD with search, filter (type / category / account / date range), sort (date / amount). Merchant name, notes, date picker. Category & account dropdowns.

  </td>
  <td width="50%">

### 🏷️ Categories
Income & expense categories with custom icons and color picker. Tabbed view. Default presets included.

### 🎯 Budgets
Monthly category spending limits. Visual progress bars with color-coded alerts (green → yellow → red). WorkManager background alerts at 90%+.

### 🏆 Goals
Savings targets with deadline tracking. Progress visualization. Deadline reminder notifications 7 days out.

### 📈 Reports & Statistics
Monthly / yearly income–expense–savings reports. Category spending breakdown with percentage bars. Net savings analysis.

  </td>
</tr>
<tr>
  <td width="100%" colspan="2">

### 🔍 Search & Filters
Full-text search across notes and merchants. Type/category/account filter chips. Date range filtering.

### 💾 Backup & Restore
JSON & CSV export via Storage Access Framework (file picker). JSON import restore with data validation. All local — no cloud.

### 🔒 Security
Optional 4-digit PIN lock with confirmation flow. Biometric (fingerprint) unlock. Data never leaves the device.

### 🎨 Theming
Light, Dark, AMOLED, System-follow themes. Material You dynamic colors on Android 12+. Edge-to-edge rendering.

### 🔔 Notifications
Background budget alerts, goal deadline reminders, and recurring bill reminders via WorkManager periodic daily checks.

  </td>
</tr>
</table>

---

## 🧱 Architecture

```
┌──────────────────────┐
│   Compose Screen     │  observes StateFlow<State>
└──────────┬───────────┘
           │ Action (sealed interface)
           ▼
┌──────────────────────┐
│   ViewModel           │  @Koin viewModel { }
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Repository          │  Interface → Impl (Koin singleton)
└──────────┬───────────┘
           │
           ▼
┌──────────────────────┐
│   Room / DataStore    │  6 entities · 6 DAOs · indexed FKs
└──────────────────────┘
```

> **MVI** (Model-View-Intent) — every screen: `State.kt` → `Action.kt` → `ViewModel.kt` → `Screen.kt`

---

## 📦 Tech Stack

| Layer | Technology |
|:------|:-----------|
| Language | **Kotlin** 2.1 |
| UI Toolkit | **Jetpack Compose** + Material 3 |
| Navigation | **Navigation 3** (`androidx.navigation3:*`) |
| DI | **Koin** 4.0 |
| Database | **Room** 2.6 |
| Preferences | **DataStore** |
| Background | **WorkManager** |
| Serialization | **kotlinx.serialization** |
| Build | **Gradle KTS** + Version Catalog |
| Testing | JUnit · MockK · Turbine · Compose UI Test |
| CI/CD | **GitHub Actions** (lint, test, build, release) |
| Quality | **Detekt** static analysis |

---

## 🗂️ Module Map

```
NexLedger (22 modules, 140+ Kotlin files)

app/                              Application + MainActivity

core/
├── common/                       DateUtils, extensions
├── model/                        6 domain models
├── database/                     6 entities + 6 DAOs + 12 repos + DI
├── datastore/                    Preferences (theme, security, currency)
├── design-system/                M3 theme — Light/Dark/AMOLED/Dynamic
├── navigation/                   Nav3 routes + NavDisplay + bottom bar
└── ui/                           Reusable: SummaryCard, TxRow, EmptyState, ErrorState

features/
├── dashboard/                    Summary, recent txs, budget bars
├── transactions/                 List + Form + Detail (12 files)
├── accounts/                     List + Form (8 files)
├── categories/                   List + Form (8 files)
├── budgets/                      List + Form (8 files)
├── goals/                        List + Form (8 files)
├── reports/                      Income/expense summary
├── statistics/                   Category breakdown charts
├── search/                       Full-text + filters
├── backup/                       JSON/CSV export + import
├── settings/                     Theme, security nav, currency
├── security/                     PIN setup numpad, biometric
└── notifications/                WorkManager daily checks
```

---

## 🚀 Quick Start

```bash
# Clone
git clone https://github.com/Act-Aks/NexLedger.git
cd NexLedger

# Generate Gradle wrapper
gradle wrapper --gradle-version 8.11.1

# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew test

# Static analysis
./gradlew lint detekt

# Instrumented tests (needs emulator)
./gradlew connectedCheck
```

Open in **Android Studio Hedgehog+** → File → Open → select `NexLedger/` → Sync → Run.

---

## 🔄 CI Pipeline

```
┌──────────┐    ┌───────────┐    ┌──────────────────┐    ┌───────────────┐
│  Lint +  │───▶│   Unit    │───▶│  Instrumented    │───▶│  Build Debug  │
│  Detekt  │    │   Tests   │    │  Tests (API 34)  │    │  + Release    │
└──────────┘    └───────────┘    └──────────────────┘    └───────┬───────┘
                                                                 │
                                                          Tag v* │
                                                                 ▼
                                                        ┌──────────────┐
                                                        │ GitHub Draft │
                                                        │   Release    │
                                                        └──────────────┘
```

- Gradle build **caching** enabled for fast CI
- **concurrency groups** prevent redundant runs
- All test results & APKs uploaded as artifacts

---

## 🧪 Testing

| Layer | Framework | What's Tested |
|:------|:----------|:--------------|
| ViewModel | JUnit + MockK + Turbine | State transitions, action dispatch, error handling |
| Repository | JUnit + MockK | DAO delegation, null safety, CRUD operations |
| Compose UI | Compose UI Testing | Screen rendering, title/content assertions |

---

## 🏗️ Design Decisions

- **No use cases for CRUD** — Repository access is enough for inserts/deletes/queries. Use cases reserved for complex multi-repo operations (reports, budget calculations).
- **Navigation callbacks** — Screens emit `sealed interface Navigation` events; never hold navigation references.
- **Client-side filtering** — Transactions screen loads all data reactively, applies filters/sort/search in the ViewModel.
- **Per-tab back stacks** — Each bottom tab has an independent `NavBackStack`, preserving state across tab switches.
- **Bottom bar auto-hide** — Detects non-tab routes via back stack entry inspection.

---

## 🔐 Security

- **Room** on-device only — no network permission required
- **PIN lock** — 4-digit with confirmation flow, stored in DataStore
- **Biometric** — fingerprint unlock (Android BiometricPrompt)
- **No analytics, no ads, no cloud** — zero data exfiltration
- **ProGuard/R8** minification for release builds

---

## 📄 License

MIT © [Act-Aks](https://github.com/Act-Aks)

---

<div align="center">
  <sub>Built with ❤️ by <a href="https://github.com/Act-Aks">Act-Aks</a></sub>
</div>
