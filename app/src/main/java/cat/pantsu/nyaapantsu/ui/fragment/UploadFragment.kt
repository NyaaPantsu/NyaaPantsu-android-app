package cat.pantsu.nyaapantsu.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.FlagChip
import cat.pantsu.nyaapantsu.model.User
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import com.github.kittinunf.fuel.core.FuelManager
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import com.pchmn.materialchips.ChipsInput
import com.pchmn.materialchips.model.ChipInterface
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_upload.*
import net.gotev.uploadservice.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.File


class UploadFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    val categories = arrayOf("_", "3_", "3_12", "3_5", "3_13", "3_6", "2_", "2_3", "2_4", "4_", "4_7", "4_14", "4_8", "5_", "5_9", "5_10", "5_18", "5_11", "6_", "6_15", "6_16", "1_", "1_1", "1_2")
    val languages = arrayOf("ca", "zh", "zh-Hant", "nl", "en", "fr", "de", "hu", "is", "it", "ja", "ko", "nb", "pt", "ro", "es",  "sv", "th")

    var c = ""
    var lang = ""
    var selectedTorrent: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.GONE
        activity.title = "Upload a torrent - NyaaPantsu"
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_upload, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uploadTypeAdapter = ArrayAdapter.createFromResource(activity, R.array.upload_type_array, R.layout.spinner_layout)
        val catAdapter = ArrayAdapter.createFromResource(activity, R.array.cat_array, R.layout.spinner_layout)
        upload_type_spinner.adapter = uploadTypeAdapter
        categorySpin.adapter = catAdapter

        upload_type_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                switchUploadType(uploadTypeAdapter.getItem(p2) as String)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        categorySpin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                c = categories[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                c = "_"
            }
        }

        if (User.id > 0) {
            anonSwitch.visibility = View.VISIBLE
        }

        choose_text.setOnClickListener { _ ->
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult<FilePickerActivity>(FILE_CODE, FilePickerActivity.EXTRA_ALLOW_MULTIPLE to false, FilePickerActivity.EXTRA_MODE to FilePickerActivity.MODE_FILE)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 10)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_done, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_done -> {
                if (valideForm()) {
                    upload(activity)
                }
                return true
            }
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    fun switchUploadType(type: String) {
        val arr = resources.getStringArray(R.array.upload_type_array)
        when (type) {
            arr[0] -> {
                magnet_edit.visibility = View.GONE
                choose_text.visibility = View.VISIBLE
                magnet_edit.text = null
            }
            arr[1] -> {
                choose_text.visibility = View.GONE
                magnet_edit.visibility = View.VISIBLE
                selectedTorrent = null
                choose_text.setText(R.string.choose)
            }
        }

        langsInput.addChipsListener(object: ChipsInput.ChipsListener {
            override fun onChipAdded(chip: ChipInterface, newSize:Int) {
                val langs = lang.split(";").toMutableList()
                langs.add(chip.info)
                lang = langs.joinToString(";")
            }
            override fun onChipRemoved(chip:ChipInterface, newSize:Int) {
                val langs = lang.split(";").toMutableList()
                langs.remove(chip.info)
                lang = langs.joinToString(";")
            }
            override fun onTextChanged(text:CharSequence) {
                // Do nothing
            }
        })
        val langTranslation = resources.getStringArray(R.array.language_array)
        val flagList: ArrayList<FlagChip> = ArrayList()
        for ((index, lg) in languages.withIndex()) {
            val flagCode = lg.replace("-", "_").toLowerCase()
            if (resources.getIdentifier("flag_"+flagCode, "drawable", activity.packageName) > 0) {
                val uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + activity.packageName + "/drawable/flag_" + flagCode)
                flagList.add(FlagChip(index.toString(), uri, langTranslation[index], lg))
            }
        }
        langsInput.filterableList = flagList

    }

    fun valideForm(): Boolean {
        choose_text.error = null
        magnet_edit.error = null
        name_edit.error = null
        (categorySpin.selectedView as TextView).error =null

        if (name_edit.text.isEmpty()) {
            name_edit.error = getString(R.string.name_required)
            return false
        }

        if (c == "") {
            (categorySpin.selectedView as TextView).error = getString(R.string.category_required)
            return false
        }

        if (selectedTorrent == null && magnet_edit.text.isEmpty()) {
            choose_text.error = getString(R.string.file_required)
            magnet_edit.error = getString(R.string.file_required)
            return false
        }

        if (lang == "") {
            toast(getString(R.string.lang_next_time))
        }

        return true
    }

    fun upload(context: Context) {
        try {
            errorText.visibility = View.GONE
            val notificationConfig = UploadNotificationConfig()
                    .setTitle(getString(R.string.nyaapantsu))
                    .setInProgressMessage(getString(R.string.upload_progress))
                    .setErrorMessage(getString(R.string.upload_error))
                    .setCompletedMessage(getString(R.string.upload_done))
            MultipartUploadRequest(context, (FuelManager.instance.basePath+"/upload"))
                    // starting from 3.1+, you can also use content:// URI string instead of absolute file
                    .addFileToUpload(selectedTorrent?.absolutePath, "torrent")
                    .setUtf8Charset()
                    .addHeader("Authorization", User.token)
                    .addParameter("username", User.name)
                    .addParameter("name", name_edit.text.toString())
                    .addParameter("magnet", magnet_edit.text.toString())
                    .addParameter("c", c)
                    .addParameter("remake", remakeSwitch.isChecked.toString())
                    .addParameter("hidden", anonSwitch.isChecked.toString())
                    .addArrayParameter("language", lang.split(";").toMutableList())
                    .addParameter("website_link", website_edit.text.toString())
                    .addParameter("desc", desc_edit.text.toString())
                    .setNotificationConfig(notificationConfig)
                    .setMaxRetries(2)
                    .setDelegate(object : UploadStatusDelegate {
                        override fun onProgress(context: Context, uploadInfo: UploadInfo) {
                            Log.d("DebugUpload", uploadInfo.toString())
                        }

                        override fun onError(context: Context, uploadInfo: UploadInfo, exception: Exception) {
                            toast(getString(R.string.try_error))
                        }

                        override fun onCompleted(context: Context, uploadInfo: UploadInfo, serverResponse: ServerResponse) {
                            // your code here
                            // if you have mapped your server response to a POJO, you can easily get it:
                            // YourClass obj = new Gson().fromJson(serverResponse.getBodyAsString(), YourClass.class);
                            val json = JSONObject(serverResponse.bodyAsString)
                            if (json.getBoolean("ok")) {
                                startActivity<TorrentActivity>("torrent" to json.getJSONObject("data").toString(), "type" to "upload")
                            } else {
                                val allErrors = json.optJSONObject("all_errors")
                                val errors = allErrors?.optJSONArray("errors")
                                if (errors != null) {
                                    errorText.text = errors.join("\n")
                                }

                                name_edit.error = if (allErrors?.optString("name") != "") allErrors?.optString("name") else null
                                (categorySpin.selectedView as TextView).error = if (allErrors?.optString("category") != "") allErrors?.optString("category") else null
                                langLabel.error = if (allErrors?.optString("language") != "") allErrors?.optString("language") else null
                                website_edit.error = if (allErrors?.optString("website") != "") allErrors?.optString("website") else null
                                errorText.visibility = View.VISIBLE
                            }
                        }

                        override fun onCancelled(context: Context, uploadInfo: UploadInfo) {
                            // your code here
                        }
                    })
                    .startUpload()
        } catch (exc: Exception) {
            Log.e("AndroidUploadService", exc.message, exc)
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            // Use the provided utility method to parse the result
            val files = Utils.getSelectedFilesFromResult(intent!!)
            for (uri in files) {
                val file = Utils.getFileForUri(uri)
                if (name_edit.text.toString() == "") {
                    name_edit.setText(file.name)
                }
                choose_text.text = file.nameWithoutExtension
                name_edit.setText(file.nameWithoutExtension)
                selectedTorrent = file
            }
        }
    }

    fun getTorrentName(torrent: String): String {
        val torrentExp = torrent.split(".")
        if (torrentExp.size > 1) {
            torrentExp.dropLast(torrentExp.size-1)
        }
        return torrentExp.joinToString(".")
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    val FILE_CODE = 13

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @return A new instance of fragment UploadFragment.
         */
        fun newInstance(): UploadFragment {
            val fragment = UploadFragment()
            return fragment
        }
    }
}// Required empty public constructor
