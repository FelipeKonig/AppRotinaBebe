package br.com.projetobebe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.com.projetobebe.AppDatabase;
import br.com.projetobebe.R;
import br.com.projetobebe.fragment.EditEventFragment;
import br.com.projetobebe.model.Event;

public class HomeAdapter extends RecyclerView.Adapter{

    private AppCompatActivity activity;
    private AppDatabase db;
    private Button bt_delete;
    private Button btEdit;

    private List<Event> eventList;

    public HomeAdapter(AppCompatActivity activity, final String type_event) {
        this.eventList = new ArrayList<>();
        this.activity = activity;

        db = AppDatabase.getDatabase(activity);
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Event> list = db.eventDao().getAll();
                if (type_event.contentEquals( "all" )){
                    eventList.addAll( list );
                }else{
                    for (Event event : list){
                        if (event.getName_type().contentEquals( type_event ))
                            eventList.add(event);
                    }
                }
            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name_event, time_event;

        MyViewHolder(@NonNull View itemView) {
            super( itemView );

            name_event = itemView.findViewById( R.id.name_event );
            time_event = itemView.findViewById( R.id.time_event);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.home_row_recycler, parent, false);

        bt_delete = view.findViewById( R.id.bt_delete );
        btEdit = view.findViewById( R.id.bt_edit );

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.name_event.setText(eventList.get(position).getName_type());
        viewHolder.time_event.setText( eventList.get( position ).getTime() );

        btEdit.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.drawer), "Deleted item",Snackbar.LENGTH_LONG);
                snackbar.setAction( R.string.message_delete_edit, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Event event = eventList.get( holder.getAdapterPosition() );

                        Fragment myFragment = new EditEventFragment(event);
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, myFragment).addToBackStack(null).commit();
                    }
                });
                snackbar.show();
            }
        } );

        bt_delete.setOnClickListener( new View.OnClickListener() {
            ArrayList<Event> list = new ArrayList<>( eventList );

            @Override
            public void onClick(View v) {
                final int position_remove = holder.getAdapterPosition();
                String nameType_event = eventList.get( position_remove ).getName_type();

               if (nameType_event.contentEquals( "baby slept" ) || nameType_event.contentEquals( "baby woke up" ) ) {
                   Snackbar snackbar = Snackbar.make(activity.findViewById(R.id.drawer), "Deleted item",Snackbar.LENGTH_LONG);
                   snackbar.setAction( R.string.message_delete_edit, new View.OnClickListener() {

                       @Override
                       public void onClick(View v) {
                           AppDatabase.databaseWriteExecutor.execute( new Runnable() {
                               @Override
                               public void run() {
                                   db.eventDao().deleteEvent( list.get( position ) );
                               }
                           } );
                           eventList.remove( position_remove );
                           notifyItemRemoved( position_remove );
                       }
                   });
                   snackbar.show();
               }else{
                   AppDatabase.databaseWriteExecutor.execute( new Runnable() {
                       @Override
                       public void run() {
                           db.eventDao().deleteEvent( list.get( position ) );
                       }
                   } );
                   eventList.remove( position_remove );
                   notifyItemRemoved( position_remove );
               }
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
