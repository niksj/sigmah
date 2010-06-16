package org.activityinfo.client.page.entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.dispatch.callback.Got;
import org.activityinfo.client.inject.AppInjector;
import org.activityinfo.client.page.*;
import org.activityinfo.client.page.common.nav.NavigationPanel;
import org.activityinfo.client.page.common.widget.VSplitFrameSet;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class DataEntryLoader implements PageLoader {

    private final AppInjector injector;

    @Inject
    public DataEntryLoader(AppInjector injector, NavigationHandler pageManager, PageStateSerializer placeSerializer) {
        this.injector = injector;

        pageManager.registerPageLoader(Frames.DataEntryFrameSet, this);
        pageManager.registerPageLoader(SiteEditor.ID, this);
        placeSerializer.registerParser(SiteEditor.ID, new SiteGridPageState.Parser());
    }

    @Override
    public void load(final PageId pageId, final PageState pageState, final AsyncCallback<Page> callback) {

        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {

                if (Frames.DataEntryFrameSet.equals(pageId)) {
                    loadFrame(pageState, callback);
                } else if (SiteEditor.ID.equals(pageId)) {
                    loadSiteGrid(pageState, callback);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                callback.onFailure(throwable);
            }
        });

    }

    private void loadFrame(PageState place, AsyncCallback<Page> callback) {

        NavigationPanel navPanel = new NavigationPanel(injector.getEventBus(),
                injector.getDataEntryNavigator());

        VSplitFrameSet frameSet = new VSplitFrameSet(Frames.DataEntryFrameSet, navPanel);

        callback.onSuccess(frameSet);
    }

    protected void loadSiteGrid(final PageState place, final AsyncCallback<Page> callback) {
        injector.getService().execute(new GetSchema(), null, new Got<SchemaDTO>() {
            @Override
            public void got(SchemaDTO schema) {

                SiteGridPageState sgPlace = (SiteGridPageState) place;
                if (sgPlace.getActivityId() == 0) {
                    sgPlace.setActivityId(schema.getFirstActivity().getId());
                }

                ActivityDTO activity = schema.getActivityById(sgPlace.getActivityId());

                SiteGridPage grid = new SiteGridPage(true);
                SiteEditor editor = new SiteEditor(injector.getEventBus(), injector.getService(),
                        injector.getStateManager(), grid);

                if (activity.getReportingFrequency() == ActivityDTO.REPORT_MONTHLY) {
                    MonthlyGrid monthlyGrid = new MonthlyGrid(activity);
                    MonthlyTab monthlyTab = new MonthlyTab(monthlyGrid);
                    MonthlyPresenter monthlyPresenter = new MonthlyPresenter(
                            injector.getEventBus(),
                            injector.getService(),
                            injector.getStateManager(),
                            activity, monthlyGrid);
                    editor.addSubComponent(monthlyPresenter);
                    grid.addSouthTab(monthlyTab);
                } else {

                    DetailsTab detailsTab = new DetailsTab();
                    DetailsPresenter detailsPresenter = new DetailsPresenter(
                            injector.getEventBus(),
                            activity,
                            injector.getMessages(),
                            detailsTab);
                    grid.addSouthTab(detailsTab);
                    editor.addSubComponent(detailsPresenter);
                }

                //  if(Maps.isLoaded()) {     load the maps api on render in SiteMap
                SiteMap map = new SiteMap(injector.getEventBus(), injector.getService(),
                        activity);

                editor.addSubComponent(map);
                grid.addSidePanel(Application.CONSTANTS.map(), Application.ICONS.map(), map);

                //  }
                editor.go((SiteGridPageState) place, activity);

                callback.onSuccess(editor);

            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

}