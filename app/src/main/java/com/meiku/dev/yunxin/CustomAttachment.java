package com.meiku.dev.yunxin;

import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;

public abstract class CustomAttachment implements MsgAttachment {

    protected int type;

    CustomAttachment(int type) {
        this.type = type;
    }

    public void fromJson(String jsonStr) {
        if (jsonStr != null) {
            parseData(jsonStr);
        }
    }

    @Override
    public String toJson(boolean send) {
        return CustomAttachParser.packData(type, packData());
    }

    public int getType() {
        return type;
    }

    protected abstract void parseData(String data);
    protected abstract String packData();
}
