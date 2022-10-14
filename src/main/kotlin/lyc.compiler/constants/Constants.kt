package lyc.compiler.constants

import kotlin.math.pow

object Constants {
    // Messages
    const val NO_FILENAME_PROVIDED_MESSAGE_ERROR = "Filename must be provided as argument."
    const val CANNOT_READ_INPUT_FILE_MESSAGE_ERROR = "There was an error trying to read input file"
    const val COMPILATION_ERROR_MESSAGE = "Compilation error:"
    const val COMPILATION_SUCCESSFUL_MESSAGE = "Compilation Successful"

    // File names
    const val SYMBOL_TABLE_FILENAME = "symbol-table.txt"
    const val INTERMEDIATE_CODE_FILENAME = "intermediate-code.txt"
    const val ASSEMBLER_FILENAME = "final.asm"

    // Values

    // Ints of 16 bits -> From −32768 to 32767
    const val INT_RANGE_NEG = -32768
    const val INT_RANGE_POS = 32767

    // Floats of 32 bits -> From -2147483648 to 2147483647
    const val FLOAT_RANGE_NEG = -2147483648
    const val FLOAT_RANGE_POS = 2147483647

    const val STRING_RANGE = 40
    const val ID_MAX_LENGTH = 30
    const val EMPTY_STRING = ""
}