package com.mk.match.di

import com.mk.match.AddMatchViewModel
import dagger.assisted.AssistedFactory

@AssistedFactory
interface AddMatchViewModelFactory {
    fun create(matchId: Int): AddMatchViewModel
}
