package com.example.proyectopelisappcine.ui.users;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.databinding.FragmentRegistroUsuarioBinding;
import com.example.proyectopelisappcine.model.User;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

import java.util.HashMap;

public class RegistroFragment extends Fragment {
    private FragmentRegistroUsuarioBinding binding;
    private EditText registerUser;
    private EditText registerPassword;
    private EditText registerEmail;
    private Button terminarRegistro;
    private Button cancelarRegistro;
    private HashMap<String, User> usuariospornombresBaseDeDatos;
    private HashMap<String, User> usuariosporncontrasenasBaseDeDatos;
    private HashMap<String, User> usuariosporemailsBaseDeDatos;
    private SharedPreferences preferences;
    FilmDatabase filmDatabase;
    UserRepository userRepository;
    private MainViewModel mainViewModel;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistroUsuarioBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        preferences = getActivity().getPreferences(requireContext().MODE_PRIVATE);

        filmDatabase = FilmDatabase.getInstance(getContext());//Nota para precauciones proximas: El objeto del activity de la base de datoas es mejor crealo en el onCreate
        userRepository = UserRepository.getInstance(filmDatabase.userDAO());

        // Obtén el MainViewModel de la MainActivity
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainViewModel = ((MainActivity) getActivity()).getMainViewModel();
        }

        registerUser = root.findViewById(R.id.value_register_user);
        registerPassword = root.findViewById(R.id.value_register_password);
        registerEmail = root.findViewById(R.id.value_register_email);
        terminarRegistro = root.findViewById(R.id.finalizar_registro);
        cancelarRegistro = root.findViewById(R.id.cancelar_registro);

        Intent intent = new Intent();
        usuariospornombresBaseDeDatos = (HashMap<String, User>) intent.getSerializableExtra("Lista_usuarios_por_nombre");
        usuariosporncontrasenasBaseDeDatos = (HashMap<String, User>) intent.getSerializableExtra("Lista_usuarios_por_contrasena");
        usuariosporemailsBaseDeDatos = (HashMap<String, User>) intent.getSerializableExtra("Lista_usuarios_por_correo");

        terminarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminarRegistro.setEnabled(false);
                RegistroUsuarioEnBaseDeDatos();
                ((MainActivity) getActivity()).NavegarDeRegistroAPerfil();
            }
        });

        cancelarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).NavegarDeRegistroAInicio();
            }
        });

        return root;
    }

    public void RegistroUsuarioEnBaseDeDatos() {
        String username = this.registerUser.getText().toString();
        String password = this.registerPassword.getText().toString();
        String email = this.registerEmail.getText().toString();
        mainViewModel.RegistroUsuario(username, password, email);
        mainViewModel.setUser(userRepository.getUser(username));
    }
}
