package com.bonial.codechallenge.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//todo import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
//todo import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.bonial.codechallenge.MainApplication
import com.bonial.codechallenge.ui.home.BrochureScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        //todo: val appContainer = (application as MainApplication).container
        setContent {
            //todo: JetnewsApp(appContainer)
            BrochureScreen()
        }
    }
}
