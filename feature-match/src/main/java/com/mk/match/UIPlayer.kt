package com.mk.match

sealed class UIPlayer {
    object NoPlayer : UIPlayer() {
        override fun toString(): String {
            return "Select player"
        }
    }
    data class SelectedPlayer(val id: Int, val name: String) : UIPlayer() {

        override fun toString(): String {
            return name
        }

    }
}