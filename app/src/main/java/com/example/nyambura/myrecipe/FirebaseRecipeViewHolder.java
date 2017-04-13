package com.example.nyambura.myrecipe;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by nyambura on 4/12/17.
 */
public class FirebaseRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mView;
    Context mContext;

    public FirebaseRecipeViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindRecipe(Recipe recipe){
        ImageView recipeImage = (ImageView) mView.findViewById(R.id.foodImageView);
        TextView recipeTitle = (TextView) mView.findViewById(R.id.foodTitle);

        Picasso.with(mContext).load(recipe.getImage()).into(recipeImage);
        recipeTitle.setText(recipe.getLabel());
    }


    @Override
    public void onClick(View v){

        final ArrayList<Recipe> recipes = new ArrayList<Recipe>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();



        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RECIPES).child(uid);
        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    recipes.add(snapshot.getValue(Recipe.class));

                }

                int itemPosition = getLayoutPosition();


                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                Recipe recipeToSend = recipes.get(itemPosition);
                intent.putExtra("recipe", Parcels.wrap(recipeToSend));

                mContext.startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

