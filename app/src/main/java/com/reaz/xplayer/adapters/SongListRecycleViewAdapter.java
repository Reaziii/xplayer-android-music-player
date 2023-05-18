package com.reaz.xplayer.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.reaz.xplayer.R;
import com.reaz.xplayer.helpers.MusicArtworkshelper;
import com.reaz.xplayer.helpers.SingleSongHelpers;
import com.reaz.xplayer.helpers.singleSongData;
import com.reaz.xplayer.helpers.utils;
import java.util.ArrayList;
public class SongListRecycleViewAdapter extends RecyclerView.Adapter<SongListRecycleViewAdapter.ViewHolder> {
    private ArrayList<singleSongData> list = new ArrayList<>();
    private utils util = new utils();
    private Context context;
    private MusicArtworkshelper musicArtworhelpers = new MusicArtworkshelper();
    public SongListRecycleViewAdapter(Context context, ArrayList<singleSongData> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.single_song, parent, false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(list.get(position).title);
        holder.albumArtiest.setText(list.get(position).album + " - " + list.get(position).artist);
        holder.duration.setText(util.formatSecondsToMinuteString(list.get(position).duration/1000));
        new SingleSongHelpers(context, holder.parent, list.get(position).songId, list.get(position).playlistId);
        LoadArtworkTask task = new LoadArtworkTask(holder.artowk, list.get(position).path, list.get(position).songId);
        task.execute();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView.getContext()).clear(holder.artowk);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, albumArtiest, duration;
        public ImageView artowk;
        public LinearLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.singlesongtitle);
            albumArtiest = itemView.findViewById(R.id.albumArtist);
            duration = itemView.findViewById(R.id.songduration);
            artowk = itemView.findViewById(R.id.singlesongartwork);
            parent = itemView.findViewById(R.id.singlesonglayout);
        }
    }

    private class LoadArtworkTask extends AsyncTask<Void, Void, Bitmap> {
        ImageView img;
        String path;
        Bitmap artwork;
        int songid;
        public LoadArtworkTask(ImageView img, String path, int songid) {
            this.img = img;
            this.songid = songid;
            this.path = path;
        }
        @Override
        protected Bitmap doInBackground(Void... voids) {
                return musicArtworhelpers.retriveCompressedMusicArtwork(songid) ;
        }

        @Override
        protected void onPostExecute(Bitmap artwork) {
            if(artwork!=null){
                Glide.with(context).load(artwork).placeholder(R.drawable.dms).error(R.drawable.dms).into(img);
            }

        }
    }

}
