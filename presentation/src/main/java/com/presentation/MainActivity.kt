package com.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.presentation.databinding.ActivityMainBinding
import com.presentation.list.ListContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val listContactsViewModel: ListContactsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.appbar.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val navController = (binding.navHostFragment.getFragment<NavHostFragment>())
            .findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.appbar.toolbar.setupWithNavController(navController, appBarConfiguration)
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchItem: MenuItem = menu!!.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        restoreSearchQueryOnConfigurationChange(searchItem, searchView)

        searchView.queryHint = getString(R.string.search_hint)
        searchView.setBackgroundColor(Color.WHITE)
        searchView.background =
            ResourcesCompat.getDrawable(resources, R.drawable.bg_white_rounded, theme)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.infoContactFragment) {
                searchItem.collapseActionView()
                searchView.onActionViewCollapsed()
                binding.appbar.toolbar.menu.findItem(R.id.search).isVisible = false
                this.title = destination.label
            }
            if (destination.id == R.id.listContactsFragment) {
                binding.appbar.toolbar.menu.findItem(R.id.search).isVisible = true
            }
        }
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                lifecycleScope.launch(Dispatchers.IO) {
                    query?.let { listContactsViewModel.search(it) }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                lifecycleScope.launch(Dispatchers.IO) {
                    delay(300)
                    newText?.let { listContactsViewModel.search(it) }
                }
                return true
            }
        })
        return true
    }

    private fun restoreSearchQueryOnConfigurationChange(
        searchItem: MenuItem,
        searchView: SearchView
    ) {
        if (listContactsViewModel.getSearchQuery().isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(listContactsViewModel.getSearchQuery(), true)
        }
    }

}