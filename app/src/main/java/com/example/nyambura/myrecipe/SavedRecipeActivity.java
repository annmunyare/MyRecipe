package com.example.nyambura.myrecipe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;

public class SavedRecipeActivity extends AppCompatActivity {
    @Bind(R.id.favoriteRecipesRecyclerView)
    RecyclerView mFavoriteRecipesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipe);
    }
}
