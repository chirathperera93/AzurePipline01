package com.ayubo.life.ayubolife.revamp.v1.model

import com.ayubo.life.ayubolife.lifeplus.PointUser

data class V1GetLeaderboardResponse(
    val result: Int,
    val data: V1GetLeaderboardData,
    val message: String
)


data class V1GetLeaderboardData(
    val community_name: String = "",
    val more_info: MoreInfoData,
    val reward: RewardData,
    val tabs: ArrayList<LeaderBoardTabs>?,
    val leaderboard: ArrayList<PointUser>,
    val empty_page: EmptyPageData,
    val backgroud_image: String = "",
    val challenge_icon: String = ""
)

data class MoreInfoData(
    val icon_url: String = "",
    val action: String = "",
    val meta: String = ""
)

data class RewardData(
    val icon_url: String = "",
    val lable: String = ""
)

data class EmptyPageData(
    val title: String = "",
    val description: String = ""
)

data class LeaderBoardTabs(
    val name: String = "",
    val on_focus: Boolean = false,
    val challenge_id: String = ""
)


data class V1GetStepSummaryResponse(
    val result: Int,
    val data: V1GetStepSummaryData,
    val message: String
)

data class V1GetStepSummaryData(
    val title: String = "",
    val avg_achievement: String = "",
    val summary: String = "",
    val chart_data: StepSummaryChartData,
    val target: Int,
    val chart_success_color: String = "",
    val chart_fail_color: String = "",
    val analysis: AnalysisData,
    val detailed_view: DetailedView
)

data class StepSummaryChartData(
    val average: ArrayList<ChartDataInfo>,
    val me: ArrayList<ChartDataInfo>
)

//data class ChartDataInfo(
//    val day: String = "",
//    val value: String = "",
//    val color: String = ""
//)

data class ChartDataInfo(
    val day: String = "",
    val value: String = ""
)

data class AnalysisData(
    val percentage: Int,
    val circle_top_img_url: String = "",
    val circle_centre: String = "",
    val circle_bottom: String = "",
    val distance: String = "",
    val calories: String = "",
    val total_points: String = "",
    val energy: String = "",
    val achivements: String = ""

)

data class DetailedView(
    val title: String = "",
    val action: String = "",
    val meta: String = ""
)

data class UpdateCityResponse(
    val result: Int,
    val data: Any,
    val message: String
)
