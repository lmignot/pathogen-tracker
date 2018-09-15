package eu.mignot.pathogentracker.surveys.addsurvey.human

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import eu.mignot.pathogentracker.R
import eu.mignot.pathogentracker.data.models.database.Human
import eu.mignot.pathogentracker.data.models.database.Infection
import eu.mignot.pathogentracker.data.models.database.Symptom
import eu.mignot.pathogentracker.data.models.ui.CurrentDisease
import eu.mignot.pathogentracker.util.asBoolean
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_sample_info_2.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import java.util.*

class HumanSampleInfoB : StepFragment() {

  private lateinit var currentDiseases: List<CurrentDisease>

  private val currentDiseasesAdapter by lazy {
    CurrentDiseasesAdapter(this)
  }

  private val diseaseListAdapter by lazy {
    ArrayAdapter.createFromResource(context, R.array.diseases, android.R.layout.simple_spinner_item)
  }

  override val layoutResourceId: Int = R.layout.fragment_sample_info_2

  override fun onResume() {
    super.onResume()
    currentDiseases = listOf()
    surveyCurrentDiseases?.adapter = currentDiseasesAdapter
    diseaseListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    addCurrentDiseaseListener()
  }

  /**
   * @see StepFragment.getModel
   */
  override fun getModel(model: Human): Human {
    model.currentInfections.addAll(vm.currentDiseases.map {
      val rl = RealmList<Symptom>()
      rl.addAll(it.symptoms.map { s -> Symptom(s) })
      Infection(it.disease, rl)
    })
    model.isFamilyMemberIll = surveyIsHouseholdMemberIll.asBoolean()
    model.isFlagged = surveyIsFlagged.isChecked
    return model
  }

  /**
   * Display a dialog with the disease spinner and
   * checkboxes for each symptom
   *
   * Dynamically drawn from the form data provided
   */
  private fun addCurrentDiseaseListener() {
    surveyAddCurrentDisease?.setOnClickListener { _ ->
      alert {
        customView {
          linearLayout {
            orientation = LinearLayout.VERTICAL
            padding = dip(24)
            lparams(matchParent, wrapContent)
            // spinner label
            textView("Disease") {
              textSize = 18f
            }.lparams(matchParent, wrapContent) {
              marginStart = dip(4)
              bottomMargin = dip(12)
            }
            // diseases spinner
            val spinner = spinner {
              adapter = diseaseListAdapter
            }.lparams(matchParent, wrapContent) {
              bottomMargin = dip(24)
            }
            // symptoms label
            textView("Symptoms") {
              textSize = 18f
            }.lparams(matchParent, wrapContent) {
              marginStart = dip(4)
              bottomMargin = dip(12)
            }
            val checkboxes: List<CheckBox> = vm.symptoms().fold(listOf()) { ls: List<CheckBox>, s ->
              ls.plus(checkBox {
                text = s
              })
            }
            positiveButton("Save") {
              val disease = spinner.selectedItem.toString()
              val symptoms: List<String> = checkboxes
                .filter { cb -> cb.isChecked }
                .map { cb -> cb.text.toString() }.toList()
              currentDiseasesAdapter.addItem(
                CurrentDisease(
                  UUID.randomUUID().toString(),
                  disease,
                  symptoms
                )
              )
              surveyCurrentDiseases.requestLayout()
            }
            negativeButton("Cancel") {}
          }
        }
      }.show()
    }
  }

  /**
   * Custom view adapter to display the selected
   * current diseases in a list, allowing items to be
   * added and removed
   *
   * @see BaseAdapter
   */
  private class CurrentDiseasesAdapter(val parent: HumanSampleInfoB): BaseAdapter() {

    private var data: List<CurrentDisease> = parent.vm.currentDiseases

    fun addItem(d: CurrentDisease) {
      parent.vm.addCurrentDisease(d)
      refresh()
    }

    fun deleteItem(id: String) {
      parent.vm.deleteCurrentDisease(id)
      refresh()
    }

    fun refresh() {
      data = parent.vm.currentDiseases
      notifyDataSetChanged()
    }

    /**
     * Use layout DSL to construct the view
     *
     * @see BaseAdapter.getView
     */
    override fun getView(i: Int, v: View?, parent: ViewGroup?): View {
      val item = getItem(i) as CurrentDisease
      return with(parent!!.context) {
        relativeLayout {
          topPadding = 18
          bottomPadding = 18
          linearLayout {
            orientation = LinearLayout.VERTICAL
            textView(item.disease) {
              textSize = 18f
            }.lparams {
              matchParent
              wrapContent
            }
            textView("symptoms: ${item.symptoms}") {
              textSize = 14f
            }.lparams {
              matchParent
              wrapContent
              topMargin = 4
            }
          }.lparams {
            matchParent
            wrapContent
            marginStart = 96
          }
          imageButton(R.drawable.close_black) {
            backgroundColor = Color.TRANSPARENT
            textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            onClick {
              deleteItem(item.id)
            }
          }.lparams {
            wrapContent
            wrapContent
            alignParentStart()
            centerVertically()
          }
        }
      }
    }

    /**
     * @see BaseAdapter.getItem
     */
    override fun getItem(position: Int): Any = data[position]

    /**
     * @see BaseAdapter.getItemId
     */
    override fun getItemId(position: Int): Long = data.indexOf(data[position]).toLong()

    /**
     * @see BaseAdapter.getCount
     */
    override fun getCount(): Int = data.size

  }

}
