package com.example.wallpaper

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment : Fragment(), View.OnClickListener {

    private var image : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = DetailFragmentArgs.fromBundle(requireArguments()).wallpaperImage

        // Set image
        Glide.with(requireContext()).load(image).listener(
            object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image loaded, show set wallpaper button
                    detail_set_btn.visibility = View.VISIBLE

                    // Hide Progress
                    detail_wallpaper_progress.visibility = View.INVISIBLE

                    return false
                }

            }
        ).into(detail_image)

        // Set Wallpaper button
        detail_set_btn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.detail_set_btn -> setWallpaper()
        }
    }

    private fun setWallpaper() {

        // Change text and disable btn
        detail_set_btn.isEnabled = false
        detail_set_btn.text = "Wallpaper Set"

        val bitmap: Bitmap = detail_image.drawable.toBitmap()

//        val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
//        wallpaperManager.setBitmap(bitmap)

        val task : SetWallpaperTask = SetWallpaperTask(requireContext(), bitmap)
        task.execute(true)
    }

    companion object{
        class SetWallpaperTask internal constructor(private val context : Context, private val bitmap : Bitmap) :
                AsyncTask<Boolean, String, String>() {
            override fun doInBackground(vararg p0: Boolean?): String {
                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setBitmap(bitmap)
                return "Wallpaper Set"
            }
        }
    }

}
