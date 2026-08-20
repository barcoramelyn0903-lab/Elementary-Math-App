package com.example.data.model

enum class Difficulty(val label: String, val recommendedAge: String, val starReward: Int) {
    EASY(label = "Level 1: Explorer", recommendedAge = "Age 6", starReward = 1),
    MEDIUM(label = "Level 2: Adventurer", recommendedAge = "Age 7", starReward = 2),
    HARD(label = "Level 3: Master", recommendedAge = "Age 8-9", starReward = 3)
}

enum class ProblemType {
    DRAG_DROP_EQUATION,     // Missing answer at end or middle
    VISUAL_COUNTERS,        // Count jungle items / crystals
    MULTIPLICATION_ARRAY,   // Visual rows x cols array
    FRACTION_PIE,           // Visual fraction pie
    COMPARE_FRACTIONS       // Which is bigger
}

enum class EquationSlotPosition {
    RIGHT_RESULT,   // num1 operator num2 = [ ? ]
    MIDDLE_OPERAND, // num1 operator [ ? ] = result
    NONE            // Prompt text only + [ ? ]
}

data class FractionData(
    val numerator: Int,
    val denominator: Int,
    val visualName: String = "slices"
) {
    fun toDisplayString(): String = "$numerator/$denominator"
}

data class MathProblem(
    val id: String,
    val topic: MathTopic,
    val difficulty: Difficulty,
    val problemType: ProblemType,
    val promptText: String,
    val voiceInstruction: String,
    val num1: Int = 0,
    val num2: Int = 0,
    val operatorSymbol: String = "+",
    val resultNumber: Int = 0,
    val slotPosition: EquationSlotPosition = EquationSlotPosition.RIGHT_RESULT,
    val correctAnswer: String,
    val options: List<String>,
    val visualItemEmoji: String = "🍌",
    val fractionData: FractionData? = null,
    val arrayRows: Int = 0,
    val arrayCols: Int = 0,
    val hintText: String = ""
)
