package com.squar.html5games;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

public class GameRecyclerViewAdapter extends RecyclerView.Adapter<GameRecyclerViewAdapter.UserViewHolder> {
    List<Item> items = new ArrayList<>();
    Context context;
    private RecyclerView recyclerView;

    public GameRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(this.context).inflate(R.layout.game_item, parent, false);
        UserViewHolder holder = new UserViewHolder(row);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        Item item = items.get(position);

        // parse the URL
        final Uri uri = Uri.parse(item.getThumbnail());

        // Listen to Download events
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>() {

            @Override
            public void onFailure(String id, Throwable throwable) {
                // Failure happened
                Toast.makeText(context, "Error loading: " + id, Toast.LENGTH_SHORT).show();
            }
        };

        // Initialize a controller and attach the listener to it.
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setControllerListener(controllerListener)
                .build();

        holder.gameImage.setController(controller);

        holder.gameImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, GameActivity.class);
            intent.putExtra("URL", item.getUrl());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List list){
        this.items.clear();
        this.items.addAll(list);
        notifyItemRangeChanged(0,list.size());
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView gameImage;

        public UserViewHolder(View itemView) {
            super(itemView);
            gameImage = (SimpleDraweeView) itemView.findViewById(R.id.game_image);
        }
    }
}