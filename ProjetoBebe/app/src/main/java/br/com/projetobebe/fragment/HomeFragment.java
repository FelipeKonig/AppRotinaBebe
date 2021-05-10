package br.com.projetobebe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.projetobebe.R;
import br.com.projetobebe.adapter.HomeAdapter;

public class HomeFragment extends Fragment {
    private onFragmentBtnSelected listener;
    private String type_event;

    public HomeFragment(String type_event) {
        this.type_event = type_event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.recycler_main,container,false );

        RecyclerView recyclerView = view.findViewById( R.id.recyclerView );
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        HomeAdapter homeAdapter = new HomeAdapter( (AppCompatActivity) this.getActivity(), type_event );

        Button addEvent = view.findViewById( R.id.addItem );

        addEvent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonSelected();
            }
        } );

        recyclerView.setAdapter( homeAdapter );
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach( context );

        if (context instanceof onFragmentBtnSelected){
            listener = (onFragmentBtnSelected) context;
        }else{
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    public interface onFragmentBtnSelected{
        void onButtonSelected();
    }
}
