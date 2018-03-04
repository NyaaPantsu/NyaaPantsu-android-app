package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.base.BaseActivity
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.mvp.presenter.SearchTorrentListPresenter
import cat.pantsu.nyaapantsu.mvp.view.SearchTorrentListView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_search_torrent_list.*
import kotlinx.android.synthetic.main.search_filter_layout.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.contentView
import javax.inject.Inject


class SearchActivity : BaseActivity(), SearchTorrentListView {

    @Inject
    lateinit var presenter: SearchTorrentListPresenter

    lateinit var adapter: TorrentListAdapter

    lateinit var recyclerView: RecyclerView

    private val categories = arrayOf("_", "3_", "3_12", "3_5", "3_13", "3_6", "2_", "2_3", "2_4", "4_", "4_7", "4_14", "4_8", "5_", "5_9", "5_10", "5_18", "5_11", "6_", "6_15", "6_16", "1_", "1_1", "1_2")
    private val status = arrayOf("0", "2", "3", "4")
    private val sizes = arrayOf("B", "KiB", "MiB", "GiB")
    private val sizesVal = arrayOf("b", "k", "m", "g")
    private var c = ""
    private var s = ""
    private var selectedSize = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val catAdapter = ArrayAdapter.createFromResource(this, R.array.cat_array, R.layout.spinner_layout)
        catSpinner.adapter = catAdapter
        catSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                c = categories[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                c = "_"
            }
        }

        val sizeAdapter = ArrayAdapter(this, R.layout.spinner_layout, sizes)
        sizeFormat.adapter = sizeAdapter
        sizeFormat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedSize = sizesVal[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedSize = "b"
            }
        }

        val statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_array, R.layout.spinner_layout)
        statusSpinner.adapter = statusAdapter
        statusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                s = status[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                s = ""
            }
        }

        filterButton.setOnClickListener {
            presenter.subscribe(this)
            presenter.loadData(
                    c,
                    filterQuery.text.toString(),
                    maxNumber.text.toString(),
                    s,
                    toDate.text.toString(),
                    fromDate.text.toString(),
                    fromSize.text.toString(),
                    toSize.text.toString(),
                    selectedSize
            )
        }

        filterFab.setOnClickListener {
            when (filterLayout.visibility) {
                View.VISIBLE -> filterLayout.visibility = View.GONE
                else -> filterLayout.visibility = View.VISIBLE
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    presenter.subscribe(this@SearchActivity)
                    presenter.loadData(
                            null,
                            query,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                    )
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(query: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onItemsLoaded(items: TorrentListResponse<TorrentModel>) {
        recyclerView = storrentlist
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setItemViewCacheSize(20)
        recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        recyclerView.isDrawingCacheEnabled = true
        adapter = TorrentListAdapter(this, items)
        adapter.setHasStableIds(true)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

    }

    override fun onError(e: Throwable?) {
        Log.e("searchtorrentlist", e.toString())
        Snackbar.make(contentView!!, e.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

}
