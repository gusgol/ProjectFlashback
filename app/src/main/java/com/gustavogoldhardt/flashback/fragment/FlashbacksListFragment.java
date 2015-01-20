package com.gustavogoldhardt.flashback.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.gustavogoldhardt.flashback.R;
import com.gustavogoldhardt.flashback.adapter.FlashbackAdapter;
import com.gustavogoldhardt.flashback.model.Flashback;
import com.gustavogoldhardt.flashback.widget.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class FlashbacksListFragment extends Fragment {

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final String LOG_TAG = "FlashbacksListFragment";

    private RecyclerView mFlashbackList;
    private LinearLayoutManager mLayoutManager;
    private FlashbackAdapter mAdapter;
    private FloatingActionButton mAdd;
    private FloatingActionButton mMerge;
    private ProgressBar mProgressBar;
    private Uri fileUri;

    private File mInputDir;
    private File mOutputDir;

    private TextView mMemoryName;

    public FlashbacksListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_flashbacks_list, container, false);

        if(rootView != null) {
            mFlashbackList = (RecyclerView) rootView.findViewById(R.id.flashback_list);
            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            mAdd = (FloatingActionButton) rootView.findViewById(R.id.add);
            mMerge = (FloatingActionButton) rootView.findViewById(R.id.merge_videos);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mFlashbackList.setLayoutManager(mLayoutManager);
            mAdapter = new FlashbackAdapter();
            mFlashbackList.setAdapter(mAdapter);
//            mMemoryName = (TextView) rootView.findViewById(R.id.lbl_memory);
//            mMemoryName.setText("Vacations 2013");
         }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdd.setOnClickListener(mOnClickAddListener);
        mMerge.setOnClickListener(mOnClickMergeListener);

        // Create video folders
        makeDirs();

        RetrieveVideosTask retrieveVideosTask = new RetrieveVideosTask();
        retrieveVideosTask.execute();

    }



    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter != null) {
            if(mAdapter.getItemCount() >= 8) mMerge.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter(ArrayList<Flashback> videos) {
        mAdapter.setFlashbacksList(videos);
        mProgressBar.setVisibility(View.GONE);
    }

    private View.OnClickListener mOnClickAddListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startCamera();
        }
    };

    private View.OnClickListener mOnClickMergeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mergeVideos();
            playMergedVideo();
        }
    };


    private ArrayList<Flashback> getFlashbacks() {
        ArrayList<Flashback> flashbacks = new ArrayList<Flashback>();

        String path = getActivity().getExternalFilesDir(null)+"/Flashback/";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        if(file != null) {
            Log.d("Files", "Size: " + file.length);
            for (int i = 0; i < file.length; i++) {
                File targetFile = file[i];
                if(targetFile.isFile()) {
                    Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(targetFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    flashbacks.add(new Flashback("id" + i, i + 1, new Date(targetFile.lastModified()), targetFile.getAbsolutePath(), thumbnail));
                    Log.d("Files", "FileName:" + file[i].getName());
                }
            }
        }
        return flashbacks;
    }

    private void makeDirs() {
        //This is where the input videos are stored
        mInputDir = new File(getActivity().getExternalFilesDir(null)+"/Flashback/");
        mInputDir.mkdir();

        //This is where the output videos are stored
        mOutputDir = new File(getActivity().getExternalFilesDir(null)+"/Flashback/Output/");
        mOutputDir.mkdir();
    }

    private void startCamera() {
        String  timestamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa").format(Calendar.getInstance().getTime());

        if(mInputDir.isDirectory()) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File file = new File(mInputDir, timestamp+".mp4");
            fileUri = Uri.fromFile(file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high

            // start the Video Capture Intent
            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        } else {
            Log.e(LOG_TAG, "Directory not created");
        }
    }

    private void mergeVideos() {
        try {
            ArrayList<Flashback> flashbacks = getFlashbacks();

            List<Movie> movies = new LinkedList<Movie>();
            for(int i=0; i < flashbacks.size(); i++) {
                movies.add(MovieCreator.build(flashbacks.get(i).getPath()));
            }

            List<Track> videoTracks = new LinkedList<Track>();
            List<Track> audioTracks = new LinkedList<Track>();

            for (Movie m : movies) {
                for (Track track : m.getTracks()) {
                    if (track.getHandler().equals("vide")) {
                        videoTracks.add(track);
                    }
                    if (track.getHandler().equals("soun")) {
                        audioTracks.add(track);
                    }
                }
            }

            Movie concatMovie = new Movie();

            concatMovie.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
            concatMovie.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));


            Container out2 =  new DefaultMp4Builder().build(concatMovie);

            WritableByteChannel wbc = new FileOutputStream(new File(mOutputDir, "output.mp4")).getChannel();
            try {
                out2.writeContainer(wbc);
            } finally {
                wbc.close();
                Log.i(LOG_TAG, "Output file has been written");
            }

        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    private void playMergedVideo() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File video = new File(mOutputDir+"/output.mp4");
        intent.setDataAndType(Uri.fromFile(video), "video/mp4");
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                mAdapter.setFlashbacksList(getFlashbacks());
                Log.i(LOG_TAG, "Video saved to: " +data.getData());
                if(mAdapter.getItemCount() >= 8) mMerge.setVisibility(View.VISIBLE);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

    private class RetrieveVideosTask extends AsyncTask<Void, Void, Void> {
        private ArrayList<Flashback> mVideos;

        @Override
        protected Void doInBackground(Void... params) {
            mVideos = getFlashbacks();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.setFlashbacksList(mVideos);
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
