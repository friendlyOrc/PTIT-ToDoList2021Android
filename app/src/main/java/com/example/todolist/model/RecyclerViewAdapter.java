package com.example.todolist.model;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.model.Task;
import com.example.todolist.R;
import com.example.todolist.activity.EditTaskActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TaskViewHolder>{
    private List<Task> mList;
    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    public void setListTask(List<Task> list){
        this.mList = list;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task t = mList.get(position);
        if(t != null){
            holder.tvName.setText(t.getName());
            holder.tvTime.setText(String.valueOf(t.getTime()));
            holder.tvCategory.setText(t.getCategory());
            if(t.getStatus() == 1){
                holder.check.setChecked(true);
            }else{
                holder.check.setChecked(false);
            }
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    // Signed in successfully, show authenticated UI.
                    Account acc = new Account();
                    acc.setId(user.getUid().toString());
                    acc.setName(user.getDisplayName());
                    acc.setEmail(user.getEmail());

                    if(holder.check.isChecked()){
                        t.setStatus(1);
                    }else{
                        t.setStatus(0);
                    }
                    mDatabase.child(acc.getId()).child(String.valueOf(t.getId())).setValue(t);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditTaskActivity.class);
                    i.putExtra("Task", t);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvTime;
        TextView tvCategory;
        CheckBox check;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameTask);
            tvTime = itemView.findViewById(R.id.tvTimeTask);
            tvCategory = itemView.findViewById(R.id.tvCateTask);
            check = itemView.findViewById(R.id.checkTask);
        }
    }
}
