package com.majotyler.hiittimer.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.platform.UrlOpener
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import com.majotyler.hiittimer.presentation.buildWorkouts.BuildWorkoutsDestination
import com.majotyler.hiittimer.presentation.buildWorkouts.BuildWorkoutsViewEvent
import com.majotyler.hiittimer.presentation.buildWorkouts.BuildWorkoutsViewModel
import com.majotyler.hiittimer.presentation.buildWorkouts.BuildWorkoutsViewModelFactory
import com.majotyler.hiittimer.presentation.addWorkoutScreen.CreateWorkoutScreen
import com.majotyler.hiittimer.presentation.addWorkoutScreen.AddWorkoutDestination
import com.majotyler.hiittimer.presentation.addWorkoutScreen.AddWorkoutViewModelFactory
import com.majotyler.hiittimer.presentation.buildWorkouts.BuildWorkoutsScreen
import com.majotyler.hiittimer.presentation.homeScreen.HomeDestination
import com.majotyler.hiittimer.presentation.homeScreen.HomeScreen
import com.majotyler.hiittimer.presentation.homeScreen.HomeViewModelFactory
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutDestination
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutScreen
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutViewModelFactory
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun NavigationRoot(
    urlOpener: UrlOpener,
) {
    HiitAppTheme {
        HiitNavDisplay(
            urlOpener = urlOpener,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun HiitNavDisplay(
    urlOpener: UrlOpener,
) {

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
                        subclass = Route.BuildWorkouts::class,
                        serializer = Route.BuildWorkouts.serializer(),
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
                val viewModelFactory = AddWorkoutViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            is AddWorkoutDestination.AddWorkout -> {
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
                            HomeDestination.NavigateToBuildWorkouts -> {
                                backStack.add(element = Route.BuildWorkouts)
                            }
                        }
                    }
                )

                HomeScreen(
                    urlOpener = urlOpener,
                    viewModel = viewModel(factory = viewModelFactory),
                )
            }
            entry<Route.BuildWorkouts> {
                val viewModelFactory = BuildWorkoutsViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            is BuildWorkoutsDestination.NavigateToAddWorkout ->
                                backStack.add(element = Route.AddWorkout)

                            is BuildWorkoutsDestination.NavigateToPlayWorkout -> {
                                backStack.add(
                                    element = Route.PlayWorkout(workouts = destination.workouts),
                                )
                            }
                        }
                    }
                )

                val viewModel: BuildWorkoutsViewModel = viewModel<BuildWorkoutsViewModel>(
                    factory = viewModelFactory,
                )

                ResultEffect<Workout>(resultBus) { workout ->
                    viewModel.onEvent(
                        event = BuildWorkoutsViewEvent.AddedWorkout(workout = workout)
                    )
                }

                BuildWorkoutsScreen(
                    viewModel = viewModel(factory = viewModelFactory),
                )
            }

            entry<Route.PlayWorkout> { route ->
                val viewModelFactory = PlayWorkoutViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            PlayWorkoutDestination.NavigateUp -> backStack.removeLast()
                        }
                    },
                    workouts = route.workouts,
                )

                PlayWorkoutScreen(
                    viewModel = viewModel(
                        factory = viewModelFactory,
                        key = Uuid.random().toString(),
                    ),
                )
            }
        }
    )
}