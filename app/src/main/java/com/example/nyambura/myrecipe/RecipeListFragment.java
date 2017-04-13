package com.example.nyambura.myrecipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;




public class RecipeListFragment extends Fragment {
    @Bind(R.id.recipesRecyclerView)
    RecyclerView mRecipesRecyclerView;
    private RecipeListAdapter mAdapter;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();

    //variables for the sharedpreferance search widget
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private String mRecentSearch;

    public RecipeListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mEditor = mSharedPreferences.edit();



        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);

        //was previously in on create part of recipelistactivity
        mRecentSearch = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_FOOD,null);

        if(mRecentSearch !=null){
            getRecipes(mRecentSearch);
        }


        return view;
    }

    //search menu create
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                addToSharedPreferences(query);

                getRecipes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    //This ensures that all functionality from the parent class will still apply despite us manually overriding portions of the menu's functionality.
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return super.onOptionsItemSelected(item);
    }

    public void getRecipes(String query){
        final RecipeService recipeService = new RecipeService();

        //searches for a type of recipe based on a query
        recipeService.findRecipe(query, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //calls on the service to pull information from the JSON set to an arraylist of recipes
                mRecipes = recipeService.proccessResults(response);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //injects the recipes into an adapter
                        mAdapter = new RecipeListAdapter(mRecipes, getActivity());
                        mRecipesRecyclerView.setAdapter(mAdapter);

                        //determines layout being used LANDSCAPE WILL WANT A DIFFERENT LAYOUT
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        mRecipesRecyclerView.setLayoutManager(gridLayoutManager);
                        mRecipesRecyclerView.setHasFixedSize(true);
                    }
                });

            }
        });


    }
    //write to shared preferance
    private void addToSharedPreferences(String searchTerm){
        mEditor.putString(Constants.PREFERENCES_SEARCH_FOOD, searchTerm).apply();
    }


}