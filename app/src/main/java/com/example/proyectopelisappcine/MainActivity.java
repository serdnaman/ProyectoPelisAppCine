package com.example.proyectopelisappcine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.proyectopelisappcine.adapters.FilmAdapter;
import com.example.proyectopelisappcine.adapters.FilmsAdapter;
import com.example.proyectopelisappcine.databinding.ActivityMainBinding;
import com.example.proyectopelisappcine.model.Favorite;
import com.example.proyectopelisappcine.model.Film;
import com.example.proyectopelisappcine.repository.FavoriteRepository;
import com.example.proyectopelisappcine.repository.FilmRepository;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;
import com.example.proyectopelisappcine.ui.films.FilmDetailActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FilmAdapter.OnFilmClickListener, FilmsAdapter.ButtonListener {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private SharedPreferences preferences;
    FilmDatabase filmDatabase;
    private MainViewModel mainViewModel;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(Context.MODE_PRIVATE);

        filmDatabase = FilmDatabase.getInstance(MainActivity.this);

        UserRepository userRepository = new UserRepository(filmDatabase.userDAO());
        FilmRepository filmRepository = new FilmRepository(filmDatabase.filmDAO());
        FavoriteRepository favoriteRepository = new FavoriteRepository(filmDatabase.favoriteDAO());

        //Creación de una instancia de MainViewModel pasando los repositorios
        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(userRepository, filmRepository, favoriteRepository);
        mainViewModel = new ViewModelProvider(this, mainViewModelFactory).get(MainViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_perfil, R.id.nav_film_list, R.id.nav_film_list_favorites, R.id.nav_configuration).setOpenableLayout(drawer).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_settings == id) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onFilmClick(Film item) {
        Intent intent = new Intent(this, FilmDetailActivity.class);
        intent.putExtra("Pelicula", (Serializable) item);
        startActivity(intent);
    }

    @Override
    public void onButtonPressed(Film film, FilmsAdapter filmsAdapter) {
        UserFilms userFilms = UserFilms.getInstance();
        userFilms.userfilmsfavorites.remove(film.getId());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                FilmDatabase filmDatabase = FilmDatabase.getInstance(MainActivity.this);
                filmDatabase.favoriteDAO().DeleteFavorites(new Favorite(film.getId(), preferences.getString("Usuario", "")));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        filmsAdapter.swap(new ArrayList<>(userFilms.userfilmsfavorites.values()));
                    }
                });
            }
        });
    }

    public MainViewModel getMainViewModel() {
        return mainViewModel;
    }

    public NavController getNavController() {
        return navController;
    }

    public void NavegarDeInicioAPerfil() {
        navController.navigate(R.id.action_nav_inicio_to_nav_perfil);
    }

    public void NavegarDeInicioARegistro() {
        navController.navigate(R.id.action_nav_inicio_to_nav_registro);
    }

    public void NavegarDePerfilAModificar() {
        navController.navigate(R.id.action_nav_perfil_to_nav_modificacion_perfil);
    }

    public void NavegarDePerfilAInicio() {
        navController.navigate(R.id.action_nav_perfil_to_nav_inicio);
    }

    public void NavegarDeRegistroAPerfil() {
        navController.navigate(R.id.action_nav_registro_to_nav_perfil);
    }

    public void NavegarDeRegistroAInicio() {
        navController.navigate(R.id.action_nav_registro_to_nav_inicio);
    }

    public void NavegarDeModificacionAPerfil() {
        navController.navigate(R.id.action_nav_modificacion_perfil_to_nav_perfil);
    }
}