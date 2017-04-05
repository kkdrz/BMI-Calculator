package pl.edu.pwr.drozd.lab1;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class AuthorActivity extends AppCompatActivity {
    @BindView(R.id.img_author)    ImageView mMyImage;
    @BindView(R.id.img_dancing1) ImageView mDancingMan;
    @BindView(R.id.img_dancing2) ImageView mDancingMan2;
    @BindView(R.id.img_dancing3) ImageView mDancingMan3;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author_activity);
        ButterKnife.bind(this);

        glideIt(R.drawable.fotk, mMyImage, new CropCircleTransformation(this), 1);
        glideIt(R.drawable.dancing_black, mDancingMan, null, 2);
        glideIt(R.drawable.dancing_elvis, mDancingMan2, null, 2);
        glideIt(R.drawable.dancing_nyan, mDancingMan3, null, 2);


        mMediaPlayer = MediaPlayer.create(this, R.raw.photoshop_keygen);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @OnClick(R.id.img_author)
    public void stopStartMainMediaPlayer() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else mMediaPlayer.start();
    }

    @OnClick(R.id.img_dancing1)
    public void imageBlackSound() {
        makeSound(R.raw.nigganigga);
    }

    public void makeSound(int music) {
        MediaPlayer temporaryPlayer = MediaPlayer.create(this, music);
        temporaryPlayer.start();
        mMediaPlayer.setVolume(0.2f, 0.2f);
        while(temporaryPlayer.isPlaying()){}
        mMediaPlayer.setVolume(1f, 1f);
    }

    private void glideIt(int image, ImageView destination, Transformation transformation, int mode) {

        if (mode == 1 && transformation != null){
            Glide
                    .with(this)
                    .load(image)
                    .centerCrop()
                    .crossFade()
                    .error(R.drawable.error)
                    .bitmapTransform(transformation)
                    .into(destination);
        } else if (mode == 2 && transformation == null) {
            Glide
                    .with(this)
                    .load(image)
                    .error(R.drawable.error)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(destination);
        }
    }

    @Override
    protected void onPause() {
        mMediaPlayer.stop();
        finish();
        super.onPause();
    }
}