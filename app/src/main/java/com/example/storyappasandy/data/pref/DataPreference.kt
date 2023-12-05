package com.example.storyappasandy.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name ="session")

class DataPreference private constructor(private val dataStore: DataStore<Preferences>){

    companion object {
        @Volatile
        private var INSTANCE: DataPreference? = null

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): DataPreference{
            return INSTANCE ?: synchronized(this){
                val instance = DataPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }


    suspend fun simpanSession(user: DataModel){
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<DataModel> {
        return dataStore.data.map { preferences ->
            DataModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false,
            )
        }

    }


    suspend fun logout(){
        dataStore.edit { prefences ->
            prefences.clear()
        }
    }


}