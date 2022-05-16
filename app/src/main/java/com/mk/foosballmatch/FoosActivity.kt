package com.mk.foosballmatch

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.mk.base.SharedNotificationViewModel
import com.mk.competitors.CreateCompetitorViewModel
import com.mk.foosballmatch.databinding.ActivityFoosActiviityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoosActiviityBinding
    private val viewModel: SharedNotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFoosActiviityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_foos_activiity)
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_stats, com.mk.match.R.id.nav_matches, com.mk.competitors.R.id.nav_competitors
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.error.observe(this) {
            val sb = Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                sb.setBackgroundTint(Color.RED)
                .show()
        }

        viewModel.notification.observe(this) { text ->
            Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
        }
    }
}