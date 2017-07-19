package com.tophawks.vm.visualmerchandising.Training;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tophawks.vm.visualmerchandising.R;

import java.util.List;


public class CustomSwipeAdapter extends PagerAdapter {

    private Context ctx;
    private List<SlidesPojo> slides;
    private LayoutInflater layoutInflater;
    String no;
    public static String bool,is,url,c,testname;
    public static Firebase mDatabase,mDatabase2,mDatabase3;
    public CustomSwipeAdapter(Context ctx,List<SlidesPojo> slides)
    {
        this.ctx=ctx;
        this.slides=slides;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        FirebaseAuth auth;
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        if(position!=Integer.parseInt(no)) {
            layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Firebase.setAndroidContext(ctx);
            View item_view = layoutInflater.inflate(R.layout.swipe_layout, container, false);
//        //Video
            //String str="https://firebasestorage.googleapis.com/v0/b/swipeimplementation.appspot.com/o/Nikon%20D7500-%20Time-lapse%20sample%20video.mp4?alt=media&token=3ae5b5b0-c72e-4ea1-9b87-e4442870b1ed";
//        Uri uri = Uri.parse(str);
//        VideoView mVideoView= (VideoView)findViewById(R.id.VideoView);
//        final ProgressBar progressBarLandScape=(ProgressBar)findViewById(R.id.progressbar);
//
//        mVideoView.setVideoURI(uri);
//        progressBarLandScape.setVisibility(View.VISIBLE);
//        mVideoView.requestFocus();
//        mVideoView.start();
//
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });
//
//        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//                    return true;
//                }
//                else if(what == MediaPlayer.MEDIA_INFO_BUFFERING_START){
//                    return true;
//                }
//                return false;
//            }
//        });
            //String str="https://firebasestorage.googleapis.com/v0/b/videoplay-e370a.appspot.com/o/Nikon%20D7500-%20Time-lapse%20sample%20video.mp4?alt=media&token=58cdbd51-a96f-481c-80b1-b755928ace58";

            SlidesPojo slidesPojo = slides.get(position);
            Uri uri = Uri.parse(slidesPojo.getUrl());
            //Toast.makeText(ctx,Integer.toString(position),Toast.LENGTH_LONG).show();
            ImageView imageView = (ImageView) item_view.findViewById(R.id.image_view);
            TextView textView = (TextView) item_view.findViewById(R.id.image_count);
            TextView textView2 = (TextView) item_view.findViewById(R.id.image_info);
            final VideoView mVideoView= (VideoView)item_view.findViewById(R.id.VideoView);
            //final ProgressBar progressBarLandScape=(ProgressBar)item_view.findViewById(R.id.progressbar);

            //Toast.makeText(ctx,Integer.toString(arr[0]),Toast.LENGTH_LONG).show();
            //Toast.makeText(ctx,Integer.toString(slides.size()),Toast.LENGTH_LONG).show();
            Glide.with(ctx).load(slidesPojo.getUrl()).into(imageView);
            textView.setText(slidesPojo.getHeading());
            textView2.setText(slidesPojo.getInfo());
            // progressBarLandScape.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(ctx);
            mediaController.setAnchorView(mVideoView);
            mVideoView.setMediaController(mediaController);
            //mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.sample));
            mVideoView.setVideoURI(uri);
            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                }
            });
       /*int c=1;
        if(c==1)
        {
            imageView.setVisibility(View.GONE);
            //progressBarLandScape.setVisibility(View.GONE);
            mVideoView.setVisibility(View.VISIBLE);
        }
        else if(c!=1)
        {
            imageView.setVisibility(View.VISIBLE);
            mVideoView.setVisibility(View.GONE);
            //progressBarLandScape.setVisibility(View.GONE);
        }*/
            if(slidesPojo.getIsimage().equals("no"))
            {
                imageView.setVisibility(View.GONE);
                //progressBarLandScape.setVisibility(View.GONE);
                mVideoView.setVisibility(View.VISIBLE);
            }
            else
            {
                imageView.setVisibility(View.VISIBLE);
                //progressBarLandScape.setVisibility(View.GONE);
                mVideoView.setVisibility(View.GONE);
            }

            container.addView(item_view);
            return item_view;
        }
        else
        {
            layoutInflater= (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Firebase.setAndroidContext(ctx);
            View item_view = layoutInflater.inflate(R.layout.swipe_layout2, container, false);
            Button now=(Button)item_view.findViewById(R.id.taketestnow);
            final Button later=(Button)item_view.findViewById(R.id.taketestlater);

            mDatabase3 = new Firebase("https://trackntrain-9219f.firebaseio.com/Users");
            mDatabase3.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    String email=dataSnapshot.child("email").getValue(String.class);
                    if(email.equals(user.getEmail()))
                    {c=dataSnapshot.getKey();
                        mDatabase2=new Firebase("https://trackntrain-9219f.firebaseio.com/Users").child(c).child("Tests");
                        later.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mDatabase3.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        String email=dataSnapshot.child("email").getValue(String.class);
                                        if(email.equals(user.getEmail())) {
                                            String name = dataSnapshot.child("name").getValue(String.class);
                                            bool = "yes";
                                            Modules.mDatabase1.child(name).setValue(bool);
                                        }
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                                mDatabase=Modules.mDatabase3;
                                mDatabase.addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        url=dataSnapshot.child("testurl").getValue(String.class);
                                        testname=dataSnapshot.child("testname").getValue(String.class);
                                        mDatabase2.child(testname).child("pending").setValue(url);
                                        //Toast.make Text(ctx,mDatabase2.getKey(),Toast.LENGTH_LONG).show();
                                        Toast.makeText(ctx,"You can take the tests later in the tests portal",Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(ctx,Tests.class);
                                        /*intent.putExtra("testname",testname);*/
                                        ctx.startActivity(intent);
                                        /*mDatabase2.push().child("pendingname").setValue(testname);*/
                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {

                                    }
                                });
                            }
                        });
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

            Button next = (Button)item_view.findViewById(R.id.studynextmodule);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)ctx).finish();
                }
            });


            now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDatabase3.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            String email=dataSnapshot.child("email").getValue(String.class);
                            if(email.equals(user.getEmail())) {
                                String name = dataSnapshot.child("name").getValue(String.class);
                                bool = "yes";
                                Modules.mDatabase1.child(name).setValue(bool);
                                Intent intent = new Intent(ctx, TestWeb.class);
                                ctx.startActivity(intent);
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
            });

            container.addView(item_view);
            return item_view;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if(position!=Integer.parseInt(no))
            container.removeView((LinearLayout)object);
        else
            container.removeView((RelativeLayout)object);
    }
    @Override
    public int getCount() {
        //return arr[0] ;
        // Toast.makeText(ctx,"Hello",Toast.LENGTH_LONG).show();
        no = Modules.no;
        //Toast.makeText(ctx, no, Toast.LENGTH_LONG).show();
        // if (Integer.parseInt(no) != 0)
        return Integer.parseInt(no)+1;

        // return 2;
    }
}