package eu.mignot.pathogentracker.surveys.surveydetail

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import eu.mignot.pathogentracker.App
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Vector
import eu.mignot.pathogentracker.data.models.database.VectorBatch
import eu.mignot.pathogentracker.data.models.database.getNextVectorSequence
import eu.mignot.pathogentracker.surveys.addsurvey.vector.AddVectorSurveyActivity
import eu.mignot.pathogentracker.surveys.surveys.SurveysActivity
import eu.mignot.pathogentracker.util.AppSettings
import eu.mignot.pathogentracker.util.formatTime
import eu.mignot.pathogentracker.util.setupToolbar
import kotlinx.android.synthetic.main.activity_vector_batch_detail.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import kotlin.properties.Delegates

class VectorBatchDetailActivity: AppCompatActivity() {

  private val batchId by lazy {
    intent.getStringExtra(AppSettings.Constants.BATCH_ID_KEY)?.let {
      it
    } ?: AppSettings.Constants.NO_ID_VALUE
  }

  private val survey by lazy {
    App.getLocalSurveysRepository().getSurvey(VectorBatch(), batchId)
  }

  private val vectorSurveys by lazy {
    App.getLocalSurveysRepository()
      .getSurveysFor(Vector(), batchId)
      .sortedByDescending { it.sequence }
  }

  private val adapter by lazy {
    VectorsListAdapter(vectorSurveys)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (batchId == AppSettings.Constants.NO_ID_VALUE) {
      startActivity<SurveysActivity>(
        AppSettings.Constants.MESSAGE_KEY to getString(R.string.error_no_surveyId)
      )
    }
    setContentView(R.layout.activity_vector_batch_detail)
    setupToolbar(toolbarVBD, getString(R.string.title_vector_batch_detail), R.drawable.arrow_back_white)
    fabAddVector?.onClick {
      startActivity<AddVectorSurveyActivity>(
        AppSettings.Constants.BATCH_ID_KEY to batchId,
        AppSettings.Constants.VECTOR_ID_KEY to UUID.randomUUID().toString(),
        AppSettings.Constants.VECTOR_SEQUENCE_KEY to vectorSurveys.getNextVectorSequence()
      )
    }
  }

  override fun onResume() {
    super.onResume()
    survey?.let {
      vectorBatchSurveyDetailId.text = it.id
      vectorBatchSurveyDetailDate.text = it.collectedOn.formatTime()
      it.locationCollected?.let {
        loc -> vectorBatchSurveyDetailLoc.text = loc.toString()
      }
      vectorBatchSurveyDetailTerritory.text = it.territory
      vectorBatchSurveyDetailTemp.text = it.temperature.toString()
      vectorBatchSurveyDetailWeather.text = it.weatherCondition
    }
    vectorBatchDetailList.adapter = adapter
    vectorBatchDetailList.layoutManager = LinearLayoutManager(this)
    vectorBatchDetailList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
  }

  class VectorsListAdapter(private var surveys: List<Vector>):
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      return SurveyItemUi()
        .createView(AnkoContext.create(parent.context, parent))
        .tag as ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      val survey = surveys[position]
      holder.bind(survey)
    }

    override fun getItemCount(): Int = surveys.size

  }

  class SurveyItemUi: AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View {

      var itemId: TextView by Delegates.notNull()
      var itemSpecies: TextView by Delegates.notNull()

      val itemView =  with(ui) {
          linearLayout {
            org.jetbrains.anko.matchParent
            org.jetbrains.anko.wrapContent
            padding = dip(4)
            orientation = android.widget.LinearLayout.VERTICAL
            isClickable = true
            isFocusable = true
            itemId = textView {
              textSize = 16f
            }.lparams {
              org.jetbrains.anko.matchParent
              org.jetbrains.anko.wrapContent
            }
            itemSpecies = textView {
              textSize = 14f
            }.lparams {
              topMargin = dip(8)
              org.jetbrains.anko.matchParent
              org.jetbrains.anko.wrapContent
            }
          }
        }
      itemView.tag = ViewHolder(
        itemView,
        ui.ctx,
        itemId,
        itemSpecies
      )
      return itemView
    }
  }

  class ViewHolder(
    itemView: View,
    private val ctx: Context,
    private val itemId: TextView,
    private val itemSpecies: TextView
  ): RecyclerView.ViewHolder(itemView) {

    fun bind(survey: Vector) {
      itemId.text = ctx.getString(R.string.survey_vector_item_id, survey.id, String.format("%03d",survey.sequence))
      itemSpecies.text = ctx.getString(R.string.survey_vector_item_species_gender, survey.species, survey.gender)
      itemView.onClick {
        ctx.startActivity<VectorDetailActivity>(
          AppSettings.Constants.VECTOR_ID_KEY to survey.id,
          AppSettings.Constants.BATCH_ID_KEY to survey.batchId
        )
      }
    }
  }

}
