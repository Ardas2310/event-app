package skg.code.event_app.ui.drawable

import android.content.Context
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

/**
 * Creates a ShimmerDrawable with the given context.
 */
fun createShimmerDrawable(): ShimmerDrawable {
    val shimmer = Shimmer.AlphaHighlightBuilder()
        .setDuration(1000)
        .setBaseAlpha(0.7f)
        .setHighlightAlpha(1f)
        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
        .setAutoStart(true)
        .build()

    return ShimmerDrawable().apply {
        setShimmer(shimmer)
    }
}