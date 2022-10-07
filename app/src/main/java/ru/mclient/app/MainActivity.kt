package ru.mclient.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.scope.ScopeActivity
import ru.mclient.startup.setupMainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupMainActivity()
    }

}