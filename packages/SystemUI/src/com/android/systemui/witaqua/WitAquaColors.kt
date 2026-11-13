/*
 * SPDX-FileCopyrightText: WitAqua
 * SPDX-License-Identifier: Apache-2.0
 */

package com.android.systemui.witaqua

import android.content.Context
import androidx.annotation.ColorInt
import com.android.systemui.res.R

/** Colors of the WitAqua look that upstream SystemUI shares with other surfaces. */
object WitAquaColors {

    /**
     * Opaque background of notifications and of the notification footer buttons.
     *
     * Upstream draws them with customColorSurfaceEffect1, which the inactive Quick Settings
     * tiles also use. Notifications get their own opaque color so the tiles can stay
     * translucent over the shade blur.
     */
    @JvmStatic
    @ColorInt
    fun notificationSurface(context: Context): Int =
        context.getColor(R.color.witaqua_notification_surface)
}
