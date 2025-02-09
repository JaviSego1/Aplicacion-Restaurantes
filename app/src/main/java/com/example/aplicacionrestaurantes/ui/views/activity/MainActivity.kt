package com.example.aplicacionrestaurantes.ui.views.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.data.models.Restaurante
import com.example.aplicacionrestaurantes.databinding.ActivityMainBinding
import com.example.aplicacionrestaurantes.ui.adapter.RestauranteAdapter
import com.example.aplicacionrestaurantes.ui.viewmodel.RestaurantViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var restauranteAdapter: RestauranteAdapter

    // Inicialización del ViewModel
    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Verificar si el usuario está logueado
        checkLoginStatus()

        // Configurar la toolbar
        setupToolbar()

        // Configuración de Navigation
        setupNavigation()

        // Observar cambios en el ViewModel
        observeViewModel()
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            redirectToLogin()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupToolbar() {
        val toolbar = binding.appBarLayoutDrawer.toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        val navView = binding.myNavView
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.restaurantesFragment),
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragmentCerrarSesion -> {
                    logout()
                    true
                }
                R.id.restaurantesFragment -> {
                    navigateToFragment(R.id.restaurantesFragment)
                    true
                }
                R.id.fragmentConfiguracion -> {
                    navigateToFragment(R.id.fragmentConfiguracion)
                    true
                }
                R.id.fragmentFiltro -> {
                    navigateToFragment(R.id.fragmentFiltro)
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        // Observar la lista de restaurantes
        restaurantViewModel.restaurantLiveData.observe(this) { restaurants ->
            // Convertir List<Restaurant> a List<Restaurante>
            val restaurantes = restaurants.map { restaurant ->
                Restaurante(
                    titulo = restaurant.titulo,
                    descripcion = restaurant.descripcion,
                    imagen = restaurant.imagen
                )
            }
            restauranteAdapter.submitList(restaurantes)
        }

        // Observar el estado del ProgressBar
        restaurantViewModel.progressBarLiveData.observe(this) { visible ->
            progressBar.visibility = if (visible) View.VISIBLE else View.GONE
        }

        // Observar los errores
        restaurantViewModel.errorLiveData.observe(this) { error ->
            // Mostrar el error con un Toast
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }



    private fun logout() {
        firebaseAuth.signOut()
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
        redirectToLogin()
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    private fun navigateToFragment(fragmentId: Int) {
        navController.navigate(fragmentId)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_op, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.restaurantesFragment -> {
                navigateToFragment(R.id.restaurantesFragment)
                true
            }
            R.id.fragmentConfiguracion -> {
                navigateToFragment(R.id.fragmentConfiguracion)
                true
            }
            R.id.fragmentFiltro -> {
                navigateToFragment(R.id.fragmentFiltro)
                true
            }
            R.id.fragmentCerrarSesion -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}