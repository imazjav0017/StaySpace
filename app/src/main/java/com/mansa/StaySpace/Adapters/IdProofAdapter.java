package com.mansa.StaySpace.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mansa.StaySpace.Owner.IdProofActivity;
import com.mansa.StaySpace.R;
import com.mansa.StaySpace.Tenants.TenantIdPhotoActivity;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class IdProofAdapter extends RecyclerView.Adapter<IdProofAdapter.ViewHolder> {
    List<String>urls;

    public IdProofAdapter(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tenant_id_proof,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String url=urls.get(position);
        holder.imageLoader.setVisibility(View.VISIBLE);
        holder.imageLoader.setMax(100);
        holder.imageLoader.setProgress(0);
        Picasso.with(holder.context).load(url).fit().into(holder.img, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("idProof","set");
                holder.imageLoader.setProgress(100);
                holder.imageLoader.setVisibility(View.INVISIBLE);
                holder.img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(holder.context, IdProofActivity.class);
                        i.putExtra("url",url);
                        holder.context.startActivity(i);
                    }
                });
            }

            @Override
            public void onError() {
                holder.imageLoader.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        ProgressBar imageLoader;
        Context context;
        public ViewHolder(View itemView) {
            super(itemView);
            context=itemView.getContext();
            img=itemView.findViewById(R.id.tenantIdImage);
            imageLoader=itemView.findViewById(R.id.imageLoader);
        }
    }
}
