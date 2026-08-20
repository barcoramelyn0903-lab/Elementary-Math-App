package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Difficulty
import com.example.data.model.EquationSlotPosition
import com.example.data.model.MathTopic
import com.example.data.model.WorldTheme
import com.example.logic.MathEngine
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("MathQuest", appName)
    }

    @Test
    fun `math engine addition equations are mathematically correct`() {
        val problems = MathEngine.generateProblems(
            topic = MathTopic.ADDITION,
            difficulty = Difficulty.EASY,
            worldTheme = WorldTheme.JUNGLE,
            count = 10
        )
        assertEquals(10, problems.size)
        problems.forEach { problem ->
            assertTrue(problem.options.contains(problem.correctAnswer))
            if (problem.slotPosition == EquationSlotPosition.RIGHT_RESULT) {
                val sum = problem.num1 + problem.num2
                assertEquals(sum.toString(), problem.correctAnswer)
            } else if (problem.slotPosition == EquationSlotPosition.MIDDLE_OPERAND) {
                val sum = problem.num1 + problem.correctAnswer.toInt()
                assertEquals(problem.resultNumber, sum)
            }
        }
    }

    @Test
    fun `math engine subtraction equations are mathematically correct`() {
        val problems = MathEngine.generateProblems(
            topic = MathTopic.SUBTRACTION,
            difficulty = Difficulty.MEDIUM,
            worldTheme = WorldTheme.SPACE,
            count = 10
        )
        assertEquals(10, problems.size)
        problems.forEach { problem ->
            assertTrue(problem.options.contains(problem.correctAnswer))
            val remainder = problem.num1 - problem.num2
            assertEquals(remainder.toString(), problem.correctAnswer)
            assertTrue(remainder >= 0)
        }
    }

    @Test
    fun `math engine multiplication equations are mathematically correct`() {
        val problems = MathEngine.generateProblems(
            topic = MathTopic.MULTIPLICATION,
            difficulty = Difficulty.EASY,
            worldTheme = WorldTheme.JUNGLE,
            count = 10
        )
        assertEquals(10, problems.size)
        problems.forEach { problem ->
            assertTrue(problem.options.contains(problem.correctAnswer))
            val product = problem.num1 * problem.num2
            assertEquals(product.toString(), problem.correctAnswer)
        }
    }

    @Test
    fun `math engine fraction problems are valid`() {
        val problems = MathEngine.generateProblems(
            topic = MathTopic.FRACTIONS,
            difficulty = Difficulty.MEDIUM,
            worldTheme = WorldTheme.SPACE,
            count = 10
        )
        assertEquals(10, problems.size)
        problems.forEach { problem ->
            assertTrue(problem.options.contains(problem.correctAnswer))
            assertNotNull(problem.promptText)
        }
    }
}
