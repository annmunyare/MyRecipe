package com.example.nyambura.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nyambura on 4/12/17.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private Context context;


    public RecipeListAdapter(ArrayList<Recipe> mRecipes, Context context) {
        this.mRecipes = mRecipes;
        this.context = context;



    }

    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(view);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.RecipeViewHolder holder, final int position){


        holder.bindRecipes(mRecipes.get(position));
    }

    @Override
    public int getItemCount(){
        return mRecipes.size();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.foodImageView)
        ImageView mFoodImage;
        @Bind(R.id.foodTitle)
        TextView mFoodTitle;
        private Context mContext;
        private Recipe currentRecipe;
        public RecipeViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);


        }
        public void bindRecipes(Recipe recipe){
            Picasso.with(mContext).load(recipe.getImage()).into(mFoodImage);
            mFoodTitle.setText(recipe.getLabel());
        }

        @Override
        public void onClick(View v) {

            int itemPosition = getLayoutPosition();


            Intent intent = new Intent(mContext, RecipeDetailActivity.class);
            currentRecipe = mRecipes.get(itemPosition);
            intent.putExtra("recipe", Parcels.wrap(currentRecipe));
            mContext.startActivity(intent);



        }


    }



}

