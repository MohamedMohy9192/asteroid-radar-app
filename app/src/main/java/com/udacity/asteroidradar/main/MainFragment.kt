package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.AsteroidApplication
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidListAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidsFilter

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this, AsteroidViewModelFactory(
                (activity?.application as AsteroidApplication).repository
            )
        )[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel

        binding.asteroidRecycler.adapter =
            AsteroidListAdapter(AsteroidListAdapter.OnAsteroidClickListener { asteroid ->
                viewModel.displayAsteroidDetails(asteroid)
            })
        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) { asteroid ->
            if (null != asteroid) {
                findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(asteroid)
                )
                viewModel.displayAsteroidDetailsComplete()
            }
        }

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateAsteroidsFilter(
            when (item.itemId) {
                R.id.show_today_menu -> AsteroidsFilter.SHOW_TODAY_ASTEROIDS
                R.id.show_week_menu -> AsteroidsFilter.SHOW_WEEK_ASTEROIDS
                else -> AsteroidsFilter.SHOW_SAVED_ASTEROIDS
            }
        )
        return true
    }
}
