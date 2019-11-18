package com.rd.pageindicatorview.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.rd.PageIndicatorView;
import com.rd.draw.data.Indicator;
import com.rd.pageindicatorview.base.BaseActivity;
import com.rd.pageindicatorview.customize.CustomizeActivity;
import com.rd.pageindicatorview.data.Customization;
import com.rd.pageindicatorview.sample.R;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseActivity {

    private PageIndicatorView pageIndicatorView;
    private Customization customization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_home);
        customization = new Customization();

        initToolbar();
        initViews();
        updateIndicator();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        boolean customization = requestCode == CustomizeActivity.EXTRAS_CUSTOMIZATION_REQUEST_CODE && resultCode == RESULT_OK;
        if (customization && intent != null) {
            this.customization = intent.getParcelableExtra(CustomizeActivity.EXTRAS_CUSTOMIZATION);
            updateIndicator();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customize, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionCustomize:
                CustomizeActivity.start(this, customization);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void initViews() {
        HomeAdapter adapter = new HomeAdapter();
        adapter.setData(createPageList());

        final ViewPager pager = findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        HomeAdapter2 adapter2 = new HomeAdapter2();
        adapter2.setData(createPageListInt());

        final ViewPager2 pager2 = findViewById(R.id.viewPager2);
        pager2.setAdapter(adapter2);

        pageIndicatorView = findViewById(R.id.pageIndicatorView);
    }

    private List<View> createPageList() {
        ArrayList<View> result = new ArrayList<>();
        for (int color : createPageListInt()) {
            result.add(createPageView(color));
        }
        return result;
    }

    @NonNull
    private List<Integer> createPageListInt() {
        List<Integer> pageList = new ArrayList<>();
        pageList.add(R.color.google_red);
        pageList.add(R.color.google_blue);
        pageList.add(R.color.google_yellow);
        pageList.add(R.color.google_green);
        pageList.add(R.color.black);
        pageList.add(R.color.green_600);
        pageList.add(R.color.gray_300);
        pageList.add(R.color.red_800);
        pageList.add(R.color.blue_100);
        pageList.add(R.color.gray_800);

        return pageList;
    }

    @NonNull
    private View createPageView(int color) {
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(color));

        return view;
    }

    private void updateIndicator() {
        if (customization == null) {
            return;
        }

        pageIndicatorView.setAnimationType(customization.getAnimationType());
        pageIndicatorView.setOrientation(customization.getOrientation());
        pageIndicatorView.setRtlMode(customization.getRtlMode());
        pageIndicatorView.setInteractiveAnimation(customization.isInteractiveAnimation());
        pageIndicatorView.setAutoVisibility(customization.isAutoVisibility());
        pageIndicatorView.setFadeOnIdle(customization.isFadeOnIdle());
        ViewPager viewPager = findViewById(R.id.viewPager);
        ViewPager2 viewPager2 = findViewById(R.id.viewPager2);
        if (customization.isUseViewPager2()) {
            viewPager.setVisibility(View.GONE);
            viewPager2.setVisibility(View.VISIBLE);
            pageIndicatorView.setViewPager(viewPager2);
        } else {
            viewPager.setVisibility(View.VISIBLE);
            viewPager2.setVisibility(View.GONE);
            pageIndicatorView.setViewPager(viewPager);
        }
        pageIndicatorView.setMaxDisplayedCount(customization.isShowLessIndicators() ? 5 : Indicator.COUNT_NONE);
    }
}
