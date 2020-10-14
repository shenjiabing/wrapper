package com.sayweee.wrapper.widget;

import android.app.Dialog;
import android.content.Context;

import com.sayweee.wrapper.R;
import com.sayweee.wrapper.base.view.ViewHelper;
import com.sayweee.wrapper.base.view.WrapperDialog;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/27.
 * Desc:
 */
public class LoadingDialog extends WrapperDialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void setDialogParams(Dialog dialog) {

    }

    @Override
    public void help(ViewHelper helper) {

    }
}
