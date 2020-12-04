package com.company.roomfloyd;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.GetContent;
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.company.roomfloyd.databinding.FragmentInsertarAlbumBinding;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class InsertarAlbumFragment extends Fragment {

    private FragmentInsertarAlbumBinding binding;
    private NavController navController;
    private AlbumsViewModel albumsViewModel;

    private Uri imagenSeleccionada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentInsertarAlbumBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        albumsViewModel = new ViewModelProvider(requireActivity()).get(AlbumsViewModel.class);

        navController = Navigation.findNavController(view);


        binding.insertar.setOnClickListener(v -> {
            if (imagenSeleccionada != null) {
                String titulo = binding.titulo.getText().toString();
                String anyo = binding.anyo.getText().toString();

                albumsViewModel.insertarAlbum(titulo, anyo, imagenSeleccionada.toString());

                albumsViewModel.establecerImagenSeleccionada(null);
                navController.popBackStack();
            } else {
                Toast.makeText(requireContext(), "Seleccione una imagen", Toast.LENGTH_SHORT).show();
            }
        });

        binding.seleccionarPortada.setOnClickListener(v -> {
            abrirGaleria();
        });

        albumsViewModel.imagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            imagenSeleccionada = uri;
            Glide.with(requireView()).load(uri).into(binding.previsualizarPortada);
        });
    }


    /* https://developer.android.com/training/permissions/requesting */

    private void abrirGaleria(){
        if (checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            lanzadorGaleria.launch("image/*");
        } else {
            lanzadorPermisos.launch(READ_EXTERNAL_STORAGE);
        }
    }

    private final ActivityResultLauncher<String> lanzadorGaleria =
            registerForActivityResult(new GetContent(), uri -> {
                albumsViewModel.establecerImagenSeleccionada(uri);
            });

    private final ActivityResultLauncher<String> lanzadorPermisos =
            registerForActivityResult(new RequestPermission(), isGranted -> {
                if (isGranted) {
                    lanzadorGaleria.launch("image/*");
                }
            });
}