package com.ayubo.life.ayubolife.prochat.di

import com.ayubo.life.ayubolife.ask.AskActivity
import com.ayubo.life.ayubolife.book_videocall.BookVideoCallActivity
import com.ayubo.life.ayubolife.challenges.ChallangeActivity
import com.ayubo.life.ayubolife.discover_search.DiscoverSearchActivity
import com.ayubo.life.ayubolife.home_group_view.GroupViewActivity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.FinalAnswerActivity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.IntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.dayanamic_questions.SeaShellsQuestionnaireActivity
import com.ayubo.life.ayubolife.janashakthionboarding.experts.ExpertsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.intro.JanashakthiIntroActivity
import com.ayubo.life.ayubolife.janashakthionboarding.medicaldataupdate.MedicalUpdateActivity
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.CalenderDetailsActivity
import com.ayubo.life.ayubolife.janashakthionboarding.mood_calender.MoodCalenderActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.JanashakthiQuestionnaireActivity
import com.ayubo.life.ayubolife.janashakthionboarding.questionnaire.question.QuestionFragment
import com.ayubo.life.ayubolife.janashakthionboarding.reportupload.ReportPreviewActivity
import com.ayubo.life.ayubolife.lifeplus.LifePlusProgramActivity
import com.ayubo.life.ayubolife.lifeplus.NewDiscoverActivity
import com.ayubo.life.ayubolife.lifeplus.ProfileNew
import com.ayubo.life.ayubolife.login.UserProfileActivity
import com.ayubo.life.ayubolife.map_challange.MapChallangeKActivity
import com.ayubo.life.ayubolife.map_challenges.activity.NewLeaderBoardActivity
import com.ayubo.life.ayubolife.map_challenges.treasureview.TreasureViewActivity
import com.ayubo.life.ayubolife.new_payment.activity.NewChangePaymentActivity
import com.ayubo.life.ayubolife.new_payment.activity.NewPaymentMainActivity
import com.ayubo.life.ayubolife.payment.activity.*
import com.ayubo.life.ayubolife.prochat.appointment.AyuboChatActivity
import com.ayubo.life.ayubolife.prochat.di.module.MainModule
import com.ayubo.life.ayubolife.programs.ProgramActivity
import com.ayubo.life.ayubolife.programs.settings.ProgramSettingsActivity
import com.ayubo.life.ayubolife.reports.activity.SelectanExpertActivity
import com.ayubo.life.ayubolife.reports.getareview.GetAReviewActivity
import com.ayubo.life.ayubolife.reports.upload.UploadReportActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.AddMoreWidgetActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.CampaignJoinActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.MyWeightActivity
import com.ayubo.life.ayubolife.revamp.v1.activity.RedeemPointActivity
import com.ayubo.life.ayubolife.twilio.TwillioCallInit
import com.ayubo.life.ayubolife.webrtc.App
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [MainModule::class])
interface MyComponent {

    fun inject(app: App)
    fun inject(ayuboChatActivity: AyuboChatActivity)
    fun inject(kanashakthiQuestionnaireActivity: JanashakthiQuestionnaireActivity)
    fun inject(seashellsQuestionnaireActivity: SeaShellsQuestionnaireActivity)
    fun inject(questionFragment: QuestionFragment)
    fun inject(expertsActivity: ExpertsActivity)
    fun inject(medicalUpdateActivity: MedicalUpdateActivity)
    fun inject(reportPreviewActivity: ReportPreviewActivity)
    fun inject(introActivity: IntroActivity)
    fun inject(finalAnswerActivity: FinalAnswerActivity)
    fun inject(janashakthiIntroActivity: JanashakthiIntroActivity)
    fun inject(moodCalenderActivity: MoodCalenderActivity)
    fun inject(calenderDetailsActivity: CalenderDetailsActivity)
    fun inject(programActivity: ProgramActivity)
    fun inject(enterMobileNumberActivityPayment: EnterMobileNumberActivityPayment)
    fun inject(enterDialogPinNumberActivity: EnterDialogPinNumberActivity)
    fun inject(paymentOptionsActivity: PaymentSettingsActivity)
    fun inject(paymentActivity: PaymentActivity)
    fun inject(otherPaymentActivity: OtherPaymentActivity)
    fun inject(newLeaderBoardActivity: NewLeaderBoardActivity)
    fun inject(paymentConfirmActivity: PaymentConfirmActivity)
    fun inject(selectanExpertActivity: SelectanExpertActivity)
    fun inject(askActivity: AskActivity)
    fun inject(getAReviewActivity: GetAReviewActivity)
    fun inject(challangeActivity: ChallangeActivity)
    fun inject(programSettingsActivity: ProgramSettingsActivity)
    fun inject(treasureViewActivity: TreasureViewActivity)
    fun inject(uploadReportActivity: UploadReportActivity)
    fun inject(lifePlusProgramActivity: LifePlusProgramActivity)
    fun inject(twillioCallInit: TwillioCallInit)
    fun inject(userProfileActivity: UserProfileActivity)
    fun inject(bookVideoCallActivity: BookVideoCallActivity)
    fun inject(groupViewActivity: GroupViewActivity)
    fun inject(discoverSearchActivity: DiscoverSearchActivity)
    fun inject(paymentPinSubmitActivity: PaymentPinSubmitActivity)
    fun inject(paymentSummaryViewActivity: PaymentSummaryViewActivity)
    fun inject(mapChallangeKActivity: MapChallangeKActivity)
    fun inject(ProfileNewActivity: ProfileNew)
    fun inject(newDiscoverActivity: NewDiscoverActivity)
    fun inject(newPaymentMainActivity: NewPaymentMainActivity)
    fun inject(newChangePaymentActivity: NewChangePaymentActivity)
    fun inject(addMoreWidgetActivity: AddMoreWidgetActivity)
    fun inject(myWeightActivity: MyWeightActivity)
    fun inject(campaignJoinActivity: CampaignJoinActivity)
    fun inject(redeemPointActivity: RedeemPointActivity)

}