package com.dario.health

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.cheezycode.randomquote.api.DarioHealthServiceAPI
import com.cheezycode.randomquote.api.RetrofitHelper
import com.dario.health.database.FavoritesMovieListDatabase
import com.dario.health.repository.DarioHealthMovieRepository
import com.google.android.material.snackbar.Snackbar

class DarioHealthApplication : Application() {
    companion object {
        var app: DarioHealthApplication? = null
        private var sweetAlertDialog: SweetAlertDialog? = null
    }

    override fun onCreate() {
        super.onCreate()
        app = this;

    }


    fun showProgressDialog(mContext: Context, title: String, message: String) {
        if (mContext == null || (mContext as Activity).isFinishing) {
            return
        }
        if (sweetAlertDialog == null) {
            sweetAlertDialog = SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            if (sweetAlertDialog == null) {
                return;
            } else {
                sweetAlertDialog!!.getProgressHelper()
                    .setBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
                sweetAlertDialog!!.setTitleText(title);
                sweetAlertDialog!!.setContentText(message);
                sweetAlertDialog!!.setCancelable(false);
                sweetAlertDialog!!.setCanceledOnTouchOutside(false);
                sweetAlertDialog!!.show();
            }

        } else {
            sweetAlertDialog!!.show();
        }
    }

    fun showAlertDialogWithOk(
        mContext: Context,
        title: String,
        message: String,
        confirmText: String,
        confirmBGColor: Int,
        confirmTextColor: Int,
        alertType: Int,
        onSweetConfirmClickListener: SweetAlertDialog.OnSweetClickListener
    ) {

        if (mContext == null || (mContext as Activity) == null || (mContext as Activity).isFinishing()) {
            return;
        }
        if (sweetAlertDialog == null) {
            sweetAlertDialog = SweetAlertDialog(mContext, alertType);
            if (sweetAlertDialog == null) {
                return;
            } else {
                sweetAlertDialog!!.setTitleText(title);
                sweetAlertDialog!!.setContentText(message);
                sweetAlertDialog!!.setCancelable(false);
                sweetAlertDialog!!.setCanceledOnTouchOutside(false);
                sweetAlertDialog!!.setConfirmText(confirmText);
                sweetAlertDialog!!.setConfirmClickListener(onSweetConfirmClickListener);
                sweetAlertDialog!!.setConfirmButtonTextColor(
                    ContextCompat.getColor(
                        mContext,
                        confirmTextColor
                    )
                );
                sweetAlertDialog!!.setConfirmButtonBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        confirmBGColor
                    )
                );
                sweetAlertDialog!!.show();


            }

        } else {
            sweetAlertDialog!!.show();
        }

    }

    fun showAlertDialogWithOkCancel(
        mContext: Context,
        title: String,
        message: String,
        confirmText: String,
        cancelText: String,
        confirmBGColor: Int,
        cancelBGColor: Int,
        alertType: Int,
        onSweetConfirmClickListener: SweetAlertDialog.OnSweetClickListener,
        onSweetCancelClickListener: SweetAlertDialog.OnSweetClickListener
    ) {

        if (mContext == null || (mContext as Activity) == null || (mContext as Activity).isFinishing()) {
            return;
        }
        if (sweetAlertDialog == null) {
            sweetAlertDialog = SweetAlertDialog(mContext, alertType);
            if (sweetAlertDialog == null) {
                return;
            } else {
                sweetAlertDialog!!.setTitleText(title);
                sweetAlertDialog!!.setContentText(message);
                sweetAlertDialog!!.setCancelable(false);
                sweetAlertDialog!!.setCanceledOnTouchOutside(false);
                sweetAlertDialog!!.setConfirmText(confirmText);
                sweetAlertDialog!!.setCancelText(cancelText);
                sweetAlertDialog!!.setConfirmClickListener(onSweetConfirmClickListener);
                sweetAlertDialog!!.setCancelClickListener(onSweetCancelClickListener);
                sweetAlertDialog!!.setConfirmButtonBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        confirmBGColor
                    )
                );
                sweetAlertDialog!!.setCancelButtonBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        cancelBGColor
                    )
                );
                sweetAlertDialog!!.show();
            }

        } else {
            sweetAlertDialog!!.show();
        }

    }

    fun dismissProgressDialog() {
        if (sweetAlertDialog != null && sweetAlertDialog!!.isShowing) {
            sweetAlertDialog!!.dismissWithAnimation()
            sweetAlertDialog = null
        }
    }


    fun showSnackbar(context: Context?, view: View?, message: String?, color: ColorType) {
        if (context == null || context as Activity? == null || context.isFinishing) {
            return
        }
        val snackbar = Snackbar
            .make(view!!, message!!, Snackbar.LENGTH_LONG)
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, getColor(color)))
        val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
        tv.setTextColor(Color.WHITE)
        snackbar.show()
    }

    private fun getColor(color: ColorType): Int {
        var color_code = 0
        color_code = when (color) {
            ColorType.SUCCESS -> R.color.green
            ColorType.ERROR -> R.color.tomato_red
            ColorType.UPDATE -> R.color.green
            ColorType.WARNING -> R.color.orange
        }
        return color_code
    }

    enum class ColorType {
        SUCCESS, ERROR, UPDATE, WARNING
    }


    fun hideSoftKeyboard(mActivity: Activity?) {
        if (mActivity == null || mActivity.isFinishing) {
            return
        }
        mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun hideKeyboardFromActivity(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}