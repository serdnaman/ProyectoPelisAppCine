package com.example.proyectopelisappcine.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.databinding.FragmentInicioBinding;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class InicioFragment extends Fragment {
    private EditText user;
    private EditText password;
    private Button botonInicio;
    private Button botonRegistrar;
    private FragmentInicioBinding binding;
    FilmDatabase filmDatabase;
    UserRepository userRepository;
    private MainViewModel mainViewModel;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
            navController = ((MainActivity) getActivity()).getNavController();
        }

        //se hace con el objetivo de que el usuario creado se cargue de inmediato en los campos y que no se tengan que escribir
        if (mainViewModel.getUser() != null) {
            mainViewModel.getUser().observe(getViewLifecycleOwner(), usuario -> {
                if (usuario != null) {
                    String username = usuario.getUsername();
                    String passwd = usuario.getPassword();
                    user.setText(username);
                    password.setText(passwd);
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        filmDatabase = FilmDatabase.getInstance(getContext());
        userRepository = UserRepository.getInstance(filmDatabase.userDAO());

        user = root.findViewById(R.id.insertUser);
        password = root.findViewById(R.id.insertPassword);
        botonInicio = root.findViewById(R.id.botonInicio);
        botonRegistrar = root.findViewById(R.id.botonRegistrar);

        botonInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String username = user.getText().toString();
               String passwd = password.getText().toString();
               mainViewModel.AutenticarUsuario(username, passwd);
               mainViewModel.setUser(userRepository.getUser(username));
               mainViewModel.getStateAuthentication().observe(getActivity(), result -> {
                    if (result) {
                        ((MainActivity) getActivity()).NavegarDeInicioAPerfil();
                    }
               });
            }
        });

        botonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).NavegarDeInicioARegistro();
            }
        });

        return root;
    }
}