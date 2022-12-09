package pl.app.projektgrupowy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.assets.Translation;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private Translation[] dataSet;

    public DashboardAdapter(Translation[] translations) {
        dataSet = translations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dashboard_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getIdTextView().setText(Integer.toString(dataSet[position].getId()));
        holder.getTitleTextView().setText(dataSet[position].getTitle());
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTextView;
        private final TextView titleTextView;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(view -> {
                // TODO Tutaj zrobić onclick
            });
            idTextView = (TextView) v.findViewById(R.id.dashboard_item_id_textview);
            titleTextView = (TextView) v.findViewById(R.id.dashboard_item_title_textview);
        }

        public TextView getIdTextView() {
            return idTextView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }
    }
}
