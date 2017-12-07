package dan.dcalabrese22.com.jobapptracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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

    @Override
    public InteractionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(InteractionsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (mInteractionsList == null) {
            return 0;
        }
        return mInteractionsList.size();
    }

    public class InteractionsViewHolder extends RecyclerView.ViewHolder {
        public InteractionsViewHolder(View view) {
            super(view);
        }
    }
}

