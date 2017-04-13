package com.example.nyambura.myrecipe;

import android.content.Context;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by nyambura on 4/12/17.
 */

public class FirebaseRecipeListAdapter extends FirebaseRecyclerAdapter<Recipe, FirebaseRecipeViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private Context mContext;
    private OnStartDragListener mOnStartDragListener;


    public FirebaseRecipeListAdapter(Class<Recipe> modelClass , int modelLayout, Class<FirebaseRecipeViewHolder> viewHolderClass, Query ref, OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout, viewHolderClass, ref);

        mRef = ref.getRef();
        mOnStartDragListener = onStartDragListener;
        mContext = context;











    }

    @Override
    protected void populateViewHolder(FirebaseRecipeViewHolder viewHolder, Recipe model, int position) {
        viewHolder.bindRecipe(model);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

}

