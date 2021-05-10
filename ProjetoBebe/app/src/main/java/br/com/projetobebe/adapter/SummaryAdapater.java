package br.com.projetobebe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.projetobebe.AppDatabase;
import br.com.projetobebe.R;
import br.com.projetobebe.controllerDao.SummaryController;
import br.com.projetobebe.model.Event;

public class SummaryAdapater extends RecyclerView.Adapter{

    private AppDatabase db;
    private List<Event> eventList;
    private List<SummaryController> eventListSummary;

    public SummaryAdapater(AppCompatActivity activity) {
        this.eventList = new ArrayList<>();
        this.eventListSummary = new ArrayList<>();

        db = AppDatabase.getDatabase(activity);
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                List<Event> list = db.eventDao().getAll();
                    eventList.addAll( list );
                eventListSummary = new SummaryController().filterEvents( eventList );
            }
        });
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date, eventMessage;

        public MyViewHolder(@NonNull View itemView) {
            super( itemView );

            date = itemView.findViewById( R.id.date_event );
            eventMessage = itemView.findViewById( R.id.happened_event );
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.summary_row_recycler, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;

        viewHolder.date.setText( eventListSummary.get( position ).getDate() );
        viewHolder.eventMessage.setText( eventListSummary.get( position ).getMessage() );
    }

    @Override
    public int getItemCount() {
        return eventListSummary.size();
    }
}
