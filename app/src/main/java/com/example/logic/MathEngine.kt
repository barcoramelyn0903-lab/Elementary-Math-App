package com.example.logic

import com.example.data.model.*
import kotlin.random.Random

object MathEngine {

    fun generateProblems(
        topic: MathTopic,
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        count: Int = 5
    ): List<MathProblem> {
        val problems = mutableListOf<MathProblem>()
        for (i in 1..count) {
            problems.add(generateSingleProblem(topic, difficulty, worldTheme, i))
        }
        return problems
    }

    private fun generateSingleProblem(
        topic: MathTopic,
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        index: Int
    ): MathProblem {
        val itemEmoji = if (worldTheme == WorldTheme.JUNGLE) {
            when (Random.nextInt(4)) {
                0 -> "🍌"
                1 -> "🥥"
                2 -> "🥭"
                else -> "🍍"
            }
        } else {
            when (Random.nextInt(4)) {
                0 -> "⭐"
                1 -> "🚀"
                2 -> "💎"
                else -> "🪐"
            }
        }

        return when (topic) {
            MathTopic.ADDITION -> generateAdditionProblem(difficulty, worldTheme, itemEmoji, index)
            MathTopic.SUBTRACTION -> generateSubtractionProblem(difficulty, worldTheme, itemEmoji, index)
            MathTopic.MULTIPLICATION -> generateMultiplicationProblem(difficulty, worldTheme, itemEmoji, index)
            MathTopic.FRACTIONS -> generateFractionProblem(difficulty, worldTheme, itemEmoji, index)
        }
    }

    private fun generateAdditionProblem(
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        itemEmoji: String,
        index: Int
    ): MathProblem {
        val (maxNum, minNum) = when (difficulty) {
            Difficulty.EASY -> Pair(8, 1)      // sums within 10
            Difficulty.MEDIUM -> Pair(12, 2)   // sums within 20
            Difficulty.HARD -> Pair(30, 8)     // 2-digit sums
        }

        val num1 = Random.nextInt(minNum, maxNum)
        val num2 = Random.nextInt(minNum, maxNum)
        val sum = num1 + num2

        val isMissingAddend = (index % 2 == 1 && difficulty != Difficulty.EASY)
        val problemType = if (difficulty == Difficulty.EASY && index % 2 == 0) {
            ProblemType.VISUAL_COUNTERS
        } else {
            ProblemType.DRAG_DROP_EQUATION
        }

        val promptText: String
        val voiceText: String
        val correctAnswer: String
        val slotPosition: EquationSlotPosition

        if (isMissingAddend) {
            slotPosition = EquationSlotPosition.MIDDLE_OPERAND
            promptText = "$num1 + [ ? ] = $sum"
            voiceText = "$num1 plus what number equals $sum? Pick the right number!"
            correctAnswer = num2.toString()
        } else {
            slotPosition = EquationSlotPosition.RIGHT_RESULT
            promptText = "$num1 + $num2 = [ ? ]"
            voiceText = "What is $num1 plus $num2? Solve the math puzzle!"
            correctAnswer = sum.toString()
        }

        val options = generateDistractors(correctAnswer.toInt(), count = 4, min = 1, max = sum + 8)

        return MathProblem(
            id = "add_${System.currentTimeMillis()}_$index",
            topic = MathTopic.ADDITION,
            difficulty = difficulty,
            problemType = problemType,
            promptText = promptText,
            voiceInstruction = voiceText,
            num1 = num1,
            num2 = num2,
            operatorSymbol = "+",
            resultNumber = sum,
            slotPosition = slotPosition,
            correctAnswer = correctAnswer,
            options = options,
            visualItemEmoji = itemEmoji,
            hintText = "Count both groups together: $num1 and $num2 equal $sum!"
        )
    }

    private fun generateSubtractionProblem(
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        itemEmoji: String,
        index: Int
    ): MathProblem {
        val (maxNum, minNum) = when (difficulty) {
            Difficulty.EASY -> Pair(10, 2)
            Difficulty.MEDIUM -> Pair(20, 5)
            Difficulty.HARD -> Pair(40, 10)
        }

        val total = Random.nextInt(minNum + 2, maxNum + 1)
        val takeAway = Random.nextInt(1, total)
        val remainder = total - takeAway

        val problemType = if (difficulty == Difficulty.EASY) ProblemType.VISUAL_COUNTERS else ProblemType.DRAG_DROP_EQUATION
        val isStoryMode = (index % 3 == 0)

        val promptText = if (isStoryMode) {
            "Start with $total $itemEmoji. Take away $takeAway. Left?"
        } else {
            "$total - $takeAway = [ ? ]"
        }

        val voiceText = if (isStoryMode) {
            "You have $total $itemEmoji. Take away $takeAway $itemEmoji. How many are left?"
        } else {
            "What is $total minus $takeAway? Find the remaining number!"
        }

        val correctAnswer = remainder.toString()
        val options = generateDistractors(remainder, count = 4, min = 0, max = total + 4)

        return MathProblem(
            id = "sub_${System.currentTimeMillis()}_$index",
            topic = MathTopic.SUBTRACTION,
            difficulty = difficulty,
            problemType = problemType,
            promptText = promptText,
            voiceInstruction = voiceText,
            num1 = total,
            num2 = takeAway,
            operatorSymbol = "-",
            resultNumber = remainder,
            slotPosition = EquationSlotPosition.RIGHT_RESULT,
            correctAnswer = correctAnswer,
            options = options,
            visualItemEmoji = itemEmoji,
            hintText = "Start with $total, take away $takeAway, and count the remaining $remainder!"
        )
    }

    private fun generateMultiplicationProblem(
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        itemEmoji: String,
        index: Int
    ): MathProblem {
        val (maxFactor, minFactor) = when (difficulty) {
            Difficulty.EASY -> Pair(5, 2)    // 2s, 3s, 4s, 5s
            Difficulty.MEDIUM -> Pair(7, 2)  // 2s to 7s
            Difficulty.HARD -> Pair(10, 3)   // up to 10x10
        }

        val rows = Random.nextInt(minFactor, maxFactor + 1)
        val cols = Random.nextInt(minFactor, maxFactor + 1)
        val product = rows * cols

        val problemType = if (index % 2 == 1 && rows <= 5 && cols <= 5) {
            ProblemType.MULTIPLICATION_ARRAY
        } else {
            ProblemType.DRAG_DROP_EQUATION
        }

        val promptText = "$rows × $cols = [ ? ]"
        val voiceText = if (problemType == ProblemType.MULTIPLICATION_ARRAY) {
            "Look at $rows rows and $cols columns! How many $itemEmoji in total?"
        } else {
            "What is $rows times $cols? Find the total product!"
        }

        val correctAnswer = product.toString()
        val options = generateDistractors(product, count = 4, min = 2, max = product + 15)

        return MathProblem(
            id = "mult_${System.currentTimeMillis()}_$index",
            topic = MathTopic.MULTIPLICATION,
            difficulty = difficulty,
            problemType = problemType,
            promptText = promptText,
            voiceInstruction = voiceText,
            num1 = rows,
            num2 = cols,
            operatorSymbol = "×",
            resultNumber = product,
            slotPosition = EquationSlotPosition.RIGHT_RESULT,
            correctAnswer = correctAnswer,
            options = options,
            visualItemEmoji = itemEmoji,
            arrayRows = rows,
            arrayCols = cols,
            hintText = "$rows groups of $cols is equal to $product!"
        )
    }

    private fun generateFractionProblem(
        difficulty: Difficulty,
        worldTheme: WorldTheme,
        itemEmoji: String,
        index: Int
    ): MathProblem {
        val fractionList = when (difficulty) {
            Difficulty.EASY -> listOf(
                FractionData(1, 2),
                FractionData(1, 3),
                FractionData(1, 4),
                FractionData(2, 4),
                FractionData(3, 4)
            )
            Difficulty.MEDIUM -> listOf(
                FractionData(1, 2),
                FractionData(2, 3),
                FractionData(3, 4),
                FractionData(2, 5),
                FractionData(3, 5),
                FractionData(4, 6)
            )
            Difficulty.HARD -> listOf(
                FractionData(3, 8),
                FractionData(5, 8),
                FractionData(2, 6),
                FractionData(4, 5),
                FractionData(5, 6),
                FractionData(3, 4)
            )
        }

        val targetFraction = fractionList.random()
        val isCompareMode = (index == 3 && difficulty != Difficulty.EASY)

        if (isCompareMode) {
            val f1 = FractionData(1, 2)
            val promptText = "Which slice is BIGGER: 1/2 or 1/4?"
            val voiceText = "Look closely at the slices. Which fraction is bigger: 1 half or 1 quarter?"
            val correctAnswer = "1/2"
            val options = listOf("1/2", "1/4", "Equal", "1/8")

            return MathProblem(
                id = "frac_comp_${System.currentTimeMillis()}_$index",
                topic = MathTopic.FRACTIONS,
                difficulty = difficulty,
                problemType = ProblemType.COMPARE_FRACTIONS,
                promptText = promptText,
                voiceInstruction = voiceText,
                slotPosition = EquationSlotPosition.NONE,
                correctAnswer = correctAnswer,
                options = options,
                fractionData = f1,
                hintText = "1 half (1/2) is larger than 1 quarter (1/4) because the whole is cut into fewer, bigger pieces!"
            )
        }

        val promptText = "What fraction of the sphere is colored?"
        val voiceText = "What fraction of the pie is shaded? ${targetFraction.numerator} out of ${targetFraction.denominator} parts are shaded!"
        val correctAnswer = targetFraction.toDisplayString()

        val distractors = mutableListOf<String>()
        distractors.add(correctAnswer)
        val alt1 = "${targetFraction.denominator - targetFraction.numerator}/${targetFraction.denominator}"
        val alt2 = "1/${targetFraction.denominator}"
        val alt3 = "${targetFraction.numerator}/${targetFraction.denominator + 1}"
        if (!distractors.contains(alt1) && alt1 != "0/${targetFraction.denominator}") distractors.add(alt1)
        if (!distractors.contains(alt2)) distractors.add(alt2)
        if (!distractors.contains(alt3)) distractors.add(alt3)
        while (distractors.size < 4) {
            val randomD = Random.nextInt(2, 9)
            val randomN = Random.nextInt(1, randomD)
            val cand = "$randomN/$randomD"
            if (!distractors.contains(cand)) distractors.add(cand)
        }

        return MathProblem(
            id = "frac_${System.currentTimeMillis()}_$index",
            topic = MathTopic.FRACTIONS,
            difficulty = difficulty,
            problemType = ProblemType.FRACTION_PIE,
            promptText = promptText,
            voiceInstruction = voiceText,
            slotPosition = EquationSlotPosition.NONE,
            correctAnswer = correctAnswer,
            options = distractors.shuffled(),
            fractionData = targetFraction,
            visualItemEmoji = itemEmoji,
            hintText = "${targetFraction.numerator} shaded parts out of ${targetFraction.denominator} total equal parts = ${targetFraction.toDisplayString()}!"
        )
    }

    private fun generateDistractors(correctVal: Int, count: Int, min: Int, max: Int): List<String> {
        val list = mutableSetOf(correctVal)
        val offsets = listOf(-1, 1, -2, 2, 3, -3, 5, -5, 10, -10).shuffled()
        for (offset in offsets) {
            if (list.size >= count) break
            val candidate = correctVal + offset
            if (candidate >= min && candidate <= max && candidate != correctVal) {
                list.add(candidate)
            }
        }
        while (list.size < count) {
            val rand = Random.nextInt(min, max.coerceAtLeast(min + count + 2))
            list.add(rand)
        }
        return list.map { it.toString() }.shuffled()
    }
}
