package com.adianta

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform