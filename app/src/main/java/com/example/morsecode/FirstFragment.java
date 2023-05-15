package com.example.morsecode;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.morsecode.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
    public void startAccelerometer() {
        Intent intent = new Intent(this.getActivity(), Vibration.class);
        startActivity(intent);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_first).setOnClickListener(view1 -> startAccelerometer());

        binding.soundButton.setOnClickListener(view12 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SoundActivity));

        view.findViewById(R.id.button_first).setOnClickListener(view13 -> startAccelerometer());

        binding.soundButton.setOnClickListener(view14 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SoundActivity));

        binding.alphabetButton.setOnClickListener(view15 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_learnAlphabetActivity));

        binding.MorseToLetterAlphabet.setOnClickListener(view16 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_morseToLetterActivity));

        binding.LetterToMorseAlphabet.setOnClickListener(view17 -> NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_letterToMorseActivity));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}