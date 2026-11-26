# CI no GitHub Actions

Workflow: [`.github/workflows/ci.yml`](../.github/workflows/ci.yml)

## Gatilhos

| Evento | Quando |
| --- | --- |
| `pull_request` | PR com destino `main` ou `develop` |
| `workflow_dispatch` | Manual: *Actions › CI › Run workflow*, ou `gh workflow run ci.yml --ref <branch>` |

`push` não dispara em branch nenhum — o CI roda no PR. Commit novo no PR reexecuta e cancela o run
anterior; run manual não é cancelado.

**PR em draft não roda o CI.** Os jobs aparecem como *Skipped*. Clicar em **Ready for review**
dispara o run completo — não é preciso um commit novo.

## Configuração do repositório

*Settings › Actions › General*:

- **Actions permissions:** `Allow all actions and reusable workflows`
- **Workflow permissions:** `Read and write permissions` — sem isso os comentários no PR falham

Sem secrets a cadastrar. Jobs de iOS usam runner `macos-latest` (cota 10x em repositório privado).

## Fluxo

```
git switch -c feature/ADK-00X develop
git push -u origin feature/ADK-00X
# abrir PR para develop -> CI dispara
# merge quando todos os jobs passarem
```

## Jobs

```
Formatacao ─┐
            ├─> Testes unitarios ──────┐
Analise     ├─> Testes de componente ──┼─> Cobertura minima ─┬─> Build Android
estatica   ─┘                          │                     └─> Build iOS
            └─> Testes iOS ────────────────────────────────────────┘
```

| Job | Comando |
| --- | --- |
| Formatacao | `./gradlew spotlessCheck` |
| Analise estatica | `./gradlew detektAll` |
| Testes unitarios | `./gradlew :shared:testAndroidHostTest :androidApp:testDebugUnitTest -PtestKind=unit` |
| Testes de componente | `./gradlew :shared:testAndroidHostTest -PtestKind=component` |
| Testes iOS (simulador) | `./gradlew :shared:iosSimulatorArm64Test -PtestKind=unit` |
| Cobertura minima | `./gradlew :koverXmlReport :koverVerify` |
| Build Android | `./gradlew :androidApp:assembleDebug` |
| Build iOS | `./gradlew :shared:linkReleaseFrameworkIosArm64` |

`-PtestKind=unit` exclui `*ComponentTest`; `-PtestKind=component` roda só eles.
Gate de cobertura: 50% de linhas no total.

## Saídas

Comentários no PR (Spotless, Detekt, cobertura), anotações no diff para testes que falharam
e artefatos de 7 dias: relatórios do Detekt, cobertura HTML, APK debug, framework iOS.

Em execução manual não há PR para comentar — o relatório fica no resumo do job.
Os gates continuam valendo.

## Antes de abrir o PR

```sh
./gradlew spotlessApply detektAll
./gradlew :shared:testAndroidHostTest :androidApp:testDebugUnitTest -PtestKind=unit
./gradlew :shared:testAndroidHostTest -PtestKind=component
./gradlew :koverXmlReport :koverVerify
```

## Problemas comuns

| Sintoma | Causa |
| --- | --- |
| CI não dispara no PR | Destino não é `main` nem `develop` |
| Todos os jobs como *Skipped* | PR em draft — clicar em `Ready for review` |
| `Resource not accessible by integration` | Workflow permissions somente leitura |
| Sem comentários em PR de fork | Esperado; relatório fica no resumo do job |
| Detekt reprova sem achados | Erro de compilação ou de configuração — ver logs |
| Jobs iOS na fila | Cota de runner `macos-latest` |
| `testKind invalido` | Valor diferente de `unit` ou `component` |
