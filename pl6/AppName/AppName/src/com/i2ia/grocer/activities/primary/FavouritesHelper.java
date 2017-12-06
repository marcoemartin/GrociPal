package com.i2ia.grocer.activities.primary;

import com.i2ia.grocer.fragments.ItemFragment;
import com.i2ia.grocer.fragments.StoreFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 
 * @author Daniel
 *
 */
public class FavouritesHelper extends FragmentPagerAdapter {
	
	public FavouritesHelper(FragmentManager fm) {
		super(fm);
	}
	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case 0:
            // Return food fragment
            return new ItemFragment();
        case 1:
            // Return store fragment
            return new StoreFragment(); 
		}
		return null;
    }

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 2;
	}

}
