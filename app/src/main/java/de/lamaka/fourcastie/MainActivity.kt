package de.lamaka.fourcastie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentFactory
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bottomNavBar: BottomNavigationView = findViewById(R.id.bottomNavBar)
        findNavController(R.id.mainNavFragment)
            .also {
                setupActionBarWithNavController(it)
                bottomNavBar.setupWithNavController(it)
            }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavFragment).navigateUp()
}