package pl.app.projektgrupowy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.app.projektgrupowy.R;
import pl.app.projektgrupowy.assets.Segment;
import pl.app.projektgrupowy.main.MainActivity;

public class TranslationAdapter extends RecyclerView.Adapter<TranslationAdapter.ViewHolder> {
    private final Segment[] dataSet;
    private final MainActivity mainActivity;

    public TranslationAdapter(Segment[] segments, MainActivity activity) {
        dataSet = segments;
        mainActivity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_translation_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getIdTextView().setText(Integer.toString(position + 1));
        holder.getSourceTextView().setText(dataSet[position].getSourceText());
        holder.setChosenSegment(position);
        holder.setActivity(mainActivity);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView idTextView;
        private final TextView sourceTextView;

        private Integer chosenSegment;
        private MainActivity mainActivity;

        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(view -> {
                mainActivity.mainViewModel.chosenSegment().setValue(chosenSegment);
            });

            idTextView = (TextView) v.findViewById(R.id.translation_index_textview);
            sourceTextView = (TextView) v.findViewById(R.id.translation_source_textview);
        }

        public TextView getIdTextView() {
            return idTextView;
        }

        public TextView getSourceTextView() {
            return sourceTextView;
        }

        public void setChosenSegment(Integer chosenItem) {
            this.chosenSegment = chosenItem;
        }

        public void setActivity(MainActivity activity) {
            this.mainActivity = activity;
        }
    }
}
