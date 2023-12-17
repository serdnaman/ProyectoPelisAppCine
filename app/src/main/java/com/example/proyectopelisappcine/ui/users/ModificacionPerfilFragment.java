package com.example.proyectopelisappcine.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.databinding.FragmentModificarPerfilBinding;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class ModificacionPerfilFragment extends Fragment {
    private FragmentModificarPerfilBinding binding;
    private EditText editarUsername;
    private EditText editarPassword;
    private EditText editarEmail;
    private Button editar;
    FilmDatabase filmDatabase;
    UserRepository userRepository;
    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentModificarPerfilBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        filmDatabase = FilmDatabase.getInstance(getContext());
        userRepository = UserRepository.getInstance(filmDatabase.userDAO());

        // Obtén el MainViewModel de la MainActivity
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        }

        editarUsername = binding.modifyUsername;
        editarPassword = binding.modifyPassword;
        editarEmail = binding.modifyEmail;
        editar = binding.botonEdicion;

        mainViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                editarUsername.setText(user.getUsername());
                editarPassword.setText(user.getPassword());
                editarEmail.setText(user.getEmail());
            }
        });

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar.setEnabled(false);
                ModificarUsuarioEnBaseDeDatos();
                ((MainActivity) getActivity()).NavegarDeModificacionAPerfil();
            }
        });

        return root;
    }

    public void ModificarUsuarioEnBaseDeDatos() {
        String username = this.editarUsername.getText().toString();
        String password = this.editarPassword.getText().toString();
        String email = this.editarEmail.getText().toString();
        mainViewModel.ModificarUsuario(username, password, email);
        mainViewModel.setUser(userRepository.getUser(username));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}