# NexLedger — Architecture & Design

> Built by [Act-Aks](https://github.com/Act-Aks)

---

## Architecture Overview

NexLedger follows **MVI** (Model-View-Intent) on **Clean Architecture** principles, with **Navigation 3** for routing and **Koin** for dependency injection.

### Data Flow

```
User taps button
       │
       ▼
Screen calls viewModel.onAction(Action.XXX)
       │
       ▼
ViewModel receives Action (sealed interface)
       │
       ├─ Updates internal MutableStateFlow<State>
       ├─ Calls Repository.someMethod()
       └─ Emits new State via StateFlow
              │
              ▼
       Screen observes via collectAsStateWithLifecycle()
              │
              ▼
       Compose recomposes with new State
```

---

## Layer Diagram

```
┌─────────────────────────────────────────────────┐
│  UI Layer (Compose Screens)                     │
│  · Stateless composables                        │
│  · observe StateFlow<State>                     │
│  · emit Action sealed events                    │
│  · handle Navigation via onNavigate lambda      │
│  · ViewModels injected via koinViewModel()      │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│  ViewModel Layer                                │
│  · Plain Kotlin class (no Hilt annotations)     │
│  · constructor(repo: Repo, ...)                  │
│  · MutableStateFlow<State> → StateFlow<State>   │
│  · fun onAction(action) dispatcher              │
│  · viewModelScope.launch { }                    │
│  · Registered in Koin: viewModel { VM(get()) }  │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│  Repository Layer                               │
│  · Interface (pure Kotlin, in database module)  │
│  · Implementation (injects DAO, no annotations) │
│  · Registered in Koin: single<Repo> { Impl() }  │
│  · Optional Use Cases for complex logic         │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│  Data Layer                                     │
│  · Room Database & DAOs                         │
│  · DataStore Preferences (theme, security)      │
│  · Room Entities (persistence layer)            │
│  · Domain Models (pure data classes in core)    │
└─────────────────────────────────────────────────┘
```

---

## Navigation 3 Architecture

### Routes

```kotlin
sealed interface NexLedgerRoute : NavKey {
    @Serializable data object Dashboard : NexLedgerRoute
    @Serializable data class EditTransaction(val transactionId: Long) : NexLedgerRoute
    // ... 23 routes total
}
```

### State Management

```kotlin
@Composable
fun rememberNavigationState(startRoute): NavigationState {
    // Per-tab NavBackStacks via rememberNavBackStack(key)
    // Current tab preserved across config changes via rememberSerializable
}
```

### Navigation Actions

```kotlin
// Navigate forward
backStack.add(NexLedgerRoute.AddTransaction())

// Go back
backStack.removeLast()

// Switch tab (preserves each tab's back stack)
navigationState.topLevelRoute.value = NexLedgerRoute.Accounts
```

### Entry Provider

```kotlin
entryProvider {
    entry<NexLedgerRoute.Dashboard> { route ->
        NavEntry(route) {
            DashboardScreen(onNavigate = { event -> ... })
        }
    }
}
```

---

## Module Dependency Rules

```
app ──► features/* ──► core/database ──► core/model
   │                    core/datastore
   │                    core/design-system
   │                    core/navigation
   │                    core/ui
   │                    core/common
```

- **core/model** — zero dependencies (pure Kotlin data classes)
- **core/database** — depends on core/model + Room
- **core/design-system** — depends only on Compose + Material 3
- **features/\*** — depend on core modules, NOT on other features
- **core/navigation** — depends on all feature modules (wires destinations)

---

## MVI Pattern

### State

Immutable data class with all default values:

```kotlin
data class DashboardState(
    val totalBalance: Double = 0.0,
    val incomeThisMonth: Double = 0.0,
    val loading: Boolean = true,
    val error: String? = null
)
```

### Action

Sealed interface:

```kotlin
sealed interface DashboardAction {
    data object Refresh : DashboardAction
    data class TransactionClicked(val transactionId: Long) : DashboardAction
    data object ErrorDismissed : DashboardAction
}
```

### ViewModel

```kotlin
class DashboardViewModel(
    private val accountRepo: AccountRepository,
    // ... other repos
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init { refresh() }

    fun onAction(action: DashboardAction) {
        when (action) {
            DashboardAction.Refresh -> refresh()
            is DashboardAction.TransactionClicked -> { /* emit side-effect */ }
            DashboardAction.ErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }
}
```

### Screen

```kotlin
@Composable
fun DashboardScreen(
    onNavigate: (DashboardNavigation) -> Unit = {},
    viewModel: DashboardViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // ... render UI based on state
}
```

---

## Koin DI Structure

```kotlin
// app/NexLedgerApp.kt
startKoin {
    androidContext(this@NexLedgerApp)
    modules(
        databaseModule,       // Room DB + DAOs (singles)
        repositoryModule,     // Repository interfaces → impls (singles)
        datastoreModule,      // Preferences
        dashboardModule,      // DashboardViewModel
        accountsModule,       // AccountViewModel, AccountFormViewModel
        // ... 13 feature modules
    )
}

// Each feature module:
val dashboardModule = module {
    viewModel { DashboardViewModel(get(), get(), get(), get()) }
}
```

---

## Room Database

| Entity | Table | Indices |
|:-------|:------|:--------|
| TransactionEntity | transactions | date, categoryId, accountId |
| AccountEntity | accounts | — |
| CategoryEntity | categories | — |
| BudgetEntity | budgets | categoryId (unique) |
| GoalEntity | goals | — |
| RecurringTransactionEntity | recurring_transactions | — |

All DAOs return `Flow<List<>>` for reactive observation.

---

## Theme System

| Variant | Surface | Use Case |
|:--------|:--------|:---------|
| Light | `#FAFAFA` | Daytime |
| Dark | `#0F172A` | Nighttime |
| AMOLED | `#000000` | Battery saving |
| System | Auto | Follows device setting |
| Dynamic | Material You | Android 12+ dynamic colors |

---

## Key Design Decisions

| Decision | Rationale |
|:---------|:----------|
| Koin over Hilt | No code generation, no annotations, faster builds |
| Nav 3 over Nav 2 | True type safety, per-tab back stacks, simpler API |
| No Use Cases for CRUD | Repository pattern sufficient; Use Cases only for multi-repo ops |
| Client-side filtering | Load all transactions reactively, filter in ViewModel |
| Navigation callbacks | Screens don't hold NavBackStack refs — decoupled |
| Per-tab back stacks | Each tab preserves independent navigation state |
| Bottom bar auto-hide | Detects non-tab routes from back stack entries |

---

## Performance

- **LazyColumn** for all lists — no eager composition
- **Stable model classes** — minimize recomposition
- **StateFlow** — lifecycle-aware state observation
- **Room indices** — fast queries on date, categoryId, accountId
- **DataStore** — async preferences, no main-thread I/O
- **WorkManager** — background work with battery optimization

---

Built by [Act-Aks](https://github.com/Act-Aks) · MIT License
