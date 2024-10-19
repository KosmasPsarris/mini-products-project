package com.kosmas.tecprin

import com.kosmas.tecprin.utils.reformatDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DateUtilsTest {

    @Test
    fun reformatDate_validInput_returnsFormattedDate() {
        val inputDate = "2023-12-20T10:30:00.000Z"
        val expectedOutput = "20/12/2023"

        val actualOutput = inputDate.reformatDate()

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun reformatDate_invalidInput_returnsNull() {
        val inputDate = "invalid-date-format"
        val expectedOutput = null

        val actualOutput = inputDate.reformatDate()

        assertEquals(expectedOutput, actualOutput)
    }

    @Test
    fun reformatDate_nullInput_returnsNull() {
        val inputDate = null
        val expectedOutput = null

        val actualOutput = inputDate?.reformatDate()

        assertEquals(expectedOutput, actualOutput)
    }
}
