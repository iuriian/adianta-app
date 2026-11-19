package com.adianta

import kotlin.test.Test
import kotlin.test.assertTrue

class SharedCommonTest {
    @Test
    fun givenAnyTarget_whenGetPlatform_thenExposesANonBlankName() {
        // given: o contrato do expect/actual, que todo target precisa honrar

        // when
        val platform = getPlatform()

        // then
        assertTrue(platform.name.isNotBlank(), "nome da plataforma: '${platform.name}'")
    }
}
