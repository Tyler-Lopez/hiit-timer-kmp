package com.majotyler.hiittimer.presentation.common.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.majotyler.hiittimer.data.repository.StravaTokenStorageRepository
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.platform.UrlOpener
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import com.majotyler.hiittimer.presentation.chooseWorkout.ChooseWorkoutDestination
import com.majotyler.hiittimer.presentation.chooseWorkout.ChooseWorkoutViewEvent
import com.majotyler.hiittimer.presentation.chooseWorkout.ChooseWorkoutViewModel
import com.majotyler.hiittimer.presentation.chooseWorkout.ChooseWorkoutViewModelFactory
import com.majotyler.hiittimer.presentation.chooseWorkout.ChooseWorkoutScreen
import com.majotyler.hiittimer.presentation.createWorkoutScreen.CreateWorkoutScreen
import com.majotyler.hiittimer.presentation.createWorkoutScreen.CreateWorkoutDestination
import com.majotyler.hiittimer.presentation.createWorkoutScreen.CreateWorkoutViewModelFactory
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
    stravaAccessCode: String?,
    stravaTokenStorageRepository: StravaTokenStorageRepository,
) {
    HiitAppTheme {
        HiitNavDisplay(
            urlOpener = urlOpener,
            stravaAccessCode = stravaAccessCode,
            stravaTokenStorageRepository = stravaTokenStorageRepository,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun HiitNavDisplay(
    urlOpener: UrlOpener,
    stravaAccessCode: String?,
    stravaTokenStorageRepository: StravaTokenStorageRepository,
) {

    val resultBus = remember { ResultEventBus() }

    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            this.serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(
                        subclass = Route.CreateWorkout::class,
                        serializer = Route.CreateWorkout.serializer(),
                    )
                    subclass(
                        subclass = Route.Home::class,
                        serializer = Route.Home.serializer(),
                    )
                    subclass(
                        subclass = Route.ChooseWorkout::class,
                        serializer = Route.ChooseWorkout.serializer(),
                    )
                }
            }
        },
        Route.Home,
    )

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Route.CreateWorkout> { navEntry ->
                val viewModelFactory = CreateWorkoutViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            is CreateWorkoutDestination.CreatedWorkout -> {
                                resultBus.sendResult<Workout>(result = destination.workout)
                                backStack.removeLast()
                            }
                            CreateWorkoutDestination.NavigateUp -> backStack.removeLast()
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
                            HomeDestination.NavigateToChooseWorkout -> {
                                backStack.add(element = Route.ChooseWorkout)
                            }
                        }
                    },
                    stravaAccessCode = stravaAccessCode,
                    stravaTokenStorageRepository = stravaTokenStorageRepository,
                )

                HomeScreen(
                    urlOpener = urlOpener,
                    viewModel = viewModel(factory = viewModelFactory),
                )
            }

            entry<Route.ChooseWorkout> {
                val viewModelFactory = ChooseWorkoutViewModelFactory(
                    router = { destination ->
                        when (destination) {
                            is ChooseWorkoutDestination.NavigateToCreateWorkout ->
                                backStack.add(element = Route.CreateWorkout)

                            is ChooseWorkoutDestination.NavigateToPlayWorkout -> {
                                backStack.add(
                                    element = Route.PlayWorkout(workout = destination.workout),
                                )
                            }
                        }
                    }
                )

                val viewModel: ChooseWorkoutViewModel = viewModel<ChooseWorkoutViewModel>(
                    factory = viewModelFactory,
                )

                ResultEffect<Workout>(resultBus) { workout ->
                    viewModel.onEvent(
                        event = ChooseWorkoutViewEvent.CreatedWorkout(workout = workout)
                    )
                }

                ChooseWorkoutScreen(
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
                    workout = route.workout,
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
