package br.com.projetobebe.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import br.com.projetobebe.AppDatabase;
import br.com.projetobebe.R;
import br.com.projetobebe.model.Event;

public class RegisterEventFragment extends Fragment {

    private  Activity activity = getActivity();
    private  AppDatabase db;

    private EditText time, date;
    private Spinner event_type;

    private List<Event> eventList = new ArrayList<>();

    public RegisterEventFragment() {
        db = AppDatabase.getDatabase(activity);
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Event> list = db.eventDao().getAll();
                eventList.addAll( list );
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.event_register,container,false );

        time = view.findViewById( R.id.time_text );
        date = view.findViewById( R.id.date_text );
        event_type = view.findViewById( R.id.spinner_event );
        Button register = view.findViewById( R.id.bt_confirm );

        ArrayAdapter<String> list_events_adapter = new ArrayAdapter<>( Objects.requireNonNull( getActivity() ),
                android.R.layout.simple_spinner_dropdown_item );
        List<String> list_events = new ArrayList<>();
        list_events.add( "baby slept" );
        list_events.add( "baby woke up" );
        list_events.add( "changed the baby" );
        list_events.add( "baby suckled" );
        list_events_adapter.addAll( list_events );

        event_type.setAdapter(list_events_adapter );

        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String time_selected = time.getText().toString();
                final String date_selected = date.getText().toString();
                final String envent_type_selected = event_type.getSelectedItem().toString();

                if (date_selected.isEmpty() || time_selected.isEmpty()){
                    Toast toast = Toast.makeText( getActivity(), "fill in all items", Toast.LENGTH_SHORT );
                    toast.show();
                }else{
                    if (verifyData( time_selected, date_selected )){
                        final boolean verify_last_event = verifyType_event(envent_type_selected);
                        final Event[] event2 = new Event[1];

                        db = AppDatabase.getDatabase(activity);
                        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    if (verify_last_event){
                                        Event event = new Event( "baby woke up",time_selected,date_selected );
                                        event2[0] = new Event( envent_type_selected,time_selected,date_selected );

                                        db.eventDao().insertEvent( event );
                                        db.eventDao().insertEvent( event2[0] );

                                    }else{
                                        event2[0] = new Event( envent_type_selected,time_selected,date_selected );
                                        db.eventDao().insertEvent( event2[0] );
                                    }
                                }
                            });

                        Toast toast = Toast.makeText( getActivity(), "event successfully registered", Toast.LENGTH_SHORT );
                        toast.show();

                        FragmentTransaction fragmentTransaction;

                        assert getFragmentManager() != null;
                        fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.container_fragment, new HomeFragment( "all"));
                        fragmentTransaction.commit();
                    }else{
                        Toast toast = Toast.makeText( getActivity(), "incorrect date or time", Toast.LENGTH_SHORT );
                        toast.show();
                    }
                }
            }
        } );

        return view;
    }

    private Boolean verifyType_event(String envent_type_selected){
        final String type_event;

        if (eventList.isEmpty()){
            return false;
        }else{
            Log.i( "last event", eventList.get( eventList.size() -1 ).getName_type() );
            type_event = eventList.get( eventList.size() -1 ).getName_type();

            boolean verify = false;
            if (type_event.contentEquals( "baby slept" ))
                verify = true;

            if (verify && envent_type_selected.contentEquals( "baby woke up" )){
               return false;
            }
            return verify;
        }
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
