package com.example.nyambura.myrecipe;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.detailTitle)
    TextView mDetailTitle;
    @Bind(R.id.linkTextView) TextView mLinkTextView;
    @Bind(R.id.caloriesTextView) TextView mCaloriesTextView;
    @Bind(R.id.servingsTextView) TextView mServingsTextView;
    @Bind(R.id.ingredientListView)
    ListView mIngredientListView;
    @Bind(R.id.detailImage)
    ImageView mDetailImage;
    private String url;
    private Recipe mRecipe;

    //variable up here so i can change icon when favorited
    private MenuItem menuItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        //unwraps parcel, set to an object so I can set the layout details
        Intent intent = getIntent();
        Recipe clickedRecipe = Parcels.unwrap(intent.getParcelableExtra("recipe"));
        String title = clickedRecipe.getLabel();
        String image = clickedRecipe.getImage();
        url = clickedRecipe.getUrl();
        Integer yield = clickedRecipe.getYield();
        Integer calories = clickedRecipe.getCalories();
        ArrayList<String> ingredients = clickedRecipe.getIngredientLines();



        //calculates an estimated Calories Per person rounded
        int roundedCalories = calories/yield;

        //sends information to the layout
        mDetailTitle.setText(title);
        mCaloriesTextView.setText(roundedCalories + " calories per person");
        mServingsTextView.setText("Serves " + yield);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredients);
        mIngredientListView.setAdapter(adapter);
        Picasso.with(this).load(image).into(mDetailImage);


        //create the Recipe
        mRecipe = new Recipe(title,image,url,ingredients,calories,yield);

        mLinkTextView.setOnClickListener(this);
    }



    //I think this can all stay on this page as it has to do with the menu
    //creates menu at top
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favorite,menu);
        menuItem = menu.findItem(R.id.action_favorite);
        return true;
    }

    //add actions that will happen in the bar (i.e. favorite and logout(which i will put in later);
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_favorite:
                menuItem.setIcon(R.drawable.ic_favorite_black_24dp);
                saveToFavorites();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onClick(View v){

        if(v == mLinkTextView){
            Intent externalLink = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(externalLink);
        }


    }


    public void saveToFavorites(){

        //will get current user to associate favorite recipes to their account only
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //states the child of recipes: userId
        DatabaseReference favoriteRef = FirebaseDatabase.
                getInstance()
                .getReference(Constants.FIREBASE_CHILD_RECIPES)
                .child(uid);

        DatabaseReference pushRef = favoriteRef.push();
        String pushId = pushRef.getKey();
        mRecipe.setPushId(pushId);
        pushRef.setValue(mRecipe);
        Toast.makeText(RecipeDetailActivity.this, "Favorited", Toast.LENGTH_SHORT).show();


    }
}
