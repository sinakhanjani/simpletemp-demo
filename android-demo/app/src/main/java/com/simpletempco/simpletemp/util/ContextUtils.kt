package com.simpletempco.simpletemp.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.simpletempco.simpletemp.R
import com.simpletempco.simpletemp.ui.custom.dialogs.MessageDialog
import com.simpletempco.simpletemp.ui.pages.common.web.WebViewActivity


object ContextUtils {

    /*fun <T> ComponentActivity.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }

    fun <T> Fragment.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collectLatest(collect)
            }
        }
    }*/

    fun EditText.value(): String = text.toString()

    fun TextView.value(): String = text.toString()

    fun TextInputLayout.value(): String = editText?.value() ?: ""

    fun Context.showMessageDialog(
        title: String? = getString(R.string.oops),
        message: String? = null,
        positiveButtonText: String? = getString(R.string.ok),
        negativeButtonText: String? = null,
        cancelable: Boolean = true,
        onPositiveButtonClick: (() -> Unit)? = null,
        onNegativeButtonClick: (() -> Unit)? = null,
        isForce: Boolean = false,
        messageSpannable: SpannableStringBuilder? = null,
    ) {
        MessageDialog.Builder()
            .title(title)
            .message(message)
            .messageSpannable(messageSpannable)
            .force(isForce)
            .positiveButtonText(positiveButtonText)
            .negativeButtonText(negativeButtonText)
            .build(this, object : MessageDialogCallback {
                override fun onDone() {
                    onPositiveButtonClick?.invoke()
                }

                override fun onCancel() {
                    onNegativeButtonClick?.invoke()
                }
            }).apply {
                setCancelable(cancelable)
                setCanceledOnTouchOutside(cancelable)
                show()
            }
    }

    fun Context.showMultiChoiceItemsDialog(
        title: String?,
        items: Array<String>,
        currentSelect: String?,
        positiveButtonText: String? = "Done",
        negativeButtonText: String? = "Cancel",
        onPositiveButtonClick: ((selectedItems: List<String>) -> Unit)? = null,
    ) {

        val selectedItems =
            items.map { item -> currentSelect?.contains(item) == true }.toMutableList()

        MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme).apply {
            title?.let { setTitle(title) }

            setMultiChoiceItems(
                items,
                selectedItems.toBooleanArray()
            ) { _, which, isChecked ->
                selectedItems[which] = isChecked
            }

            positiveButtonText?.let { text ->
                setPositiveButton(text) { dialog, _ ->
                    onPositiveButtonClick?.invoke(items.filterIndexed { index, _ -> selectedItems[index] })
                    dialog.dismiss()
                }
            }

            negativeButtonText?.let { text ->
                setNegativeButton(text) { dialog, _ ->
                    dialog.dismiss()
                }
            }

            show()
        }
    }

    fun Context.showSingleChoiceItemsDialog(
        title: String?,
        items: Array<String>,
        currentSelect: String?,
        onSelectItemCallback: ((selectedItem: String) -> Unit)? = null,
    ) {

        val currentSelectedItem = items.indexOf(currentSelect)

        MaterialAlertDialogBuilder(this).apply {
            title?.let { setTitle(title) }

            setSingleChoiceItems(
                ArrayAdapter(this.context, R.layout.item_single_choice, items),
                currentSelectedItem
            ) { dialog, which ->
                onSelectItemCallback?.invoke(items[which])
                dialog.dismiss()
            }

            show()
        }
    }

    fun Fragment.hasStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun Fragment.getStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //request for the permission
            requireContext().showMessageDialog(
                requireContext().getString(R.string.permission),
                requireContext().getString(R.string.storage_permission_msg),
                requireContext().getString(R.string.permission),
                requireContext().getString(R.string.cancel),
                true,
                {
                    val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                    val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    requireActivity().startActivity(intent)
                }
            )
        } else {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                0
            )
        }
    }

    fun Fragment.navigateToPrivacyPolicy() {
        navigateToWebView(AppConfig.PRIVACY_POLICY_URL)
    }

    fun Fragment.navigateToTermsOfServices() {
        navigateToWebView(AppConfig.TERMS_OF_SERVICE_URL)
    }

    fun Fragment.navigateToWebView(url: String) {
        val intent = Intent(requireActivity(), WebViewActivity::class.java).apply {
            putExtra("url", url)
        }
        activity?.startActivity(intent)
        activity?.overridePendingTransition(R.anim.page_enter, R.anim.page_exit)
    }

    // check internet connection
    @SuppressLint("MissingPermission")
    fun Context.isInternetConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun Context.getResColor(colorId: Int) = ResourcesCompat.getColor(resources, colorId, null)

    fun Context.getResDrawable(drawableId: Int) =
        ResourcesCompat.getDrawable(resources, drawableId, null)

    fun Fragment.checkLocationPermissions(): Boolean {
        return arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun Fragment.isLocationEnable(): Boolean {
        return (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
            .isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

}