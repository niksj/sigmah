package org.activityinfo.client.page.welcome;

import org.activityinfo.client.Place;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageLoader;
import org.activityinfo.client.page.PagePresenter;

import com.google.gwt.user.client.rpc.AsyncCallback;
/*
 * @author Alex Bertram
 */

public class StaticPageLoader implements PageLoader {

    public void load(PageId pageId, Place initialPlaceHint, AsyncCallback<PagePresenter> callback) {

        StaticPage staticPage = new StaticPage();
        staticPage.navigate((StaticPlace) initialPlaceHint);
        callback.onSuccess(staticPage);
    }
}
