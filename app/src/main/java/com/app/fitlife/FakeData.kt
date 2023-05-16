package com.app.fitlife

class FakeData {
    fun getData (): Float {
        val peso = 90f
        val altura = 1.60f
        return (peso/ (altura * altura))
    }
}