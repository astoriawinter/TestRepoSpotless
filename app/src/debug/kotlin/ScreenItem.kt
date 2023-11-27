package com.lamoda.lite.utils.screens

import com.lamoda.checkout.internal.model.CheckoutType
import com.lamoda.deeplink.api.LinkSource
import com.lamoda.domain.Constants.EMPTY_OWNER_ID
import com.lamoda.lite.Application
import com.lamoda.lite.R
import com.lamoda.lite.businesslayer.analytics.events.ReviewSource
import com.lamoda.lite.mvp.model.deeplinks.WishesGroupPath
import com.lamoda.lite.mvp.model.profile.premium.PremiumStatusOpenedFrom
import com.lamoda.lite.mvp.presenter.reviews.ReviewAskerMode
import com.lamoda.lite.mvp.view.profile.authorized.premium.PremiumStatusScreen
import com.lamoda.lite.mvp.view.wishes.WishesGroupScreen
import com.lamoda.lite.navigation.itlc.ItlcLimitsScreen
import com.lamoda.lite.navigation.main.GoodsRemovingScreen
import com.lamoda.lite.navigation.main.StoriesScreen
import com.lamoda.lite.navigation.main.WishListScreen
import com.lamoda.lite.navigation.product.CatalogFromLink
import com.lamoda.lite.navigation.product.ReviewAskerScreen
import com.lamoda.lite.navigation.profile.AppNotificationsSettingsScreen
import com.lamoda.lite.navigation.profile.ProfileReviewsScreen
import com.lamoda.navigation.DialogScreen
import com.lamoda.navigation.Host
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.android.support.SupportAppScreen

const val DEFAULT_KEY = "default"

interface ScreenItem {
    val screenTitleId: Int
    val host: Host
        get() = Host.COMMON
    val configurations: Map<String, Screen>
    val currentConfiguration: String?
}

class CustomScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_custom
    override val configurations: Map<String, Screen> by lazy { buildConfiguration() }

    private val findPaths = listOf(
        "com.lamoda.lite.navigation.main",
        "com.lamoda.lite.navigation.product",
    )

    private fun buildConfiguration(): Map<String, Screen> {
        val screenName = currentConfiguration ?: return mapOf()
        var clazz: Class<*>? = null
        for (path in findPaths) {
            try {
                clazz = Class.forName("$path.$screenName")
                break
            } catch (e: ClassNotFoundException) {
                continue
            }
        }
        val screen = clazz?.newInstance() as? Screen ?: return mapOf()

        return mapOf(DEFAULT_KEY to screen)
    }
}

class PremiumStatusItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_premium_status
    override val host: Host
        get() = Host.COMMON
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to PremiumStatusScreen(openedFrom = PremiumStatusOpenedFrom.PROFILE),
        "from_landing" to PremiumStatusScreen(
            serviceTitle = "30 дней на возврат",
            openedFrom = PremiumStatusOpenedFrom.PRODUCT_PAGE,
        ),
    )
}

class CatalogFromLinkScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_catalog_from_link
    override val host: Host
        get() = Host.CATALOG
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to CatalogFromLink("ru/n/", LinkSource.Internal.DEBUG_SCREEN),
        "aaaaaaaaaaaaaaaaaaaaaaaaa" to CatalogFromLink("aaaaaaaaaaaaaaaaaaaaaaaaa", LinkSource.Internal.DEBUG_SCREEN),
    )
}

class WishListScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_wishlist
    override val host: Host
        get() = Host.WISHES
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to WishListScreen(),
    )
}

class ReviewsScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_reviews
    override val configurations: Map<String, SupportAppScreen> = mapOf(
        DEFAULT_KEY to ProfileReviewsScreen(),
    )
}

class QuestionsScreenItem(override val currentConfiguration: String?) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_questions
    override val configurations: Map<String, SupportAppScreen> = mapOf(
        DEFAULT_KEY to Application.getComponentManager().getCustomerComponent().myQuestionsApi().getScreen(),
    )
}

class AppNotificationsSettingsScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_app_notifications_settings
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to AppNotificationsSettingsScreen(),
    )
}

class WishesGroupScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_wishes_group
    override val configurations: Map<String, SupportAppScreen> = mapOf(
        DEFAULT_KEY to WishesGroupScreen(
            WishesGroupPath(
                "https://www.lamoda.ru/wishlist/shared/2IYpuJ3pqJ5p/",
                "2IYpuJ3pqJ5p",
            ),
        ),
    )
}

class GoodsRemovingScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_goods_removing
    override val configurations: Map<String, SupportAppScreen> = mapOf(
        DEFAULT_KEY to GoodsRemovingScreen(CheckoutType.FULL),
    )
}

class StoriesScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_stories
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to StoriesScreen("235"),
    )
}

class ReviewAskerScreenItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_review_asker
    override val configurations: Map<String, DialogScreen> = mapOf(
        DEFAULT_KEY to ReviewAskerScreen(EMPTY_OWNER_ID, ReviewSource.FEATURED),
        "review_sent" to ReviewAskerScreen(EMPTY_OWNER_ID, ReviewSource.FEATURED, ReviewAskerMode.REVIEW_SENT),
        "from_catalog" to ReviewAskerScreen(EMPTY_OWNER_ID, ReviewSource.CATALOG),
        "from_favorite" to ReviewAskerScreen(EMPTY_OWNER_ID, ReviewSource.FAVORITE),
    )
}

class ItlcLimitsScreenItem(override val currentConfiguration: String?) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_itlc_limits
    override val configurations: Map<String, DialogScreen> = mapOf(
        DEFAULT_KEY to ItlcLimitsScreen(),
    )
}

class PickAddressOnMapItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_pick_address_on_map
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to DebugPickAddressOnMapScreen(),
    )
}

class PickpointOnYandexMapItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_pickpoint_on_yandex_map
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to DebugPickpointOnYandexMapScreen(),
    )
}

class DeliveryMethodsOnMapItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_delivery_methods_on_map
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to DebugDeliveryMethodsOnMapScreen(),
    )
}

class MultiDeliveryDetailsItem(override val currentConfiguration: String? = null) : ScreenItem {
    override val screenTitleId: Int = R.string.screen_title_multi_delivery_details
    override val configurations: Map<String, Screen> = mapOf(
        DEFAULT_KEY to DebugMultiDeliveryDetailsScreen(),
    )
}
