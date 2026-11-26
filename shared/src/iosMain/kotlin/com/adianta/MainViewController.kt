package com.adianta

import androidx.compose.ui.window.ComposeUIViewController

// entry point consumido pelo Swift como MainViewControllerKt.MainViewController()
@Suppress("ktlint:standard:function-naming", "FunctionNaming")
fun MainViewController() = ComposeUIViewController { App() }
