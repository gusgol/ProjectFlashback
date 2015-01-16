package com.gustavogoldhardt.flashback.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gustavogoldhardt.flashback.R;
import com.gustavogoldhardt.flashback.adapter.FlashbackAdapter;
import com.gustavogoldhardt.flashback.widget.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class FlashbacksListFragment extends Fragment {

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final String LOG_TAG = "FlashbacksListFragment";

    private RecyclerView mFlashbackList;
    private LinearLayoutManager mLayoutManager;
    private FlashbackAdapter mAdapter;
    private FloatingActionButton mAdd;
    private Uri fileUri;

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
            mAdd = (FloatingActionButton) rootView.findViewById(R.id.add);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mFlashbackList.setLayoutManager(mLayoutManager);
            mAdapter = new FlashbackAdapter();
//            mFlashbackList.setAdapter(mAdapter);
//            mMemoryName = (TextView) rootView.findViewById(R.id.lbl_memory);
//            mMemoryName.setText("Vacations 2013");
         }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdd.setOnClickListener(mOnClickAddListener);
    }

    private View.OnClickListener mOnClickAddListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startCamera();
        }
    };

    private void startCamera() {


//        String  timestamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa").format(Calendar.getInstance().getTime());
//        File filePath = Environment.getExternalStorageDirectory();
//        File dir = new File(filePath.getAbsolutePath()+ "/Flashbacks/");
//        dir.mkdirs();
//        File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Flashbacks/Video_"+timestamp+".avi");
//        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//
//        fileUri = Uri.fromFile(mediaFile);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 1);
//        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
//
//        // start the Video Capture Intent
//        startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

        String  timestamp = new SimpleDateFormat("MM-dd-yyyy_HH-mm-ss aa").format(Calendar.getInstance().getTime());
        File fileDir = new File(Environment.getExternalStorageDirectory()+"/Flashback/");

        if(fileDir.mkdir()) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File file = new File(fileDir, timestamp+".avi");
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

    public File getAlbumStorageDir(Context context, String albumName) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_MOVIES), albumName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(getActivity(), "Video saved to: " +data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }

}
