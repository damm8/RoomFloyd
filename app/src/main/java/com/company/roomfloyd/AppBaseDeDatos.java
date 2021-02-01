package com.company.roomfloyd;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* https://developer.android.com/training/data-storage/room */

@Database(entities = {Album.class}, version = 2, exportSchema = false)
public abstract class AppBaseDeDatos extends RoomDatabase {

    static Executor executor = Executors.newSingleThreadExecutor();

    public abstract AlbumsDao obtenerAlbumsDao();

    private static volatile AppBaseDeDatos db;

    public static AppBaseDeDatos getInstance(final Context context){
        if (db == null) {
            synchronized (AppBaseDeDatos.class) {
                if (db == null) {
                    db = Room.databaseBuilder(context, AppBaseDeDatos.class, "app.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    insertarDatosIniciales(getInstance(context).obtenerAlbumsDao());
                                }

                                @Override
                                public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                                    super.onDestructiveMigration(db);
                                    insertarDatosIniciales(getInstance(context).obtenerAlbumsDao());
                                }
                            })
                            .build();
                }
            }
        }

        return db;
    }

    private static void insertarDatosIniciales(AlbumsDao dao) {
        List<Album> albums = Arrays.asList(
                new Album("The Pipersss at the Gates of Dawn", "1967", "file:///android_asset/piper.jpg"),
                new Album("A Saucerful of Secrets","1968","file:///android_asset/secrets.jpg"),
                new Album("Ummagumma","1969","file:///android_asset/ummagumma.jpg"),
                new Album("Atom Heart Mother","1970","file:///android_asset/atom.jpg"),
                new Album("Meddle","1971","file:///android_asset/meddle.jpg"),
                new Album("Obscured by Clouds","1972","file:///android_asset/clouds.jpg"),
                new Album("The Dark Side of the Moon","1973","file:///android_asset/dark.jpg"),
                new Album("Wish You Were Here","1975","file:///android_asset/wish.jpg"),
                new Album("Animals","1977","file:///android_asset/animals.jpg"),
                new Album("The Wall","1979","file:///android_asset/wall.jpg"),
                new Album("A Collection of Great Dance Songs","1981","file:///android_asset/dance.jpg"),
                new Album("The Final Cut","1983","file:///android_asset/cut.jpg"),
                new Album("A Momentary Lapse of Reason","1987","file:///android_asset/lapse.jpg"),
                new Album("Delicate Sound of Thunder","1988","file:///android_asset/thunder.jpg"),
                new Album("Shine On","1992","file:///android_asset/shineon.jpg"),
                new Album("The Division Bell","1994","file:///android_asset/division.jpg"),
                new Album("Pulse","1995","file:///android_asset/pulse.jpg"),
                new Album("The Endless River","2014","file:///android_asset/river.jpg"),
                new Album("The Late Years","2019","file:///android_asset/late.jpg")
        );

        executor.execute(()-> {
            dao.insertarAlbums(albums);
        });
    }

    @Dao
    public interface AlbumsDao {
        @Insert
        void insertarAlbum(Album album);

        @Insert
        void insertarAlbums(List<Album> albums);

        @Query("SELECT * FROM Album")
        LiveData<List<Album>> obtenerAlbums();
    }
}