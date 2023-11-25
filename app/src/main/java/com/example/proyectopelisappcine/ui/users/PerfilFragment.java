package com.example.proyectopelisappcine.ui.users;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.UserFilms;
import com.example.proyectopelisappcine.databinding.FragmentPerfilBinding;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class PerfilFragment extends Fragment {
    private FragmentPerfilBinding binding;
    private TextView valueUsername;
    private TextView valuePassword;
    private TextView valueEmail;
    private Button botonEditar;
    private Button botonCerrarSesion;
    private Button botonEliminarCuenta;
    private SharedPreferences preferences;
    FilmDatabase filmDatabase;
    UserRepository userRepository;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        preferences = requireActivity().getPreferences(requireContext().MODE_PRIVATE);

        filmDatabase = FilmDatabase.getInstance(getContext());
        userRepository = UserRepository.getInstance(filmDatabase.userDAO());

        // Obtén el MainViewModel de la MainActivity
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        }

        valueUsername = root.findViewById(R.id.valueUsername);
        valuePassword = root.findViewById(R.id.valuePassword);
        valueEmail = root.findViewById(R.id.valueEmail);

        botonEditar = (Button) root.findViewById(R.id.editar_usuario);
        botonCerrarSesion = (Button) root.findViewById(R.id.cerrar_sesion);
        botonEliminarCuenta = (Button) root.findViewById(R.id.eliminar_cuenta);

        if (mainViewModel.getUser() != null) {
            mainViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    valueUsername.setText(user.getUsername());
                    valuePassword.setText(user.getPassword());
                    valueEmail.setText(user.getEmail());
                }
            });
        }

        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).NavegarDePerfilAModificar();
            }
        });

        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CerrarSesionUsuario(valueUsername.getText().toString());
                //((MainActivity) getActivity()).NavegarDePerfilAInicio();

                //mainViewModel.CerrarSesion();
                //((MainActivity) getActivity()).NavegarDePerfilAInicio();

                CerrarSesionUsuario();
            }
        });

        botonEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).NavegarDePerfilAInicio();
                EliminarUsuario();
            }
        });

        return root;
    }

    public void CerrarSesionUsuario() {
        //UserFilms userFilms = UserFilms.getInstance();
        //userFilms.userfilmsfavorites.clear();
        //mainViewModel.CerrarSesion(username);

        mainViewModel.CerrarSesion();
        ((MainActivity) getActivity()).NavegarDePerfilAInicio();
    }

    public void EliminarUsuario() {
        UserFilms userFilms = UserFilms.getInstance();
        userFilms.userfilmsfavorites.clear();

        String username = valueUsername.getText().toString();
        mainViewModel.DeleteUserByUsername(username);
    }
}
