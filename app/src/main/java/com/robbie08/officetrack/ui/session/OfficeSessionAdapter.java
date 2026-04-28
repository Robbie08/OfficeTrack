package com.robbie08.officetrack.ui.session;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.robbie08.officetrack.R;
import com.robbie08.officetrack.model.OfficeSessionModel;

import java.util.ArrayList;
import java.util.List;

public class OfficeSessionAdapter extends RecyclerView.Adapter<OfficeSessionAdapter.OfficeSessionViewHolder> {

    public interface OnSessionClickListener {
        void onSessionClick(OfficeSessionModel session);
    }

    private final List<OfficeSessionModel> sessions = new ArrayList<>();
    private final OnSessionClickListener listener;

    public OfficeSessionAdapter(OnSessionClickListener listener) {
        this.listener = listener;
    }

    public void setSessions(List<OfficeSessionModel> newSessions) {
        sessions.clear();

        if (newSessions != null) {
            sessions.addAll(newSessions);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OfficeSessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_office_session, parent, false);

        return new OfficeSessionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeSessionViewHolder holder, int position) {
        OfficeSessionModel session = sessions.get(position);

        holder.sessionDateTextView.setText(session.getDisplayDate());
        holder.sessionTimeRangeTextView.setText(session.getDisplayTimeRange());
        holder.sessionDurationTextView.setText(session.getDisplayDuration());

        View.OnClickListener clickListener = v -> {
            if (listener != null) {
                listener.onSessionClick(session);
            }
        };
        holder.editSessionButton.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return sessions.size();
    }

    static class OfficeSessionViewHolder extends RecyclerView.ViewHolder {
        TextView sessionDateTextView;
        TextView sessionTimeRangeTextView;
        TextView sessionDurationTextView;
        ImageButton editSessionButton;

        public OfficeSessionViewHolder(@NonNull View itemView) {
            super(itemView);

            sessionDateTextView = itemView.findViewById(R.id.sessionDateTextView);
            sessionTimeRangeTextView = itemView.findViewById(R.id.sessionTimeRangeTextView);
            sessionDurationTextView = itemView.findViewById(R.id.sessionDurationTextView);
            editSessionButton = itemView.findViewById(R.id.editSessionButton);
        }
    }
}