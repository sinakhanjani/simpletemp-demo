package com.simpletempco.simpletemp

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.simpletempco.simpletemp.data.AppRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var repo: AppRepository


    override fun onCreate() {
        super.onCreate()

        if (repo.isLogin()) {
            initFcmToken()
        }
    }

    fun initFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            CoroutineScope(IO).launch {
                updateFcmToken(token)
            }
        })
    }

    private suspend fun updateFcmToken(token: String) {
        repo.updateFcmToken(token)
    }

}