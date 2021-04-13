package com.becker.nolan.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.becker.nolan.recipeapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val recipeList = ArrayList<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.recipeListView.adapter = RecipeAdapter(recipeList)
        binding.recipeListView.layoutManager = LinearLayoutManager(this)
        binding.recipeListView.setHasFixedSize(true)

        getRecipes()

    }

    private fun getRecipes() {
        recipeList.clear()

        AndroidNetworking.initialize(this)
        AndroidNetworking.get("https://api.nolanbecker.net/api/recipe/recipes/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(object: JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray?) {
                        val gson = Gson()
                        if (response != null) {
                            for (i in 0 until response.length()) {
                                val recipeObject = response.getJSONObject(i).toString()
                                val recipe: Recipe = gson.fromJson(recipeObject, Recipe::class.java)
                                recipeList.add(recipe)
                            }
                            binding.recipeListView.adapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onError(anError: ANError?) {
                        if (anError != null) {
                            anError.message?.let {
                                Log.e("getRecipes", it)
                            }
                        }
                    }
                })
    }

}