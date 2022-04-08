@file:Suppress("DEPRECATION")

package com.example.spotifyclone

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.spotifyclone.database.Token
import com.example.spotifyclone.database.TokenRepository
import com.example.spotifyclone.database.TonezoneDB
import com.example.spotifyclone.player.PlayerScreenViewModel
import com.example.tonezone.R
import com.example.tonezone.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import com.spotify.sdk.android.auth.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

const val REDIRECT_URI = "com.tonezone://callback"


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory(this)
    }

    private val playerViewModel: PlayerScreenViewModel by viewModels()

    private lateinit var repository: TokenRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        repository = TokenRepository(TonezoneDB.getInstance(application).tokenDao)
        setupNav()
        setupMiniPlayer()

    }

    private fun setupMiniPlayer(){
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.isHideable = false

        binding.miniPlayer.miniPlayerFrame.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.miniPlayer.playerViewModel = playerViewModel
    }


    private fun setupNav(){
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomBar.setupWithNavController(navController)
        binding.bottomBar.selectedItemId = R.id.homeFragment

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.loginFragment -> {
                    binding.bottomBar.visibility = View.GONE
                }
                else  -> {
                    binding.bottomBar.visibility = View.VISIBLE
                }
            }
        }

    }

    override fun onBackPressed() {
        val currentDestination= navController.currentDestination

        if (currentDestination != null) {
            when(currentDestination.id) {
                R.id.loginFragment -> {
                    finish()
                }
            }
        }
        super.onBackPressed()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // Check if result comes from the correct activity
        if (requestCode == LoginActivity.REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, intent)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    mainViewModel.token = response.accessToken
                    mainViewModel.getCurrentUserProfileData()

                    runBlocking(Dispatchers.IO) {
                        repository.clear()
                    }

                    runBlocking(Dispatchers.IO) {
                        repository.insert(Token(response.accessToken))
                    }

                    navigateToHome()

                }
                AuthorizationResponse.Type.ERROR -> {
                    mainViewModel.token =  "Not Found"

                }
                else -> {
                    mainViewModel.token =  "Not Found"
                }
            }


        }
    }

    private fun navigateToHome(){
        mainViewModel.user.observe(this) {
            if(it!=null) {
                navController.popBackStack()
                navController.navigate(R.id.home)
            }
        }
    }
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.nav_host)
//        return navController.navigateUp()
//    }

    override fun onDestroy() {
        super.onDestroy()
        runBlocking(Dispatchers.IO) {
            repository.clear()
        }

    }
}