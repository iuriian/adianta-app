package com.adianta

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedLogicAndroidHostTest {
    @Test
    fun givenTheAndroidTarget_whenGetPlatform_thenNameIdentifiesAndroid() {
        // given: o actual de androidMain, que monta o nome a partir do Build

        // when
        val platform = getPlatform()

        // then
        assertTrue(platform.name.startsWith("Android"), "nome da plataforma: '${platform.name}'")
    }
}
