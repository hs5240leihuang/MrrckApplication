package com.meiku.dev.yunxin.recent;

import java.util.List;

import android.content.Context;

import com.meiku.dev.yunxin.TAdapter;
import com.meiku.dev.yunxin.TAdapterDelegate;
import com.netease.nimlib.sdk.msg.model.RecentContact;

/**
 * 最近联系人列表的adapter，管理了一个callback，新增红点拖拽控件
 */
public class RecentContactAdapter extends TAdapter<RecentContact> {

    private RecentContactsCallback callback;

    public RecentContactAdapter(Context context, List<RecentContact> items, TAdapterDelegate delegate) {
        super(context, items, delegate);
    }

    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }
}
