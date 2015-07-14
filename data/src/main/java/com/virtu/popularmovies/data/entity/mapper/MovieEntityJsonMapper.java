package com.virtu.popularmovies.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.virtu.popularmovies.data.entity.MovieEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by virtu on 09/07/2015.
 */
public class MovieEntityJsonMapper {

    private final Gson gson;


    @Inject
    public MovieEntityJsonMapper(){
        this.gson = new Gson();
    }

    /**
     *
     * @param movieJsonResponse
     * @return {@link MovieEntity}
     * @throws JsonSyntaxException if the json hans´t got a valid structure
     */
    public MovieEntity transformMovieEntity(String movieJsonResponse) throws JsonSyntaxException{
        try{
            Type movieEntityType = new TypeToken<MovieEntity>() {}.getType();
            MovieEntity movieEntity = this.gson.fromJson(movieJsonResponse,movieEntityType);

            return movieEntity;
        }catch (JsonSyntaxException jsonException){
            throw jsonException;
        }
    }

    /**
     * Transform a Json List in a List of {@link MovieEntity}
     * @param movieListJsonResponse
     * @return List of {@link MovieEntity}
     * @throws JsonSyntaxException,JSONException if the json hans´t got a valid structure
     */
    public List<MovieEntity> transformMovieEntityCollection(String movieListJsonResponse)
            throws JsonSyntaxException, JSONException {

        List<MovieEntity> movieEntityList;
        try{
            JSONObject template = new JSONObject(movieListJsonResponse);

            JSONArray jsonArray = template.getJSONArray("results");
            Type listMovieEntityType = new TypeToken<List<MovieEntity>>() {}.getType();
            movieEntityList = this.gson.fromJson(jsonArray.toString(),listMovieEntityType);
            return movieEntityList;
        }catch (JsonSyntaxException jsonSyntaxException){
            throw jsonSyntaxException;
        }

    }
}
