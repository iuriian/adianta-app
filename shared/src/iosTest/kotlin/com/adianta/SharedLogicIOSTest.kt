package com.adianta

import kotlin.test.Test
import kotlin.test.assertTrue
import platform.UIKit.UIDevice

class SharedLogicIOSTest {
    @Test
    fun givenTheIosTarget_whenGetPlatform_thenNameCarriesTheSystemVersion() {
        // given: o actual de iosMain compoe o nome com systemName + systemVersion.
        // A assercao usa a versao, e nao o systemName, porque o UIKit ja devolveu
        // tanto "iOS" quanto "iPhone OS" dependendo da versao do simulador
        val systemVersion = UIDevice.currentDevice.systemVersion

        // when
        val platform = getPlatform()

        // then
        assertTrue(platform.name.endsWith(systemVersion), "nome da plataforma: '${platform.name}'")
    }
}
