package com.example.proyectopelisappcine.ui.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.proyectopelisappcine.MainActivity;
import com.example.proyectopelisappcine.MainViewModel;
import com.example.proyectopelisappcine.R;
import com.example.proyectopelisappcine.databinding.FragmentRegistroUsuarioBinding;
import com.example.proyectopelisappcine.repository.UserRepository;
import com.example.proyectopelisappcine.roomdb.FilmDatabase;

public class RegistroFragment extends Fragment {
    private FragmentRegistroUsuarioBinding binding;
    private EditText registerUser;
    private EditText registerPassword;
    private EditText registerEmail;
    private Button terminarRegistro;
    private Button cancelarRegistro;
    FilmDatabase filmDatabase;
    UserRepository userRepository;
    private MainViewModel mainViewModel;
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegistroUsuarioBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        filmDatabase = FilmDatabase.getInstance(getContext());
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

        terminarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                terminarRegistro.setEnabled(false);
                RegistroUsuarioEnBaseDeDatos();
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
        String username = registerUser.getText().toString();
        String password = registerPassword.getText().toString();
        String email = registerEmail.getText().toString();
        mainViewModel.getStateVerification().observe(getViewLifecycleOwner(), result -> {
            if (result) {
                Toast.makeText(getActivity(), "El usuario que se quiere registrar ya existe", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    if (!username.equals("") && !password.equals("") && !email.equals("")) {
                        mainViewModel.RegistroUsuario(username, password, email);
                        mainViewModel.setUser(userRepository.getUser(username));
                        ((MainActivity) getActivity()).NavegarDeRegistroAPerfil();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //Se verifica si el usuario ya existe
        mainViewModel.VerificarUsuario(username);
    }
}