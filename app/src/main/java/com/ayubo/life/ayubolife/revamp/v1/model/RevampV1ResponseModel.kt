package com.ayubo.life.ayubolife.revamp.v1.model

import com.google.gson.JsonObject


data class GetWidgetsResponse(
    val result: Int,
    val data: WidgetData,
    val message: String
)

data class PostWidgetsResponse(
    val result: Int,
    val data: PostWidgetData,
    val message: String
)

data class WidgetData(
    val main_heading: String,
    val widgets: ArrayList<Widget>
)

data class PostWidgetData(
    val id: String,
    val title: String,
    var activated_status: String
)

data class Widget(
    val id: Int,
    val title: String,
    var activated_status: Boolean = false,
)


data class GetYTDSummaryResponse(
    val result: Int,
    val data: YTDData,
    val message: String
)

data class YTDData(
    val heading: String,
    val info_box_1: InfoBoxData,
    val info_box_2: InfoBoxData,
    val info_box_3: InfoBoxData,
    val info_box_4: InfoBox4Data,
    val sub_heading_1: String,
    val badges: ArrayList<YTDBadges>,
    val sub_heading_2: String,
    val challenges: ArrayList<YTDChallenges>,
    val bottom_lable: BottomLable,
    val offers_button: OffersButton,
    val disclaimer: String
)


data class OffersButton(
    val text: String,
    val action: String,
    val meta: String,
)

data class BottomLable(
    val text: String,
    val color: String,
    val action: String,
    val meta: String,
)

data class InfoBoxData(
    val title: String,
    val value: String
)

data class InfoBox4Data(
    val title: String,
    val value_1: String,
    val value_2: String,
)

data class YTDBadges(
    val badge_id: String,
    val icon: String,
    val action: String,
    val meta: String,
)

data class YTDChallenges(
    val image: String,
    val type: String,
    val status_lable: StatusLable,
    val title: String,
    val counter_1: Counter,
    val target_1: String,
    val lable_1: String,
    val Reward: Reward,
    val counter_2: Counter,
    val target_2: String,
    val lable_2: String,
    val footer: FooterData,
    val action: String,
    val meta: String,
)

data class StatusLable(
    val text: String,
    val bg_color: String
)

data class Counter(
    val value: String,
    val color: String
)

data class Reward(
    val icon: String,
    val text: String
)

data class FooterData(
    val lable_1: String,
    val value_1: String,
    val lable_2: String,
    val value_2: String
)

data class GetV1DashboardResponse(
    val result: Int,
    val data: V1DashboardData,
    val message: String
)

data class GetV1DashboardItemCloseResponse(
    val result: Int,
    val data: JsonObject,
    val message: String
)

data class V1DashboardData(
    val dashboard_title: String?,
    val settings: ArrayList<V1DashboardSettingsData>?,
    val tabs: V1DashboardTabsData?,
    val upcoming: ArrayList<DashboardCardItem>,
    val points: V1DashboardPointsData,
    val cards: ArrayList<V1DashboardWidgetCard>,
    val daily_tip: V1DashboardDailyTip
)

data class V1DashboardSettingsData(
    val icon: String,
    val title: String,
    val action: String,
    val meta: String
)

data class V1DashboardTabsData(
    val todo: String,
    val perks: String,
    val records: String
)

data class V1DashboardPointsData(
    val label: String,
    val points: String,
    val icon_url: String,
    val btn_text: String,
    val action: String,
    val meta: String
)

data class V1DashboardDailyTip(
    val main_image: String = "",
    val title: String = "",
    val desc: String = "",
    val bottom_text: String = "",
    val bottom_text_color: String = "",
    val action: String = "",
    val meta: String = "",
    val corner_click: DailyTipCornerClick,
    val lable: DailyTipCornerClick,
)

data class V1DashboardWidgetCard(
    val title: String = "",
    val title_text_color: String = "",
    val center_main_text: String = "",
    val center_main_text_color: String = "",
    val center_main_bg_color: String = "",
    val card_center_image_url: String = "",
    val footer_text: String = "",
    val footer_text_color: String = "",
    val footer_desc: String = "",
    val footer_desc_text_color: String = "",
    val class_name: String = "",
    val type: String = "",
    val category: String = "",
    val priority: Int,
    val cardbgcolor_gradient_top: String = "",
    val cardbgcolor_gradient_bottom: String = "",
    val bottom_left_1_text: String = "",
    val bottom_left_1_color: String = "",
    val bottom_right_1_text: String = "",
    val bottom_right_1_color: String = "",
    val bottom_left_2_text: String = "",
    val bottom_left_2_color: String = "",
    val bottom_right_2_text: String = "",
    val bottom_right_2_color: String = "",
    val bottom_right_icon_url: String = "",
    val bottom_action: String = "",
    val bottom_meta: String = "",
    val card_click: ActionMetaCardClick,
    val circle: CircleItem,
    val custom_1: String = "",
    val custom_2: String = "",
    val custom_3: String = "",
)


data class DashboardCardItem(
    val id: String = "",
    val summary: String = "",
    val summary_color: String = "",
    val title: String = "",
    val title_text_color: String = "",
    val subheading: String = "",
    val subheading_text_color: String = "",
    val subheading_right: String = "",
    val subheading_right_text_color: String = "",
    val center_main_text: String = "",
    val center_main_text_color: String = "",
    val center_sub_text: String = "",
    val center_sub_text_color: String = "",
    val card_icon_url: String = "",
    val card_center_image_url: String = "",
    val card_bg: String? = "",
    val card_bground: String? = "",
    val footer_text: String = "",
    val footer_text_color: String = "",
    val footer_text_bg_color: String = "",
    val footer_icon: String = "",
    val footer_desc: String = "",
    val footer_desc_color: String = "",
    val footer_desc_text_color: String = "",
    val class_name: String = "",
    val type: String = "",
    val category: String = "",
    val priority: Int,
    val cardbgcolor_gradient_top: String = "",
    val cardbgcolor_gradient_bottom: String = "",
    val card_click: ActionMetaCardClick,
    val corner_click: ActionMetaCornerClick,
    val circle_left: CircleItem,
    val circle_right: CircleItem,
    val custom_1: String = "",
    val custom_2: String = "",
    val custom_3: String = "",
)

data class ActionMetaCardClick(
    val action: String = "",
    val meta: String = ""
)

data class DailyTipCornerClick(
    val icon_url: String = "",
    val bg_color: String = "",
    val text: String = "",
    val text_color: String = ""
)

data class ActionMetaCornerClick(
    val action: String = "",
    val meta: String = "",
    val icon_url: String = "",
    val bg_color: String = ""
)

data class CircleItem(
    val icon_url: String = "",
    val center_text: String = "",
    val sub_text: String = "",
    val percentage: Int,
    val fill_color: String = ""
)

data class BadgesEarnedItem(
    val id: String = "",
    val icon_url: String = ""
)

data class GetWeightSummaryResponse(
    val result: Int,
    val data: WeightSummaryData,
    val message: String

)

data class WeightSummaryData(
    val user_id: String = "",
    val page_name: String = "",
    val info_box_left: InfoBox,
    val info_box_center: InfoBox,
    val info_box_right: InfoBox,
    val center_button: CenterButtonData,
    val data_left: DataLeftItem,
    val data_right: DataLeftItem,
    val chart_1_data: ChartDataItem,
    val chart_2_data: ChartDataItem
)

data class InfoBox(
    val text: String = "",
    val text_color: String = "",
    val value: String = "",
    val value_color: String = ""
)

data class CenterButtonData(
    val text: String = "",
    val btn_color: String = "",
    val text_color: String = "",
    val action: String = "",
    val meta: String = ""
)

data class DataLeftItem(
    val title: String = "",
    val title_color: String = "",
    val value: String = "",
    val value_color: String = "",
    val footer_text: String = "",
    val footer_text_color: String = ""
)

data class ChartDataItem(
    val name: String = "",
    val uom: String = "",
    val data: ArrayList<ChartDataItemDetail>
)

data class ChartDataItemDetail(
    val date: String = "",
    val value: String = ""
)

data class GetCampaignDetailResponse(
    val result: Int,
    val data: CampaignData,
    val message: String
)

data class CampaignData(
    val heading: String = "",
    val image_url: String = "",
    val htmlbody: String = ""
)

data class JoinCampaignResponse(
    val result: Int,
    val data: JsonObject,
    val message: String
)

data class JoinCampaignRequestBody(
    val campaign_id: String,
    val payment_ref: String
)

data class UpdateCorporateEmailBody(
    val corporate_email: String
)

data class UpdateCorporateEmailResponse(
    val result: Int,
    val data: JsonObject,
    val message: String
)

data class GetAllRewardsResponse(
    val result: Int,
    val data: RedeemPointRewardData,
    val message: String
)

data class RedeemPointRewardData(
    val empty: RedeemPointRewardEmpty,
    val Page_Header: String = "",
    val points: RedeemPoint? = null,
    var data: ArrayList<RedeemPointData> = ArrayList<RedeemPointData>(),
    val disclaimer: String = "",
    val filter_active_bg_color: String = "",
    val filter_active_text_color: String = "",
    val filter_inactive_bg_color: String = "",
    val filter_inactive_text_color: String = "",
    val filter_types: ArrayList<RedeemPointRewardDataFilterTypes> = ArrayList<RedeemPointRewardDataFilterTypes>(),
)

data class RedeemPointRewardDataFilterTypes(
    val type: String = "",
    val icon: String = ""
)

data class RedeemPointData(
    val category_name: String = "",
    val category_text_color: String = "",
    var rewards: List<RedeemPointDataRewards>
)

data class RedeemPointDataRewards(
    val tile_image: String = "",
    val title: String = "",
    val title_color: String = "",
    val sub_title: String = "",
    val sub_title_color: String = "",
    val center_description: String = "",
    val center_description_color: String = "",
    val bottom_text: String = "",
    val bottom_text_color: String = "",
    val button: RedeemPointDataRewardsButton,
    val bottom_icon: String = "",
    val lable: RedeemPointDataRewardsLable,
    val type: String = ""
)

data class RedeemPointDataRewardsLable(
    val text: String = "",
    val bg_color: String = "",
    val text_color: String = ""
)

data class RedeemPointDataRewardsButton(
    val text: String = "",
    val text_color: String = "",
    val bg_color: String = "",
    val action: String = "",
    val meta: String = ""
)

data class RedeemPoint(
    val label: String = "",
    val points: String = "",
    val icon_url: String = "",
    val action: String = "",
    val meta: String = ""
)

data class RedeemPointRewardEmpty(
    val image: String = "",
    val main_text: String = "",
    val sub_text: String = ""
)

data class DoRedeemRewardBody(
    val reward_id: String = ""
)

data class DoRedeemRewardsResponse(
    val result: Int,
    val data: JsonObject,
    val message: String
)