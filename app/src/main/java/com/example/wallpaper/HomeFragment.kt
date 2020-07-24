package com.example.wallpaper

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), (WallpapersModel) -> Unit {

    private val firebaseRepository = FirebaseRepository()
    private var navController: NavController? = null

    private var wallpapersList: List<WallpapersModel> = ArrayList()
    private val wallpapersListAdapter: WallpapersListAdapter = WallpapersListAdapter(wallpapersList, this)

    private var isLoading : Boolean = true

    private val wallpapersViewModel: WallpapersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // initialize action bar
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Wallpaper"

        // initialize navController
        navController = Navigation.findNavController(view)

        // Check if user is login
        if (firebaseRepository.getUser() == null){
            // User not login, go to register page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment2)
        }

        // Initialize recycler view
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager = GridLayoutManager(context, 3)
        wallpapers_list_view.adapter = wallpapersListAdapter

        // Reach bottom of recyclerview
        wallpapers_list_view.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    // Reached at bottom and not scrolling anymore
                    if (!isLoading){
                        // Load next page
//                        firebaseRepository.queryWallpapers()
                        wallpapersViewModel.loadWallpapersData()
                        isLoading = true

                        Log.d("HOME_FRAGMENT_LOG", "Reached bottom, load new content")
                    }

                }
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val wallpapersViewModel: WallpapersViewModel by viewModels()
        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()

            // Loading complete
            isLoading = false

        })
    }

    override fun invoke(wallpaper: WallpapersModel) {
        // Clicked on wallpaper from the list, navigate to details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.image)
        navController!!.navigate(action)
    }

}
