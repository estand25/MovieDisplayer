package com.example.andriod.popularmoviev2.adapter;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.andriod.popularmoviev2.activity.DetailMovieFragment;
import com.example.andriod.popularmoviev2.activity.DetailTrailerFragment;
import com.example.andriod.popularmoviev2.data.MovieContract;

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
    Uri mUri;
    /**
     * Constructions the PageAdapter class
     * @param fm - FragmentManager for the tab layout
     * @param NumOfTabs - Number of tabs in tab layout
     */
    public PageAdapter(FragmentManager fm, int NumOfTabs, Uri uri){
        super(fm);

        // Set the class variable to the passed in parmeters
        this.mNumOftabs = NumOfTabs;
        this.mUri = uri;
    }

    /**
     * Returns a fragement at the position location in the tablayout
     * @param position - fragment location in the tab
     * @return - returns fragment
     */
    @Override
    public Fragment getItem(int position){
        // Create a new instance of Bundle
        Bundle arguments = new Bundle();

        // Switch to different fragment based on pasted in position
        // from the Detail Activity or Main Activity depending on App Layout (one penal or two Penals)
        switch (position){
            case 0:
                // Get the Uri value in the Bundle based on the String value identify
                arguments.putParcelable(DetailMovieFragment.getMOVIE_DETAIL_URI(), mUri);

                // Create a new instance of a fragment
                DetailMovieFragment detailMovieFragment = new DetailMovieFragment();

                // Add an argument to the new fragment
                detailMovieFragment.setArguments(arguments);

                // Return the fragment to the TabLayout
                return detailMovieFragment;

            case 1:

                // Get the Uri value in the Bundle based on the String value identify
                /*arguments.putParcelable(DetailReviewFragment.getREVIEW_DETAILS_URI(), MovieContract.ReviewEntry.buildReviewMovieIDUri(Integer.parseInt(mUri.getPathSegments().get(1))));

                // Create a new instance of a fragment
                DetailReviewFragment detailReviewFragment = new DetailReviewFragment();

                // Add an argument to the new fragment
                detailReviewFragment.setArguments(arguments);

                // Return the fragment to the TabLayout
                return detailReviewFragment;

                arguments.putParcelable(DetailRFragment.getREVIEW_DETAILS_URI(), MovieContract.ReviewEntry.buildReviewMovieIDUri(Integer.parseInt(mUri.getPathSegments().get(1))));

                DetailRFragment detailRFragment = new DetailRFragment();

                detailRFragment.setArguments(arguments);

                return detailRFragment;*/

            case 2:
                // Populate the trailer table for this specific movie
                //movieSyncUploader.getTrailerInfor(Integer.parseInt(mUri.getPathSegments().get(1)));

                // Get the Uri value in the Bundle based on the String value identify
                arguments.putParcelable(DetailTrailerFragment.getTRAILER_DETAILS_URI(), MovieContract.TrailerEntry.buildTrailerMovieIDUri(Integer.parseInt(mUri.getPathSegments().get(1))));

                // Create a new instance of a fragment
                DetailTrailerFragment detailTrailerFragment = new DetailTrailerFragment();

                // Add an argument to the new fragment
                detailTrailerFragment.setArguments(arguments);

                // Return the fragment to the TabLayout
                return detailTrailerFragment;
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
