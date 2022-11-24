package com.example.drawer.fragments

import android.content.Context
import android.content.SharedPreferences.Editor
import android.os.*
import android.view.*
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.drawer.R
import com.example.drawer.adapter.HomeAdapter
import com.example.drawer.adapter.ItemListener
import com.example.drawer.databinding.FragmentOverViewBinding
import com.example.drawer.databinding.ShrBackdropBinding
import com.example.drawer.entity.Pixabay
import com.example.drawer.model.HomeViewModel
import com.example.drawer.util.Vibrate
import com.google.android.material.transition.MaterialElevationScale
import com.example.drawer.adapter.NavigationIconClickListener
import kotlinx.coroutines.launch


private const val TAG="OverFragment"
class OverViewFragment : Fragment(),ItemListener {
    private lateinit var search:SearchView
    private val viewModel:HomeViewModel by viewModels()
    private lateinit var binding:FragmentOverViewBinding
    private lateinit var backDropBinding:ShrBackdropBinding
    private lateinit var listener: NavigationIconClickListener
    private lateinit var editor: Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        editor=initPreferences()!!


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding=FragmentOverViewBinding.inflate(layoutInflater,container,false)
        backDropBinding= ShrBackdropBinding.bind(binding.root)
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        listener= NavigationIconClickListener(requireActivity(), binding.recycleView,
            AccelerateDecelerateInterpolator(), closeIcon =AppCompatResources.getDrawable(requireContext(),R.drawable.shr_close_menu),
            openIcon = AppCompatResources.getDrawable(requireContext(),R.drawable.shr_menu)
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding.navButton.setOnClickListener(listener)
        val homeAdapter=HomeAdapter(this)
//        val controller:LayoutAnimationController=
        binding.recycleView.apply {
            adapter=homeAdapter
            layoutManager=when(activity?.getSharedPreferences("data",Context.MODE_PRIVATE)?.getString("type","")){
                "linear"->{
                    LinearLayoutManager(context)
                }
                "staggered"->{
                    StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL)
                }
                else -> {
                    GridLayoutManager(context,2)
                }
            }
            viewModel.data.observe(viewLifecycleOwner) {
                homeAdapter.submitList(viewModel.data.value)
            }
            viewModel.viewModelScope.launch {
                viewModel.getData()
            }
            homeAdapter.submitList(viewModel.data.value)
        }
        backDropBinding.linearBotton.setOnClickListener {
            binding.navButton.performClick()
            editor.putString("type","linear")
            editor.apply()
            binding.recycleView.layoutManager=LinearLayoutManager(context)
        }
        backDropBinding.girdButton.setOnClickListener {
            binding.navButton.performClick()
            editor.putString("type","default")
            editor.apply()
            binding.recycleView.layoutManager=GridLayoutManager(context,2)
        }
        backDropBinding.staggleButton.setOnClickListener {
            binding.navButton.performClick()
            editor.putString("type","staggered")
            editor.apply()
            binding.recycleView.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.HORIZONTAL)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.tool_menu,menu)
        search= menu.findItem(R.id.app_bar_search).actionView as SearchView
        search.maxWidth=700
        search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    viewModel.setKeyWord(p0)
                }
                viewModel.viewModelScope.launch {
                    viewModel.getData()
                }
                context?.let {
                   val imm=activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    if (imm.isActive){
                        imm.hideSoftInputFromWindow(search.windowToken,0)
                        search.clearFocus()
                    }
                    search.onActionViewCollapsed()
                 }

                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
               return false
            }

        })
    }

    override fun onCardClicked(item: View, pixabay: Pixabay) {
        exitTransition = MaterialElevationScale(false).apply {
            duration =getString(R.string.animator).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = getString(R.string.animator).toLong()
        }
        Vibrate.occurVibrate(requireContext())
        Bundle().apply{
                putParcelable("pixabay",pixabay)
                val action=OverViewFragmentDirections.actionOverViewFragmentToLargeFragment(this)
                val extras = FragmentNavigatorExtras(item to "card_${pixabay.id}")
               item.findNavController().navigate(action,extras)
            }
    }

    private fun initPreferences(): Editor? {
        val editor=activity?.getSharedPreferences("data",Context.MODE_PRIVATE)?.edit()
        return editor


    }


}