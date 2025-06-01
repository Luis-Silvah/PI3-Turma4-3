package com.team43.superidpi3.utils

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SenhaViewModel: ViewModel() {
    var senha by mutableStateOf("")
        private set

    val senhaHasErrors by derivedStateOf {
        if (senha.isNotEmpty() && senha.length < 6) return@derivedStateOf true
            else return@derivedStateOf false
    }

    fun updateSenha(input: String) {
        senha = input
    }
}