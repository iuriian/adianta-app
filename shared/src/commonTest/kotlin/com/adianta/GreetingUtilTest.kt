package com.adianta

import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingUtilTest {
    @Test
    fun givenAName_whenSayHello_thenInterpolatesItIntoTheGreeting() {
        // given
        val name = "Adianta"

        // when
        val result = sayHello(name)

        // then
        assertEquals("Hello, Adianta!", result)
    }

    @Test
    fun givenAnEmptyName_whenSayHello_thenKeepsThePunctuation() {
        // given
        val name = ""

        // when
        val result = sayHello(name)

        // then
        assertEquals("Hello, !", result)
    }
}
