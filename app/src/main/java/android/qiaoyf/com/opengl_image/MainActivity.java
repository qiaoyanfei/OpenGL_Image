package android.qiaoyf.com.opengl_image;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


public class MainActivity extends FragmentActivity {

    private RefWatcher mRefWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRefWatcher = LeakCanary.install(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ViewPager) findViewById(R.id.main_pager)).setAdapter(new MainPagerAdapter(this));
    }

    RefWatcher getRefWatcher() {
        return mRefWatcher;
    }

}
