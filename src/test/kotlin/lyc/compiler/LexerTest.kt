package lyc.compiler

import com.google.common.truth.Truth.assertThat
import lyc.compiler.constants.Constants.EMPTY_STRING
import lyc.compiler.constants.Constants.FLOAT_RANGE_NEG
import lyc.compiler.constants.Constants.FLOAT_RANGE_POS
import lyc.compiler.constants.Constants.ID_MAX_LENGTH
import lyc.compiler.constants.Constants.INT_RANGE_NEG
import lyc.compiler.constants.Constants.INT_RANGE_POS
import lyc.compiler.constants.Constants.STRING_RANGE
import lyc.compiler.factories.LexerFactory
import lyc.compiler.model.*
import org.apache.commons.text.CharacterPredicates
import org.apache.commons.text.RandomStringGenerator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import java.io.IOException

class LexerTest {
    private var lexer: Lexer? = null

    @Test
    @Throws(Exception::class)
    fun comment() {
        scan("/* This is a comment */")
        assertThat(nextToken()).isEqualTo(ParserSym.EOF)
    }

    @Test
    fun invalidStringConstantLength() {
        assertThrows(InvalidLengthException::class.java) {
            scan(String.format("\"%s\"", getRandomString()))
            nextToken()
        }
    }

    @Test
    fun invalidStringConstantLengthUpperLimit() {
        assertThrows(InvalidLengthException::class.java) {
            scan(String.format("\"%s\"", getRandomString(STRING_RANGE + 1)))
            nextToken()
        }
    }

    @Test
    fun validStringConstantLengthLowerLimit() {
        assertDoesNotThrow {
            scan(String.format("\"%s\"", getRandomString(STRING_RANGE - 1)))
            nextToken()
        }
    }

    @Test
    fun validStringConstantLengthZero() {
        assertDoesNotThrow {
            scan(String.format("\"%s\"", EMPTY_STRING))
            nextToken()
        }
    }

    @Test
    fun invalidIdLength() {
        assertThrows(InvalidLengthException::class.java) {
            scan(getRandomString())
            nextToken()
        }
    }

    @Test
    fun invalidPositiveIntegerConstantValue() {
        assertThrows(InvalidIntegerException::class.java) {
            scan(String.format("%d", 9223372036854775807L))
            nextToken()
        }
    }

    @Test
    fun invalidNegativeIntegerConstantValue() {
        assertThrows(InvalidIntegerException::class.java) {
            scan(String.format("%d", -9223372036854775807L))
            nextToken()
        }
    }

    @Test
    fun validNegativeIntegerConstantMinValue() {
        assertDoesNotThrow {
            scan(String.format("%d", INT_RANGE_NEG))
            nextToken()
        }
    }

    //TODO : Review this
    @Test
    @Throws(Exception::class)
    fun random_test() {
        scan("/* ASDASD ASDASD AS */")
        assertThat(nextToken()).isEqualTo(ParserSym.EOF)
    }

    @Test
    fun validNegativeIntegerConstantMinValuePlusOne() {
        assertDoesNotThrow {
            scan(String.format("%d", INT_RANGE_NEG + 1))
            nextToken()
        }
    }

    @Test
    fun validNegativeIntegerConstantValueZero() {
        assertDoesNotThrow {
            scan(String.format("%d", 0))
            nextToken()
        }
    }

    @Test
    fun validPositiveIntegerConstantMaxValueMinusOne() {
        assertDoesNotThrow {
            scan(String.format("%d", INT_RANGE_POS - 1))
            nextToken()
        }
    }

    @Test
    fun validPositiveIntegerConstantMaxValue() {
        assertDoesNotThrow {
            scan(String.format("%d", INT_RANGE_POS))
            nextToken()
        }
    }

    @Test
    fun invalidPositiveFloatConstantValue() {
        assertThrows(InvalidFloatException::class.java) {
            scan(String.format("%f", 9.223372036854776E18))
            nextToken()
        }
    }

    @Test
    fun invalidNegativeFloatConstantValue() {
        assertThrows(InvalidFloatException::class.java) {
            scan(String.format("%f", -9.223372036854776E18))
            nextToken()
        }
    }

    @Test
    fun validNegativeFloatConstantMinValue() {
        assertDoesNotThrow {
            scan(String.format("%f", FLOAT_RANGE_NEG + 0.0))
            nextToken()
        }
    }

    @Test
    fun validNegativeFloatConstantMinValuePlusOne() {
        assertDoesNotThrow {
            scan(String.format("%f", FLOAT_RANGE_NEG + 1.0))
            nextToken()
        }
    }

    @Test
    fun validNegativeFloatConstantValueZero() {
        assertDoesNotThrow {
            scan(String.format("%f", 0.0))
            nextToken()
        }
    }

    @Test
    fun validPositiveFloatConstantMaxValue() {
        assertDoesNotThrow {
            scan(String.format("%f", FLOAT_RANGE_POS + 0.0))
            nextToken()
        }
    }

    @Test
    fun validPositiveFloatConstantMaxValueMinusOne() {
        assertDoesNotThrow {
            scan(String.format("%f", FLOAT_RANGE_POS - 1.0))
            nextToken()
        }
    }

    @Test
    fun validPositiveFloatLeftDot() {
        assertDoesNotThrow {
            scan(String.format("%f", .075))
            nextToken()
        }
    }

    @Test
    fun validPositiveFloatRightDdt() {
        assertDoesNotThrow {
            scan(String.format("%f", 90.0))
            nextToken()
        }
    }

    @Test
    fun validNegativeFloatLeftDot() {
        assertDoesNotThrow {
            scan(String.format("%f", -.075))
            nextToken()
        }
    }

    @Test
    fun validNegativeFloatRightDdt() {
        assertDoesNotThrow {
            scan(String.format("%f", -90.0))
            nextToken()
        }
    }

    @Test
    @Throws(Exception::class)
    fun assignmentWithExpressions() {
        scan("c := d * ( e - 21 ) / 4")
        assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER)
        assertThat(nextToken()).isEqualTo(ParserSym.ASSIG)
        assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER)
        assertThat(nextToken()).isEqualTo(ParserSym.MULT)
        assertThat(nextToken()).isEqualTo(ParserSym.OPEN_BRACKET)
        assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER)
        assertThat(nextToken()).isEqualTo(ParserSym.SUB)
        assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT)
        assertThat(nextToken()).isEqualTo(ParserSym.CLOSE_BRACKET)
        assertThat(nextToken()).isEqualTo(ParserSym.DIV)
        assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT)
        assertThat(nextToken()).isEqualTo(ParserSym.EOF)
    }

    @Test
    fun unknownCharacter() {
        assertThrows(
            UnknownCharacterException::class.java
        ) {
            scan("#")
            nextToken()
        }
    }

    @AfterEach
    fun resetLexer() {
        lexer = null
    }

    private fun scan(input: String) {
        lexer = LexerFactory.create(input)
    }

    @Throws(IOException::class, CompilerException::class)
    private fun nextToken(): Int {
        return lexer!!.next_token().sym
    }

    private fun getRandomString(size: Int = ID_MAX_LENGTH * 2): String {
        return RandomStringGenerator.Builder().filteredBy(CharacterPredicates.LETTERS).withinRange('a'.code, 'z'.code)
            .build().generate(size)
    }
}