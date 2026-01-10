package com.majotyler.hiittimer.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import com.majotyler.hiittimer.presentation.TimerDestination
import com.majotyler.hiittimer.presentation.TimerScreen
import com.majotyler.hiittimer.presentation.TimerViewEvent
import com.majotyler.hiittimer.presentation.TimerViewModel
import com.majotyler.hiittimer.presentation.TimerViewModelFactory
import com.majotyler.hiittimer.presentation.createWorkoutScreen.CreateWorkoutScreen
import com.majotyler.hiittimer.presentation.createWorkoutScreen.WorkoutDestination
import com.majotyler.hiittimer.presentation.createWorkoutScreen.WorkoutViewModelFactory
import com.majotyler.hiittimer.presentation.homeScreen.HomeDestination
import com.majotyler.hiittimer.presentation.homeScreen.HomeScreen
import com.majotyler.hiittimer.presentation.homeScreen.HomeViewModel
import com.majotyler.hiittimer.presentation.homeScreen.HomeViewModelFactory
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutScreen
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutVIewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun NavigationRoot() {
    HiitAppTheme {
        HiitNavDisplay()
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun HiitNavDisplay() {

    val resultBus = remember { ResultEventBus() }

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            this.serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        subclass = Route.AddWorkout::class,
                        serializer = Route.AddWorkout.serializer(),
                    )
                    subclass(
                        subclass = Route.Home::class,
                        serializer = Route.Home.serializer(),
                    )
                    subclass(
                        subclass = Route.Timer::class,
                        serializer = Route.Timer.serializer(),
                    )
                }
            }
        },
        Route.Home,
    )

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route.AddWorkout> { navEntry ->
                val viewModelFactory = WorkoutViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            is WorkoutDestination.AddWorkout -> {
                                resultBus.sendResult<Workout>(result = destination.workout)
                                backStack.removeLast()
                            }
                        }
                    }
                )

                CreateWorkoutScreen(
                    viewModel = viewModel(
                        key = Uuid.random().toString(),
                        factory = viewModelFactory,
                    )
                )
            }

            entry<Route.Home> {
                val viewModelFactory = HomeViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            HomeDestination.NavigateToTimer -> {
                                backStack.add(element = Route.Timer)
                            }
                        }
                    }
                )

                HomeScreen(
                    viewModel = viewModel(factory = viewModelFactory),
                )
            }
            entry<Route.Timer> {
                val viewModelFactory = TimerViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            TimerDestination.NavigateToAddWorkout ->
                                backStack.add(element = Route.AddWorkout)
                            TimerDestination.NavigateToPlayWorkout->
                                backStack.add(Route.PlayWorkout)
                        }
                    }
                )

                val viewModel: TimerViewModel = viewModel<TimerViewModel>(
                    factory = viewModelFactory,
                )

                ResultEffect<Workout>(resultBus) { workout ->
                    viewModel.onEvent(
                        event = TimerViewEvent.AddedWorkout(workout = workout)
                    )
                }

                TimerScreen(
                    viewModel = viewModel(factory = viewModelFactory),
                )
            }

            entry<Route.PlayWorkout> {
                val viewModel: PlayWorkoutVIewModel = viewModel()
                PlayWorkoutScreen(viewModel)
            }
        }
    )
}