package com.ikalogic.ika.exceptions;

import android.content.res.Resources;

import com.ikalogic.ika.Notify;

public class NotifyDefaultChannelInfoNotFoundException extends Resources.NotFoundException {
    public NotifyDefaultChannelInfoNotFoundException(){}
    @Override
    public String getMessage() {
        return "One or more of the next values is missing from string resources: " +
                Notify.ChannelData.ID+", " +
                Notify.ChannelData.NAME+" or " +
                Notify.ChannelData.DESCRIPTION;
    }
}
