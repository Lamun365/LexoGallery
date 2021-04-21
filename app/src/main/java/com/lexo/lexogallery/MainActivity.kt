package com.lexo.lexogallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lexo.lexogallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
//        navController = navHostFragment.findNavController()
//        binding.bottomNav.setupWithNavController(navController)
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }



//    fun BottomNavigationView.testListener(uri: String, displayName: String, size: String) {
//        this.setOnNavigationItemReselectedListener {
//            if (it.itemId == R.id.detailsFrag) {
//                val action = NavGraphDirections.actionGlobalDetailsFrag(uri, displayName, size)
//                navController.navigate(action)
//            }
//        }
//    }



}