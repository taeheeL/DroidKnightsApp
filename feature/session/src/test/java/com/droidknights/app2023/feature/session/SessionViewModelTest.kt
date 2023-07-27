package com.droidknights.app2023.feature.session

import app.cash.turbine.test
import com.droidknights.app2023.core.domain.usecase.GetSessionsUseCase
import com.droidknights.app2023.core.model.Level
import com.droidknights.app2023.core.model.Room
import com.droidknights.app2023.core.model.Session
import com.droidknights.app2023.core.testing.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

internal class SessionViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getSessionsUseCase: GetSessionsUseCase = mockk()
    private lateinit var viewModel: SessionViewModel

    private val fakeSession = Session(
        title = "title",
        content = emptyList(),
        speakers = emptyList(),
        level = Level.BASIC,
        tags = emptyList(),
        room = Room.TRACK1,
        startTime = LocalDateTime(2023, 9, 12, 13, 0, 0),
        endTime = LocalDateTime(2023, 9, 12, 13, 30, 0),
    )

    @Test
    fun `세션 데이터를 확인할 수 있다`() = runTest {
        // given
        coEvery { getSessionsUseCase() } returns listOf(fakeSession)
        viewModel = SessionViewModel(getSessionsUseCase)

        // when & then
        viewModel.uiState.test {
            val actual = awaitItem().sessions.first()
            assertEquals(fakeSession, actual)
        }
    }

    @Test
    fun `트랙별 세션 데이터를 확인할 수 있다`() = runTest {
        // given
        coEvery { getSessionsUseCase() } returns listOf(
            fakeSession(Room.TRACK1), fakeSession(Room.TRACK1), fakeSession(Room.TRACK1),

            fakeSession(Room.TRACK2), fakeSession(Room.TRACK2),

            fakeSession(Room.TRACK3),
        )
        viewModel = SessionViewModel(getSessionsUseCase)

        // when & then
        viewModel.uiState.test {
            val actual = awaitItem()
            assertEquals(3, actual.getSessionsBy(Room.TRACK1).size)
            assertEquals(2, actual.getSessionsBy(Room.TRACK2).size)
            assertEquals(1, actual.getSessionsBy(Room.TRACK3).size)
        }
    }

    private fun fakeSession(room: Room): Session = fakeSession.copy(room = room)
}