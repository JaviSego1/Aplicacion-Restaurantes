package com.example.aplicacionrestaurantes.ui.views.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aplicacionrestaurantes.R
import com.example.aplicacionrestaurantes.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        // Verificar si el usuario está logueado
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (!isLoggedIn) {
            // Redirigir al LoginActivity y limpiar la pila de actividades
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Configurar la toolbar
        val toolbar = binding.appBarLayoutDrawer.toolbar
        setSupportActionBar(toolbar)

        // Configuración de Navigation
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHost.navController

        val navView = binding.myNavView
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.restaurantesFragment, R.id.fragmentConfiguracion, R.id.fragmentFiltro), // Destinos principales
            binding.drawerLayout // DrawerLayout
        )

        // Vincular la toolbar con el navController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Configurar el NavigationView con el navController
        navView.setupWithNavController(navController)

        // Configurar el listener para los ítems del menú en el NavigationView
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.fragmentCerrarSesion -> {
                    // Cerrar sesión con Firebase
                    firebaseAuth.signOut()

                    // Actualizar SharedPreferences
                    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

                    // Redirigir al LoginActivity y limpiar la pila de actividades
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    // Cerrar la actividad actual (opcional, dependiendo de tu flujo)
                    finish()

                    // Cerrar el Drawer
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.restaurantesFragment -> {
                    // Navegar al fragment Home
                    navController.navigate(R.id.restaurantesFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.fragmentConfiguracion -> {
                    // Navegar al fragment Configuración
                    navController.navigate(R.id.fragmentConfiguracion)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.fragmentFiltro -> {
                    // Navegar al fragment Filtro
                    navController.navigate(R.id.fragmentFiltro)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
    }

    // Este método permite que funcione correctamente el botón de navegación hacia arriba (hamburguesa/retroceso)
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_op, menu)
        return true
    }

    // Navegación desde el menú de opciones
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.restaurantesFragment -> {
                navController.navigate(R.id.restaurantesFragment)
                true
            }
            R.id.fragmentConfiguracion -> {
                navController.navigate(R.id.fragmentConfiguracion)
                true
            }
            R.id.fragmentFiltro -> {
                navController.navigate(R.id.fragmentFiltro)
                true
            }
            R.id.fragmentCerrarSesion -> {
                // Cerrar sesión con Firebase
                firebaseAuth.signOut()
                val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
