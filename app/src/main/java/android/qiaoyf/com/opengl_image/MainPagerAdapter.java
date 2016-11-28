package android.qiaoyf.com.opengl_image;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

class MainPagerAdapter extends FragmentStatePagerAdapter {
	private final String[] mPageTitles;

	MainPagerAdapter(FragmentActivity act) {
		super(act.getSupportFragmentManager());
		mPageTitles = act.getResources().getStringArray(R.array.pages);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new JavaOpenGLFragment();
			case 1:
				return new NativeOpenGLFragment();
			default:
				throw new IndexOutOfBoundsException("Invalid page index");
		}
	}

	@Override
	public int getCount() {
		return mPageTitles.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mPageTitles[position];
	}
}
