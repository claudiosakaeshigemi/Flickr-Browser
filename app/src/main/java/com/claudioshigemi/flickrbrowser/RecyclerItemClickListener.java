package com.claudioshigemi.flickrbrowser;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Claudio on 10/12/2017.
 */

class RecyclerItemClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerItemClickListen";

    interface OnRecyclerClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private final OnRecyclerClickListener mListener;
    private final GestureDetectorCompat mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnRecyclerClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: COMEÇOU. ");
                View childview = recyclerView.findChildViewUnder(e.getX(),e.getY() );
                if (childview != null && mListener != null){
                    mListener.onItemClick(childview, recyclerView.getChildAdapterPosition(childview ) );
                }
                return  true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: COMEÇOU");
                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY() );
                if (childView != null && mListener != null ){
                    Log.d(TAG, "onLongPress: CHAMANDO listener.onItemLongClick ");
                    mListener.onItemLongClick( childView, recyclerView.getChildAdapterPosition(childView ));
                }
                
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if(mGestureDetector != null) {
            boolean result = mGestureDetector.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent(): returned: " + result);
            return result;
        } else {
            Log.d(TAG, "onInterceptTouchEvent(): returned: false");
            return false;
        }
    }
}









































