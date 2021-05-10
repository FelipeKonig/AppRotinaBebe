package br.com.projetobebe.fragment;

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
import br.com.projetobebe.adapter.SummaryAdapater;

public class SummaryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.recycler_main,container,false );

        RecyclerView recyclerView = view.findViewById( R.id.recyclerView );
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        SummaryAdapater summaryAdapater = new SummaryAdapater( (AppCompatActivity) this.getActivity() );

        Button button = view.findViewById( R.id.addItem );
        button.setVisibility( View.INVISIBLE );

        recyclerView.setAdapter( summaryAdapater );
        return view;
    }
}
