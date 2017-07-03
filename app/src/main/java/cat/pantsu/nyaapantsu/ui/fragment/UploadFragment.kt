package cat.pantsu.nyaapantsu.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_upload.*
import org.jetbrains.anko.find
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.AdapterView
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.User
import cat.pantsu.nyaapantsu.ui.activity.ViewActivity
import com.github.kittinunf.fuel.core.FuelManager
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.Utils
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import java.io.File
import net.gotev.uploadservice.UploadNotificationConfig
import net.gotev.uploadservice.MultipartUploadRequest
import net.gotev.uploadservice.UploadInfo
import net.gotev.uploadservice.ServerResponse
import net.gotev.uploadservice.UploadStatusDelegate
import org.jetbrains.anko.startActivity
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UploadFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UploadFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null
    val categories = arrayOf("_", "3_", "3_12", "3_5", "3_13", "3_6", "2_", "2_3", "2_4", "4_", "4_7", "4_14", "4_8", "5_", "5_9", "5_10", "5_18", "5_11", "6_", "6_15", "6_16", "1_", "1_1", "1_2")
    val languages = arrayOf("", "multiple", "other", "ca-es", "zh-cn", "zh-tw", "nl-nl", "en-us", "fr-fr", "de-de", "hu-hu", "is-is", "it-it", "ja-jp", "ko-kr", "nb-no", "pt-br", "pt-pt", "ro-ro", "es-es", "es-mx", "sv-se", "th-th")

    var c = ""
    var lang = ""
    var selectedTorrent: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        val catAdapter = ArrayAdapter.createFromResource(activity, R.array.cat_array, R.layout.spinner_layout)
        categorySpin.adapter = catAdapter
        val langAdapter = ArrayAdapter.createFromResource(activity, R.array.language_array, R.layout.spinner_layout)
        languageSpin.adapter = langAdapter

        languageSpin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                lang = languages[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                lang = ""
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

        chooseButton.setOnClickListener { _ ->
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult<FilePickerActivity>(FILE_CODE, FilePickerActivity.EXTRA_ALLOW_MULTIPLE to false, FilePickerActivity.EXTRA_MODE to FilePickerActivity.MODE_FILE)
            } else {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 10)
            }
        }

        uploadButton.setOnClickListener { _ ->
            if (valideForm()) {
                upload(activity)
            }
        }
    }

    fun valideForm(): Boolean {
        torrentName.error=null
        (categorySpin.getSelectedView() as TextView).error =null
        torrentName.error = null

        if (torrentName.text.isEmpty()) {
            torrentName.error = getString(R.string.name_required)
            return false
        }

        if (c == "") {
            (categorySpin.getSelectedView() as TextView).error = getString(R.string.category_required)
            return false
        }

        if (selectedTorrent == null) {
            torrentName.error = getString(R.string.file_required)
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
                    .addParameter("name", nameUpload.text.toString())
                    .addParameter("c", c)
                    .addParameter("language", lang)
                    .addParameter("remake", remakeSwitch.isChecked.toString())
                    .addParameter("hidden", anonSwitch.isChecked.toString())
                    .addParameter("website_link", websiteUpload.text.toString())
                    .addParameter("desc", descriptionUpload.text.toString())
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
                            var json = JSONObject(serverResponse.bodyAsString)
                            if (json.getBoolean("ok")) {
                                startActivity<ViewActivity>("torrent" to json.getJSONObject("data").toString())
                            } else {
                                var allErrors = json.optJSONObject("all_errors")
                                var errors = allErrors?.optJSONArray("errors")
                                if (errors != null) {
                                    errorText.text = errors.join("\n")
                                }
                                nameUpload.error = if (allErrors?.optString("name") != "") allErrors?.optString("name") else null
                                (categorySpin.getSelectedView() as TextView).error = if (allErrors?.optString("category") != "") allErrors?.optString("category") else null
                                (languageSpin.getSelectedView() as TextView).error = if (allErrors?.optString("language") != "") allErrors?.optString("language") else null
                                websiteUpload.error = if (allErrors?.optString("website") != "") allErrors?.optString("website") else null
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
                if (torrentName.text.toString() == "") {
                    torrentName.text = file.name
                }
                nameUpload.setText(file.nameWithoutExtension)
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
