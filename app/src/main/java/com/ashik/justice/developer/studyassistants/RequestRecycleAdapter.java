package com.ashik.justice.developer.studyassistants;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;


public class RequestRecycleAdapter extends RecyclerView.Adapter<RequestRecycleAdapter.ViewHolder> {
private List<ListRequest> request_list;
private Context context;

    public RequestRecycleAdapter(List<ListRequest> request_list, Context context) {
        this.request_list = request_list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View view=LayoutInflater.from(parent.getContext())
             .inflate(R.layout.custom_recycle,parent,false);
     return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      final ListRequest listRequest=request_list.get(position);
      if (!listRequest.getUser_image().equals("default")) {
          Picasso.with(context).load(listRequest.getUser_image()).placeholder(R.drawable.profile).into(holder.imageView);
      }
        SpannableString span1 = new SpannableString(listRequest.getUser_name());
        span1.setSpan(new AbsoluteSizeSpan(40), 0, listRequest.getUser_name().length(), SPAN_INCLUSIVE_INCLUSIVE);
        String re="sent you a request to add your session";
        SpannableString span2 = new SpannableString(re);
        span2.setSpan(new AbsoluteSizeSpan(30), 0, re.length(), SPAN_INCLUSIVE_INCLUSIVE);
        CharSequence finalText = TextUtils.concat(span1, " ", span2);

        holder.username.setText(finalText);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ShowRequestDetails.class);
                intent.putExtra("fire_id",listRequest.getFire_id());
              context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return request_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

     public CircleImageView imageView;
     public TextView username;
     public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);

            imageView=(CircleImageView)itemView.findViewById(R.id.req_student_image);
            username=(TextView)itemView.findViewById(R.id.req_student_name);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.card_latout);

        }
    }
}
