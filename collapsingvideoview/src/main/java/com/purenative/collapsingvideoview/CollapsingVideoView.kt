package com.purenative.collapsingvideoview

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
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
//        state = BottomSheetBehavior.STATE_EXPANDED
        isHideable = false
        val videoPeekHeight = context.resources.getDimension(R.dimen.video_peek_height)
        peekHeight = videoPeekHeight.toInt()
        setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                motionVideoView.progress = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {}
        })
    }

    private val motionVideoView = MotionVideoView(context).apply {
        val lp = CoordinatorLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.behavior = motionVideoViewBehavior
        layoutParams = lp
        this@CollapsingVideoView.addView(this)
    }

    private inner class MotionVideoView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : MotionLayout(context, attrs, defStyleAttr) {

        private val backgroundView = View(context).apply {
            id = R.id.background
            val width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            val height = context.resources.getDimension(R.dimen.video_peek_height).toInt()
            val lp = ConstraintLayout.LayoutParams(width, height)
            lp.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
            lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            layoutParams = lp
            this@MotionVideoView.addView(this)
        }

        private val videoView = VideoView(context).apply {
            val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            layoutParams = lp
            val uri = Uri.parse("https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_30mb.mp4")
            setVideoURI(uri)
            setOnPreparedListener {
                start()
            }
        }

        private val videoContainerView = FrameLayout(context).apply {
            id = R.id.video
            val width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            val height = context.resources.getDimension(R.dimen.video_peek_height).toInt()
            val lp = ConstraintLayout.LayoutParams(width, height)
            lp.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
            lp.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
            lp.dimensionRatio = "w,16:9"
            layoutParams = lp
            this@MotionVideoView.addView(this)
            addView(videoView)
        }

        init {
            loadLayoutDescription(R.xml.collapsing_scene)
        }
    }
}