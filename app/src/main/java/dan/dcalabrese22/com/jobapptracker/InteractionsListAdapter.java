package dan.dcalabrese22.com.jobapptracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dan.dcalabrese22.com.jobapptracker.database.DbOperations;
import dan.dcalabrese22.com.jobapptracker.objects.Interaction;

/**
 * Created by dcalabrese on 12/6/2017.
 */

public class InteractionsListAdapter extends RecyclerView.Adapter<InteractionsListAdapter.InteractionsViewHolder> {

    private List<Interaction> mInteractionsList;
    private DbOperations mOperator;


    public void setInteractionData(List<Interaction> interactionList) {
        mInteractionsList = interactionList;
        notifyDataSetChanged();
    }

    public void clearInteractions() {
        mInteractionsList.clear();
    }

    public List<Interaction> getInteractionsList(){
        return mInteractionsList;
    }

    @Override
    public InteractionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.interaction, parent, false);
        return new InteractionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InteractionsViewHolder holder, int position) {
        TextView note = holder.mNote;
        Interaction interaction = mInteractionsList.get(position);
        note.setText(interaction.getNote());
    }

    @Override
    public int getItemCount() {
        if (mInteractionsList == null) {
            return 0;
        }
        return mInteractionsList.size();
    }

    public class InteractionsViewHolder extends RecyclerView.ViewHolder {

        private TextView mNote;

        public InteractionsViewHolder(View view) {
            super(view);
            mNote = view.findViewById(R.id.tv_interaction_note);
        }
    }
}

