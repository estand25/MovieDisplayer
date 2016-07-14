package com.example.andriod.popularmoviev2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.andriod.popularmoviev2.activity.DetailMovieFragment;
import com.example.andriod.popularmoviev2.activity.DetailReviewFragment;
import com.example.andriod.popularmoviev2.activity.DetailTrailerFragment;

/**
 * Note: I'm using the example of mulitply tabs for the individual movie
 * details (General Movie Details, Movie Trailer, and Movie Review)
 * http://www.truiton.com/2015/06/android-tabs-example-fragments-viewpager/
 *
 * PageAdapter - Holder the handles the individual fragment state page location
 * Created by StandleyEugene on 7/14/2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOftabs;

    /**
     * Constructions the PageAdapter class
     * @param fm - FragmentManager for the tab layout
     * @param NumOfTabs - Number of tabs in tab layout
     */
    public PageAdapter(FragmentManager fm, int NumOfTabs){
        super(fm);
        this.mNumOftabs = NumOfTabs;
    }

    /**
     * Returns a fragement at the position location in the tablayout
     * @param position - fragment location in the tab
     * @return - returns fragment
     */
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                DetailMovieFragment detailMovieFragment = new DetailMovieFragment();
                return detailMovieFragment;
            case 1:
                DetailTrailerFragment detailTrailerFragment = new DetailTrailerFragment();
                return detailTrailerFragment;
            case 2:
                DetailReviewFragment detailReviewFragment = new DetailReviewFragment();
                return detailReviewFragment;
            default:
                return null;
        }
    }

    /**
     * Returns the location number of tabs
     * @return - returns int of tab count
     */
    @Override
    public int getCount(){
        return mNumOftabs;
    }
}
