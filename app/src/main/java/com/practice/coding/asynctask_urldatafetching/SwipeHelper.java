package com.practice.coding.asynctask_urldatafetching;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

public class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    public interface SwipeHelperListener {
        void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction, int position);
       // void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState);
    }

    private SwipeHelperListener listener;

    private MyAdapter adapter;

    public SwipeHelper(int dragDirs, int swipeDirs) {
        super(dragDirs, swipeDirs);
    }

    SwipeHelper(MyAdapter adapter, Context context) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT); //swipe direction
        this.listener = (SwipeHelperListener) context;

        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder targetViewHolder) {

        adapter.onItemDragUpOrDown(viewHolder.getAdapterPosition(), targetViewHolder.getAdapterPosition());
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /*
    getDefaultUIUtil() will be used by ItemTouchHelper to detect whenever there is UI change on the view. We use this function to keep the background view in a static position and move the foreground view.
     */

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View view = ((MyAdapter.VHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onSelected(view);

        }
    }

   /* //
    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((MyAdapter.VHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY,
                actionState, isCurrentlyActive);
    }*/

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((MyAdapter.VHolder) viewHolder).viewForeground;
        getDefaultUIUtil().clearView(foregroundView);

    }


    //  In onChildDrawOver() the x-position of the foreground view is changed while user is swiping the view.
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((MyAdapter.VHolder) viewHolder).viewForeground;
        getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }
}
