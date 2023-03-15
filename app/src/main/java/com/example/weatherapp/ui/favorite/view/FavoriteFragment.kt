package com.example.weatherapp.ui.favorite.view


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.data.repo.Repository
import com.example.weatherapp.databinding.FavoriteFragmentBinding
import com.example.weatherapp.ui.favorite.viewModel.ApiState
import com.example.weatherapp.ui.favorite.viewModel.FavoriteFactoryViewModel
import com.example.weatherapp.ui.favorite.viewModel.FavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest


class FavoriteFragment : Fragment() {

    private var _binding: FavoriteFragmentBinding? = null

    private val binding get() = _binding!!
    lateinit var factoryViewModel: FavoriteFactoryViewModel


    private lateinit var adapter: FavoriteAdapter


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = Repository.getRepository(requireActivity().application)
        factoryViewModel = FavoriteFactoryViewModel(repository)
        val favoriteViewModel =
            ViewModelProvider(this, factoryViewModel).get(FavoriteViewModel::class.java)

        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.fabFav.setOnClickListener { view ->
            if (Utility.checkForInternet(requireContext())) {

                Navigation.findNavController(view).navigate(R.id.Nav_map)

            } else {
                Snackbar.make(
                    requireView(),
                    getString(R.string.no_internet_txt),
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Setting", View.OnClickListener {
                        startActivityForResult(
                            Intent(
                                Settings.ACTION_SETTINGS
                            ), 0
                        );
                    }).show()
            }
        }
        lifecycleScope.launchWhenStarted {
            favoriteViewModel.favList.collectLatest {
                when (it) {
                    is ApiState.loading -> {

                    }
                    is ApiState.Success -> {

                        adapter =
                            FavoriteAdapter(it.data, { it ->


                                val alert: AlertDialog.Builder =
                                    AlertDialog.Builder(requireActivity(), R.style.MyDialogTheme)

                                alert.setTitle(getString(R.string.Warning))
                                alert.setMessage(getString(R.string.Do_You))
                                alert.setIcon(R.drawable.img_22)
                                alert.setPositiveButton(getString(R.string.Delete)) { _: DialogInterface, _: Int ->
                                    favoriteViewModel.deleteFavoriteWeather(it)
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.Item),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }

                                val dialog = alert.create()
                                dialog.show()

                            })

                            { it ->
                                favoriteViewModel.getAllFavoritePlacesDetails(it)
                                val bundle = Bundle()
                                bundle.putSerializable("favorite", it)
                                Navigation.findNavController(root!!)
                                    .navigate(
                                        R.id.action_nav_favorite_to_favoritePlacesDetailsFragment,
                                        bundle
                                    )

                            }

                        binding.FavoriteRectcleView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_LONG).show()
                    }

                }


            }
        }

        return root
    }
}