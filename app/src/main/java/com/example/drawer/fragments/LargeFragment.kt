package com.example.drawer.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.drawer.R
import com.example.drawer.adapter.AppBarLayoutStateChangeListener
import com.example.drawer.databinding.BottomViewBinding
import com.example.drawer.databinding.FragmentLargeBinding
import com.example.drawer.entity.Pixabay
import com.example.drawer.util.Vibrate
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LargeFragment : Fragment() {
    val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show()
                val bitmap = binding.largeView.drawable.toBitmap(pixabay.width, pixabay.height)
                lifecycleScope.launch {
                    savePhoto(bitmap)
                }

            } else {
                Toast.makeText(context, "请求存储权限失败", Toast.LENGTH_SHORT).show()
            }
        }

    private lateinit var pixabay: Pixabay
    private lateinit var binding: FragmentLargeBinding
    private lateinit var controller: WindowInsetsControllerCompat
    private lateinit var bottomBinding:BottomViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // Scope the transition to a view in the hierarchy so we know it will be added under
            // the bottom app bar but over the elevation scale of the exiting HomeFragment.
            drawingViewId = R.id.fragmentContainerView
            duration = getString(R.string.animator).toLong()
            scrimColor = Color.TRANSPARENT
//            setAllContainerColors(121212)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLargeBinding.inflate(inflater, container, false)
        bottomBinding=BottomViewBinding.bind(binding.root)
        val args: LargeFragmentArgs by navArgs()
        pixabay = args.pixabay.getParcelable<Pixabay>("pixabay")!!
        binding.parentCard.transitionName = "card_${pixabay.id}"
        context?.let {
            Glide.with(it).load(
                pixabay?.LargerURL
            ).listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context,"图片加载异常",Toast.LENGTH_SHORT)

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.progressBar.visibility = View.GONE
                    binding.download.visibility = View.VISIBLE
                    return false
                }
            }).into(binding.largeView)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCardView()
        controller = ViewCompat.getWindowInsetsController(binding.root)!!
        binding.appBar.addOnOffsetChangedListener(object : AppBarLayoutStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {

                when (state) {
                    AppBarLayoutStateChangeListener.State.COLLAPSED -> {
                        binding.download.visibility = View.INVISIBLE
                        binding.bottomBar.visibility = View.INVISIBLE

                        controller.hide(WindowInsetsCompat.Type.statusBars())
//                        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
                    }
                    AppBarLayoutStateChangeListener.State.EXPANDED -> {
                        if (binding.progressBar.visibility == View.GONE) {
                            binding.download.visibility = View.VISIBLE
                            binding.bottomBar.visibility = View.VISIBLE
//                            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                            controller.show(WindowInsetsCompat.Type.statusBars())

                        }
                    }
                    else -> {}
                }


            }

            override fun TriggerAnimation() {
               translater(bottomBinding.cardView)
                translater(bottomBinding.secondCardView,250)
                translater(bottomBinding.thirdCardView,300)

            }
        })
        binding.toolbar.title = "作者:${pixabay.author}"
        bottomBinding.authorText.text = pixabay.id
        bottomBinding.downloadText.text = pixabay.downloads.toString()
        val size = resources.getString(R.string.size, pixabay.pictureSize.toFloat() / (1024 * 1024))
        bottomBinding.sizeText.text = size
        binding.download.setOnClickListener {
            Vibrate.occurVibrate(requireContext())
            requestPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }




    }

    private suspend fun savePhoto(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {

            val saveUri = requireContext().contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
            ) ?: kotlin.run {
                Toast.makeText(requireContext(), "存储失败", Toast.LENGTH_SHORT).show()
                return@withContext
            }
            requireContext().contentResolver.openOutputStream(saveUri).use {
                if (bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)) {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(), "存储成功", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    MainScope().launch {
                        Toast.makeText(
                            requireContext(), "存储失败", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    private fun initCardView(){
        bottomBinding.cardView.translationX=-1080f
        bottomBinding.secondCardView.translationX=-1080f
        bottomBinding.thirdCardView.translationX=-1080f

    }
    private fun translater(view: View,delay: Int =200) {

        // Translate the view 200 pixels to the right and back

        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 000f)
//        animator.repeatCount = 1
        animator.startDelay= delay.toLong()
//        animator.repeatMode = ObjectAnimator.REVERSE
        animator.interpolator=DecelerateInterpolator()
        animator.duration=300
        animator.start()
    }
}