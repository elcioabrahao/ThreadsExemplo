package br.usjt.threadsexemplo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    ImageView imageView;
    String urlImagem;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = (EditText) findViewById(R.id.editText2);
        imageView = (ImageView) findViewById(R.id.imageView);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                urlImagem = editText.getText().toString();

                if (urlImagem.length() > 0) {

                    int selected = radioGroup.getCheckedRadioButtonId();

                    switch (selected){

                        case 2131492980:
                            loadImage();
                            break;
                        case 2131492981:
                            new DownloadImageTask((ImageView) findViewById(R.id.imageView)).execute(urlImagem);
                            break;
                        default:
                            Picasso.with(MainActivity.this).load(urlImagem).into(imageView);
                            break;

                    }


                }

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imageView.setImageResource(R.drawable.camera);

            }
        });
    }


    // método 1 - Thread
    private void loadImage() {

        new Thread(new Runnable() {
            public void run() {
                try {
                    final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(urlImagem).getContent());
                    imageView.post(new Runnable() {
                        public void run() {
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("Erro", "Erro:" + e.getMessage());
                }
            }
        }).start();

    }


    // metodo 2 - Assyktask
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }


}
