package com.udacity.asteroidradar.detail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentDetailBinding
import com.udacity.asteroidradar.main.MainViewModel

class DetailFragment : Fragment() {
    //private lateinit var viewModel: DetailViewModel
    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val asteroid = DetailFragmentArgs.fromBundle(requireArguments()).selectedAsteroid

        binding.asteroid = asteroid
        viewModel.displayExplanationDialog.observe(viewLifecycleOwner) {
            if (it) {
                displayAstronomicalUnitExplanationDialog()
                viewModel.onDisplayExplanationDialogDone()
            }
        }
        return binding.root
    }

    private fun displayAstronomicalUnitExplanationDialog() {
        val builder = AlertDialog.Builder(requireActivity())
            .setMessage(getString(R.string.astronomica_unit_explanation))
            .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }
}
