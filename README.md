Este é um projeto Kotlin Multiplatform com alvos Android e iOS.

* [/iosApp](./iosApp/iosApp) contém um aplicativo iOS. Mesmo que você compartilhe sua UI com o Compose Multiplatform,
  você precisa desse ponto de entrada para o seu app iOS. É também aqui que você deve adicionar código SwiftUI do seu projeto.

* [/shared](./shared/src) é para o código que será compartilhado entre os seus aplicativos Compose Multiplatform.
  Ele contém várias subpastas:
  - [commonMain](./shared/src/commonMain/kotlin) é para o código que é comum a todos os alvos.
  - As outras pastas são para código Kotlin que será compilado apenas para a plataforma indicada no nome da pasta.
    Por exemplo, se você quiser usar o CoreCrypto da Apple na parte iOS do seu app Kotlin,
    a pasta [iosMain](./shared/src/iosMain/kotlin) seria o lugar certo para essas chamadas.
    Da mesma forma, se você quiser editar a parte específica de Desktop (JVM), a pasta
    [jvmMain](./shared/src/jvmMain/kotlin) é o local apropriado.

### Executando os aplicativos

Use as configurações de execução disponíveis no widget de execução da barra de ferramentas da sua IDE. Você também pode usar estes comandos e opções:

- App Android: `./gradlew :androidApp:assembleDebug`
- App iOS: abra o diretório [/iosApp](./iosApp) no Xcode e execute a partir de lá.

### Executando os testes

Use o botão de execução na margem do editor da sua IDE, ou execute os testes usando as tarefas do Gradle:

- Testes Android: `./gradlew :shared:testAndroidHostTest`
- Testes iOS: `./gradlew :shared:iosSimulatorArm64Test`

---

Saiba mais sobre [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…