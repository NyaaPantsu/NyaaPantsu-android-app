package cat.pantsu.nyaapantsu.ui.fragment

import android.app.DatePickerDialog
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.TorrentList

import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.content_search.*
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit



class SearchFragment : Fragment() {
    val categories = arrayOf("_", "3_", "3_12", "3_5", "3_13", "3_6", "2_", "2_3", "2_4", "4_", "4_7", "4_14", "4_8", "5_", "5_9", "5_10", "5_18", "5_11", "6_", "6_15", "6_16", "1_", "1_1", "1_2")
    val status = arrayOf("0", "2", "3", "4")
    val sizes = arrayOf("B", "KiB", "MiB", "GiB")
    val sizesVal = arrayOf("b", "k", "m", "g")
    private var c = ""
    private var s = ""
    private var selectedSize = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.VISIBLE

        activity.title = getString(R.string.title_activity_search)
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.content_search, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var catAdapter = ArrayAdapter.createFromResource(activity, R.array.cat_array, R.layout.spinner_layout)
        catSpinner.adapter = catAdapter
        catSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                c = categories[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                c = "_"
            }
        }
        var sizeAdapter = ArrayAdapter(activity, R.layout.spinner_layout, sizes)
        sizeFormat.adapter = sizeAdapter
        sizeFormat.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSize = sizesVal[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedSize = "b"
            }
        }

        var statusAdapter = ArrayAdapter.createFromResource(activity, R.array.status_array, R.layout.spinner_layout)
        statSpinner.adapter = statusAdapter
        statSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                s = status[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                s = ""
            }
        }
        val myCalendar = Calendar.getInstance()
        fromDate.setOnClickListener { _ ->

            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                fromDate.setText(dateFormat.format(myCalendar.time))
            }
            DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        toDate.setOnClickListener { _ ->

            val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                toDate.setText(dateFormat.format(myCalendar.time))
            }
            DatePickerDialog(activity, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        searchButton.setOnClickListener { _ ->
            var torrentListFragment = TorrentList.newInstance(queryText.text.toString(), c, s, maxNumber.text.toString(), fromSize.text.toString(), toSize.text.toString(), selectedSize, formatDate(fromDate.text.toString()), formatDate(toDate.text.toString()))
            fragmentManager.beginTransaction().replace(R.id.main_fragment, torrentListFragment as Fragment).addToBackStack(null).commit()
        }
    }

    fun formatDate(str: String): String {
        if (str != "") {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val cal = Calendar.getInstance()
            cal.time = dateFormat.parse(str)


            val msDiff = Calendar.getInstance().timeInMillis - cal.timeInMillis
            val daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff)
            return daysDiff.toString()
        }
        return ""
    }
}
