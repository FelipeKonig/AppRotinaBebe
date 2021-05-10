package br.com.projetobebe.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Arrays;

import br.com.projetobebe.AppDatabase;
import br.com.projetobebe.R;
import br.com.projetobebe.model.Event;

public class EditEventFragment extends Fragment {

    private Activity activity = getActivity();
    private AppDatabase db;

    private EditText time, date;
    private TextView typeEvent;

    private Event event;

    public EditEventFragment(Event event) {
        this.event = event;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.event_edit,container,false );

        time = view.findViewById( R.id.time_text );
        date = view.findViewById( R.id.date_text );
        TextView oldTime = view.findViewById( R.id.edit_old_time );
        TextView oldDate = view.findViewById( R.id.edit_old_date );
        typeEvent = view.findViewById( R.id.edit_type_event );
        Button edit = view.findViewById( R.id.bt_confirm );

        oldTime.setText( event.getTime() );
        oldDate.setText( event.getDate() );
        typeEvent.setText( event.getName_type() );

        edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String time_selected = time.getText().toString();
                final String date_selected = date.getText().toString();

                if (date_selected.isEmpty() || time_selected.isEmpty()){
                    Toast toast = Toast.makeText( getActivity(), "fill in all items", Toast.LENGTH_SHORT );
                    toast.show();
                }else {
                    if (verifyData( time_selected, date_selected )){
                        event.setTime( time_selected );
                        event.setDate( date_selected );

                        db = AppDatabase.getDatabase(activity);
                        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                               db.eventDao().updateEvent( event );
                            }
                        });
                        FragmentTransaction fragmentTransaction;

                        assert getFragmentManager() != null;
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace( R.id.container_fragment, new HomeFragment( "all" ) );
                        fragmentTransaction.commit();
                    }else{
                        Toast toast = Toast.makeText( getActivity(), "incorrect date or time", Toast.LENGTH_SHORT );
                        toast.show();
                    }
                }
            }
        });
        return view;
    }

    private boolean verifyData(String time, String date){

        return verifyTime( time ) && verifyDate( date );
    }

    private boolean verifyTime(String time){
        try {
            String[] time_value = time.split( ":",2 );

            int index = 0;
            boolean verify = true;
            for (String value_index : time_value) {
                int value = Integer.parseInt( value_index );

                Log.i( "Time", String.valueOf( value ) );
                Log.i( "Index", String.valueOf( index ) );

                if (index == 0){
                    if (value < 0 || value > 23 )
                        verify = false;
                }else{
                    if(value < 0 || value > 59)
                        verify = false;
                }
                index++;
            }

            if (verify){
                Log.i( "Time is valid:", Arrays.toString( time_value ) );
                return true;
            }
            return false;

        }catch (Exception e){
            return false;
        }
    }

    private boolean verifyDate(String date){
        try {
            String[] date_value = date.split( "/",3 );

            int index = 0;
            boolean verify = true;
            for (String value_index : date_value) {
                int value = Integer.parseInt( value_index );

                Log.i( "day", String.valueOf( value ) );
                Log.i( "Index", String.valueOf( index ) );

                if (index == 0){
                    if (value < 1 || value > 31 )
                        verify = false;
                }else if(index == 1){
                    if(value < 1 || value > 12)
                        verify = false;
                }else{
                    if(value < 0)
                        verify = false;
                }
                index++;
            }

            if (verify){
                Log.i( "Date is valid:", Arrays.toString( date_value ) );
                return true;
            }
            return false;

        }catch (Exception e){
            return false;
        }
    }
}
