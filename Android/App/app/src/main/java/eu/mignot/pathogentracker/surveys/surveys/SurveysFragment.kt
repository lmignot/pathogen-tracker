package eu.mignot.pathogentracker.surveys.surveys

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.surveys.data.*
import eu.mignot.pathogentracker.surveys.data.models.ui.UiSurvey
import eu.mignot.pathogentracker.surveys.surveydetail.HumanDetailActivity
import eu.mignot.pathogentracker.surveys.surveydetail.VectorBatchDetailActivity
import eu.mignot.pathogentracker.util.AppSettings
import kotlinx.android.synthetic.main.activity_surveys.*
import kotlinx.android.synthetic.main.fragment_surveys.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import kotlin.properties.Delegates

class SurveysFragment: Fragment(), AnkoLogger {

  private val vm by lazy {
    SurveysViewModel(
      UiSurveysRepository
    )
  }

  private val adapter by lazy {
    SurveysListAdapter(vm.getSurveys())
  }

  private val loginProvider by lazy {
    App.getLoginProvider()
  }

  companion object {
    fun newInstance(): Fragment = SurveysFragment()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_surveys, container, false)
  }

  override fun onResume() {
    super.onResume()
    surveysList.adapter = adapter
    surveysList.layoutManager = LinearLayoutManager(context)
    surveysList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

    // Show the welcome message if no surveys available
    if (vm.getSurveys().isEmpty()) {
      (activity!!.noData as LinearLayout).visibility = View.VISIBLE
    }

    if (loginProvider.hasUser()) {
      // Show the user's name or anonymous text
      activity!!.userNameTextView?.text = getString(
        R.string.welcome_user,
        loginProvider.getCurrentUser()?.displayName ?: getString(R.string.anonymous)
      )
    }
  }

  class SurveysListAdapter(private var surveys: List<UiSurvey>):
    RecyclerView.Adapter<SurveysViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveysViewHolder {
      return SurveyItemUi()
        .createView(AnkoContext.create(parent.context, parent))
        .tag as SurveysViewHolder
    }

    override fun onBindViewHolder(holder: SurveysViewHolder, position: Int) {
      val survey = surveys[position]
      holder.bind(survey)
    }

    override fun getItemCount(): Int = surveys.size

  }

  class SurveyItemUi: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {

      var surveyItemId: TextView by Delegates.notNull()
      var surveyItemLocation: TextView by Delegates.notNull()
      var surveyItemDate: TextView by Delegates.notNull()
      var surveyItemAvatar: ImageView by Delegates.notNull()
      var surveyItemAvatarLetter: TextView by Delegates.notNull()
      var surveyItemFlag: ImageView by Delegates.notNull()
      var surveyItemQueue: ImageView by Delegates.notNull()

      val itemView =  with(ui) {
        relativeLayout {
          bottomPadding = dip(12)
          topPadding = dip(12)
          lparams(matchParent, wrapContent)
          isClickable = true
          isFocusable = true
          linearLayout {
            lparams(wrapContent, wrapContent)
            leftPadding = dip(56)
            rightPadding = dip(24)
            orientation = LinearLayout.VERTICAL
            surveyItemId = textView {
              ellipsize = TextUtils.TruncateAt.END
              singleLine = true
              textColor = ContextCompat.getColor(context, R.color.colorPrimaryText)
              textSize = 18f
            }.lparams(matchParent, wrapContent)
            surveyItemLocation = textView {
              singleLine = true
              textColor = ContextCompat.getColor(context, R.color.colorSecondaryText)
              textSize = 12f
            }.lparams(matchParent, wrapContent)
            surveyItemDate = textView {
              singleLine = true
              textColor = ContextCompat.getColor(context, R.color.colorSecondaryText)
              textSize = 11f
            }.lparams(matchParent, wrapContent)
          }
          relativeLayout {
            lparams(wrapContent, wrapContent)
            surveyItemAvatar = imageView(R.drawable.bg_avatar_survey_human)
              .lparams(dip(40), dip(40)) {
              alignParentStart()
              alignParentTop()
            }
            surveyItemAvatarLetter = textView {
              textColor = ContextCompat.getColor(context, R.color.white)
              textSize = 20f
            }.
              lparams(wrapContent, wrapContent) {
                centerInParent()
              }
          }
          surveyItemFlag = imageView(R.drawable.flag) {
            adjustViewBounds = false
          }.lparams(dip(18), dip(18)) {
              alignParentTop()
              alignParentEnd()
            }
          surveyItemQueue = imageView(R.drawable.cloud_done) {
            adjustViewBounds = false
          }.lparams(dip(18), dip(18)) {
              alignParentBottom()
              alignParentEnd()
            }
        }
      }
      itemView.tag = SurveysViewHolder(
        itemView,
        ui.ctx,
        surveyItemId,
        surveyItemLocation,
        surveyItemDate,
        surveyItemAvatar,
        surveyItemAvatarLetter,
        surveyItemFlag,
        surveyItemQueue
      )
      return itemView
    }
  }

  class SurveysViewHolder(
    itemView: View,
    private val ctx: Context,
    private val surveyItemId: TextView,
    private val surveyItemLocation: TextView,
    private val surveyItemDate: TextView,
    private val surveyItemAvatar: ImageView,
    private val surveyItemAvatarLetter: TextView,
    private val surveyItemFlag: ImageView,
    private val surveyItemQueue: ImageView
  ): RecyclerView.ViewHolder(itemView) {

    fun bind(survey: UiSurvey) {
      surveyItemId.text = survey.surveyId
      surveyItemLocation.text = survey.surveyLocation
      surveyItemDate.text = survey.surveyDate
      surveyItemAvatar.setImageDrawable(getAvatar(survey.surveyType))
      surveyItemAvatarLetter.text = getAvatarLetter(survey.surveyType)
      surveyItemFlag.visibility = if (survey.isFlagged) View.VISIBLE else View.GONE
      surveyItemQueue.visibility = if (survey.isUploaded) View.VISIBLE else View.GONE
      itemView.onClick {
        when (survey.surveyType) {
          is SurveyType.VECTOR -> ctx.startActivity<VectorBatchDetailActivity>(
            AppSettings.Constants.BATCH_ID_KEY to survey.surveyId
          )
          is SurveyType.PATIENT -> ctx.startActivity<HumanDetailActivity>(
            AppSettings.Constants.BATCH_ID_KEY to survey.surveyId
          )
          is SurveyType.NONE -> Toast.makeText(ctx, "This is weird...", Toast.LENGTH_SHORT).show()
        }
      }
    }

    private fun getAvatar(surveyType: SurveyType): Drawable = when (surveyType) {
      is SurveyType.VECTOR -> itemView.context.getDrawable(R.drawable.bg_avatar_survey_vector_batch)
      else -> itemView.context.getDrawable(R.drawable.bg_avatar_survey_human)
    }

    private fun getAvatarLetter(surveyType: SurveyType): String = when (surveyType) {
      is SurveyType.PATIENT -> "H"
      is SurveyType.VECTOR -> "M"
      is SurveyType.NONE -> "?"
    }
  }

}
