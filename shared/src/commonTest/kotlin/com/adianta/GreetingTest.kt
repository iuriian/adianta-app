package com.adianta

import kotlin.test.Test
import kotlin.test.assertEquals

class GreetingTest {
    @Test
    fun givenTheCurrentPlatform_whenGreet_thenGreetsWithThePlatformName() {
        // given: o nome vem do actual de cada target, entao o esperado e derivado
        // dele em vez de fixado, para o cenario valer em Android e em iOS
        val platform = getPlatform().name
        val greeting = Greeting()

        // when
        val result = greeting.greet()

        // then
        assertEquals("Hello, $platform!", result)
    }

    @Test
    fun givenTheSameGreeting_whenGreetTwice_thenTheResultIsStable() {
        // given
        val greeting = Greeting()

        // when
        val first = greeting.greet()
        val second = greeting.greet()

        // then
        assertEquals(first, second)
    }
}
