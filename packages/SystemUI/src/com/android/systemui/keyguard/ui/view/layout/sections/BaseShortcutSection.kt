package com.android.systemui.keyguard.ui.view.layout.sections

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.Paint
import android.graphics.BlurMaskFilter
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.android.systemui.animation.view.LaunchableImageView
import com.android.systemui.keyguard.shared.model.KeyguardSection
import com.android.systemui.keyguard.ui.binder.KeyguardQuickAffordanceViewBinder
import com.android.systemui.res.R

abstract class BaseShortcutSection : KeyguardSection() {
    protected var leftShortcutHandle: KeyguardQuickAffordanceViewBinder.Binding? = null
    protected var rightShortcutHandle: KeyguardQuickAffordanceViewBinder.Binding? = null

    override fun removeViews(constraintLayout: ConstraintLayout) {
        leftShortcutHandle?.destroy()
        leftShortcutHandle = null
        rightShortcutHandle?.destroy()
        rightShortcutHandle = null
        constraintLayout.removeView(R.id.start_button)
        constraintLayout.removeView(R.id.end_button)
    }

    protected fun getBlurredDrawable(context: Context, drawableRes: Int, radius: Float): Drawable? {
        val originalDrawable = ResourcesCompat.getDrawable(context.resources, drawableRes, context.theme)
        val bitmap = originalDrawable?.toBitmap() ?: return null

        val paint = Paint().apply {
            maskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
            isAntiAlias = true
        }

        val blurredBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(blurredBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)

        return blurredBitmap.toDrawable(context.resources)
    }

    protected fun addLeftShortcut(constraintLayout: ConstraintLayout) {
        val padding =
            constraintLayout.resources.getDimensionPixelSize(
                R.dimen.keyguard_affordance_fixed_padding
            )
        val view =
            LaunchableImageView(constraintLayout.context, null).apply {
                id = R.id.start_button
                scaleType = ImageView.ScaleType.FIT_CENTER
                background = getBlurredDrawable(context, R.drawable.keyguard_bottom_affordance_bg, 50f)
                foreground =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.keyguard_bottom_affordance_selected_border,
                        context.theme
                    )
                visibility = View.INVISIBLE
                setPadding(padding, padding, padding, padding)
            }
        constraintLayout.addView(view)
    }

    protected fun addRightShortcut(constraintLayout: ConstraintLayout) {
        if (constraintLayout.findViewById<View>(R.id.end_button) != null) return

        val padding =
            constraintLayout.resources.getDimensionPixelSize(
                R.dimen.keyguard_affordance_fixed_padding
            )
        val view =
            LaunchableImageView(constraintLayout.context, null).apply {
                id = R.id.end_button
                scaleType = ImageView.ScaleType.FIT_CENTER
                background = getBlurredDrawable(context, R.drawable.keyguard_bottom_affordance_bg, 50f)
                foreground =
                    ResourcesCompat.getDrawable(
                        context.resources,
                        R.drawable.keyguard_bottom_affordance_selected_border,
                        context.theme
                    )
                visibility = View.INVISIBLE
                setPadding(padding, padding, padding, padding)
            }
        constraintLayout.addView(view)
    }

    /**
     * Defines equality as same class.
     *
     * This is to enable set operations to be done as an optimization to blueprint transitions.
     */
    override fun equals(other: Any?): Boolean {
        return other is BaseShortcutSection
    }

    /**
     * Defines hashcode as class.
     *
     * This is to enable set operations to be done as an optimization to blueprint transitions.
     */
    override fun hashCode(): Int {
        return KEY.hashCode()
    }

    companion object {
        private const val KEY = "shortcuts"
    }
}
