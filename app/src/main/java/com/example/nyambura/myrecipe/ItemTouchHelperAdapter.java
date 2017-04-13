package com.example.nyambura.myrecipe;

/**
 * Created by nyambura on 4/13/17.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
