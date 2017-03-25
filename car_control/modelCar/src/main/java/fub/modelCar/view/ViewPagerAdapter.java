package fub.modelCar.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

/**
 * Created by hp1 on 21.01.2015.
 * Modified by Daniel Neumann on 29.03.16.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final int NumbOfTabs = 1; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
            return new ControlFragment();
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        Fragment fragment = getItem(position);

        if ((obj != null && fragment != null) && !(obj.getClass().getSimpleName().equals(fragment.getClass().getSimpleName()))) {
            destroyItem(container, position, obj);
            return super.instantiateItem(container, position);
        } else {
            return obj;
        }
    }
}