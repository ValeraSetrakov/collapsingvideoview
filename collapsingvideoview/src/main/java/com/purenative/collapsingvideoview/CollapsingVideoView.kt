package com.purenative.collapsingvideoview

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CollapsingVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    companion object {
        fun log(message: String) =
            Log.d(CollapsingVideoView::class.java.simpleName, message)
    }

    private val motionVideoViewBehavior: BottomSheetBehavior<MotionVideoView> = BottomSheetBehavior<MotionVideoView>().apply {
        isHideable = false
        peekHeight = 200
        setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                log("Slide offset $slideOffset")
                motionVideoView.progress = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                log("New state $newState")
            }
        })
    }

    private val motionVideoView = MotionVideoView(context).apply {
        val lp = CoordinatorLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.behavior = motionVideoViewBehavior
        layoutParams = lp
        this@CollapsingVideoView.addView(this)
        setBackgroundColor(Color.BLUE)
    }

    private inner class MotionVideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : MotionLayout(context, attrs, defStyleAttr) {
        private val videoView = VideoView(context).apply {
            id = R.id.video_view
            val lp = ConstraintLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams = lp
            val uri = Uri.parse("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_30mb.mp4")
            setVideoURI(uri)
//            setMediaController(MediaController(context))
            setOnPreparedListener {
                start()
            }
            this@MotionVideoView.addView(this)
        }

        init {
            loadLayoutDescription(R.xml.collapsing_scene)
        }
    }
}