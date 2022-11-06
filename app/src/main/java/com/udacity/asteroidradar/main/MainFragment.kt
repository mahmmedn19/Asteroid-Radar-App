package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = MainAsteroidAdapter(MainAsteroidAdapter.OnClickListener {
            viewModel.onAsteroidClicked(it)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroid.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.submitList(it)
            }
        }
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) {
            if (it != null) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.doneNavigating()
            }
        }


        //setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.main_overflow_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.view_week_asteroids_menu -> viewModel.onViewWeekAsteroidsClicked()
                    R.id.view_today_asteroids_menu -> viewModel.onTodayAsteroidsClicked()
                    R.id.view_saved_asteroids_menu -> viewModel.onSavedAsteroidsClicked()
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}
