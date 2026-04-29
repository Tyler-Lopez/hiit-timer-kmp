# HIIT Timer — Kotlin Multiplatform

HIIT Timer is a cross-platform workout timer for Android and iOS, built with Kotlin 
Multiplatform and Compose Multiplatform. Users build a queue of workouts with custom 
intervals, work and rest durations, and a repetition count, then run the queue 
back-to-back with a real-time progress timer. Completed sessions can be logged to 
Strava via OAuth.

The codebase is split into three layers (data, domain, presentation) where the domain 
has no dependency on the other two. The app is fully localized in English and Spanish.

---

## 1. ViewModels decoupled from navigation

### Problem

In most Compose apps, ViewModels trigger navigation by calling into a navigation 
controller or emitting events tied to a specific framework. This creates a hard 
dependency: the ViewModel needs the navigation library to compile, and testing 
requires mocking framework internals.

### Approach

Navigation outcomes are expressed as a `sealed Destination` interface per screen. 
The ViewModel calls `router.routeTo(destination)`, where `Router<Destination>` is a 
`fun interface` injected as a lambda from `NavigationRoot`. The ViewModel imports 
nothing from Compose or any navigation library.

```kotlin
fun interface Router<Destination> {
    fun routeTo(destination: Destination)
}
```

### Result

ViewModels compile and test in isolation. Navigation behavior is verified by asserting 
which `Destination` was passed to the router, with no test doubles for framework classes.

---

## 2. Cross-screen results without shared state

### Problem

When `AddWorkoutScreen` completes, it needs to return a `Workout` to `BuildWorkoutsScreen`. 
The common solution, a shared ViewModel, ties two screens together that have no business 
knowing about each other. Framework-specific result APIs exist but bind the solution 
to a platform.

### Approach

A `ResultEventBus` backed by a Kotlin `Channel` handles the handoff. The sender calls 
`sendResult<Workout>(result)` and the receiver subscribes with `ResultEffect<Workout>`. 
The channel key is derived from the type using reified generics, so there are no string 
literals that can drift out of sync.

```kotlin
// sender
resultBus.sendResult<Workout>(result = workout)

// receiver
ResultEffect<Workout>(resultBus) { workout ->
    viewModel.onEvent(BuildWorkoutsViewEvent.AddedWorkout(workout))
}
```

### Result

Neither screen holds a reference to the other. The bus is scoped to the navigation 
graph and works identically on Android and iOS.

---

## 3. A timer that measures time instead of counting ticks

### Problem

A naive timer increments a counter by a fixed amount on each coroutine tick. The problem 
is that `delay` is not perfectly precise and can oversleep by a few milliseconds per frame. 
Over hundreds of ticks, that error accumulates and the displayed time diverges from 
real elapsed time.

### Approach

The timer snapshots a `TimeMark` from Kotlin's monotonic clock on each tick and measures 
actual elapsed milliseconds, then resets the mark for the next interval.

```kotlin
currentStepProgressMs += runningMark.elapsedNow().inWholeMilliseconds
runningMark = TimeSource.Monotonic.markNow()
```

### Result

Progress always reflects real elapsed time regardless of scheduler noise. The update 
loop runs at ~60 fps to keep the circular progress arc visually smooth.

---

## Stack

Kotlin 2.2 · Compose Multiplatform 1.9 · Ktor 3 · JetBrains Navigation3 ·
kotlinx-datetime · kotlinx-serialization
