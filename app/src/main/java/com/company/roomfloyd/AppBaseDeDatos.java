package com.company.roomfloyd;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

/* https://developer.android.com/training/data-storage/room */

@Database(entities = {Album.class}, version = 1, exportSchema = false)
public abstract class AppBaseDeDatos extends RoomDatabase {

    public abstract AlbumsDao obtenerAlbumsDao();

    private static volatile AppBaseDeDatos db;

    public static AppBaseDeDatos getInstance(final Context context){
        if (db == null) {
            synchronized (AppBaseDeDatos.class) {
                if (db == null) {
                    db = Room.databaseBuilder(context, AppBaseDeDatos.class, "app.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return db;
    }

    @Dao
    public interface AlbumsDao {
        @Insert
        void insertarAlbum(Album album);

        @Query("SELECT * FROM Album")
        LiveData<List<Album>> obtenerAlbums();
    }
}